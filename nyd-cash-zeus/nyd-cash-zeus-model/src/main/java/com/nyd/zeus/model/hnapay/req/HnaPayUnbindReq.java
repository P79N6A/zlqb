package com.nyd.zeus.model.hnapay.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;

@Data
@ApiModel(description = "解绑卡")
public class HnaPayUnbindReq implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "业务协议号")
	private String bizProtocolNo;

	@ApiModelProperty(value = "支付协议号")
	private String payProtocolNo;
	
}
