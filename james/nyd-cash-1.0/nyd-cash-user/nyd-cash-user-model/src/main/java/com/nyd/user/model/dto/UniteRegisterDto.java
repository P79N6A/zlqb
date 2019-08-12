package com.nyd.user.model.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class UniteRegisterDto implements Serializable{
	
	 /**手机号*/
    private String mobile;
    /**引流商户号 */
    private String channelId;
    /**app名称*/
    private String appName;
}
