package com.nyd.zeus.model.helibao.vo.pay.req.chanpay;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "畅捷支付接口")
@Data
public class ChangJiePayVO implements Serializable {

	private static final long serialVersionUID = 1L;





	@ApiModelProperty(value = "渠道")
	private String code; // 渠道 区分不同商户
	
	@ApiModelProperty(value = "原交易订单号")
	private String oriPayTrxId;
	
	
	@ApiModelProperty(value = "保留字段")
	private String extension; // 保留字段
	
	@ApiModelProperty(value = "短信验证码")
	private String smsCode; // 短信验证码
	
	
	

	
	

}
