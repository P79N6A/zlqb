package com.nyd.zeus.model.hnapay.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;

@Data
@ApiModel(description = "预扣款")
public class HnaPayPreTransReq implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "支付金额")
	private String amount;

	@ApiModelProperty(value = "业务协议号")
	private String bizProtocolNo;
	
	@ApiModelProperty(value = "支付协议号")
	private String payProtocolNo;
	
	@ApiModelProperty(value = "后台通知地址")
	private String notifyUrl = "http://www.baidu.com";
	
	@ApiModelProperty(value = "商户用户ID")
	private String merUserId;
	
	@ApiModelProperty(value = "商户请求流水号")
	private String merOrderId;
}
