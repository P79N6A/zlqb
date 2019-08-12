package com.nyd.zeus.service.rabbit;

import org.springframework.stereotype.Component;

/**
 * Created by zhujx on 2017/12/1.
 * 账单往订单发送消息
 */
@Component
public class ZeusToOrderProducer extends BaseProducer {
    @Override
    public String getPattern() {
        return "zeus";
    }
}
