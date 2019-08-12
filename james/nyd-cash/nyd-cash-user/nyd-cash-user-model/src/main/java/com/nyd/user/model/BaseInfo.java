package com.nyd.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Dengw on 2017/11/10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BaseInfo implements Serializable {
	// 用户ID
	private String userId;
	// 账号
	private String accountNumber;
	// IOS使用
	private String userToken;
	// 账号是否存在
	private Boolean existFlag;
	// 设备Id
	private String deviceId;
	// 操作系统
	private String os;
	// android版本
	private Integer version;
	// ios版本
	private String versionName;
	// 设置密码使用鉴权
	private String passwordToken;

	private String appName;
}
