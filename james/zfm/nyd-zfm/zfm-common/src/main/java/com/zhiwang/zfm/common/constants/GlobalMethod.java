package com.zhiwang.zfm.common.constants;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 功能说明： 全局方法
 * 修改人：yuanhao
 * 修改内容：
 * 修改注意点：
 */
public class GlobalMethod {
	
	/**
	 * 功能说明： UUID
	 * 修改人：yuanhao
	 * 修改内容：
	 * 修改注意点：
	 */
	public static String getUUID(){
		return UUID.randomUUID().toString().replaceAll(Pattern.quote("-"),"");
	}
}
