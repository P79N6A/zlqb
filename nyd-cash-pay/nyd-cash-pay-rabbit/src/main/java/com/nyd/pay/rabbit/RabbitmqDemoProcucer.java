package com.nyd.pay.rabbit;

import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Lait on 10/17/2016.
 */
public class RabbitmqDemoProcucer {
    public static void main(String args[]) {
        String xml = "classpath:com/nyd/pay/configs/rabbit/xml/nyd-pay-rabbit-producer.xml";


        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext(xml);
        UserMessage userMessage = new UserMessage();
        RabbitmqProducerProxy rabbitmqProducerProxy = classPathXmlApplicationContext.getBean("rabbitmqProducerProxy",RabbitmqProducerProxy.class);

        MessageProperties messageProperties = classPathXmlApplicationContext.getBean("messageProperties",MessageProperties.class);;
        //org.springframework.messaging.Message message = MessageBuilder.withPayload(userMessage).setHeader("oo","hh").build();
        for(int i=0;i<100;i++){
            String s = "lait.zhang-" + i;
            Message message = new Message(s.getBytes(),messageProperties);
            //rabbitmqProducerProxy.send(messages);
            rabbitmqProducerProxy.send("sps.lait",message);
        }
        //Message receiveMessage = rabbitmqProducerProxy.sendAndReceive(messages);
        //System.out.println("=================" + receiveMessage.getBody());
    }
}
