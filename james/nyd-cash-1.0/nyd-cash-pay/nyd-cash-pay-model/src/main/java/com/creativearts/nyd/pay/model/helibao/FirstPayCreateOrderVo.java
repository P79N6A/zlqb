package com.creativearts.nyd.pay.model.helibao;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 首次支付下单请求数据
 */
@Data
public class FirstPayCreateOrderVo implements Serializable {
    //交易类型
    private String P1_bizType = "QuickPayCreateOrder";

    //商户编号
    private String P2_customerNumber;

    //用户id
    private String P3_userId;

    //商户订单号
    private String P4_orderId;

    //时间戳
    private String P5_timestamp;

    //姓名
    private String P6_payerName;

    //证件类型
    private String P7_idCardType = "IDCARD";

    //证件号码
    private String P8_idCardNo;

    //银行卡号
    private String P9_cardNo;

    /**
     * 目前我们这边支付均为储蓄卡
     */
    //信用卡有效期年份(当银行卡是信用卡时必输)
   private String P10_year;
//
//    //信用卡有效期月份(当银行卡是信用卡时必输)
   private String P11_month;
//
//    //cvv2(当银行卡是信用卡时必输)
    private String P12_cvv2;

    //手机号码
    private String P13_phone;

    //交易币种
    private String P14_currency = "CNY";        //货币类型 暂只支持人民币：CNY

    //交易金额
    private BigDecimal P15_orderAmount;         //金额 两位小数 订单金额，以元为单位，最小金额为1.01

    //商品名称
    private String P16_goodsName  = "支付";

    //商品描述（非必传参数）
    private String P17_goodsDesc;

    //终端类型
    private String P18_terminalType ="IMEI";

    //终端标识
    private String P19_terminalId = "122121212121";

    //下单ip
    private String P20_orderIp ="127.0.0.1";

    //订单有效时间（非必传参数）
    private String P21_period;

    //有效时间单位（非必传参数）
    private String P22_periodUnit;

    //服务器通知回调地址
    private String P23_serverCallbackUrl;

    //银行卡信息参数是否加密（非必传参数）
//    private String P24_isEncrypt;

    //签名
    private String sign;//签名
}
