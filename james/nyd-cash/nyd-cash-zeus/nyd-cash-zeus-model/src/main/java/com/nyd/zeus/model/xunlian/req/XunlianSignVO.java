package com.nyd.zeus.model.xunlian.req;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description="确认签约")
public class XunlianSignVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="验证码")
	private String smsVerifyCode;
	
	@ApiModelProperty(value="流水号（原发送短信流水号）")
	private String merOrderId;
	
	@ApiModelProperty(value="短信发送编号（未知字段）")
	private String smsSendNo;
	
}
