package com.nyd.user.model.t;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * 个人信息
 * @author admin
 *
 */
@JsonIgnoreProperties
@Data
public class UserInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userId;
	private String userName;//客户名称
	private String phone;//手机号码
	private String idCard;//身份证
	private String companyName;//单位名称
	private String companyAddress;//公司地址
	private String permanentAddress;//常住地址
	private String education;//学历
	private String isMatrimony;//是否结婚
	private String job;//工作
	private String salary; //月收入
	private String industry;// 职业类型
}
