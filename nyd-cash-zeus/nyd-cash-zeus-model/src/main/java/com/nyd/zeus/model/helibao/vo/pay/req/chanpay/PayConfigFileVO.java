package com.nyd.zeus.model.helibao.vo.pay.req.chanpay;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@ApiModel(description = "畅捷支付配置文件获取接口")
public class PayConfigFileVO {
	@ApiModelProperty(value = "业务主键")
	private String id;			//业务主键
	@ApiModelProperty(value = "畅捷支付商户对应code")
	private String code;			//富友商户对应code
	@ApiModelProperty(value = "商户号")
	private String memberId;			//商户号
	@ApiModelProperty(value = "版本号")
	private String version;			//版本号
	@ApiModelProperty(value = "公钥")
	private String pubKey;			//公钥
	@ApiModelProperty(value = "私钥")
	private String prdKey;			//私钥
	@ApiModelProperty(value = "畅捷支付请求url")
	private String payUrl;			//畅捷支付请求url
	@ApiModelProperty(value = "密钥")
	private String payKey;			//密钥
	@ApiModelProperty(value = "渠道")
	private String channel;			//
	@ApiModelProperty(value = "回調url")
	private String noticeUrl;			//
	
	
}
