package com.nyd.zeus.model.xunlian.req;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description="代扣查询")
public class XunlianQueryPayVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="商户订单号")
	private String merOrderId;
	
}
