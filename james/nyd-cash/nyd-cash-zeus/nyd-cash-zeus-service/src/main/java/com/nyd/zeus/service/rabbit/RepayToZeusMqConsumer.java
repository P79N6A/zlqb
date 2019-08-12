package com.nyd.zeus.service.rabbit;

import com.creativearts.nyd.pay.model.RepayMessage;
import com.nyd.zeus.model.BillInfo;
import com.nyd.zeus.model.OverdueBillInfo;
import com.nyd.zeus.service.BillService;
import com.nyd.zeus.service.enums.BillStatusEnum;
import com.tasfe.framework.rabbitmq.RabbitmqMessageProcesser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhujx on 2017/12/4.
 * 监听还款队列消息处理
 */
public class RepayToZeusMqConsumer implements RabbitmqMessageProcesser<RepayMessage> {

    private static Logger LOGGER = LoggerFactory.getLogger(RepayToZeusMqConsumer.class);

    @Autowired
    BillService billService;

    @Override
    public void processMessage(RepayMessage message) {

        if(message != null && "0".equals(message.getRepayStatus())){
            LOGGER.info(message.toString());
            try{
                BillInfo billInfo = billService.getBillInfo(message.getBillNo()).getData();
                if(billInfo != null){
                    String billStatus = billInfo.getBillStatus();
                    if (BillStatusEnum.REPAY_SUCCESS.getCode().equals(billStatus)){
                        LOGGER.info("该笔账单已还清,billNo is" + billInfo.getBillNo());
                        return;
                    }
                    BigDecimal alreadyRepayAmount =   billInfo.getAlreadyRepayAmount().add(message.getRepayAmount());
                    BigDecimal waitRepayAmount = billInfo.getWaitRepayAmount();

                    //本次还款金额大于等于剩余待还款金额
                    if(message.getRepayAmount().compareTo(waitRepayAmount) == 1
                            || message.getRepayAmount().compareTo(waitRepayAmount) == 0){

                        billInfo.setWaitRepayAmount(new BigDecimal(0));
                        billInfo.setAlreadyRepayAmount(alreadyRepayAmount);
                        billInfo.setWaitRepayPrinciple(new BigDecimal(0));
                        //实际结清时间
                        billInfo.setActualSettleDate(new Date());
                        billInfo.setBillStatus(BillStatusEnum.REPAY_SUCCESS.getCode());

                        //更新账单信息
                        billService.updateBillInfoByBillNo(billInfo);

                        if(BillStatusEnum.REPAY_OVEDUE.getCode().equals(billStatus)){
                            //逾期中还款
                            OverdueBillInfo overdueBillInfo = billService.getOverdueBillInfo(message.getBillNo()).getData();
                            overdueBillInfo.setBillStatus(BillStatusEnum.REPAY_SUCCESS.getCode());
                            //更新逾期账单（OverdueBill）状态
                            billService.updateOverdueBillInfoByBillNo(overdueBillInfo);
                        }
                    }else {
                        BigDecimal waitRepayPrinciple = billInfo.getWaitRepayPrinciple();
                        //剩余待还款本金大于0
                        if(waitRepayPrinciple.compareTo(new BigDecimal(0)) == 1){
                            //剩余待还款本金小于等于本次还款金额
                            if(waitRepayPrinciple.compareTo(message.getRepayAmount()) == 0
                                    || waitRepayPrinciple.compareTo(message.getRepayAmount()) == -1){
                                billInfo.setWaitRepayPrinciple(new BigDecimal(0));
                            }else{
                                billInfo.setWaitRepayPrinciple(waitRepayPrinciple.subtract(message.getRepayAmount()));
                            }
                        }

                        billInfo.setAlreadyRepayAmount(alreadyRepayAmount);
                        waitRepayAmount = waitRepayAmount.subtract(message.getRepayAmount());
                        billInfo.setWaitRepayAmount(waitRepayAmount);
                        //更新账单信息
                        billService.updateBillInfoByBillNo(billInfo);
                    }
                }
            }catch(Exception e){
                LOGGER.error("RepayToZeusMqConsumer Exception" + e);
            }
        }else{
            LOGGER.error("RepayToZeusMqConsumer message body is null" + message.toString());
        }
    }

    /*public static void main(String args[]) {
        String xml = "com/nyd/zeus/configs/service/xml/nyd-zeus-rabbit.xml";
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext(xml);
    }*/
}
