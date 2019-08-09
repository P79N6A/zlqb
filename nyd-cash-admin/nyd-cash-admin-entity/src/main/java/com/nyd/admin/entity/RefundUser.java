package com.nyd.admin.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
@Table(name="t_refund_user")
public class RefundUser implements Serializable{
	
	@Id
	private Long id;
	
	private String userId;
	
	private String userName;
	
	private String accountNumber;
	
	private String orderNo;
	
	private BigDecimal refundAmonut;
	
	private int ifRemove;
	
	private int deleteFlag;
	
	private String createTime;
	
	private String updateTime;
	
	private String updateBy;

}
