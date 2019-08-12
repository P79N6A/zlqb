package com.nyd.user.model.vo;

import com.nyd.user.model.common.PageCommon;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 查询客户信息VO
 */
@Data
@ApiModel(value = "custInfoQueryVO", description = "查询客户信息VO")
public class CustInfoQueryVO extends PageCommon{

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "订单id")
	private String orderId;
	@ApiModelProperty(value = "客户id")
	private String custInfoId;
	@ApiModelProperty(value = "手机号")
	private String mobile;


	@ApiModelProperty(value = "通话记录-手机号")
	private String callNo;

	@ApiModelProperty(value = "通话记录-姓名")
	private String name;

}
