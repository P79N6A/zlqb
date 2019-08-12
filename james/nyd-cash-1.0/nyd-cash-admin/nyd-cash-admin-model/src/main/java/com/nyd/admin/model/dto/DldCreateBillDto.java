package com.nyd.admin.model.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DldCreateBillDto implements Serializable{
	
	private String bankcardNo;
	
	private String businessOrderNo;
	
	private String businessOrderType;
	
	private String idNumber;
	
	private String merchantCode;//商户号
	
	private String mobile;
	
	private String payAmount;
	
	private String payChannelCode;
	
	private String payType;
	
	private String userId;
	
	private String memberId;
	

}
