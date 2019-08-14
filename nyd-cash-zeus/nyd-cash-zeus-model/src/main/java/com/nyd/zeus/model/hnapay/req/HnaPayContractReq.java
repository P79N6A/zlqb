package com.nyd.zeus.model.hnapay.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;

@Data
@ApiModel(description = "预绑卡")
public class HnaPayContractReq implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "用户Id")
	private String merUserId;
	
	@ApiModelProperty(value = "姓名")
	private String holderName;

	@ApiModelProperty(value = "账号")
	private String cardNo;

	@ApiModelProperty(value = "手机号")
	private String mobileNo;

	@ApiModelProperty(value = "手机号")
	private String identityCode;

	@ApiModelProperty(value = "商户请求流水号")
	private String merOrderId;

}
