package com.nyd.capital.service.dld.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONObject;

public class JsonUtils {
	/**
	 * 对外提供获取多嵌套json值的方法,已重载
	 *
	 * @param result    初始化输入的json字符串,也被递归调用之后覆盖
	 * @param Hierarchy 要获取最终的值要经历几个json层级
	 * @param keys      各层级下对应的json的key,个数与Hierarchy的值一致
	 * @return
	 */
	public static Object getValue(Object result, int Hierarchy, String[] jsonKeys) {
		int i = 0;
		// 递归调用getValue方法,取到最终所需要的json字符串中的值
		while (i < jsonKeys.length) {
			result = getValue(result, Hierarchy, jsonKeys[i]);
			i++;
		}
		// 返回最终需要的值
		return result;
	}

	/**
	 * 用于递归调用,获取单层的json的某个值
	 *
	 * @param result    初始化输入的json字符串
	 * @param Hierarchy 要获取最终的值要经历几个json层级
	 * @param firstkey  对应到json的key,仅限于当前层级
	 * @return
	 */
	public static Object getValue(Object result, int Hierarchy, String firstkey) {
		// 将字符串转换成json对象
		JSONObject jsonObject = JSONObject.fromObject(result);
		// 构造jsonToMap对象,准备存储jsonObject对象中的数据
		Map jsonToMap = new HashMap<Object, Object>();
		// 获取迭代器
		Iterator ite = jsonObject.keys();
		// 遍历jsonObject对象中的数据,将数据添加到jsonToMap对象
		while (ite.hasNext()) {
			String key = ite.next().toString();
			String value = jsonObject.get(key).toString();
			jsonToMap.put(key, value);
		}
		// 返回所需要的值
		return jsonToMap.get(firstkey);
	}
}
