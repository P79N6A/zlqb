package com.nyd.user.model;

import lombok.Data;
import lombok.ToString;

/**
 * @author liuqiu
 */
@Data
@ToString
public class GeeTestDto {

    private String challenge;
    private String validate;
    private String secCode;
    private String ip;
    private String deviceId;
    private String appName;
    private String accountNumber;
    /**
     * 是否原生,1-原生,0-H5
     */
    private String ifNative;
    private String process_id;
    private String sign;
    private String accesscode;

}
