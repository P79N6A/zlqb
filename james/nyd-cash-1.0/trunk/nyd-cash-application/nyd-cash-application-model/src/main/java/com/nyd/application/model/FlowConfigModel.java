package com.nyd.application.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class FlowConfigModel implements Serializable{
	
	/**引流商渠道号*/
    private String channelId;
    /**引流商名称*/
    private String partnerName;
    /**本平台在引流商的标记*/
    private String appId;
    /**是否需要公钥校验*/
    private Integer publicValidate;
    /**公钥*/
    private String publicKey;
    /**是否已删除*/
    private Integer deleteFlag;

}
