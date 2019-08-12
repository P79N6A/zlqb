package com.nyd.order.service.rabbit;

import com.alibaba.fastjson.JSONObject;
import com.nyd.application.api.AgreeMentContract;
import com.nyd.capital.model.RemitMessage;
import com.nyd.order.dao.OrderDao;
import com.nyd.order.dao.OrderDetailDao;
import com.nyd.order.dao.OrderStatusLogDao;
import com.nyd.order.entity.OrderStatusLog;
import com.nyd.order.model.OrderDetailInfo;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.enums.OrderStatus;
import com.tasfe.framework.rabbitmq.RabbitmqMessageProcesser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Dengw on 2017/12/4
 */
public class ZeusMqProcesser implements RabbitmqMessageProcesser<RemitMessage> {
    private static Logger LOGGER = LoggerFactory.getLogger(ZeusMqProcesser.class);

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderDetailDao orderDetailDao;

    @Autowired
    private OrderStatusLogDao orderStatusLogDao;

    @Autowired(required = false)
    private AgreeMentContract agreeMentContract;


    @Override
    public void processMessage(RemitMessage message) {
        if (message != null&&"0".equals(message.getRemitStatus())) {
            LOGGER.info("receive msg from remit, msg is " + message.toString());
            Integer borrowTime = 0;
            try {
                List<OrderInfo> list = orderDao.getObjectsByOrderNo(message.getOrderNo());
                if(list != null && list.size()>0){
                    if(OrderStatus.WAIT_LOAN.getCode() < list.get(0).getOrderStatus()){
                        LOGGER.info("update order status failed, current status gt update status! orderNo="+message.getOrderNo());
                        return;
                    }
                    borrowTime = list.get(0).getBorrowTime();
                }
            } catch (Exception e) {
                LOGGER.error("get orderInfo failed, orderNo = " + message.getOrderNo() ,e);
            }
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderNo(message.getOrderNo());
            orderInfo.setOrderStatus(OrderStatus.LOAN_SUCCESS.getCode());
            orderInfo.setPayTime(message.getRemitTime()==null?new Date():message.getRemitTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(orderInfo.getPayTime());
            calendar.add(Calendar.DAY_OF_MONTH, +borrowTime);
            orderInfo.setPromiseRepaymentTime(calendar.getTime());
            try {
                orderDao.update(orderInfo);
                OrderStatusLog orderStatusLog = new OrderStatusLog();
                orderStatusLog.setOrderNo(message.getOrderNo());
                orderStatusLog.setBeforeStatus(OrderStatus.WAIT_LOAN.getCode());
                orderStatusLog.setAfterStatus(OrderStatus.LOAN_SUCCESS.getCode());
                orderStatusLogDao.save(orderStatusLog);
            } catch (Exception e) {
                LOGGER.error("update order failed, orderNo = " + message.getOrderNo() ,e);
            }
//            //协议签章
//            try {
//                List<OrderInfo> orderList = orderDao.getObjectsByOrderNo(message.getOrderNo());
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("orderNo",message.getOrderNo());
//                if (orderList!=null&&orderList.size()>0) {
//                    OrderInfo orderInfo1 = orderList.get(0);
//                    jsonObject.put("userId",orderInfo1.getUserId());
//                    jsonObject.put("loanMoney",orderInfo1.getLoanAmount());
//                    jsonObject.put("loanDay",orderInfo1.getBorrowTime());
//                    jsonObject.put("loanRate",orderInfo1.getAnnualizedRate());
//                }
//                List<OrderDetailInfo> detailList = orderDetailDao.getObjectsByOrderNo(message.getOrderNo());
//                if (detailList!=null&&detailList.size()>0) {
//                    OrderDetailInfo orderDetailInfo = detailList.get(0);
//                    jsonObject.put("userName",orderDetailInfo.getRealName());
//                    jsonObject.put("idNumber",orderDetailInfo.getIdNumber());
//                    jsonObject.put("mobile",orderDetailInfo.getMobile());
//                    jsonObject.put("model",orderDetailInfo.getModel());
//                    jsonObject.put("brand",orderDetailInfo.getBrand());
//                    jsonObject.put("deviceId",orderDetailInfo.getDeviceId());
//                }
//                agreeMentContract.signOtherAgreeMent(jsonObject);
//            } catch (Exception e) {
//                LOGGER.error("signOtherAgreeMent failed,orderNo= "+ message.getOrderNo(),e);
//            }
        }else if(message != null&&"1".equals(message.getRemitStatus())){
            LOGGER.info("receive msg from remit, msg is " + message.toString());
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
//            orderInfo.setPayTime(new Date());
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
        }else {
            LOGGER.error("message is null!");
        }
    }

}
