package com.nyd.zeus.model.liandong.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description="协议支付")
public class LiandongPaymentVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="客户订单号")
	private String mer_cust_id;
	
	@ApiModelProperty(value="支付协议号")
	private String usr_pay_agreement_id;
	
	@ApiModelProperty(value="用户协议号")
	private String usr_busi_agreement_id;
	
	@ApiModelProperty(value="联动交易号 16位")
	private String trade_no;
	 
	
}
