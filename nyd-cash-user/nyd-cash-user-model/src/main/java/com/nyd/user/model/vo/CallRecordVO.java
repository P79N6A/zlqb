package com.nyd.user.model.vo;

import java.io.Serializable;

import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 客户通讯录实体
 */
@Data
public class CallRecordVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "订单id")
	private String orderId;
	@ApiModelProperty(value = "客户id")
	private String custInfoId;
	@ApiModelProperty(value = "手机号")
	private String mobile;
	@ApiModelProperty(value = "客户姓名")
	private String name;
	@ApiModelProperty(value = "呼叫类型")
	private String callStaus;
	@ApiModelProperty(value = "呼叫时长")
	private String callLength;
	@ApiModelProperty(value = "最近一次呼叫时间")
	private String callTime;
	

	
}
