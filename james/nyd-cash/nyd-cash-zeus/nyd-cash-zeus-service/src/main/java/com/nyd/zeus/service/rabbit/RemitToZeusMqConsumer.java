package com.nyd.zeus.service.rabbit;

import com.alibaba.fastjson.JSON;
import com.nyd.capital.model.RemitMessage;
import com.nyd.order.api.OrderContract;
import com.nyd.order.api.OrderDetailContract;
import com.nyd.order.model.OrderDetailInfo;
import com.nyd.order.model.OrderInfo;
import com.nyd.product.api.ProductContract;
import com.nyd.product.model.ProductInfo;
import com.nyd.zeus.model.BillInfo;
import com.nyd.zeus.service.BillService;
import com.nyd.zeus.service.enums.BillStatusEnum;
import com.nyd.zeus.service.util.DateUtil;
import com.tasfe.framework.rabbitmq.RabbitmqMessageProcesser;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;


/**
 * Created by zhujx on 2017/11/27.
 * 监听放款队列消息处理
 */
public class RemitToZeusMqConsumer implements RabbitmqMessageProcesser<RemitMessage> {

    private static Logger LOGGER = LoggerFactory.getLogger(RemitToZeusMqConsumer.class);

    @Autowired
    OrderContract orderContract;

    @Autowired
    OrderDetailContract orderDetailContract;

    @Autowired
    ProductContract productContract;

    @Autowired
    BillService billService;

    @Autowired
    ZeusToOrderProducer zeusToOrderProducer;

    /**
     * 监听处理放款成功消息
     * @param message
     */
    @Override
    public void processMessage(RemitMessage message) {
        if(message != null && "0".equals(message.getRemitStatus())){
            LOGGER.info(message.toString());
            try{
                OrderInfo orderInfo = orderContract.getOrderByOrderNo(message.getOrderNo()).getData();
                if(orderInfo == null){
                    LOGGER.info("RemitToZeusMqConsumer" + "查无此订单");
                    return ;
                }
                String appName="nyd";
                BillInfo billInfo = new BillInfo();
                billInfo.setUserId(orderInfo.getUserId());
                billInfo.setOrderNo(message.getOrderNo());
                billInfo.setIbankOrderNo(orderInfo.getIbankOrderNo());
                if(StringUtils.isNotBlank(orderInfo.getAppName())){
                    appName=orderInfo.getAppName();
                }
                billInfo.setAppName(appName);
                OrderDetailInfo orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(message.getOrderNo()).getData();
                if(orderDetailInfo == null){
                    LOGGER.info("RemitToZeusMqConsumer" + "查无此订单详情");
                    return ;
                }
                ProductInfo productConfigInfo = productContract.getProductInfo(orderDetailInfo.getProductCode()).getData();
                if(productConfigInfo == null){
                    LOGGER.info("RemitToZeusMqConsumer" + "查无此产品信息");
                    return ;
                }
                if(productConfigInfo.getProductType() == 0){
                    //单期
                    billInfo.setCurPeriod(1);
                    billInfo.setPeriods(1);
                    //进行处理
                    if (message.getRemitTime() !=null){
                        billInfo.setPromiseRepaymentDate(DateUtil.addDayFromRemitTime(message.getRemitTime(),orderInfo.getBorrowTime()));
                    }else {
                        billInfo.setPromiseRepaymentDate(DateUtil.addDay(orderInfo.getBorrowTime()));
                    }
                    billInfo.setCurRepayAmount(orderInfo.getLoanAmount().add(orderInfo.getInterest()));
                    billInfo.setRepayPrinciple(orderInfo.getLoanAmount());
                    billInfo.setWaitRepayPrinciple(orderInfo.getLoanAmount());
                    billInfo.setRepayInterest(orderInfo.getInterest());
                    billInfo.setTestStatus(orderInfo.getTestStatus());
                    //应实收金额
                    billInfo.setReceivableAmount(billInfo.getCurRepayAmount());
                    billInfo.setWaitRepayAmount(billInfo.getCurRepayAmount());
                    billInfo.setAlreadyRepayAmount(new BigDecimal(0));
                    billInfo.setBillStatus(BillStatusEnum.REPAY_ING.getCode());
                }else if(productConfigInfo.getProductType() == 1){
                    //分期

                }
                LOGGER.info("保存的账单信息"+ JSON.toJSONString(billInfo));
                ResponseData responseData = billService.saveBillInfo(billInfo);
                //生成账单成功通知订单系统修改订单状态为success
                if ("0".equals(responseData.getStatus())){
                    zeusToOrderProducer.sendMsg(message);
                }
            }catch(Exception e){
                LOGGER.error("RemitToZeusMqConsumer Exception" + e);
            }
        }else if(message != null && "1".equals(message.getRemitStatus())){
            zeusToOrderProducer.sendMsg(message);
        }else{
            LOGGER.error("RemitToZeusMqConsumer message body is null" + message.toString());
        }
    }

}
