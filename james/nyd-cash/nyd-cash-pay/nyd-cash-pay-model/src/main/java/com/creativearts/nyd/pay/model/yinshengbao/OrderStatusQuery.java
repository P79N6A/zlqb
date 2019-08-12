package com.creativearts.nyd.pay.model.yinshengbao;

import lombok.Data;

@Data
public class OrderStatusQuery {

    //订单号
    private String orderId;

    //商户编号
    private String accountId;

    //mac
    private String mac;
}
