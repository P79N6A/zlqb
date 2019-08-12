package com.nyd.zeus.model.xunlian.req;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description="代扣")
public class XunlianPaymentVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="签约协议号")
	private String protocolId;
	
	@ApiModelProperty(value="姓名")
	private String name;
	
	@ApiModelProperty(value="交易账号")
	private String account;
	
	@ApiModelProperty(value="流水号",hidden=true)
	private String payOrderId;
	
	@ApiModelProperty(value="金额 两位小数，整数部分不超过13位，单位：元")
	private String amount;
	
	
}
