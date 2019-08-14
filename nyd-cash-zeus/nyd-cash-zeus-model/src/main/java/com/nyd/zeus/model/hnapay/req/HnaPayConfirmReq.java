package com.nyd.zeus.model.hnapay.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;

@Data
@ApiModel(description = "绑卡")
public class HnaPayConfirmReq implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "商户请求流水号")
	private String merOrderId;

	@ApiModelProperty(value = "三方请求流水号")
	private String hnapayOrderId;
	
	@ApiModelProperty(value = "短信")
	private String smsCode;

}
