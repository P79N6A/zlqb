package com.nyd.zeus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;


/**
 * xingsheng on 2017/11/18.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentRiskRequestXinsheng implements Serializable{
     
	private String amount;

	//"业务协议号"
	private String bizProtocolNo;
	
	//"支付协议号"
	private String payProtocolNo;
	
	// "商户用户ID"
	private String merUserId;

    
    
    
}
