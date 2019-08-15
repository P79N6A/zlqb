package com.nyd.zeus.model.hnapay.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;

@Data
@ApiModel(description = "单笔付款")
public class HnaPayPayReq implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "商户请求流水号")
	private String merOrderId;

	@ApiModelProperty(value = "支付金额")
	private String tranAmt;

	@ApiModelProperty(value = "收款方姓名")
	private String payeeName;

	@ApiModelProperty(value = "收款方账户")
	private String payeeAccount;

	@ApiModelProperty(value = "商户异步通知地址")
	private String notifyUrl;

}
