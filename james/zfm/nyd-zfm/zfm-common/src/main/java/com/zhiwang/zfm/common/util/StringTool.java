package com.zhiwang.zfm.common.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能说明：String的工具类
 * 典型用法：该类的典型使用方法和用例
 * 特殊用法：该类在系统中的特殊用法的说明	
 * @author yuanhao
 * 修改人: 
 * 修改原因：
 * 修改时间：
 * 修改内容：
 * 创建日期：2015-5-29
 * Copyright zzl-apt
 */
public class StringTool {
	
	/**
	 * 功能说明：String是否是空或null
	 * yuanhao  2015-6-19
	 * @param 
	 * @return 该方法的返回值的类型，含义   
	 * @throws  该方法可能抛出的异常，异常的类型、含义。
	 * 最后修改时间：最后修改时间
	 * 修改人：yuanhao
	 * 修改内容：
	 * 修改注意点：
	*/
	public static boolean isEmptyOrNull(String str){
		if(str==null||(str.trim())==null||"".equals(str.trim())){
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * 功能说明:去除最后一个字符
	 * yuanhao  2015-6-19
	 * @param 
	 * @return 该方法的返回值的类型，含义   
	 * @throws  该方法可能抛出的异常，异常的类型、含义。
	 * 最后修改时间：最后修改时间
	 * 修改人：yuanhao
	 * 修改内容：
	 * 修改注意点：
	*/
	public static  String removeLastChar(String str){
		if(str==null||"".equals(str)){
			return "";
		}
		return str.substring(0,str.length()-1);
	}
	
	/**
	 * 功能说明:去除重复(主要针对id)
	 * yuanhao  2015-6-19
	 * @param
	 * @return 该方法的返回值的类型，含义   
	 * @throws  该方法可能抛出的异常，异常的类型、含义。
	 * 最后修改时间：最后修改时间
	 * 修改人：yuanhao
	 * 修改内容：
	 * 修改注意点：
	*/
	public static String distinctString(String str){
		String resultString="";
		if(str==null||"".equals(str)){
			return "";
		}
		String[] ids=str.split(",");
		Set<String> set = new HashSet<String>();
		for(String id:ids){
			set.add(id);
		}
		Iterator<String> it= set.iterator();
		while(it.hasNext()){
			String childStr=it.next();
			resultString+=childStr+",";
		}
		return removeLastChar(resultString);
		
	}
	
	/**
	 * 功能说明:获得最后的四个字符
	 * yuanhao  2015-6-19
	 * @param 
	 * @return 该方法的返回值的类型，含义   
	 * @throws  该方法可能抛出的异常，异常的类型、含义。
	 * 最后修改时间：最后修改时间
	 * 修改人：yuanhao
	 * 修改内容：
	 * 修改注意点：
	*/
	public static String getIC(Object ic){
		if(ic==null){
			return "";
		}
		String newIc=ic.toString();
		if(isEmptyOrNull(newIc)||newIc.length()<4){
			return "";
		}
		return newIc.substring(newIc.length()-4);
	}
	/**
	 * 功能说明:判断是否是非空或null
	 * yuanhao  2015-6-19
	 * @param 
	 * @return 该方法的返回值的类型，含义   
	 * @throws  该方法可能抛出的异常，异常的类型、含义。
	 * 最后修改时间：最后修改时间
	 * 修改人：yuanhao
	 * 修改内容：
	 * 修改注意点：
	*/
	public static boolean isNotEmptyOrNull(String string){
		if(string!=null&&!"".equals(string)){
			return true;
		}
		return false;
	}

	/**
	 * 功能说明:字符串数组是否包含
	 * yuanhao  2015-6-19
	 * @param 
	 * @return 该方法的返回值的类型，含义   
	 * @throws  该方法可能抛出的异常，异常的类型、含义。
	 * 最后修改时间：最后修改时间
	 * 修改人：yuanhao
	 * 修改内容：
	 * 修改注意点：
	*/
	public static boolean contains(String[] arrayString,String key){
		if(arrayString==null||arrayString.length==0){return false;}
		for(int i=0;i<arrayString.length;i++){
			if(key.equals(arrayString[i])){
				return true;
			}
		}
		return false;
	}
	
	
	public static String removeHTMLTag(String htmlStr) {
		String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
		String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

		Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签

		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签

		htmlStr = htmlStr.replace(" ", "");
		htmlStr = htmlStr.replaceAll("\\s*|\t|\r|\n", "");
		htmlStr = htmlStr.replace("“", "");
		htmlStr = htmlStr.replace("”", "");
		htmlStr = htmlStr.replaceAll("　", "");

		return htmlStr.trim(); // 返回文本字符串
	}
	
	
}
