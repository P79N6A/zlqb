package com.zhiwang.zfm.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 功能说明：提供一些精确的运算
 * @author chenbo
 * @email 381756915@qq.com
 * @date 2018-01-5 19:22
 */
public class ChkUtil {
	
	public static final String CHECK_VERSION = "\\d+(\\.\\d+)*";
	public static final String EXP_a_z = "[a-z]*";
	public static final String EXP_A_Z = "[A-Z]*";
	public static final String EXP_a_z_A_Z = "[a-zA-Z]*";
	public static final String EXP_0_9 = "[0-9]*";
	public static final String EXP_5_13 = "^\\d{5,13}$";
	public static final String EXP_0_9_a_z = "[0-9a-z]*";
	public static final String EXP_0_9_a_z_A_Z = "[0-9a-zA-Z]*";
	public static final String EXP_0_9_a_z__ = "[0-9a-z_]*";
	public static final String EXP_EMAIL = "^([a-z0-9A-Z_]+[_|\\-|\\.]?)+[a-z0-9A-Z_]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	public static final String EXP_PRICE = "^([1-9]\\d+|[1-9])(\\.\\d\\d?)*$";
	public static final String EXP_MOBILE = "^(13[0-9]|15[0-9]|17[0-9]|18[0-9]|14[0-9]|19[0-9]|16[0-9])[0-9]{8}|1110[0-9]{7}$";
	
	public static final String EXP_TEL = "^(\\(\\d{3,4}\\)|\\d{3,4}-|\\s)+\\d{7,8}$";
	public static final String EXP_POSTALCODE = "^[0-9]{6}$";

	public static final String EXP_IP = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
	public static final String EXP_URL = "^[a-zA-z]+://[^><\"' ]+";
	public static final String EXP_DATE = "[0-9]{4}[-]{1}[0-9]{1,2}[-]{1}[0-9]{1,2}";
	public static final String EXP_DATETIME = "[0-9]{4}[-]{1}[0-9]{1,2}[-]{1}[0-9]{1,2}[ ]{1}[0-9]{1,2}[:]{1}[0-9]{1,2}";
	public static final String EXP_DATETIMESECOND = "[0-9]{4}[-]{1}[0-9]{1,2}[-]{1}[0-9]{1,2}[ ]{1}[0-9]{1,2}[:]{1}[0-9]{1,2}[:]{1}[0-9]{1,2}";
	public static final String DATESTRING_TAIL = "000000000";
	public static final String EXP_DATETIMESECOND2 = "[0-9]{4}[-]{1}[0-9]{1,2}[-]{1}[0-9]{1,2}[ ]{1}[0-9]{1,2}[:]{1}[0-9]{1,2}[:]{1}[0-9]{1,2}[.]{1}[0-9]{1,3}";
	public static final String EXP_JAVA_SCRIPT_ONE = "[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']";
	public static final String EXP_JAVA_SCRIPT_TWO = "script";
	
	public static final String EXP_USERNAME = "^[a-zA-Z0-9_-]{4,30}$";
	public static final String EXP_PASSWORD = "^(?![^a-zA-Z]+$)(?!\\D+$).{6,16}$";
	
	public static final String EXP_NAME = "^[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*$";
	public static final String EXP_IC = "^[0-9Xx]{18}$";
	public static final String EXP_CAPTCHA = "^[0-9A-Za-z]{4}$";
	public static final String EXP_CAPTCHA_PHONEMSG = "^[0-9]{6}$";
	public static final String EXP_REALNAME = "^[\u4E00-\u9FA5\uf900-\ufa2d]{2,4}$";
	
	public static final String EXP_CAR_NO = "^[A-Z_0-9]{5}$";		//车牌号后5位
	public static final String EXP_NUMBER = "^[0-9]{1,}$";
	
	public static final String EXP_SQL_INJECT_ONE = "(/\\*(?:.|[\\n\\r])*?\\*/)|"
			+ "(\\b(select|update|and|or|order|delete|system_user|version|CONCAT|information_schema|WHEN|THEN|FROM|UNION|database|web.xml|conf|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";
	
	
	public static final String[] disableUsernames = new String[]{"51bel","bel","beier","zhongzilian","zzl","admin","test"};
	
	public static final String EXP_WEIXIN = "^[a-zA-Z\\d_-]{6,20}$";
	
