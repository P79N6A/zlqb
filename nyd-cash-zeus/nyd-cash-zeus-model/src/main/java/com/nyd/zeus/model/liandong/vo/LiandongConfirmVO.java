package com.nyd.zeus.model.liandong.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description="签约短信")
public class LiandongConfirmVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="签约订单号")
	private String bind_id;
	
	@ApiModelProperty(value="验证码")
	private String verify_code;
	
}
