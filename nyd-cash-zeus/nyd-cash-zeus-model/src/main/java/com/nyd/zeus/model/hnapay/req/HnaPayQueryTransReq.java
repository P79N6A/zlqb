package com.nyd.zeus.model.hnapay.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;

@Data
@ApiModel(description = "查询扣款")
public class HnaPayQueryTransReq implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "商户请求流水号")
	private String merOrderId;

	@ApiModelProperty(value = "原商户订单请求时间 格式：YYYYMMDD")
	private String submitTime;

}
