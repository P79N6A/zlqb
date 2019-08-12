package com.nyd.order.model.refund.request;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nyd.order.model.common.PageCommon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 退款处理列表查询入参
 *
 */
@JsonIgnoreProperties
@ApiModel(value = "退款处理列表查询入参", description = "退款处理列表查询入参")
public class RefundListRequest extends PageCommon implements Serializable {
	
	private static final long serialVersionUID = 7067284048294496570L;

	@ApiModelProperty(value = "退款时间")
    private String refundDate;

	@ApiModelProperty(value = "客户姓名")
    private String name;
    
	@ApiModelProperty(value = "手机号码")
    private String phone;
    
	@ApiModelProperty(value = "状态 0:待处理、1:处理中、2:已退款、3:退款失败、4:拒绝")
    private String status;
	
	@ApiModelProperty(value = "开始日期")
    private String beginTime;
	
	@ApiModelProperty(value = "结束日期")
    private String endTime;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRefundDate() {
		return refundDate;
	}

	public void setRefundDate(String refundDate) {
		this.refundDate = refundDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
