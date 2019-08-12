package com.nyd.user.model.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NydUniteRegisterDto implements Serializable{
	
	private String appId;//商户唯一标示
	
	private String appName;//渠道来源
	
	private String mobile;//手机号
	
	private String channelId;//引流商户号
	
	private String timestamp;//时间戳
	
	private String sign;//签名

}
