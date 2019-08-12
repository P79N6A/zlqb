package com.zhiwang.zfm.common.request.mybatise;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;


@ApiModel(value="生成DAO,SERVICE请求参数(开发专用)")
@JsonIgnoreProperties(ignoreUnknown=true)
public class MybatiseCreatorRequest {

	@ApiModelProperty(value = "表名", required=true)
	@NotEmpty
	private String tableName;
	
	@ApiModelProperty(value = "表名对应的中文注释", required=true)
	@NotEmpty
	private String codeName;
	
//	@ApiModelProperty(value = "模块包路径：com\\fdcz\\pro\\system", required=true)
//	@NotEmpty
//	private String modulePakPath;
	
//	@ApiModelProperty(value = "模块包：com.fdcz.pro.system", required=true)
//	@NotEmpty
//	private String modulePackage; 
	
//	@ApiModelProperty(value = "数据库名(PS:erpdb)", required=true)
//	@NotEmpty
//	private String dbName;
	
	@ApiModelProperty(value = "前缀(PS:bg_)", required=false)
	@NotEmpty
	private String prefix;
	
	@ApiModelProperty(value = "模块名(PS:user)", required=true)
	@NotEmpty
	private String modelName;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

//	public String getModulePakPath() {
//		return modulePakPath;
//	}
//
//	public void setModulePakPath(String modulePakPath) {
//		this.modulePakPath = modulePakPath;
//	}

//	public String getModulePackage() {
//		return modulePackage;
//	}
//
//	public void setModulePackage(String modulePackage) {
//		this.modulePackage = modulePackage;
//	}

//	public String getDbName() {
//		return dbName;
//	}
//
//	public void setDbName(String dbName) {
//		this.dbName = dbName;
//	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
	
}
