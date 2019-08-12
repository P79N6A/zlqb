package com.nyd.user.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
@Table(name="t_refund_order")
public class RefundOrder implements Serializable{
	
	@Id
	private Long id;
	
	private String userId;
	
	private String userName;
	
	private String accountNumber;
	
	private String merchantCode;
	
	private String businessOrderType;
	
	private String refundNo;
	
	private BigDecimal refundAmonut;
	
	private String bankAccount;
	
	private String bankName;
	
	private int refundStatus;
	
	private int deleteFlag;
	
	private String createTime;
	
	private String updateTime;
	
	private String updateBy;

}
