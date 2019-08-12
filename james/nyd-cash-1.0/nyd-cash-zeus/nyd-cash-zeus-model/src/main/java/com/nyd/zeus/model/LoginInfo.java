package com.nyd.zeus.model;

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
public class LoginInfo implements Serializable {
	// 账号
	private String accountNumber;
	// 用户ID
	private String userId;
	
	private String password;
	// 用户设备号
	private String deviceId;
	
	private String userToken;
}
