package com.nyd.order.service.rabbit;

import org.springframework.stereotype.Component;

/**
 * Created by Dengw on 2017/11/27
 * 订单往支付发送消息
 */
@Component
public class OrderToPayProducer extends BaseProducer {
    @Override
    public String getPattern() {
        return "pay";
    }
}
