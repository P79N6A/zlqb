package com.nyd.zeus.model.helibao.vo.pay.req.chanpay;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "畅捷支付查询接口")
public class ChangjieQueryPayVO implements Serializable {

	static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "原交易请求流水")
	private String orderTrxId; // 原交易请求流水

	@ApiModelProperty(value = "渠道")
	private String code; // 渠道



	public String getOrderTrxId() {
		return orderTrxId;
	}

	public void setOrderTrxId(String orderTrxId) {
		this.orderTrxId = orderTrxId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
