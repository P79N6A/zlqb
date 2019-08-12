/**
 * Project Name:nyd-cash-capital-service
 * File Name:RsaHelp.java
 * Package Name:com.nyd.capital.service.jx.util
 * Date:2018年8月9日下午5:27:53
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.nyd.capital.service.jx.util;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nyd.capital.service.jx.config.JxConfig;

/**
 * ClassName:RsaHelp <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年8月9日 下午5:27:53 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Component
public class SignUtil {
	Logger logger = LoggerFactory.getLogger(SignUtil.class);
	
	@Autowired
	private JxConfig jxConfig;

	/**
	 * 获取签名
	 * 
	 * @param signStr  待签名字符串
	 * @return
	 * @throws Exception
	 */
	public String sign(String signStr){
		String sign = null;
		RSAHelper signer = null;
		try {
			RSAKeyUtil rsaKey = new RSAKeyUtil(new File(jxConfig.getPrivateKey()), jxConfig.getPrivatePass());
			signer = new RSAHelper(rsaKey.getPrivateKey());
			
		    sign = signer.sign(signStr);
		} catch (Exception e) {
			logger.error("签名校验异常"+e.getMessage());
		}
		return sign;
	}
	
	/**
	 *  签名校验
	 * @param signText  待验的签名
	 * @param dataText  待签名字符串
	 * @return
	 */
	public boolean verify(String signText,String dataText){
		signText = signText.replaceAll("[\\t\\n\\r]", "");

		boolean b = false;
		try {
			RSAKeyUtil ru = new RSAKeyUtil(new File(jxConfig.getPublicKey())); 
			RSAHelper signHelper = new RSAHelper(ru.getPublicKey()); 
			b = signHelper.verify(dataText , signText);
		} catch (Exception e) {
			logger.error("签名校验异常"+e.getMessage());
		}
		
		return b;
	}

	/**
	 * 拼接字符串
	 * @param map
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String mergeMap(Map map) {
		map = new TreeMap(map);
		String requestMerged = "";
		StringBuffer buff = new StringBuffer();
		Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
		Map.Entry<String, String> entry;
		while (iter.hasNext()) {
			entry = iter.next();
			if (!"SIGN".equalsIgnoreCase(entry.getKey())) {
				if(entry.getValue()==null){
					entry.setValue("");
					buff.append("");
				}else{
						buff.append(String.valueOf(entry.getValue()));
				}
			}
		}
		requestMerged = buff.toString();
		return requestMerged;
	}
}

