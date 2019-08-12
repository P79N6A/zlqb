package com.nyd.capital.model.dld;

import java.io.File;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 借款客户注册请求参数
 * @author zhangdk
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserParams implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2059008647040081370L;
	private String realName;
	private String identityNumber;
	private String phone;
	private String ppFlag;
	private File photoOne;
	private File photoTwo;
	private File photoThree;
	private String companyName;
	private String usci="";
	private String address="";
	private String sex="";
	private String birthday="";
	private String education="";

}
