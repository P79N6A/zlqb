package com.nyd.zeus.model.hnapay.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;

@Data
@ApiModel(description = "扣款")
public class HnaPayTransReq implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "商户请求流水号")
	private String merOrderId;

	@ApiModelProperty(value = "三方支付订单号")
	private String hanpayOrderId;

	@ApiModelProperty(value = "短信验证码")
	private String smsCode = "";
}
