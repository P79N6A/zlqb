package com.nyd.order.model.order;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nyd.order.model.common.PageCommon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@JsonIgnoreProperties
@ApiModel(value = "orderParam", description = "查询未分配订单参数")
public class OrderParamVO extends PageCommon{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "客户姓名")
	private String userName;// 客户姓名
	@ApiModelProperty(value = "手机号码")
	private String phone;//手机号码
	@ApiModelProperty(value = "注册渠道")
	private String channel;//注册渠道
	@ApiModelProperty(value = "进入开始时间")
	private String inTimeStart;//进入开始时间
	@ApiModelProperty(value = "进入结束时间")
	private String inTimeEnd;//进入结束时间
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getInTimeStart() {
		return inTimeStart;
	}
	public void setInTimeStart(String inTimeStart) {
		this.inTimeStart = inTimeStart;
	}
	public String getInTimeEnd() {
		return inTimeEnd;
	}
	public void setInTimeEnd(String inTimeEnd) {
		this.inTimeEnd = inTimeEnd;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
