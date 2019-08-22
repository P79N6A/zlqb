package com.nyd.zeus.model.liandong.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description="签约短信")
public class LiandongChargeVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="通知地址")
	private String notify_url;
	
	@ApiModelProperty(value="商户订单号")
	private String order_id;
	
	@ApiModelProperty(value="付款金额（元 保留2位小数）")
	private String amount;
	
	@ApiModelProperty(value="收款方账号")
	private String recv_account;
	
	@ApiModelProperty(value="收款方户名")
	private String recv_user_name;
	
	@ApiModelProperty(value="原商户订单日期 (YYYYMMDD)")
	private String mer_date;
	 
	
	
}
