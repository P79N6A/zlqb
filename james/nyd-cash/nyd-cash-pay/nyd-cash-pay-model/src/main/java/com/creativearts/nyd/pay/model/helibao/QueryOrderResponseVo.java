package com.creativearts.nyd.pay.model.helibao;

import lombok.Data;

import java.io.Serializable;

@Data
public class QueryOrderResponseVo implements Serializable{
	  private String rt1_bizType;//交易类型
	  private String rt2_retCode;//返回码
	  private String rt3_retMsg;//返回信息
	  private String rt4_customerNumber;//商户编号
	  private String rt5_orderId;//商户订单号
	  private String rt6_serialNumber;//平台流水号
	  private String rt7_orderAmount;//交易金额
	  private String rt8_bankName;//银行卡名称
	  private String rt9_bankCode;//银行编码
	  private String rt10_onlineCardType;//银行卡类型
	  private String rt11_completeDate;//订单完成时间
	  private String rt12_cardAfterFour;//银行卡后四位
	  private String rt13_orderStatus;//订单状态
	  private String rt14_reason;//错误原因
	  private String sign;//签名
	  

}
