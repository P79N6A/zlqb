package com.nyd.user.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
@Data
public class RefundOrderInfo implements Serializable{
	
	private String userId;
	
	private String userName;
	
	private String accountNumber;
	
	private String merchantCode;
	
	private String refundNo;
	
	private String payChannelOrder;
	
	private String bankAccount;
	
	private String businessOrderType;
	
	private String bankName;
	
	private BigDecimal refundAmonut;
	
	private int refundStatus;
	
	private String updateBy;
	
	private Integer pageNum;
	
	private Integer pageSize;

}
