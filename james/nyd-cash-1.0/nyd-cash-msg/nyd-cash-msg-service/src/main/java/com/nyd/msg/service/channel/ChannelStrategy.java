package com.nyd.msg.service.channel;

import com.nyd.msg.service.utils.Message;

public interface ChannelStrategy {

    boolean sendSms(Message vo, boolean batch);

}
