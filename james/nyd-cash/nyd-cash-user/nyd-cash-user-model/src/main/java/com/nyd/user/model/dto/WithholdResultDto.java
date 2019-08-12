package com.nyd.user.model.dto;

import java.io.Serializable;

import lombok.Data;
@Data
public class WithholdResultDto implements Serializable{
	
	private String  amount;
	
	private String bankName;
	
	private String bankcardNo;
	
	private String businessOrderNo;
	
	private String businessOrderType;
	
	private String merchantCode;
	
	private String orderNo;
	
	private String payChannelCode;
	
	private String payOrderNo;
	
	private String state;

}
