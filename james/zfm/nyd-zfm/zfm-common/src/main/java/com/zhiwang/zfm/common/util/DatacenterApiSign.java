package com.zhiwang.zfm.common.util;

import com.zhiwang.zfm.common.util.security.MD5;

/**
 * 风控数据校验
 * @author psb
 *
 */
public class DatacenterApiSign {

	private static String key = "5m2c70102l122*2H7&692#7!356%390c";
	
	public static boolean validateSign(String act,String ts,String nonce, String pid,String uid,String reqSign,String token) {
		
	    StringBuffer sign = new StringBuffer();
	    sign.append(act);
	    sign.append(",");
	    sign.append(ts);
	    sign.append(",");
	    sign.append(nonce);
	    sign.append(",");
	    sign.append(pid);
	    sign.append(",");
	    sign.append(uid);
	    sign.append(",");
	    sign.append(token);
	    return MD5.GetMD5Code(sign.toString()).equals(reqSign);
	}
	
	public static String getSign(String act,String ts,String nonce, String pid,String uid,String token) {
		 StringBuffer sign = new StringBuffer();
		    sign.append(act);
		    sign.append(",");
		    sign.append(ts);
		    sign.append(",");
		    sign.append(nonce);
		    sign.append(",");
		    sign.append(pid);
		    sign.append(",");
		    sign.append(uid);
		    sign.append(",");
		    sign.append(token);
		    return MD5.GetMD5Code(sign.toString());
	}

	public static String getSign(String  mobileNo,String d) {
		 StringBuffer sb=new StringBuffer();
	    sb.append(mobileNo);
	    sb.append("&");
	    sb.append(d);
	    sb.append("&");
	    sb.append(key);
	    return MD5.GetMD5Code(sb.toString());
	}
	
	public static Boolean validateOrdinarySign(String  mobileNo,String d,String sign){
	    StringBuffer sb=new StringBuffer();
	    sb.append(mobileNo);
	    sb.append("&");
	    sb.append(d);
	    sb.append("&");
	    sb.append(key);
	    return MD5.GetMD5Code(sb.toString()).equalsIgnoreCase(sign);
	}
	
	
	public static void main(String[] args) {
//		String d = DateUtils.getCurrentTime(DateUtils.STYLE_15);
//		String mobile = "17316599712";
//		StringBuffer sb=new StringBuffer();
//	    sb.append(mobile);
//	    sb.append("&");
//	    sb.append(d);
//	    sb.append("&");
//	    sb.append(key);
//	    String sign = MD5.GetMD5Code(sb.toString());
//	    // 1@507@425$1869334f4b432d3n5*2i36
//	    
		String aa = RandomUtil.randomString1(32);
		System.out.println(aa);
//		System.out.println(mobile + ":" + sign + ":" + d);
		
	}
	
	
	/**
	 * 
	* 获取随机唯一值
	* @return
	* psb
	* @return 返回类型
	* @throws Exception 异常
	 */
	public static String getRandomNumber() {
		
		return RandomUtil.randomString(16);
		
	}
	
	/**
	 * 
	* 获取随机唯一值
	* @return
	* psb
	* @return 返回类型
	* @throws Exception 异常
	 */
	public static String getRandom() {
		
		return RandomUtil.randomString1(32);
		
	}
	
}
