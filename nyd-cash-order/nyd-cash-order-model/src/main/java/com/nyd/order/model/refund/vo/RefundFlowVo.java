package com.nyd.order.model.refund.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties
@ApiModel(value = "退款列表查询入参", description = "退款列表查询入参")
public class RefundFlowVo implements Serializable {

	private static final long serialVersionUID = -2721301529029240453L;
	
	@ApiModelProperty(value = "id")
    private String id;
	@ApiModelProperty(value = "用户id")
    private String userId;
	@ApiModelProperty(value = "期望退款时间")
    private String refundDate;
	@ApiModelProperty(value = "状态")
    private String status;
	@ApiModelProperty(value = "退款金额")
    private String refundAmount;
	@ApiModelProperty(value = "实际退款金额")
    private String realRefundAmount;
	@ApiModelProperty(value = "备注")
    private String remarks;
	@ApiModelProperty(value = "实际退款时间")
    private String realRefundDate;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRefundDate() {
		return refundDate;
	}
	public void setRefundDate(String refundDate) {
		this.refundDate = refundDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}
	public String getRealRefundAmount() {
		return realRefundAmount;
	}
	public void setRealRefundAmount(String realRefundAmount) {
		this.realRefundAmount = realRefundAmount;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getRealRefundDate() {
		return realRefundDate;
	}
	public void setRealRefundDate(String realRefundDate) {
		this.realRefundDate = realRefundDate;
	}

}
