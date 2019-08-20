package com.nyd.zeus.model.xunlian.req;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description="对公代付")
public class XunlianChargeEnterVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="金额 两位小数，整数部分不超过13位（单位：元）")
	private String amount;
	
	@ApiModelProperty(value="姓名")
	private String name;
	
	@ApiModelProperty(value="身份证")
	private String idCode;
	
	@ApiModelProperty(value="卡号")
	private String account;
	
	@ApiModelProperty(value="订单号")
	private String merOrderId;
	
	@ApiModelProperty(value="银行名称")
	private String bankName;
	
	@ApiModelProperty(value="收款方联行行号")
	private String receiveAccount;
	
	@ApiModelProperty(value="收款方开户行省份(若商户未输入收款方联行行号，此字段必填)")
	private String receiveProvince;
	
	@ApiModelProperty(value="收款方开户行城市(若商户未输入收款方联行行号，此字段必填)")
	private String receiveCity;
	
	
	

	
	
	
	
}
