package com.nyd.order.model.order;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties
@ApiModel(value = "orderDetials", description = "订单详情参数")
public class OrderDetialsVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "订单编号")
    private String orderNo;
	@ApiModelProperty(value = "客户姓名")
    private String userName;
	@ApiModelProperty(value = "userid")
    private String userid;
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public OrderDetialsVO(String orderNo, String userName, String userid) {
		super();
		this.orderNo = orderNo;
		this.userName = userName;
		this.userid = userid;
	}
	public OrderDetialsVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
