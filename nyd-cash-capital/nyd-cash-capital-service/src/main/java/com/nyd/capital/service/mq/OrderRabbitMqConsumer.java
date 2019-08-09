package com.nyd.capital.service.mq;

import com.alibaba.fastjson.JSON;
import com.nyd.capital.model.enums.FundSourceEnum;
import com.nyd.capital.model.enums.RemitStatus;
import com.nyd.capital.service.FundFactory;
import com.nyd.capital.service.FundService;
import com.nyd.order.api.CapitalOrderRelationContract;
import com.nyd.order.model.enums.BorrowConfirmChannel;
import com.nyd.order.model.msg.OrderMessage;
import com.nyd.zeus.api.RemitContract;
import com.tasfe.framework.rabbitmq.RabbitmqMessageProcesser;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Component
public class OrderRabbitMqConsumer implements RabbitmqMessageProcesser<OrderMessage> {

    private static Logger LOGGER = LoggerFactory.getLogger(OrderRabbitMqConsumer.class);
    @Autowired
    private FundFactory fundFactory;
    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;
    @Autowired
    private RemitContract remitContract;
    @Autowired
    private CapitalOrderRelationContract capitalOrderRelationContract;

    @Override
    public void processMessage(OrderMessage message) {

//        try {
//            String userId = message.getUserId();
//            String orderNo = message.getOrderNo();
//            String fundCode = message.getFundCode();
//
//            LOGGER.info("放款rabbitmq消费" + JSON.toJSONString(message));
//
//            if (message.getChannel() == null) {
//                message.setChannel(BorrowConfirmChannel.NYD.getChannel());
//            }
////        String fundCode = "nyd1002"; // 空中金融
////        String fundCode = "nyd1001";
//
//            FundSourceEnum fundSourceEnum = FundSourceEnum.toEnum(fundCode);
//            if (fundSourceEnum == null) {
//                LOGGER.error("根据资金源编码 获取不到资金源" + orderNo);
//                return;
//            }
//
//            // 根据资金的工厂类，进行构建资金类
//            FundService fundService = fundFactory.buildChannel(fundSourceEnum);
//            List orders = null;
//
//            try {
//                // 拼装所有请求空中金融的订单列表
//                orders = fundService.generateOrders(userId, orderNo, message.getChannel());
//            } catch (Exception e) {
//                LOGGER.info(userId + "-" + orderNo + "生成订单错误" + e.getMessage());
////            RemitMessage remitMessage = new RemitMessage();
////            remitMessage.setRemitStatus("1");
////            remitMessage.setOrderNo(orderNo);
////            rabbitmqProducerProxy.convertAndSend("pay.nyd",remitMessage);
////            RemitInfo remitInfo = new RemitInfo();
////            remitInfo.setOrderNo(orderNo);
////            remitInfo.setRemitStatus("1");
////
////            remitInfo.setErrorCode("001");
////
////            try {
////                remitContract.save(remitInfo);
////            } catch (Exception e1) {
////                e1.printStackTrace();
////            }
//                return;
//            }
//            if (orders == null || orders.size() == 0) {
//                LOGGER.error("组装订单数据失败" + orderNo);
//                return;
//            }
//            try {
//                // 推送资产到资金方
//                RemitStatus result = fundService.sendOrder(orders);
//                if (result == RemitStatus.CORRECT) {
//                    if (fundSourceEnum == FundSourceEnum.KZJR) {
//                        LOGGER.info("等待kzjr异步通知");
//                    } else if (fundSourceEnum == FundSourceEnum.WT) {
//                        LOGGER.info("等待稳通放款");
//                    } else if (fundSourceEnum == FundSourceEnum.QCGZ) {
//                        LOGGER.info("等待七彩格子放款");
//                    } else if (fundSourceEnum == FundSourceEnum.JX) {
//                        LOGGER.info("等待即信放款");
//                    }
//
//                } else if (result == RemitStatus.ERROR) {
//                    if (fundSourceEnum == FundSourceEnum.KZJR) {
//                        LOGGER.error("kzjr提交订单返回失败" + orderNo);
//                    } else if (fundSourceEnum == FundSourceEnum.WT) {
//                        LOGGER.error("wt提交订单返回失败" + orderNo);
//                    } else if (fundSourceEnum == FundSourceEnum.QCGZ) {
//                        LOGGER.error("qcgz提交订单返回失败" + orderNo);
//                    } else if (fundSourceEnum == FundSourceEnum.JX) {
//                        LOGGER.error("jx提交订单返回失败" + orderNo);
//                    }
//                    //            RemitMessage remitMessage = new RemitMessage();
//                    //            remitMessage.setRemitStatus("1");
//                    //            remitMessage.setOrderNo(orderNo);
//                    //            rabbitmqProducerProxy.convertAndSend("pay.nyd",remitMessage);
//                    //
//                    //            RemitInfo remitInfo = new RemitInfo();
//                    //            remitInfo.setOrderNo(orderNo);
//                    //            remitInfo.setRemitStatus("1");
//                    //
//                    //            remitInfo.setErrorCode("002");
//                    //
//                    //            try {
//                    //                remitContract.save(remitInfo);
//                    //            } catch (Exception e1) {
//                    //                e1.printStackTrace();
//                    //            }
//                }
//            } catch (Exception e) {
//                LOGGER.error("sendOrder to fund has exception,order is " + orderNo, e);
//            }
//        } catch (Exception e) {
//            LOGGER.error("接受审核通过消息进行处理时发生异常");
//        }
//    }
//
//
//    public static void main(String args[]) {
//        String xml = "com/nyd/capital/configs/rabbit/xml/nyd-capital-rabbit-consumer.xml";
//        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext(xml);
//    }
//
    }
}
