package com.nyd.zeus.service.rabbit;

import com.alibaba.fastjson.JSON;
import com.creativearts.collection.dto.CollectionFlatAccountDto;
import com.creativearts.nyd.pay.model.RepayMessage;
import com.nyd.order.api.BillYmtContract;
import com.nyd.order.model.YmtKzjrBill.BillYmtInfo;
import com.nyd.order.model.YmtKzjrBill.OverdueBillYmtInfo;
import com.nyd.zeus.dao.RepayDao;
import com.nyd.zeus.model.BillInfo;
import com.nyd.zeus.model.OverdueBillInfo;
import com.nyd.zeus.model.RepayInfo;
import com.nyd.zeus.service.BillService;
import com.nyd.zeus.service.enums.BillStatusEnum;
import com.tasfe.framework.rabbitmq.RabbitmqMessageProcesser;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by hwei on 2018/12/15.
 * 催收代扣销账
 */
public class CollectionToNydConsumer implements RabbitmqMessageProcesser<CollectionFlatAccountDto> {

    private static Logger LOGGER = LoggerFactory.getLogger(CollectionToNydConsumer.class);
    @Autowired
    private RepayDao repayDao;
    @Autowired(required = false)
    private BillYmtContract billYmtContract;
    @Autowired
    BillService billService;
    @Autowired
    RepayToIbankProducer repayToIbankProducer;

