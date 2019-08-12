package com.zhiwang.zfm.common.bulid;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhiwang.zfm.common.util.ChkUtil;

/**
 * 创建Bean
 * @author gaoyufeng
 *
 */
public class CreateBean {
	static String url;   
	static String username ;
	static String password ;
	static String rt="\r\t";
	static String dbInstance;
	String SQLTables="show tables";
	static{
	try{
		Class.forName("com.mysql.jdbc.Driver");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@SuppressWarnings("static-access")
	public void setMysqlInfo(String url,String username,String password,String dbInstance){
		   this.url=url;
		   this.username=username;
		   this.password=password;
		   this.dbInstance = dbInstance;
	}
	public Connection getConnection() throws SQLException{
		return DriverManager.getConnection(url, username, password);
	}
    public List<String> getTables() throws SQLException{
    	Connection con=this.getConnection();
    	PreparedStatement ps= con.prepareStatement(SQLTables);
    	ResultSet rs=ps.executeQuery();
    	List<String> list=new ArrayList<String>();
    	while(rs.next()){
    		String tableName=rs.getString(1);
    		list.add(tableName);
    	}
    	rs.close();
		ps.close();
		con.close();
		return list;
	}
    
    /**
     * 查询表的字段，封装成List
     * @param tableName
     * @return
     * @throws SQLException
     */
    public List<ColumnData> getColumnDatas(String tableName) throws SQLException{
    	String SQLColumns="SELECT distinct COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT FROM information_schema.columns WHERE table_name =  '"+tableName+"' "+ "and table_schema='"+dbInstance+"' ";
        Connection con=this.getConnection();
    	PreparedStatement ps=con.prepareStatement(SQLColumns);
    	List<ColumnData>  columnList= new ArrayList<ColumnData>();
        ResultSet rs=ps.executeQuery();
        StringBuffer str = new StringBuffer();
		StringBuffer getset=new StringBuffer();
        while(rs.next()){
        	String name = rs.getString(1);
        	/** 转换name值,转换大小写，去除下划线*/
        	String beanName = getColumnNameToBeansName(name.toLowerCase());
			String type = rs.getString(2);
			String comment = rs.getString(3);
			type=this.getType(type);
			ColumnData cd= new ColumnData();
			cd.setColumnName(name);
			cd.setBeanName(beanName);
			cd.setDataType(type);
			cd.setColumnComment(comment);
			columnList.add(cd);
        }
        argv=str.toString();
        method=getset.toString();
		rs.close();
		ps.close();
		con.close();
		return columnList;
    }
    
    private String method;
    private String argv;
    
    /**
     * 生成实体Bean 的属性和set,get方法
     * @param tableName
     * @return
     * @throws SQLException
     */
    public String getBeanFeilds(String tableName) throws SQLException{
    	List<ColumnData> dataList = getColumnDatas(tableName);
    	StringBuffer str = new StringBuffer();
    	StringBuffer getset = new StringBuffer();
        for(ColumnData d : dataList){
        	String name = d.getBeanName();
			String type =  d.getDataType();
			String comment =  d.getColumnComment();
//			type=this.getType(type);
			String maxChar=name.substring(0, 1).toUpperCase();
			str.append("\r\t").append("private ").append(type+" ").append(name).append(";\t\t\t//").append(comment);
			String method=maxChar+name.substring(1, name.length());
			getset.append("\r\t").append("public ").append(type+" ").append("get"+method+"() {\r\t");
			getset.append("    return this.").append(name).append(";\r\t}");
			getset.append("\r\t").append("public void ").append("set"+method+"("+type+" "+name+") {\r\t");
			getset.append("    this."+name+"=").append(name).append(";\r\t}");
        }
        argv=str.toString();
        method=getset.toString();
		return argv+method;
    }
   
    public String getType(String type){
    	type=type.toLowerCase();
    	if("char".equals(type) || "varchar".equals(type) || "text".equals(type)){
			return "String";
		}else if("int".equals(type) ){
			return "Integer";
		}else if("bigint".equals(type)){
			return "Long";
		}else if("timestamp".equals(type) || "date".equals(type)  || "datetime".equals(type)){
			//return "java.sql.Timestamp";
			return "java.util.Date";
		}else if("double".equals(type)){
			return "Double";
		}else if("decimal".equals(type)){
			return "java.math.BigDecimal";
		}
    	return null;
    }
    public void getPackage(int type,String createPath, String content,String packageName,String className,String extendsClassName,String ... importName) throws Exception{
    	if(null==packageName){
    		packageName="";
    	}
    	StringBuffer sb=new StringBuffer();
    	sb.append("package ").append(packageName).append(";\r");
    	sb.append("\r");
    	for(int i=0;i<importName.length;i++){
    		sb.append("import ").append(importName[i]).append(";\r");
    	}
    	sb.append("\r");
    	sb.append("/**\r *  entity. @author wolf Date:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"\r */");
    	sb.append("\r");
    	sb.append("\rpublic class ").append(className);
    	if(null!=extendsClassName){
    		sb.append(" extends ").append(extendsClassName);
    	}
    	if(type==1){ //bean
    	   sb.append(" ").append("implements java.io.Serializable {\r");
    	}else{
    		sb.append(" {\r");
    	}
    	sb.append("\r\t");
    	sb.append("private static final long serialVersionUID = 1L;\r\t");
    	String temp=className.substring(0, 1).toLowerCase();
    	temp+= className.substring(1, className.length());
    	if(type==1){
    	sb.append("private "+className+" "+temp+"; // entity ");
    	}
    	sb.append(content);
    	sb.append("\r}");
//    	System.out.println(sb.toString());
    	this.createFile(createPath, "", sb.toString());
    }
   
    /**
     * 
     * 
     * 功能：表名转换成类名 每_首字母大写
     * @param tableName
     * @return
     */
    public String getTablesNameToClassName(String tableName, String prefix){
    	if(ChkUtil.isEmpty(prefix)) {
    		tableName = tableName.substring(0, tableName.length());
    	}else {
    		tableName = tableName.substring(prefix.length(), tableName.length());
    	}
    	String[] split=tableName.split("_");
    	if(split.length>1){
    		StringBuffer sb=new StringBuffer();
            for(int i=0;i<split.length;i++){
            	String tempTableName=split[i].substring(0, 1).toUpperCase()+split[i].substring(1, split[i].length());
                sb.append(tempTableName);
            }
//            System.out.println(sb.toString());
            return sb.toString();
    	}else{
    		String tempTables=split[0].substring(0, 1).toUpperCase()+split[0].substring(1, split[0].length());
    		return tempTables;
    	}
    }
    
    /**
     * 
     * 
     * 功能：表名转换成类名 每_首字母大写
     * @param tableName
     * @return
     */
    public String getColumnNameToBeansName(String columnName){
    	
    	String[] split = columnName.split("_");
    	if(split.length > 1){
    		StringBuffer sb = new StringBuffer();
    		sb.append(split[0]);
            for(int i=1;i<split.length;i++){
            	String tempColumnName = split[i].substring(0, 1).toUpperCase()+split[i].substring(1, split[i].length());
                sb.append(tempColumnName);
            }
            return sb.toString();
    	}else{
    		return split[0];
    	}
    }
    
    /**
     * 
     * 
     * 创建文件
     * 
     */
   public void createFile(String path,String fileName,String str) throws IOException{
    	FileWriter writer=new FileWriter(new File(path+fileName));
    	writer.write(new String(str.getBytes("utf-8")));
    	writer.flush();
    	writer.close();
    }
   /**
    * 生成SQL
    * @param tableName
    * @throws Exception
    */
    static String selectStr="select ";
    static String from=" from ";
    
    public Map<String,Object> getAutoCreateSql(String tableName) throws Exception{
   	 	 Map<String,Object> sqlMap=new HashMap<String, Object>();
   	 	 List<ColumnData> columnDatas = getColumnDatas(tableName);
	     String columns = this.getColumnSplit(columnDatas);
	     String[] columnList =  getColumnList(columns);  //表所有字段
	     String columnFields =  getColumnFields(columns); //表所有字段 按","隔开
	     // 获取bean字段
	     String beans = this.getBeanSplit(columnDatas);
	     String[] beanList =  getBeanList(beans);  //表所有实体属性
	     /** String beanFields =  getBeanFields(beans); //表所有实体属性 按","隔开*/
	     String insert="insert into "+tableName+"("+columns.replaceAll("\\|", ",")+")\n values(#{"+beans.replaceAll("\\|", "},#{")+"})";
	     String update= getUpdateSql(tableName, columnList, beanList);
	     String updateSelective = getUpdateSelectiveSql(tableName, columnDatas);
	     String selectById = getSelectByIdSql(tableName, columnList, beanList);
	     String delete = getDeleteSql(tableName, columnList, beanList);
	     String batchDelete = getBatchDeleteSql(tableName, columnList);
	     sqlMap.put("columnList",columnList);
	     sqlMap.put("columnFields",columnFields);
	     sqlMap.put("insert", insert);
	     sqlMap.put("update", update);
	     sqlMap.put("delete", delete);
	     sqlMap.put("updateSelective", updateSelective);
	     sqlMap.put("selectById", selectById);
	     sqlMap.put("batchDelete", batchDelete);
	     return sqlMap;
   }
    
    /**
     * delete 
     * @param tableName
     * @param columnsList
     * @return
     * @throws SQLException
     */
    public String getDeleteSql( String tableName,String[] columnsList, String[] beanList)throws SQLException{
   	 StringBuffer sb=new StringBuffer();
   	 sb.append("delete ");
	     sb.append("\t from ").append(tableName).append(" where ");
	     sb.append(columnsList[0]).append(" = #{").append(beanList[0]).append("}");
   	return sb.toString();
   }
    
    /**
     * batchDelete 
     * @param tableName
     * @param columnsList
     * @return
     * @throws SQLException
     */
    public String getBatchDeleteSql( String tableName,String[] columnsList)throws SQLException{
   	 StringBuffer sb=new StringBuffer();
   	 sb.append("delete ");
	     sb.append("\t from ").append(tableName).append(" where ");
	     sb.append(columnsList[0]).append(" in ");
   	return sb.toString();
   }
    
    /**
     * 根据id查询
     * @param tableName
     * @param columnsList
     * @return
     * @throws SQLException
     */
    public String getSelectByIdSql( String tableName,String[] columnsList, String[] beanList)throws SQLException{
    	 StringBuffer sb=new StringBuffer();
    	 sb.append("select <include refid=\"Base_Column_List\" /> \n");
 	     sb.append("\t from ").append(tableName).append(" where ");
 	     sb.append(columnsList[0]).append(" = #{").append(beanList[0]).append("}");
    	return sb.toString();
    }
    
    /**
     * 获取所有的字段，并按","分割
     * @param columns
     * @return
     * @throws SQLException
     */
    public String getColumnFields(String columns)throws SQLException{
    	String fields = columns;
    	if(fields != null && !"".equals(fields)){
    		fields = fields.replaceAll("[|]", ",");
    	}
    	return fields;
    }
    
    /**
     * 2015-11-12 weiyingni
     * 此处为重复代码，防止后期需要修改bean
     * 获取所有的实体对象，并按","分割
     * @param beans
     * @return
     * @throws SQLException
     */
    public String getBeanFields(String beans)throws SQLException{
    	String fields = beans;
    	if(fields != null && !"".equals(fields)){
    		fields = fields.replaceAll("[|]", ",");
    	}
    	return fields;
    }
    
    /**
     * @param columns
     * @return
     * @throws SQLException
     */
    public String[] getColumnList(String columns)throws SQLException{
    	 String[] columnList=columns.split("[|]");
	     return columnList;
    }
    
    /**
     * 2015-11-12 weiyingni
     * 此处为重复代码，防止后期需要修改bean
     * @param beans
     * @return
     * @throws SQLException
     */
    public String[] getBeanList(String beans)throws SQLException{
    	 String[] beanList = beans.split("[|]");
	     return beanList;
    }
    
    /**
     * 修改记录
     * @param tableName
     * @param columnsList
     * @return
     * @throws SQLException
     */
    public String getUpdateSql(String tableName,String[] columnsList, String [] beanList)throws SQLException{
    	 StringBuffer sb=new StringBuffer();
    	 
	     for(int i=1;i<columnsList.length;i++){
	    	  String column=columnsList[i];
	    	  String bean = beanList[i];
	    	  sb.append(column+"=#{"+bean+"}");
	    	  //最后一个字段不需要","
	    	  if((i+1) < columnsList.length){
	    		  sb.append(",");
	    	  }
	     }
    	 String update = "update "+tableName+" set "+sb.toString()+" where "+columnsList[0]+"=#{"+beanList[0]+"}";
	     return update;
    }
    
    public String getUpdateSelectiveSql(String tableName,List<ColumnData> columnList)throws SQLException{
   	 StringBuffer sb=new StringBuffer();
   	    ColumnData cd = columnList.get(0); //获取第一条记录，主键
   	 	sb.append("\t<trim  suffixOverrides=\",\" >\n");
   	 	 for(int i=1;i<columnList.size();i++){
   	 		 ColumnData data = columnList.get(i);
   	 		 String columnName = data.getColumnName();
   	 		 String beansName = data.getBeanName();
   	 		 sb.append("\t<if test=\"").append(beansName).append(" != null ");
   	 		 //String 还要判断是否为空
   	 		 if("String" == data.getDataType()){
   	 			sb.append(" and ").append(beansName).append(" != ''");
   	 		 }
   	 		 sb.append(" \">\n\t\t");
   	 		 sb.append(columnName+"=#{"+beansName+"},\n");
   	 		 sb.append("\t</if>\n");
	     }
	     sb.append("\t</trim>");
	     String update = "update "+tableName+" set \n"+sb.toString()+" where "+cd.getColumnName()+"=#{"+cd.getBeanName()+"}";
	     return update;
   }
   
    /**
     * 获取所有列名字
     */
    public String getColumnSplit(List<ColumnData> columnList) throws SQLException{
 	     StringBuffer commonColumns=new StringBuffer();
 	     for(ColumnData data : columnList){
 	    	 commonColumns.append(data.getColumnName()+"|");
 	     }
 	     return commonColumns.delete(commonColumns.length()-1, commonColumns.length()).toString();
    }
    
    /**
     * 2015-11-12
     * 添加方法获取所有bean名字
     */
    public String getBeanSplit(List<ColumnData> beanList) throws SQLException{
 	     StringBuffer commonbeans = new StringBuffer();
 	     for(ColumnData data : beanList){
 	    	commonbeans.append(data.getBeanName()+"|");
 	     }
 	     return commonbeans.delete(commonbeans.length()-1, commonbeans.length()).toString();
    }
}
