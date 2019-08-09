package com.nyd.mq.service.rabbit;

import com.nyd.member.model.msg.MemberFeeLogMessage;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by hwei on 2018/01/09.
 * 会员费代扣后发消息通知会员
 */
@Component
public class PayToMemberProducer {

    @Autowired
    RabbitmqProducerProxy rabbitmqProducerProxy;

    public void sendMsg(MemberFeeLogMessage msg){
        String pattern = getPattern();
        rabbitmqProducerProxy.convertAndSend(pattern,msg);
    }
    public String getPattern() {
        return "mfee.nyd";
    }
}