	/**
	 * 
	 * 函数功能说明 		替换HTML特殊字符
	 * panye  2014-11-24 
	 * 修改内容 
	 * @param 
	 * @return   
	 * @throws 
	 */
	public static String  replaceHtmlJs(String  s){
		if(isEmpty(s))
			return s;
		
		s = s.replace("&","&amp");
		s = s.replace("'","''");
		s = s.replace("<","&lt");
		s = s.replace(">","&gt");
		s = s.replace("chr(60)","&lt");
		s = s.replace("chr(37)","&gt");
		s = s.replace("\"","&quot");
		s = s.replace(";",";");
		s = s.replace("\n","<br/>");
		s = s.replace(" ","&nbsp");
		return s;
	}
	
	
	/**
	 * 
	 * 后台校验
	 * panye  2014-7-3 
	 * 修改内容 
	 * @param	 obj校验对象
	 * @param	 regExp 正则
	 * @return   正确 返回true 
	 * @throws 
	 */
	public static boolean chkRight(Object obj, String regExp) {
		if (obj == null)
			return false;

		Pattern pattern = Pattern.compile(regExp);
		Matcher matcher = pattern.matcher(obj.toString().trim());
		return matcher.matches();
	}
	
	/**
	 * 
	 * 函数功能说明 	字符串长度验证
	 * panye  2014-9-24 
	 * 修改内容 
	 * @param 
	 * @return   
	 * @throws 
	 */
	public static boolean chkStrLength(int length,String... regExps){
		if(isEmpty(regExps))
			return true;
		
		for (String str : regExps) {
			if(!chkStrLength(length,str))
				return false;
		}
		return true;
	}
	
	/**
	 * 
	 * 函数功能说明 	将对象转换成STRING
	 * panye  2014-9-24 
	 * 修改内容 
	 * @param 
	 * @return   
	 * @throws 
	 */
	public static String valueOf(Object obj){
		
		return obj == null ? "" : obj.toString();
	}
	
	/**
	 * 
	 * 函数功能说明 	将对象转换成STRING
	 * panye  2014-9-24 
	 * 修改内容 
	 * @param 
	 * @return   
	 * @throws 
	 */
	public static String valueOf(Object obj,String defaultValue){
		
		return obj == null ? defaultValue : obj.toString();
	}
	
