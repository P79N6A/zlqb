package com.nyd.zeus.model.xunlian;

import java.io.Serializable;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@ApiModel(description = "讯联主动支付接口")
@Data
public class XunlianReqPaymentVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "订单号")
	private String orderNo; // 订单号
}
