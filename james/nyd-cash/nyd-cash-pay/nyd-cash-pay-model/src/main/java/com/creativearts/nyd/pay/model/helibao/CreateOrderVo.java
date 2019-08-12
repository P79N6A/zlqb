package com.creativearts.nyd.pay.model.helibao;

import com.creativearts.nyd.pay.model.annotation.RequireField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

//下单请求数据
@Data
public class CreateOrderVo implements Serializable{
    @RequireField
    private String P1_bizType = "Withhold";//接口类型

    @RequireField
    private String P2_customerNumber;//商户编号

    @RequireField
    private String P3_orderId;//商户订单号

    @RequireField
    private String P4_timestamp;//时间戳

    @RequireField
    private String P5_payerName;//姓名

    @RequireField
    private String P6_idCardType = "IDCARD";//证件类型

    @RequireField
    private String P7_idCardNo;//证件号码

    @RequireField
    private String P8_cardNo;//银行卡号

    private String P9_phone;//手机号码

    @RequireField
    private String P10_currency = "CNY";//货币类型 暂只支持人民币：CNY

    @RequireField
    private BigDecimal P11_orderAmount;//金额 两位小数 订单金额，以元为单位，最小金额为1.01

    @RequireField
    private String P12_goodsCatalog = "100030";//商户类别

    @RequireField
    private String P13_goodsName = "支付";//商户名称

    private String P14_goodsDesc;//商品描述

    @RequireField
    private String P15_terminalType ="IMEI";//终端类型

    @RequireField
    private String P16_terminalId = "122121212121";//终端标识

    @RequireField
    private String P17_orderIp="127.0.0.1";//下单IP

    @RequireField
    private String P18_serverCallbackUrl;//服务器通知回调地址


    private String sign;//签名

    
	
}
