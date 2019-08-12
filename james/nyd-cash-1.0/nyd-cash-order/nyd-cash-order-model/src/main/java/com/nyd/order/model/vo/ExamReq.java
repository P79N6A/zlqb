package com.nyd.order.model.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class ExamReq implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//订单号
	private String orderNo;
	
	//审核产品
	private String checkProduct;
	
	//审核意见
	private String remark;
	
	private String loanUserId; //登录人
	
	private String loanUserName;
	
	private String checkProductName;
}
