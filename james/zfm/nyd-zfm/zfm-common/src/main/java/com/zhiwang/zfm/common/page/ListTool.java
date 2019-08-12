package com.zhiwang.zfm.common.page;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * 功能说明：list的通用处理类
 * 修改人：yuanhao
 * 修改内容：
 * 修改注意点：
 */
public class ListTool<T> {

	
	/**
	 * 功能说明：是否为空 null 
	 * 修改人：yuanhao
	 * 修改内容：
	 * 修改注意点：
	 */
	public  boolean isEmpty(List<T> list){
		if(list == null || list.size() == 0){
			return true;
		}
		return false;
	}
	/**
	 * 功能说明：是否不为为空且 不为 null 
	 * 修改人：yuanhao
	 * 修改内容：
	 * 修改注意点：
	 */
	public  boolean isNotEmpty(List<T> list){
		return !isEmpty(list);
	}
	
	
	/**
	 * 功能说明：将list中的object 强制转换到 对应的class对象
	 * 修改人：yuanhao
	 * 修改内容：
	 * 修改注意点：
	 */
	public  List<T> parseToObject(List<Object> list,Class clazz){
		List resultList = new ArrayList();
		for(Object obj:list){
			JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(obj));
			Object t2 = null;
			try {
				t2 = Class.forName(clazz.getName()).newInstance();
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			Method[] methods =  t2.getClass().getMethods();
			for(Method method:methods){
				String methodName = method.getName();
				if(methodName.startsWith("set")){
					String key = methodName.substring(3, methodName.length());
					key  = key.replaceAll("_", "");
					String mKey = key.toLowerCase();
					for(String jsonKey:json.keySet()){
						String jKey = jsonKey.replaceAll("_", "");
						jKey = jKey.toLowerCase();
						if(jKey.equalsIgnoreCase(mKey)){
							Parameter[] paras =method.getParameters();
							String type = paras[0].getType().getName();
							try {
								if(type.endsWith("String")){
									method.invoke(t2,new Object[]{json.getString(jsonKey)});
								}
								if(type.endsWith("Double")){
									method.invoke(t2,new Object[]{json.getDouble(jsonKey)});
								}
								if(type.endsWith("Integer")){
									method.invoke(t2,new Object[]{json.getInteger(jsonKey)});
								}
								if(type.endsWith("Long")){
									method.invoke(t2,new Object[]{json.getLong(jsonKey)});
								}
								if(type.endsWith("Date")){
									method.invoke(t2,new Object[]{json.getDate(jsonKey)});
								}
								if(type.endsWith("ecimal")){
									method.invoke(t2,new Object[]{json.getBigDecimal(jsonKey)});
								}
							} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								}
						}
					}
				}
			}
			resultList.add(t2);
		}
		return resultList;
	}
}
