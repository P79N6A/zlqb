package com.nyd.application.service.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

public class BeanCommonUtils extends org.springframework.beans.BeanUtils {

	public static <T> List<T> copyListProperties(List<?> sourcesList,
			Class<T> clazz) {
		Assert.notNull(sourcesList, "SourceList must not be null");
		List<T> ls = new ArrayList<T>();
		for (Object source : sourcesList) {
			  Gson gson = new Gson();
			  T desObj = gson.fromJson(JSONObject.toJSONString(source),clazz);
			//T desObj = instantiate(clazz);
			copyProperties(source, desObj);
			ls.add(desObj);
		}
		return ls;
	}
	
   
	public static void main(String[] args) {}
	
	

	public static <T> T copyProperties(Object source, Class<T> clazz) {
		if(source == null) {
			return null;
		}
		T desObj = instantiate(clazz);
		copyProperties(source, desObj);
		return desObj;
	}

	public static Object mapToObject(Map<String, Object> map, Class<?> beanClass)
			throws Exception {
		if (map == null)
			return null;

		Object obj = beanClass.newInstance();

		org.apache.commons.beanutils.BeanUtils.populate(obj, map);

		return obj;
	}

	public static Map<?, ?> objectToMap(Object obj) {
		if (obj == null)
			return null;

		return new org.apache.commons.beanutils.BeanMap(obj);
	}

	/**
	 *      * 将一个 JavaBean 对象转化为一个 Map      * @param bean      * @return      * @throws
	 * IntrospectionException      * @throws IllegalAccessException      * @throws
	 * InvocationTargetException      
	 */
	public static Map convertBean2Map(Object bean)
			throws IntrospectionException, IllegalAccessException,
			InvocationTargetException {
		Class<? extends Object> type = bean.getClass();
		Map returnMap = new HashMap<>();
		BeanInfo beanInfo = Introspector.getBeanInfo(type);
		PropertyDescriptor[] propertyDescriptors = beanInfo
				.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (!"class".equals(propertyName)) {
				Method readMethod = descriptor.getReadMethod();
				Object result = readMethod.invoke(bean, new Object[0]);
				if (result != null) {
					returnMap.put(propertyName, result);
				} else {
					returnMap.put(propertyName, null);
				}
			}
		}
		return returnMap;
	}

	/**
	 * 将 List<JavaBean>对象转化为List<Map>
	 * 
	 * @param beanList
	 * @return
	 * @throws Exception
	 */
	public static <T> List<Map> convertListBean2ListMap(List<T> beanList,
			Class<T> T) throws Exception {
		List<Map> mapList = new ArrayList<>();
		for (int i = 0; i < beanList.size(); i++) {
			Object bean = beanList.get(i);
			Map map = convertBean2Map(bean);
			mapList.add(map);
		}
		return mapList;
	}
	
	
}