	/**
	 * 
	 * 函数功能说明 	字符串长度验证
	 * panye  2014-9-24 
	 * 修改内容 
	 * @param 
	 * @return   
	 * @throws 
	 */
	public static boolean chkStrLength(int length,String str){
		if(str != null && str.length() > length){
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * SQL参数注入 
	 * panye  2014-7-3 
	 * 修改内容 
	 * @param regExps 校验数组
	 * @return   
	 * @throws 
	 */
	public static boolean chkParamSqlInject(String... regExps) {
		if(isEmpty(regExps))
			return true;
		
		for (String string : regExps) {
			if (!chkParamSqlInject(string))
				return false;
		}
		return true;
	}
	
	/**
	 * 
	 * SQL参数注入
	 * panye  2014-7-3 
	 * 修改内容 
	 * @param 
	 * @return   
	 * @throws 
	 */
	private static boolean chkParamSqlInject(String regExp) {
		
		if(ChkUtil.isEmpty(regExp))
			return true;
		
		regExp = regExp.replaceAll("%","");
		Pattern sqlPattern = Pattern.compile(EXP_SQL_INJECT_ONE,Pattern.CASE_INSENSITIVE);
		String param = "";
		try {
			param = URLDecoder.decode(regExp,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (sqlPattern.matcher(param).find())
			return false;
		
		return true;
	}
	

//	/**
//	 * 
//	 *  判断一些特殊字符  panye  
//	 * 2014-6-26   
//	 * 修改内容   
//	 * @param   
//	 * @return     
//	 * @throws 
//	 */
//	public static boolean chkSqlInject(Object... objs) {
//		// for (Object obj : objs) {
//		// if(!chkSqlInject(obj)){
//		// return false;
//		// }
//		// }
//		return true;
//	}

	/**
	 * 
	 *  同种类型对象 单独正则验证  
	 * panye  
	 * 2014-6-26   
	 * 修改内容   
	 * @param  objs 需要验证的数组对象 
	 *  @param 
	 * regExp 验证正则 
	 *  @return boolean      
	 * @throws 
	 */
	public static boolean chkRight(Object[] objs, String regExp) {
		for (Object obj : objs) {
			if (!chkRight(obj, regExp))
				return false;
		}
		return true;
	}

	/**
	 * 
	 *  验证多种正则   
	 * panye  
	 * 2014-6-25   
	 * 修改内容   
	 * @param obj 需要验证的对象 
	 *  @param regExps
	 * 正则数组 
	 *  @param @return      
	 *  @return boolean      
	 * @throws null
	 */
	public static boolean chkRigth(Object obj, String... regExps) {
		for (String reg : regExps) {
			if (!chkRight(obj, reg))
				return false;
		}
		return true;
	}

	public static boolean isEmpty(Object data) {
		return (data == null) || ("".equals(data.toString().trim()));
	}
	
	public static boolean isEmptys(String... strs) {
		for (String data : strs) {
			if((data == null) || ("".equals(data.toString().trim()))){
				return true;
			}
		}
		return false;
	}

	public static boolean isStr(String str) {
		return !isEmpty(str);
	}

	public static boolean isMoney(String str) {
		if (isEmpty(str))
			return false;
		try {
			Double.valueOf(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isInteger(String str) {
		if (isEmpty(str))
			return false;
		try {
			Integer.valueOf(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	

	public static boolean isLong(String str) {
		if (isEmpty(str))
			return false;
		try {
			Long.valueOf(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isDouble(String str) {
		if (isEmpty(str))
			return false;
		try {
			Double.valueOf(str);
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
	
	public static boolean isDisableUsername(String username) {
		if(isEmpty(username)) {
			return true;
		}
		for(String disableUsername:disableUsernames) {
			if(username.toLowerCase().indexOf(disableUsername)!=-1) {
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * 
	 * 函数功能说明 	过滤html标签，获取文本值
	 * 关福旺 2015-10-10 19:43:38
	 * 修改内容 
	 * @param 
	 * @return   
	 * @throws 
	 */
	public static String filterHtml(String inputString){
		 String htmlStr = inputString; // 含html标签的字符串
	        String textStr = "";
	        java.util.regex.Pattern p_script;
	        java.util.regex.Matcher m_script;
	        java.util.regex.Pattern p_style;
	        java.util.regex.Matcher m_style;
	        java.util.regex.Pattern p_html;
	        java.util.regex.Matcher m_html;
	        java.util.regex.Pattern p_nbsp;
	        java.util.regex.Matcher m_nbsp;
	 
	        try {
	            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
	                                                                                                        // }
	            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
	                                                                                                    // }
	            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
	            
	            String regEx_nbsp = "&nbsp;"; // 定义&nbsp;标签的正则表达式
	 
	            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
	            m_script = p_script.matcher(htmlStr);
	            htmlStr = m_script.replaceAll(""); // 过滤script标签
	 
	            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
	            m_style = p_style.matcher(htmlStr);
	            htmlStr = m_style.replaceAll(""); // 过滤style标签
	 
	            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
	            m_html = p_html.matcher(htmlStr);
	            htmlStr = m_html.replaceAll(""); // 过滤html标签
	            
	            p_nbsp = Pattern.compile(regEx_nbsp, Pattern.CASE_INSENSITIVE);
	            m_nbsp = p_nbsp.matcher(htmlStr);
	            htmlStr = m_nbsp.replaceAll(""); // 过滤nbsp标签
	 
	            textStr = htmlStr;
	 
	        } catch (Exception e) {
	            System.err.println("Html2Text: " + e.getMessage());
	        }
	 
	        return textStr;// 返回文本字符串
	    }
	
	
	
	/**
	* 判断是否为空，并返回默认值
	* @param object
	* @param isEmptyRsult
	* @return
	* @return 返回类型
	* @throws Exception 异常
	*/
	public static String isEmptyReturnString(Object object,String isEmptyRsult){
		return isEmpty(object)?isEmptyRsult:object.toString();
	}
	
	/**
	* 判断是否为空，并返回默认值
	* @param object
	* @param isEmptyRsult
	* @return
	* @return 返回类型
	* @throws Exception 异常
	*/
	public static Integer isEmptyReturnInteger(Object object,Integer isEmptyRsult){
		return isEmpty(object)?isEmptyRsult:Integer.valueOf(object.toString());
	}
	
	/**
	 * 去掉字符串前后空格
	 * @param str
	 * @return
	 */
	public static String removeBlank(String str) {
		return str == null ? null : str.trim();
	}
}
