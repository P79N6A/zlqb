package com.nyd.order.model.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * Created by  on 2019/07/17
 * 新增退款申请
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SaveRefunRecordVo implements Serializable{
	
	private static final long serialVersionUID = 1L;

	
	//订单编号
	@ApiModelProperty(value = "订单编号")
    private String orderNo;
	@ApiModelProperty(value = "退款日期")
    private String refundTime;
	@ApiModelProperty(value = "退款金额")
    private String refundAmount;
	@ApiModelProperty(value = "备注")
    private String remark;
	
	private String sysUserId;
	private String sysUserName;
	
}
