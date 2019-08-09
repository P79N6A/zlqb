package com.creativearts.nyd.pay.model.yinshengbao;

import lombok.Data;

import java.io.Serializable;

@Data
public class YsbQuickPayConfirmVo implements Serializable {

    //商户编号
    private String accountId;

    //用户 ID
    private String customerId;

    //订单号
    private String orderNo;

    //短信验证码
    private String vericode;

    //用户授权码
    private String token;

    //数字签名
    private String mac;
}
