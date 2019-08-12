package com.nyd.order.service.rabbit;

import org.springframework.stereotype.Component;

/**
 * @author liuqiu
 */
@Component
public class WithholdProducer extends BaseProducer{
    @Override
    public String getPattern() {
        return "withhold";
    }
}
