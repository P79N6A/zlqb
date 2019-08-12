package com.zhiwang.zfm.service.bulid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.zhiwang.zfm.common.bulid.CreateJava;
import com.zhiwang.zfm.service.api.bulid.IJavaGeneratorService;

/** 
 * 生成Java代码业务逻辑接口实现
 * @author
 * @date 创建时间：2018年4月24日 下午5:18:13 
 * @version 1.0.0 
 * @parameter  
 * @throws 
 * @return  
 */
@Service
@Transactional
public class JavaGeneratorServiceImpl implements IJavaGeneratorService{
	
	public static final Logger LOGGER = LoggerFactory.getLogger(JavaGeneratorServiceImpl.class);

	/**
	 * 生成Java代码
	 * param
	 * @param tableName  表名   
	 * @param codeName  表名对应的中文注释
	 * @param modulePakPath 模块包路径：com\\fdcz\\pro\\system
	 * @param modulePackage 模块包：com.fdcz.pro.system
	 * @param dbName   数据库名称
	 * @param prefix   数据库表前缀
	 */
	@Override
	public JSONObject generator(JSONObject params) {
		
		JSONObject resultJson = new JSONObject();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> map =  new HashMap<String, Object>();
		try {
			/** 设置数据库中对应表名*/
			map.put("tableName", params.getString("tableName")); // bg_bank_recharge_limit
			/** 设置对应包名*/
			map.put("packageName", "");  //
			/** 设置对应表名备注*/
			map.put("codeName", params.getString("codeName"));    // 贝尔充值限额表
			/** 设置对应目录模块名*/
			map.put("modelName", params.getString("modelName"));  // bank\recharge
			
			list.add(map);
			
			/** 设置数据库名*/
			String dbName = params.getString("dbName");  // erpdb
			/** 设置要去除的数据库前缀*/
			String prefix = params.getString("prefix"); //  bg_
			/** 设置模块包路径*/
			String modulePakPath = "\\src\\main";
			/** 设置模块包*/
			String modulePackage = "com.bel"; // com.bel
			for (int i=0; i<list.size(); i++)
			{
				Map<String, Object> m = list.get(i);
				CreateJava.create((String)m.get("tableName"), (String)m.get("codeName"), modulePakPath + (String)m.get("packageName") , modulePackage + (String)m.get("packageName"), dbName, prefix, (String)m.get("modelName"));
			}
			resultJson.put("responseCode", 1);
			resultJson.put("info", "生成Java代码成功");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("生成Java代码异常,请求参数:{}",new Object[]{params});
			LOGGER.error(e.getMessage(),e);
			resultJson.put("responseCode", 0);
			resultJson.put("info", "生成Java代码异常");
		}
		return resultJson;
	}
}
