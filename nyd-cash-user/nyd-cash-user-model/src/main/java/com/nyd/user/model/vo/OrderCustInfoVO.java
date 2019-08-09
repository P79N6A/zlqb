package com.nyd.user.model.vo;

import java.io.Serializable;

import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 订单详情-客户信息
 */
@Data
public class OrderCustInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "客户姓名")
	private String custName;
	@ApiModelProperty(value = "手机号码")
	private String mobile;
	@ApiModelProperty(value = "身份证号码")
	private String custIc;
	@ApiModelProperty(value = "单位名称")
	private String companyName;
	@ApiModelProperty(value = "单位地址")
	private String companyAddress;
	@ApiModelProperty(value = "常住地址")
	private String homeAddress;
	@ApiModelProperty(value = "月收入")
	private String monthlyIncome;
	@ApiModelProperty(value = "学历")
	private String education;
	@ApiModelProperty(value = "婚姻")
	private String marriage;
	@ApiModelProperty(value = "职业类型")
	private String jobType;
	@ApiModelProperty(value = "职业")
	private String job;
	
}
