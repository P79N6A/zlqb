package com.nyd.user.api;

import java.util.Map;

/**
 * 
 * @author zhangdk
 *
 */
public interface AccountInfoApi {
	
	void convertAccountInRedis(Map<String, Object> params);
	
	String getLoginPrefix(String accountNum);

}
