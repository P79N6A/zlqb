package com.nyd.capital.service.mq;

import com.nyd.capital.model.RemitMessage;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;

/**
 * Created by dengw on 2017/11/27.
 */
public class ToBillRabbitMqProducer {
    public static void main(String args[]) {
        String xml = "classpath:com/nyd/capital/configs/service/xml/nyd-capital-rabbit-producer.xml";

        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext(xml);
        RabbitmqProducerProxy rabbitmqProducerProxy = classPathXmlApplicationContext.getBean("rabbitmqProducerProxy",RabbitmqProducerProxy.class);

        //MessageProperties messageProperties = classPathXmlApplicationContext.getBean("messageProperties",MessageProperties.class);;
        //Message message = new Message(billInfo.toString().getBytes(),messageProperties);
//        OrderMessage orderMessage = new OrderMessage();
//        orderMessage.setOrderNo("1234");
//        orderMessage.setUserId("5678");

        RemitMessage remitMessage = new RemitMessage();
        remitMessage.setOrderNo("101512475540163001");
        remitMessage.setRemitStatus("0");
       remitMessage.setRemitAmount(new BigDecimal("1000.00"));



        for(int i=0;i<20;i++) {
            rabbitmqProducerProxy.convertAndSend("remit.order", remitMessage);
        }

        /*for(int i=0;i<10;i++){
            String s = "lait.zhang-" + i;
            Message message = new Message(s.getBytes(),messageProperties);
            //rabbitmqProducerProxy.send(messages);
            rabbitmqProducerProxy.send("zeus.lait",message);
        }*/
    }
}
