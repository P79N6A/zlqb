package com.nyd.pay.rabbit;

import com.rabbitmq.client.Channel;
import com.tasfe.framework.rabbitmq.RabbitmqMessageProcesser;
import org.springframework.amqp.core.Message;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by Lait on 10/17/2016.
 */
public class RabbitmqDemoProcesser implements RabbitmqMessageProcesser {
    public static void main(String args[]) {

        String xml = "classpath:com/nyd/pay/configs/rabbit/xml/nyd-pay-rabbit-consumer.xml";
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext(xml);
    }


    public void processMessage(Message message, Channel channel) {

        try {
            /**
             * 消费者在开启acknowledge的情况下，对接收到的消息可以根据业务的需要异步对消息进行确认。
             * 然而在实际使用过程中，由于消费者自身处理能力有限，从rabbitmq获取一定数量的消息后，希望rabbitmq不再将队列中的消息推送过来，
             * 当对消息处理完后（即对消息进行了ack，并且有能力处理更多的消息）再接收来自队列的消息。
             * 在这种场景下，我们可以通过设置basic.qos信令中的prefetch_count来达到这种效果。
             */
            //channel.basicQos(5);
            /**We're about to tell the server to deliver us the messages from the queue.
             * Since it will push us messages asynchronously,
             * we provide a callback in the form of an object that will buffer the messages
             * until we're ready to use them. That is what QueueingConsumer does.*/
            //QueueingConsumer consumer = new QueueingConsumer(channel);
            /**
             * 把名字为orderQueue的Channel的值回调给QueueingConsumer,即使一个worker在处理消息的过程中停止了，这个消息也不会失效
             */
            //channel.basicConsume(messages.getMessageProperties().getConsumerQueue(), false, consumer);
            //QueueingConsumer.Delivery delivery = consumer.nextDelivery();

            Thread.sleep(2000);
            // 处理信息
            //doSomethings(new String(delivery.getBody()));
            doSomethings(message);
            // 手动确认处理下一个消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }  catch (InterruptedException e) {
            try {
                ////处理失败,调用nack，并将消息重新回到队列
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void doSomethings(String message){
        System.out.println("=======================================================" + message);
    }

    private void doSomethings(Message message){
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++" + message.getBody().toString());
    }

    @Override
    public void processMessage(Object message) {

    }
}
