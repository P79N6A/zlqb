package com.creativearts.nyd.pay.model.helibao;

import lombok.Data;

import java.io.Serializable;

@Data
public class NotifyResponseVo implements Serializable{
	private String rt1_bizType;
	private String rt2_retCode;
	private String rt3_retMsg;
	 private String rt4_customerNumber;
	 private String rt5_orderId;
	 private String rt6_serialNumber;
	 private String rt7_orderAmount;
	 private String rt8_bankName;
	 private String rt9_bankCode;
	 private String rt10_onlineCardType;
	 private String rt11_completeDate;
	 private String rt12_cardAfterFour;
	 private String rt13_orderStatus;
	 private String sign;

	 
}
