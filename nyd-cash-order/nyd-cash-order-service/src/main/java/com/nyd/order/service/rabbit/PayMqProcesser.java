package com.nyd.order.service.rabbit;

import com.nyd.capital.model.RemitMessage;
import com.nyd.order.dao.OrderDao;
import com.nyd.order.dao.OrderStatusLogDao;
import com.nyd.order.entity.OrderStatusLog;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.enums.OrderStatus;
import com.tasfe.framework.rabbitmq.RabbitmqMessageProcesser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created by Dengw on 2017/12/1
 */
public class PayMqProcesser extends BaseProcesser implements RabbitmqMessageProcesser<RemitMessage> {
    private static Logger LOGGER = LoggerFactory.getLogger(PayMqProcesser.class);

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderStatusLogDao orderStatusLogDao;

    @Override
    public void processMessage(RemitMessage message) {
        if(message != null){
            LOGGER.info("receive msg from pay, msg is " + message.toString());
            try {
                List<OrderInfo> list = orderDao.getObjectsByOrderNo(message.getOrderNo());
                if(list != null && list.size()>0){
                    if(OrderStatus.WAIT_LOAN.getCode() < list.get(0).getOrderStatus()){
                        LOGGER.info("update order status failed, current status gt update status! orderNo="+message.getOrderNo());
                        return;
                    }
                }
            } catch (Exception e) {
                LOGGER.error("get orderInfo failed, orderNo = " + message.getOrderNo() ,e);
            }
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderNo(message.getOrderNo());
            orderInfo.setOrderStatus(OrderStatus.LOAN_FAIL.getCode());
            orderInfo.setPayTime(new Date());
            try {
                orderDao.update(orderInfo);
                OrderStatusLog orderStatusLog = new OrderStatusLog();
                orderStatusLog.setOrderNo(message.getOrderNo());
                orderStatusLog.setBeforeStatus(OrderStatus.WAIT_LOAN.getCode());
                orderStatusLog.setAfterStatus(OrderStatus.LOAN_FAIL.getCode());
                orderStatusLogDao.save(orderStatusLog);
            } catch (Exception e) {
                LOGGER.error("update order failed, orderNo = " + message.getOrderNo() ,e);
            }
            //发送短信通知用户
//            sendMobileSms(7,message.getOrderNo());
        }else{
            LOGGER.error("message is null!");
        }
    }
}
