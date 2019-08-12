package com.zhiwang.zfm.common.response.bean.cust;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 基本信息订单集合
 */
public class CustBaseInfoOrderRespVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "平台编码")
	private String orderPlatCode;
	@ApiModelProperty(value = "平台名称")
	private String orderPlatName;
	@ApiModelProperty(value = "更新时间")
	private String orderUpdateTime;
	@ApiModelProperty(value = "注册时间")
	private String registerTime;
	@ApiModelProperty(value = "联系电话")
	private String orderMobile;
	@ApiModelProperty(value = "现居住地址")
	private String orderAddress;
	@ApiModelProperty(value = "qq号码")
	private String orderQq;
	@ApiModelProperty(value = "微信号码")
	private String orderWechar;
	@ApiModelProperty(value = "借款用途证明")
	private String repaymentPurpose;

	public String getOrderPlatCode() {
		return orderPlatCode;
	}

	public void setOrderPlatCode(String orderPlatCode) {
		this.orderPlatCode = orderPlatCode;
	}

	public String getOrderPlatName() {
		return orderPlatName;
	}

	public void setOrderPlatName(String orderPlatName) {
		this.orderPlatName = orderPlatName;
	}

	public String getOrderUpdateTime() {
		return orderUpdateTime;
	}

	public void setOrderUpdateTime(String orderUpdateTime) {
		this.orderUpdateTime = orderUpdateTime;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getOrderMobile() {
		return orderMobile;
	}

	public void setOrderMobile(String orderMobile) {
		this.orderMobile = orderMobile;
	}

	public String getOrderAddress() {
		return orderAddress;
	}

	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}

	public String getOrderQq() {
		return orderQq;
	}

	public void setOrderQq(String orderQq) {
		this.orderQq = orderQq;
	}

	public String getOrderWechar() {
		return orderWechar;
	}

	public void setOrderWechar(String orderWechar) {
		this.orderWechar = orderWechar;
	}

	public String getRepaymentPurpose() {
		return repaymentPurpose;
	}

	public void setRepaymentPurpose(String repaymentPurpose) {
		this.repaymentPurpose = repaymentPurpose;
	}

}
