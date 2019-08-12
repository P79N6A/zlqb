package com.creativearts.nyd.pay.model.yinshengbao;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessageReturn implements Serializable {

    //用户授权码
    private String token;

    //用户id
    private String customerId;

    //订单号
    private String orderNo;

    //短信验证码
    private String vericode;

}
