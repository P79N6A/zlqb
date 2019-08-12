package com.nyd.order.service.rabbit;

import com.alibaba.fastjson.JSON;
import com.ibank.order.api.KzlicaiContract;
import com.ibank.order.api.OrderContract;
import com.ibank.order.model.OrderDetailInfo;
import com.ibank.order.model.enums.OrderStatus;
import com.ibank.order.model.kzlicai.req.RepayNotifyReqDTO;
import com.ibank.pay.model.RepayMessage;
import com.nyd.order.dao.YmtKzjrBill.BillYmtDao;
import com.nyd.order.dao.YmtKzjrBill.OverdueBillYmtDao;
import com.nyd.order.model.YmtKzjrBill.BillYmtInfo;
import com.nyd.order.model.YmtKzjrBill.OverdueBillYmtInfo;
import com.nyd.order.model.YmtKzjrBill.enums.BillStatusEnum;
import com.tasfe.framework.rabbitmq.RabbitmqMessageProcesser;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 空中金融通过合利宝还款成功后的处理逻辑
 *
 * @author cm
 */

public class YmtKzjrRepayMqProcessor implements RabbitmqMessageProcesser<RepayMessage> {
    private static Logger logger = LoggerFactory.getLogger(YmtKzjrRepayMqProcessor.class);

    @Autowired
    private OrderContract orderContract;

    @Autowired
    private BillYmtDao billYmtDao;

    @Autowired
    private OverdueBillYmtDao overdueBillYmtDao;

    @Autowired
    private KzlicaiContract kzlicaiContract;

