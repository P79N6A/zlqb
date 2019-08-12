package com.zhiwang.zfm.common.util.bean;

import javax.servlet.http.HttpServletRequest;





/**
 * Classname  管理session
 * Version	  1.0
 * @author zhuliang
 * 2014-11-25
 * Copyright notice
 */
public final class SessionUtils {

	public static final String SESSION_USER = "User";		//当前登录的用户
	
	public static final String SESSION_TOKEN = "TOKEN";		//TOKEN
	
	public static final String SESSION_CODE = "LOGIN_VERIFICATION_CODE_SESSION";// 验证码
	
	public final static String BEFORE_LOGIN_PATH = "BEFORE_LOGIN_URL";			// 上次登录地址
	
	public static final String SESSION_REG_MOBILE_CODE = "REG_MOBILE_CODE";	// 注册手机验证码 
	
	public static final String SESSION_BIND_CARD_MOBILE_CODE = "BIND_CARD_MOBILE_CODE";	// 激活存管账户手机验证码
	
	public static final String SESSION_CHANGE_WITHDRAW_PW_MOBILE_CODE = "CHANGE_WITHDRAW_PW_MOBILE_CODE";	// 修改存管账户密码手机验证码
	
	public static final String SESSION_PASSWORD_MOBILE_CODE = "SESSION_PASSWORD_MOBILE_CODE";//找回密码手机验证码
	
	public static final String SESSION_CHANGE_REG_MOBILE_CODE = "CHANGE_REG_MOBILE_CODE";	// 修改注册手机验证码
	
	public static final String SESSION_REG_USERNAME_VALID = "REG_USERNAME_VALID";	// 注册用户名防重复验证
	
	public static final String SESSION_REG_MOBILE_VALID = "REG_MOBILE_VALID";	// 注册手机防重复验证
	
	public static final String SESSION_BIND_CARD_IC_VALID = "BIND_CARD_IC_VALID";	// 激活存管账户身份证防重复验证
	
	public static final String SESSION_BIND_CARD_NUMBER_VALID = "BIND_CARD_NUMBER_VALID";	// 激活存管账户银行卡防重复验证
	
	public static final String SESSION_CAHNGE_REG_MOBILE_VALID = "CAHNGE_REG_MOBILE_VALID";	// 修改注册手机防重复验证
	
	public static final String SESSION_BEL_BANK_MOBILE_CODE = "BEL_BANK_MOBILE_CODE";	// 修改注册手机防重复验证
	
	// ===========================oscache key 
	public static final String OSE_ = "OSE_";
	
	
	
	
	/**
	  * 设置session的值
	  * @param request
	  * @param key
	  * @param value
	  */
	 public static void setAttr(HttpServletRequest request,String key,Object value){
		 request.getSession(true).setAttribute(key, value);
	 }
	 
	 /**
	  * 设置session的值
	  * @param request
	  * @param key
	  * @param value
	  */
	 public static void setAttr(HttpServletRequest request,String key,Object value,int times){
		 request.getSession(true).setMaxInactiveInterval(times);
		 request.getSession(true).setAttribute(key, value);
	 }
	 
	 /**
	  * 获取session的值
	  * @param request
	  * @param key
	  * @param value
	  */
	 public static Object getAttr(HttpServletRequest request,String key){
		 return request.getSession(true).getAttribute(key);
	 }
	 
	 /**
	  * 删除Session值
	  * @param request
	  * @param key
	  */
	 public static void removeAttr(HttpServletRequest request,String key){
		 request.getSession(true).removeAttribute(key);
	 }
	 
	 

	 /**
	  * 从session中移除用户信息
	  * @param request
	  * @param key
	  */
	 public static void removeUser(HttpServletRequest request){
		 request.getSession(true).removeAttribute(SESSION_USER);
	 }

}
