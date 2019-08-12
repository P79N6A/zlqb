package com.nyd.zeus.model.xunlian.req;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description="代付")
public class XunlianQueryChargeVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="订单号")
	private String orderId;
	

	
	
	
	
}