    @Override
    public void processMessage(RepayMessage repayMessage) {

        if(repayMessage == null || StringUtils.isBlank(repayMessage.getBillNo())){
            logger.error("还款mq 参数有异常："+ JSON.toJSONString(repayMessage));
            return;
        }

        String orderSon = repayMessage.getBillNo();
        logger.info("子订单号："+orderSon);
        String assetNo = repayMessage.getAssetNo();
        logger.info("资产编号："+assetNo);
        Integer repayStatus = Integer.valueOf(repayMessage.getRepayStatus());
        logger.info("还款状态："+repayStatus);

        /**
         * 更新t_order_detail状态
         */
        try {
            ResponseData<List<OrderDetailInfo>> responseData = orderContract.getOrderDetaiByOrderSon(orderSon);
            if ("0".equals(responseData.getStatus())){
                List<OrderDetailInfo> list = responseData.getData();
                if (list != null && list.size() > 0 ){
                    OrderDetailInfo orderDetail = new OrderDetailInfo();
                    //还款时间
                    orderDetail.setOrderStatusTime(repayMessage.getRepayTime());
                    //订单状态
                    orderDetail.setOrderStatus(OrderStatus.REPAYMENT_FINISHED.getCode());
                    //子订单号
                    orderDetail.setOrderSno(repayMessage.getBillNo());

                    ResponseData data = orderContract.updateOrderDetail(orderDetail);
                    if ("0".equals(data.getStatus())){
                        logger.info("子订单号："+orderSon+"t_order_detail更新成功");
                    }else {
                        logger.info("子订单号："+orderSon+"t_order_detail更新失败"+",失败原因是:"+data.getMsg());
                    }
                }else {
                    logger.info("do not find orderDetail,it means exists error,because every orderSon have orderDetail");
                }
            }
        }catch (Exception e){
            logger.error("update t_order_detail has error",e);
            e.printStackTrace();
        }

        /**
         * 更新t_bill
         */
        try {
            List<BillYmtInfo> billYmtInfos = billYmtDao.selectByOrderSno(orderSon);
            if (billYmtInfos != null && billYmtInfos.size() > 0 ){
                BillYmtInfo billInfo = new BillYmtInfo();
                //子订单号
                billInfo.setOrderSno(orderSon);
                //实际结清时间
                billInfo.setActualSettleDate(repayMessage.getRepayTime());
                //剩余还款本金
                billInfo.setWaitRepayPrinciple(new BigDecimal(0));
                //剩余应还款金额
                billInfo.setWaitRepayAmount(new BigDecimal(0));
                //已还款金额
                billInfo.setAlreadyRepayAmount(repayMessage.getRepayAmount());
                //账单状态
                billInfo.setBillStatus(BillStatusEnum.REPAY_SUCCESS.getCode());
                billYmtDao.updateByOrderSno(billInfo);
                logger.info("子订单号："+orderSon+"t_bill更新成功");
            }else {
                logger.info("do not find bill,it means exist error,because every orderSon have bill");
            }
        }catch (Exception e){
            logger.error("update t_bill has error",e);
            e.printStackTrace();
        }

        /**
         * 更新t_overdue_bill(只有逾期才会更新逾期表t_overdue_bill)
         */
        try {
            List<OverdueBillYmtInfo> overdueBillYmtInfos = overdueBillYmtDao.selectByOrderSno(orderSon);
            if (overdueBillYmtInfos != null && overdueBillYmtInfos.size() > 0 ){
                OverdueBillYmtInfo overdueBillInfo = new OverdueBillYmtInfo();
                //子订单号
                overdueBillInfo.setOrderSno(orderSon);
                //账单状态
                overdueBillInfo.setBillStatus(BillStatusEnum.REPAY_SUCCESS.getCode());

                overdueBillYmtDao.updateByOrderSno(overdueBillInfo);
                logger.info("子订单号："+orderSon+"t_overdue_bill更新成功");
            }else {
                logger.info("do not find overDueBill,it means the orderSon don't due");
            }
        }catch (Exception e){
            logger.error("update t_overdue_bill has error",e);
            e.printStackTrace();
        }


        /**
         * 更新完成以后，通知空中金融(分两种情况考虑，逾期通知,未逾期通知)
         */
        try {
            ResponseData<List<OrderDetailInfo>> responseData = orderContract.getOrderDetaiByOrderSon(orderSon);
            List<OrderDetailInfo> orderDetailInfoList = null;
            if ("0".equals(responseData.getStatus())){
                orderDetailInfoList = responseData.getData();
            }
            List<BillYmtInfo> billYmtInfoList = billYmtDao.selectByOrderSno(orderSon);
            List<OverdueBillYmtInfo> overdueBillYmtInfoList = overdueBillYmtDao.selectByOrderSno(orderSon);
            //逾期通知
            if (orderDetailInfoList != null && orderDetailInfoList.size() > 0 && billYmtInfoList!= null && billYmtInfoList.size() >0 &&
                    overdueBillYmtInfoList!= null &&  overdueBillYmtInfoList.size() > 0){
                OrderDetailInfo orderDetailInfo = orderDetailInfoList.get(0);
                logger.info("订单信息:"+JSON.toJSON(orderDetailInfo));
                BillYmtInfo billYmtInfo = billYmtInfoList.get(0);
                logger.info("账单信息:"+JSON.toJSON(billYmtInfo));
                OverdueBillYmtInfo overdueBillYmtInfo = overdueBillYmtInfoList.get(0);
                logger.info("逾期账单信息:"+JSON.toJSON(overdueBillYmtInfo));

                if (orderDetailInfo.getOrderStatus() == 100 && billYmtInfo.getBillStatus().equals("B003") && overdueBillYmtInfo.getBillStatus().equals("B003")){
                    RepayNotifyReqDTO repayNotifyReqDTO = new RepayNotifyReqDTO();
                    repayNotifyReqDTO.setAssetCode(assetNo);
                    repayNotifyReqDTO.setRepayType(2);
                    repayNotifyReqDTO.setCurrentPeriod(billYmtInfo.getCurPeriod());
                    Date date = repayMessage.getRepayTime();
                    long ts = date.getTime();
                    repayNotifyReqDTO.setRepayDate(ts);
                    try {
                        ResponseData data = kzlicaiContract.repayNotify(repayNotifyReqDTO);
                        if ("0".equals(data.getStatus())){
                            logger.info("还款完成(逾期)，给空中金融发通知OK");
                        }else {
                            logger.info("还款完成(逾期)，给空中金融发通知fail"+",失败原因为:"+data.getMsg());
                        }
                    }catch (Exception e){
                        logger.error("send over due message to kzjr has error");
                        e.printStackTrace();
                    }
                }
            }else if (orderDetailInfoList != null && orderDetailInfoList.size() > 0 && billYmtInfoList!= null && billYmtInfoList.size() >0 &&
                    overdueBillYmtInfoList.size() == 0){
                OrderDetailInfo orderDetailInfo = orderDetailInfoList.get(0);
                logger.info("订单信息:"+JSON.toJSON(orderDetailInfo));
                BillYmtInfo billYmtInfo = billYmtInfoList.get(0);
                logger.info("账单信息:"+JSON.toJSON(billYmtInfo));
                if (orderDetailInfo.getOrderStatus() == 100 && billYmtInfo.getBillStatus().equals("B003")){
                    RepayNotifyReqDTO repayNotifyReqDTO = new RepayNotifyReqDTO();
                    repayNotifyReqDTO.setAssetCode(assetNo);
                    repayNotifyReqDTO.setRepayType(2);
                    repayNotifyReqDTO.setCurrentPeriod(billYmtInfo.getCurPeriod());
                    Date date = repayMessage.getRepayTime();
                    long ts = date.getTime();
                    repayNotifyReqDTO.setRepayDate(ts);
                    try {
                        ResponseData data = kzlicaiContract.repayNotify(repayNotifyReqDTO);
                        if ("0".equals(data.getStatus())){
                            logger.info("还款完成(未逾期)，给空中金融发通知OK");
                        }else {
                            logger.info("还款完成(未逾期)，给空中金融发通知fail"+",失败原因为:"+data.getMsg());
                        }
                    }catch (Exception e){
                        logger.error("send message to kzjr has error");
                        e.printStackTrace();
                    }
                }

            }

        }catch (Exception e){
            logger.error("update message and send message exist error ",e);
            e.printStackTrace();
        }

    }
}
