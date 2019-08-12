package com.creativearts.nyd.pay.model.yinshengbao;

import lombok.Data;

import java.io.Serializable;

@Data
public class YsbQuickPayNotifyResponseVo implements Serializable {

    //响应码
    private String result_code;

    //返回信息
    private String result_msg;

    //订单号
    private String orderNo;

    //用户 ID
    private String userId;

    //银行名称
    private String bankName;

    //银行卡尾号
    private String tailNo;

    //商户授权码
    private String token;

    //金额
    private String amount;

    //数字签名
    private String mac;

}
