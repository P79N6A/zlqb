package com.nyd.zeus.model.liandong.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description="取消绑卡")
public class LiandongCancelVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="商户用户标识")
	private String mer_cust_id;
	
	@ApiModelProperty(value="业务协议号")
	private String usr_busi_agreement_id;
	
	@ApiModelProperty(value="支付协议号")
	private String usr_pay_agreement_id;
	
}
