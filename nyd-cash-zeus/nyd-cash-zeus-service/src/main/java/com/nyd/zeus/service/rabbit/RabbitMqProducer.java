package com.nyd.zeus.service.rabbit;

import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;

/**
 * Created by dengw on 2017/11/27.
 */
public class RabbitMqProducer {
   /* public static void main(String args[]) {
        String xml = "classpath:com/nyd/zeus/configs/service/xml/nyd-zeus-rabbit.xml";

        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext(xml);
        RabbitmqProducerProxy rabbitmqProducerProxy = classPathXmlApplicationContext.getBean("rabbitmqProducerProxy",RabbitmqProducerProxy.class);

        *//*ZeusMessage zeusMessage = new ZeusMessage();
        zeusMessage.setOrderNo("101512098526102001");
        zeusMessage.setRemitAmount(new BigDecimal(2000));
        zeusMessage.setRemitStatus("0");*//*

        RepayMessage rePayMessage = new RepayMessage();
        rePayMessage.setBillNo("1111");
        rePayMessage.setRepayAmount(new BigDecimal(2500));
        rePayMessage.setRepayStatus("0");


        for(int i=0;i<100;i++) {
            //rabbitmqProducerProxy.convertAndSend("zeus2.bill", zeusMessage);
            rabbitmqProducerProxy.convertAndSend("repay.bill", rePayMessage);
        }
    }*/
}
