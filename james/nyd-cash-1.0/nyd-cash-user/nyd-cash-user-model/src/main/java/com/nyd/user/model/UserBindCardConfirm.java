package com.nyd.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 绑卡信息
 * 
 * @author zhangdk
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserBindCardConfirm {
	private String requestNo;
	private String validatecode;
	private String userId;
	private String bankCode;
	private String bankName;
	//平台编码
    private String appName;
}

