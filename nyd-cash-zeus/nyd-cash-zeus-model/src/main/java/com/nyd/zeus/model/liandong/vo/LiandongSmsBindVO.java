package com.nyd.zeus.model.liandong.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description="签约短信")
public class LiandongSmsBindVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="卡号")
	private String card_id;
	
	@ApiModelProperty(value="手机号")
	private String media_id;
	
	@ApiModelProperty(value="姓名")
	private String card_holder;
	
	@ApiModelProperty(value="身份证")
	private String identity_code;
	
}
