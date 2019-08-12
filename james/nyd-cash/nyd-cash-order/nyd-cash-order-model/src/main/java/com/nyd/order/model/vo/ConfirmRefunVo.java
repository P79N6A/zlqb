package com.nyd.order.model.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * Created by  on 2019/07/17
 * 确认退款
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ConfirmRefunVo implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "流水id")
    private String refundId;
	@ApiModelProperty(value = "用户id")
    private String userId;
	@ApiModelProperty(value = "结果:1 同意 0 拒绝")
    private Integer isResult;
	@ApiModelProperty(value = "退款金额")
	private String refundAmount;
	@ApiModelProperty(value = "备注")
	private String remark;
	private String sysUserId;
	private String sysUserName;
	private String bankNumber;
	
}
