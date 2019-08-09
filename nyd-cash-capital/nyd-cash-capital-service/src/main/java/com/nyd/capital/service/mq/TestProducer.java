package com.nyd.capital.service.mq;

import com.nyd.order.model.msg.OrderMessage;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Cong Yuxiang
 * 2017/12/4
 **/
public class TestProducer {
    public static void main(String args[]) {
        String xml = "classpath:com/nyd/capital/configs/service/xml/nyd-capital-rabbit-producer-test.xml";

        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext(xml);
        RabbitmqProducerProxy rabbitmqProducerProxy = classPathXmlApplicationContext.getBean("rabbitmqProducerProxy",RabbitmqProducerProxy.class);

        //MessageProperties messageProperties = classPathXmlApplicationContext.getBean("messageProperties",MessageProperties.class);;
        //Message message = new Message(billInfo.toString().getBytes(),messageProperties);
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setOrderNo("12345");
        orderMessage.setUserId("5678");






        rabbitmqProducerProxy.convertAndSend("test.order", orderMessage);

        /*for(int i=0;i<10;i++){
            String s = "lait.zhang-" + i;
            Message message = new Message(s.getBytes(),messageProperties);
            //rabbitmqProducerProxy.send(messages);
            rabbitmqProducerProxy.send("zeus.lait",message);
        }*/
    }
}