    @Override
    public void processMessage(CollectionFlatAccountDto message) {
        LOGGER.info("CollectionToNydConsumer message is "+ JSON.toJSONString(message));
        if(message==null|| StringUtils.isBlank(message.getBillNo())) {
            return;
        }
        if (StringUtils.isNotBlank(message.getOrderType())&&"kzjr".equals(message.getOrderType())) {   //kzjr还款
            //保存流水
            RepayInfo repayInfo = new RepayInfo();
            repayInfo.setRepayNo(message.getWithholdOrderNo());
            repayInfo.setBillNo(message.getBillNo());
            repayInfo.setRepayTime(message.getSubmitPayEndTime());
            repayInfo.setRepayAmount(message.getPayAmount());
            repayInfo.setRepayAccount(message.getBankcardNo());
            repayInfo.setRepayChannel(message.getPayChannelCodeBx());
            repayInfo.setRepayStatus("0");
            repayInfo.setRepayType("17");
            repayInfo.setAppName("ymt");  //空中金融全部是ymt
            try {
                repayDao.save(repayInfo);
                //销账
                ResponseData<BillYmtInfo> responseData = billYmtContract.getBillYmtByBillNo(message.getBillNo());
                if (responseData!=null&&"0".equals(responseData.getStatus())) {
                    BillYmtInfo billYmtInfo = responseData.getData();
                    if (billYmtInfo!=null) {
                        String billStatus = billYmtInfo.getBillStatus();
                        BigDecimal alreadyRepayAmount =   billYmtInfo.getAlreadyRepayAmount().add(message.getPayAmount());
                        BigDecimal waitRepayAmount = billYmtInfo.getWaitRepayAmount();
                        if (message.getPayAmount().compareTo(waitRepayAmount) == 1 || message.getPayAmount().compareTo(waitRepayAmount) == 0) {  //结清
                            billYmtInfo.setWaitRepayPrinciple(new BigDecimal(0));
                            billYmtInfo.setWaitRepayAmount(new BigDecimal(0));
                            billYmtInfo.setAlreadyRepayAmount(alreadyRepayAmount);
                            //实际结清时间
                            billYmtInfo.setActualSettleDate(message.getSubmitPayEndTime());
                            billYmtInfo.setBillStatus(BillStatusEnum.REPAY_SUCCESS.getCode());
                            billYmtInfo.setUpdateTime(null);
                            billYmtContract.updateByOrderSno(billYmtInfo);
                            if(BillStatusEnum.REPAY_OVEDUE.getCode().equals(billStatus)){
                                //逾期中还款
                                ResponseData<OverdueBillYmtInfo> overdueBillYmtInfoResponseData = billYmtContract.getOverdueBillInfoByBillNo(message.getBillNo());
                                if (overdueBillYmtInfoResponseData!=null&&"0".equals(overdueBillYmtInfoResponseData.getStatus())) {
                                    OverdueBillYmtInfo overdueBillYmtInfo = overdueBillYmtInfoResponseData.getData();
                                    overdueBillYmtInfo.setBillStatus(BillStatusEnum.REPAY_SUCCESS.getCode());
                                    overdueBillYmtInfo.setUpdateTime(null);
                                    //更新逾期账单（OverdueBill）状态
                                    billYmtContract.updateOverDuebillYmt(overdueBillYmtInfo);
                                }
                            }
                            RepayMessage msg = new RepayMessage();
                            msg.setRepayTime(message.getSubmitPayEndTime());
                            msg.setBillNo(billYmtInfo.getOrderSno());
                            repayToIbankProducer.sendMsg(msg);
                        } else { //未结清
                            BigDecimal waitRepayPrinciple = billYmtInfo.getWaitRepayPrinciple();
                            //剩余待还款本金大于0
                            if(waitRepayPrinciple.compareTo(new BigDecimal(0)) == 1){
                                //剩余待还款本金小于等于本次还款金额
                                if(waitRepayPrinciple.compareTo(message.getPayAmount()) == 0
                                        || waitRepayPrinciple.compareTo(message.getPayAmount()) == -1){
                                    billYmtInfo.setWaitRepayPrinciple(new BigDecimal(0));
                                }else{
                                    billYmtInfo.setWaitRepayPrinciple(waitRepayPrinciple.subtract(message.getPayAmount()));
                                }
                            }
                            billYmtInfo.setAlreadyRepayAmount(alreadyRepayAmount);
                            waitRepayAmount = waitRepayAmount.subtract(message.getPayAmount());
                            billYmtInfo.setWaitRepayAmount(waitRepayAmount);
                            //更新账单信息
                            billYmtContract.updateByOrderSno(billYmtInfo);
                        }
                    }
                }
            } catch(Exception e) {
                LOGGER.error("CollectionToNydConsumer kzjr error",e);
            }
        } else {   //其他还款 侬要贷还款
            try {
                ResponseData<BillInfo> responseData = billService.getBillInfo(message.getBillNo());
                if (responseData!=null&&"0".equals(responseData.getStatus())) {
                    BillInfo billInfo = responseData.getData();
                    if (billInfo != null) {
                        //保存流水
                        //保存流水
                        RepayInfo repayInfo = new RepayInfo();
                        repayInfo.setRepayNo(message.getWithholdOrderNo());
                        repayInfo.setBillNo(message.getBillNo());
                        repayInfo.setRepayTime(message.getSubmitPayEndTime());
                        repayInfo.setRepayAmount(message.getPayAmount());
                        repayInfo.setRepayAccount(message.getBankcardNo());
                        repayInfo.setRepayChannel(message.getPayChannelCodeBx());
                        repayInfo.setRepayStatus("0");
                        repayInfo.setRepayType("4");
                        if (StringUtils.isNotBlank(billInfo.getAppName())) {
                            repayInfo.setAppName(billInfo.getAppName());
                        } else {
                            repayInfo.setAppName("nyd");
                        }
                        repayDao.save(repayInfo);
                        String billStatus = billInfo.getBillStatus();
                        BigDecimal alreadyRepayAmount = billInfo.getAlreadyRepayAmount().add(message.getPayAmount());
                        BigDecimal waitRepayAmount = billInfo.getWaitRepayAmount();
                        //本次还款金额大于等于剩余待还款金额
                        if (message.getPayAmount().compareTo(waitRepayAmount) == 1
                                || message.getPayAmount().compareTo(waitRepayAmount) == 0) {
                            billInfo.setWaitRepayAmount(new BigDecimal(0));
                            billInfo.setAlreadyRepayAmount(alreadyRepayAmount);
                            billInfo.setWaitRepayPrinciple(new BigDecimal(0));
                            //实际结清时间
                            billInfo.setActualSettleDate(new Date());
                            billInfo.setBillStatus(BillStatusEnum.REPAY_SUCCESS.getCode());
                            //更新账单信息
                            billService.updateBillInfoByBillNo(billInfo);
                            if (BillStatusEnum.REPAY_OVEDUE.getCode().equals(billStatus)) {
                                //逾期中还款
                                OverdueBillInfo overdueBillInfo = billService.getOverdueBillInfo(message.getBillNo()).getData();
                                overdueBillInfo.setBillStatus(BillStatusEnum.REPAY_SUCCESS.getCode());
                                //更新逾期账单（OverdueBill）状态
                                billService.updateOverdueBillInfoByBillNo(overdueBillInfo);
                            }
                            //如果是银码头通知银码头
                            if (StringUtils.isNotBlank(billInfo.getIbankOrderNo())) {
                                RepayMessage msg = new RepayMessage();
                                msg.setRepayTime(message.getSubmitPayEndTime());
                                msg.setBillNo(billInfo.getIbankOrderNo());
                                repayToIbankProducer.sendMsg(msg);
                            }
                        } else {
                            BigDecimal waitRepayPrinciple = billInfo.getWaitRepayPrinciple();
                            //剩余待还款本金大于0
                            if (waitRepayPrinciple.compareTo(new BigDecimal(0)) == 1) {
                                //剩余待还款本金小于等于本次还款金额
                                if (waitRepayPrinciple.compareTo(message.getPayAmount()) == 0
                                        || waitRepayPrinciple.compareTo(message.getPayAmount()) == -1) {
                                    billInfo.setWaitRepayPrinciple(new BigDecimal(0));
                                } else {
                                    billInfo.setWaitRepayPrinciple(waitRepayPrinciple.subtract(message.getPayAmount()));
                                }
                            }
                            billInfo.setAlreadyRepayAmount(alreadyRepayAmount);
                            waitRepayAmount = waitRepayAmount.subtract(message.getPayAmount());
                            billInfo.setWaitRepayAmount(waitRepayAmount);
                            //更新账单信息
                            billService.updateBillInfoByBillNo(billInfo);
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error("CollectionToNydConsumer nyd error",e);
            }
        }

    }
}
