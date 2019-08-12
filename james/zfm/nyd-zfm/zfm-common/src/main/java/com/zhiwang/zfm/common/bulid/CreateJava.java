package com.zhiwang.zfm.common.bulid;

import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.zhiwang.zfm.common.util.ChkUtil;


/**
 * 创建JAVA
 * 
 */
@Component
public class CreateJava {
	
	private static String url;
	
	private static String username;
	
	private static String passWord;
	
	private static String projectAbbr;

	public static String getUrl() {
		return url;
	}

	@Value("${spring.datasource.url:#{null}}")
	public void setUrl(String url) {
		CreateJava.url = url;
	}

	public String getUsername() {
		return username;
	}

	@Value("${spring.datasource.username:#{null}}")
	public void setUsername(String username) {
		CreateJava.username = username;
	}

	public static String getPassWord() {
		return passWord;
	}

	@Value("${spring.datasource.password:#{null}}")
	public void setPassWord(String passWord) {
		CreateJava.passWord = passWord;
	}
	
	
	public static String getProjectAbbr() {
		return projectAbbr;
	}

	@Value("${project.name:#{null}}")
	public void setProjectAbbr(String projectAbbr) {
		CreateJava.projectAbbr = projectAbbr;
	}

	/**
	 * 
	 * @param tableName
	 *            表名
	 * @param codeName
	 *            表名对应的中文注释
	 * @param modulePakPath
	 *            模块包路径：com\\fdcz\\pro\\system
	 * @param modulePackage
	 *            模块包：com.fdcz.pro.system
	 * 2015-11-12 weiyingni 修改，添加参数modelName
	 */
	public static void create(String tableName, String codeName, String modulePakPath, String modulePackage, String dbName, String prefix, String modelName) throws Exception{
		
		try {
			if (null == tableName || "".equals(tableName)) {
				return;
			}

			if (null == codeName || "".equals(codeName)) {
				return;
			}

			if (null == modulePakPath || "".equals(modulePakPath)) {
				return;
			}

			if (null == modulePackage || "".equals(modulePackage)) {
				return;
			}
			
			
			CreateBean createBean = new CreateBean();
			createBean.setMysqlInfo(url, username, passWord, dbName);
			/** 此处修改成您的 表名 和 中文注释 ***/
			String className = createBean.getTablesNameToClassName(tableName, prefix);
			String lowerName = className.substring(0, 1).toLowerCase() + className.substring(1, className.length());
			
			// 项目跟路径路径，此处修改为您的项目路径
			String rootPath = CommonPageParser.getRootPath();
			// 资源路径
			/** String resourcePath = rootPath + "\\resource\\";*/
			// java路径
			String javaPath = rootPath.substring(0, rootPath.lastIndexOf("\\"));
			String moduleSimplePackage = modulePackage.substring(modulePackage.lastIndexOf(".") + 1, modulePackage.length());
			
			String modelPath = "\\java\\com\\zhiwang\\"+projectAbbr+"\\entity\\"+ modelName +"\\" + className + ".java";
			String searchFormPath = "\\java\\com\\zhiwang\\"+projectAbbr+"\\entity\\"+ modelName +"\\query\\"  + className + "SearchForm.java";
			String mapperPath = "\\java\\com\\zhiwang\\"+projectAbbr+"\\dao\\"+ modelName +"\\"  + className + "Mapper.java";
			String servicePath = "\\java\\com\\zhiwang\\"+projectAbbr+"\\service\\api\\"+ modelName +"\\"  + className + "Service.java";
			String serviceImplPath = "\\java\\com\\zhiwang\\"+projectAbbr+"\\service\\impl\\"+ modelName +"\\"  + className + "ServiceImpl.java";
			String sqlMapperPath = "\\resources\\mapper\\"+ modelName +"\\"  + className + "Mapper.xml";

			VelocityContext context = new VelocityContext();
			context.put("className", className); //
			context.put("lowerName", lowerName);
			context.put("codeName", codeName);
			context.put("tableName", tableName);
			String packageModel = "";
			
			/** 当模块名不为空时，拼接路径*/
			if(!ChkUtil.isEmpty(modelName)){
				modelName = modelName.replace("\\", ".");
				packageModel = "."+modelName;
			}
			context.put("moduleName", packageModel);
			context.put("modulePackage", modulePackage);
			context.put("moduleSimplePackage", moduleSimplePackage);
			/****************************** 生成bean字段 *********************************/
			try {
				context.put("feilds", createBean.getBeanFeilds(tableName)); // 生成bean
			} catch (Exception e) {
				e.printStackTrace();
			}

			/******************************* 生成sql语句 **********************************/
			try {
				Map<String, Object> sqlMap = createBean.getAutoCreateSql(tableName);
				context.put("columnDatas", createBean.getColumnDatas(tableName)); // 生成bean
				context.put("SQL", sqlMap);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
			
			// -------------------生成文件代码---------------------/
			CommonPageParser.WriterPage(context, "TempBean.java", javaPath+"\\"+projectAbbr+"-dao\\"+ modulePakPath, modelPath); // 生成实体Bean
			CommonPageParser.WriterPage(context, "TempSearchForm.java", javaPath +"\\"+projectAbbr+"-dao\\"+ modulePakPath, searchFormPath); // 生成Model
			CommonPageParser.WriterPage(context, "TempMapper.java", javaPath +"\\"+projectAbbr+"-dao\\"+ modulePakPath, mapperPath); // 生成MybatisMapper接口
			CommonPageParser.WriterPage(context, "TempService.java", javaPath +"\\"+projectAbbr+"-service-api\\"+ modulePakPath, servicePath);// 生成Service
			CommonPageParser.WriterPage(context, "TempServiceImpl.java", javaPath +"\\"+projectAbbr+"-service-impl\\"+ modulePakPath, serviceImplPath);// 生成Service
			CommonPageParser.WriterPage(context, "TempMapper.xml", javaPath +"\\"+projectAbbr+"-web\\"+ modulePakPath, sqlMapperPath);// 生成XMLforMapper
			
			/** 2015-11-11 weiyingni 修改生成xml路径 原路径为：
			 	CommonPageParser.WriterPage(context, "TempMapper.xml", resourcePath + modulePakPath, sqlMapperPath);// 生成XMLforMapper
			 	2015-11-12 weiyingni 注释掉可不生成控制层
			CommonPageParser.WriterPage(context, "TempController.java", javaPath + modulePakPath, controllerPath);// 生成Controller*/	
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}
