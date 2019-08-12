package com.nyd.zeus.model.xunlian.req;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description="身份认证")
public class IdentifyauthVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="姓名")
	private String name;
	
	@ApiModelProperty(value="账号")
	private String account;
	
	@ApiModelProperty(value="银行名称")
	private String organName;
	
	@ApiModelProperty(value="手机号")
	private String mobile;
	
	@ApiModelProperty(value="手机号")
	private String idCode;
	
	@ApiModelProperty(value="请求流水号")
	private String merOrderId;


}
