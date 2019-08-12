package com.nyd.capital.service.mq;

import com.rabbitmq.client.Channel;
import com.tasfe.framework.rabbitmq.RabbitmqConsumerProxy;
import com.tasfe.framework.rabbitmq.RabbitmqMessageProcesser;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.adapter.AbstractAdaptableMessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;

/**
 * Cong Yuxiang
 * 2017/12/4
 **/
@Setter
public class NewRabbitmqConsumerProxy extends AbstractAdaptableMessageListener implements ChannelAwareMessageListener, InitializingBean {
    private Logger logger = LoggerFactory.getLogger(RabbitmqConsumerProxy.class);

    private RabbitmqMessageProcesser rabbitmqMessageProcesser;

    private MessageConverter messageConverter;



    @Override
    public void onMessage(final Message message, final Channel channel) {
//        logger.info("开始处理消息: " + message.toString());

        // 获取对象
        Object msg = messageConverter.fromMessage(message);
        try {

            rabbitmqMessageProcesser.processMessage(msg);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception ae) {
            // trans.setStatus(ae);
            try {
                // 处理失败,调用nack，并将消息重新回到队列
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        } finally {
        }



    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }


}
