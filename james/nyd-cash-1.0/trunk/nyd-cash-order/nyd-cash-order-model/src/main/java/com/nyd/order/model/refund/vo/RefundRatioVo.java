package com.nyd.order.model.refund.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties
@ApiModel(value = "退款比例查询出参", description = "退款比例查询出参")
public class RefundRatioVo implements Serializable {
	
	private static final long serialVersionUID = 7877160701586345985L;

	@ApiModelProperty(value = "今日总收入")
    private String totalIncome;
	
	@ApiModelProperty(value = "已退款")
    private String refunded;
	
	@ApiModelProperty(value = "退款比例")
    private String refundRatio;

	public String getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(String totalIncome) {
		this.totalIncome = totalIncome;
	}

	public String getRefunded() {
		return refunded;
	}

	public void setRefunded(String refunded) {
		this.refunded = refunded;
	}

	public String getRefundRatio() {
		return refundRatio;
	}

	public void setRefundRatio(String refundRatio) {
		this.refundRatio = refundRatio;
	}

}
