package com.nyd.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Dengw on 2017/11/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountInfo implements Serializable {
	// 账号
	private String accountNumber;
	// 用户ID
	private String userId;
	// 短信验证码
	private String smsCode;
	// 原始密码
	private String password;
	// 新密码
	private String newPassword;
	// 用户来源
	private String source;
	// 用户设备号
	private String deviceId;
	// 渠道地址
	private String chanelUrl;
	/**用户渠道（0 xxd 1银码头）*/
	private Integer channel;
	// 关联银马头用户id
	private String iBankUserId;
	// app名称
	private String appName;
	// 前端图形验证结果
	private String verifyResult;
	// 手机系统类型
	private String os;
	// 设置密码使用鉴权
	private String passwordToken;
	private String sequence;
	private String sole;
	private String ifNative;
}
