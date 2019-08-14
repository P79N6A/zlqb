package com.nyd.zeus.model.hnapay.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;

@Data
@ApiModel(description = "退款")
public class HnaPayRefundReq implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "商户请求流水号")
	private String merOrderId;

	@ApiModelProperty(value = "原商户支付订单号")
	private String orgMerOrderId;
	
	@ApiModelProperty(value = "原订单支付下单请求时间 YYYYMMDDHHMMSS")
	private String orgSubmitTime;
	
	@ApiModelProperty(value = "原订单金额")
	private String orderAmt;
	
	@ApiModelProperty(value = "退款金额")
	private String refundOrderAmt;
	
	@ApiModelProperty(value = "商户异步通知地址")
	private String notifyUrl;

}
