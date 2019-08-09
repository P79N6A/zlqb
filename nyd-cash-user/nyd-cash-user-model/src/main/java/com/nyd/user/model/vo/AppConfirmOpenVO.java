package com.nyd.user.model.vo;

import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
@Data
public class AppConfirmOpenVO {
	@ApiModelProperty(value = "用户id")
	private String userId;
	@ApiModelProperty(value = "银行卡号")
	private String cardNumber;
	@ApiModelProperty(value = "客户姓名")
	private String userName;
	@ApiModelProperty(value = "手机号码")
	private String mobile;
	@ApiModelProperty(value = "商户订单号")
	private String merchantNumber;
	@ApiModelProperty(value = "短信验证吗")
	private String verifyCode;
	@ApiModelProperty(value = "银行编码")
	private String bankCode;
	@ApiModelProperty(value = "身份证号")
	private String custIc;
	@ApiModelProperty(value = "合利宝用户id")
	private String hlbUserId;
	
}
