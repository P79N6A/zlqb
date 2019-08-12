package com.zhiwang.zfm.service.api.bulid;

import com.alibaba.fastjson.JSONObject;

/** 
 * 生成Java代码业务逻辑接口
 * @author 
 * @date 创建时间：2018年4月24日 下午5:18:40 
 * @version 1.0.0 
 * @parameter  
 * @throws 
 * @return  
 */
public interface IJavaGeneratorService {
	
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
	public JSONObject generator(JSONObject params);
}
