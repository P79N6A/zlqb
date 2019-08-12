package com.nyd.user.model.request;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;

/**
 * 请求
 */
@Data
public class PayBackQueryReq implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "订单号")
    private String orderNo;
	
}
