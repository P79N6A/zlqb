package com.nyd.user.model.vo;

import java.io.Serializable;

import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

@Data
public class AppPreCardVO  implements Serializable{
	@ApiModelProperty(value = "用户id")
	private String userId;
	@ApiModelProperty(value = "银行卡号")
	private String cardNumber;
	@ApiModelProperty(value = "手机号码")
	private String mobile;
	@ApiModelProperty(value = "商户订单号")
	private String merchantNumber;
}
