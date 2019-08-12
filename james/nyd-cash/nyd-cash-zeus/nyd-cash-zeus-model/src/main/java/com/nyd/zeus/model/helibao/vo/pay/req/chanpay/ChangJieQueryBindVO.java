package com.nyd.zeus.model.helibao.vo.pay.req.chanpay;

import java.io.Serializable;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="畅捷绑卡查询接口")
public class ChangJieQueryBindVO  implements Serializable{
	 static final long serialVersionUID = 1L;
	 
	 @ApiModelProperty(value="用户标识",required=true)
	 private String merUserId ;
	 
	 @ApiModelProperty(value="渠道",required=true)
	 private String code ;  //渠道
	 
	 @ApiModelProperty(value="金额（元）最多两位小数",required=true)
	 private String amt ;  //金额（元）最多两位小数



	public String getMerUserId() {
		return merUserId;
	}

	public void setMerUserId(String merUserId) {
		this.merUserId = merUserId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	 
	 
}
