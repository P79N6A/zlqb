package com.creativearts.nyd.pay.model.helibao;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 绑卡支付请求对象
 */
@Data
public class BindCardPayVo implements Serializable {

    //交易类型
    private String P1_bizType = "QuickPayBindPay";

    //商户编号
    private String P2_customerNumber;

    //绑卡ID
    private String P3_bindId;

    //用户id
    private String P4_userId;

    //商户订单号
    private String P5_orderId;

    //时间戳
    private String P6_timestamp;

    //交易币种
    private String P7_currency = "CNY";        //货币类型 暂只支持人民币：CNY

    //交易金额
    private BigDecimal P8_orderAmount;         //金额 两位小数 订单金额，以元为单位，最小金额为1.01

    //商品名称
    private String P9_goodsName  = "支付";

    //商品描述
    private String P10_goodsDesc;

    //终端类型
    private String P11_terminalType ="IMEI";

    //终端标识
    private String P12_terminalId = "122121212121";

    //下单ip
    private String P13_orderIp ="127.0.0.1";

    //订单有效时间
    private String P14_period;

    //订单有效时间单位
    private String P15_periodUnit;

    //服务器通知回调地址
    private String P16_serverCallbackUrl;

    //短信验证码
    private String P17_validateCode;

    //签名
    private String sign;
}
