package com.nyd.admin.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
@Table(name="t_refund")
public class Refund implements Serializable {
	
	@Id
	private Long id;
	
	private String userId;
	
	private String userName;
	
	private String accountNumber;
	
	private String refundNo;
	
	private String orderNo;
	
	private BigDecimal refundAmonut;
	//999初始化状态（申请未提交）1000处理中1001审核通过1002审核拒绝
	private Integer requestStatus;
	
	private Integer deleteFlag;
	
	private String createTime;
	
	private String updateTime;
	
	private String updateBy;

}
