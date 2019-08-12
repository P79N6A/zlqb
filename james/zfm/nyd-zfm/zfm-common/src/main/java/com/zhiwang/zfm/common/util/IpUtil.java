package com.zhiwang.zfm.common.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * 功能说明：获取IP地址 
 * @author 潘尚斌
 * 修改人: 
 * 修改原因：
 * 修改时间：
 * 修改内容：
 * 创建日期：2015-5-12
 * Copyright zzl-apt
 */
public class IpUtil {
	
	/**
	 * 	获取IP地址(nginx多级代理)
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");  
        if (isEffective(ip) && ip.indexOf(",") > -1) {  
            String[] array = ip.split(",");  
            for (String str : array) {  
                if (isEffective(str)) {  
                    ip = str;  
                    break;  
                }  
            }  
        }  
        if (!isEffective(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (!isEffective(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (!isEffective(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (!isEffective(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (!isEffective(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;
	}
	
	private static boolean isEffective(String remoteAddr) {  
        if ((null != remoteAddr) && (!"".equals(remoteAddr.trim()))  
                && (!"unknown".equalsIgnoreCase(remoteAddr.trim()))) {  
            return true;  
        }  
        return false;  
    }  
}
