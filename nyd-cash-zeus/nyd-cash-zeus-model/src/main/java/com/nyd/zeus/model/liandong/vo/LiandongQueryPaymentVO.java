package com.nyd.zeus.model.liandong.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description="签约短信")
public class LiandongQueryPaymentVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="商户订单号")
	private String order_id;
	
}
