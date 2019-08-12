package com.nyd.order.model.order;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 认证信息图片
 * @author admin
 *
 */
@Data
@JsonIgnoreProperties
@ApiModel(value = "UserPicture", description = "用户证件")
public class UserPicture implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "证件正面")
	private String idCardPicFront;//证件正面
	@ApiModelProperty(value = "证件背面")
	private String idCardPicReverse;//证件背面
	@ApiModelProperty(value = "活体照片")
	private String icCardPic;//活体照片
}
