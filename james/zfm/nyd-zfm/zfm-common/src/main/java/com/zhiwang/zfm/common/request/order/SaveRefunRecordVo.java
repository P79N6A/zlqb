package com.zhiwang.zfm.common.request.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zhiwang.zfm.common.request.PageCommon;


/**
 * Created by  on 2019/07/17
 * 新增退款申请
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("新增退款申请")
public class SaveRefunRecordVo{
	//订单编号
	@ApiModelProperty(value = "订单编号")
    private String orderNo;
    //订单状态
	@ApiModelProperty(value = "退款日期")
    private String refundTime;
    //产品期数
	@ApiModelProperty(value = "退款金额")
    private String refundAmount;
    //用户姓名
	@ApiModelProperty(value = "备注")
    private String remark;
   
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getRefundTime() {
		return refundTime;
	}
	public void setRefundTime(String refundTime) {
		this.refundTime = refundTime;
	}
	public String getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
