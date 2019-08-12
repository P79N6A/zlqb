package com.nyd.order.service.rabbit;

import org.springframework.stereotype.Component;

/**
 * Created by Dengw on 2017/11/27
 * 订单往审核发送消息
 */
@Component
public class OrderToAuditProducer extends BaseProducer {
    @Override
    public String getPattern() {
        return "audit";
    }
}
