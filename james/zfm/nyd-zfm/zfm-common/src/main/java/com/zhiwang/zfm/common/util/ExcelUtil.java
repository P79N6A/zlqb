package com.zhiwang.zfm.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;


/**
 * 
 * 功能说明：excel 工具类
 * @author panye
 * 修改人: 
 * 修改原因：
 * 修改时间：
 * 修改内容：
 * 创建日期：2015-5-12
 * Copyright zzl-apt
 */

@Component
public class ExcelUtil {
	
	private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
	
	/**
	 * 
	 * 功能说明：根据导入的文件进行解析		
	 * yh  2014-11-29
	 * @param excelFilePath 文件地址   titles 表头  realTitles 对应的表值		
	 * @return String 时间字符串   
	 * @throws  
	 * 最后修改时间：
	 * 修改人：yh
	 * 修改内容：
	 * 修改注意点：
	 * @throws IOException
	 */
	public static JSONObject importExcel(MultipartFile excelFilePath,String[] titles,String[] realTitles){
		//返回的结果集合
		JSONObject resultJson =  new JSONObject();
		resultJson.put("status", "y");
		Workbook  wb = null;
		try{
			//如果是xlsx的则是2007版本的
			wb = (Workbook) new HSSFWorkbook(excelFilePath.getInputStream());//new XSSFWorkbook(excelFilePath.getInputStream());
			}catch(Exception e){
				try {
					wb =new XSSFWorkbook(excelFilePath.getInputStream());
				} catch (IOException e1) {
					logger.warn(e.getMessage(),e);
				}
		}
		Sheet sheet= wb.getSheetAt(0);											//得到第一个工作簿
		Cell cell1 = sheet.getRow(1).getCell(2);
		int sumRow=sheet.getPhysicalNumberOfRows();								//得到所有的row行数
		Row row0=sheet.getRow(0);  
		//得到行头  并解析
		for(int i=0;i<titles.length;i++){
			Cell cell=row0.getCell(i);											//得到对应的抬头
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);						//设置cell为字符串类型
			if(!cell.toString().equals(titles[i])){
				resultJson.put("status", "n");
				resultJson.put("info", "表头与实际不匹配!"); 
				return resultJson;
			}
		}
		//用来存储数据的List
		List<Map> jsonList=new ArrayList<Map>();
		//得到表具体内容并解析
		for(int i=1;i<sumRow;i++){
			Row row = sheet.getRow(i);
			Map baseMap=new HashMap();
			for(int j=0;j<realTitles.length;j++){
				row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
				baseMap.put(realTitles[j],getCellValue(row.getCell(j)));
			}
			jsonList.add(baseMap);
		}
		resultJson.put("rows", jsonList);
		return resultJson;
	}
	/**
	 * 
	 * 功能说明：根据导入的文件进行解析		
	 * yh  2014-11-29
	 * @param excelFilePath 文件地址   titles 表头  realTitles 对应的表值		
	 * @return String 时间字符串   
	 * @throws  
	 * 最后修改时间：
	 * 修改人：yh
	 * 修改内容：
	 * 修改注意点：
	 * @throws IOException
	 */
	public static JSONObject importExcel(MultipartFile excelFilePath,String[] titlesMap){
		//返回的结果集合
		JSONObject resultJson =  new JSONObject();
		resultJson.put("status", "y");
		if(titlesMap ==null || titlesMap.length==0){
			resultJson.put("status", "n");
			resultJson.put("info", "实际内容不能为空!"); 
			return resultJson;
		}
		String[] titles = new String[titlesMap.length];
		String[] realTitles = new String[titlesMap.length];
		for(int i=0;i<titlesMap.length;i++){
			String[] title = titlesMap[i].split(Pattern.quote(":"));
			titles[i] = title[0];
			realTitles[i] = title[1];
		}
		Workbook  wb = null;
		try{
			//如果是xlsx的则是2007版本的
			wb = (Workbook) new HSSFWorkbook(excelFilePath.getInputStream());//new XSSFWorkbook(excelFilePath.getInputStream());
		}catch(Exception e){
			try {
				wb =new XSSFWorkbook(excelFilePath.getInputStream());
			} catch (IOException e1) {
				logger.warn(e.getMessage(),e);
			}
		}
		Sheet sheet= wb.getSheetAt(0);	
		int sumRow=sheet.getPhysicalNumberOfRows();								//得到所有的row行数
		Row row0=sheet.getRow(0);  
		//得到行头  并解析
		for(int i=0;i<titles.length;i++){
			Cell cell=row0.getCell(i);											//得到对应的抬头
			if (cell.getCellType() != HSSFCell.CELL_TYPE_NUMERIC)
			{
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);						//设置cell为字符串类型
			}
			if(!cell.toString().equals(titles[i])){
				resultJson.put("status", "n");
				resultJson.put("info", "表头与实际不匹配!"); 
				return resultJson;
			}
		}
		//用来存储数据的List
		List<Map> jsonList=new ArrayList<Map>();
		//得到表具体内容并解析
		for(int i=1;i<sumRow;i++){
			Row row = sheet.getRow(i);
			Map baseMap=new HashMap();
			for(int j=0;j<realTitles.length;j++){
				if(row.getCell(j) == null || "".equals(getCellValue(row.getCell(j)))){
					resultJson.put("status", "n");
					System.out.println(realTitles[j]);
					resultJson.put("info","第"+(i+1)+"行,第"+(j+1)+"列的数据为空操作终止!"); 
					return resultJson;
				}
				if(row.getCell(j).getCellType() != Cell.CELL_TYPE_STRING && HSSFDateUtil.isCellDateFormatted(row.getCell(j))){
					String time = "";
				//用于转化为日期格式
				Date d = row.getCell(j).getDateCellValue();
				DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
				time = formater.format(d);
				baseMap.put(realTitles[j],time);
				}
				else
				{
					row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
					baseMap.put(realTitles[j],getCellValue(row.getCell(j)));
				}
			}
			jsonList.add(baseMap);
		}
		resultJson.put("rows", jsonList);
		resultJson.put("sheetName",wb.getSheetName(0));
		return resultJson;
	}
	/**
	 * 
	 * 功能说明：根据导入的文件进行解析		
	 * yh  2014-11-29
	 * @param excelFilePath 文件地址   titles 表头  realTitles 对应的表值		
	 * @return String 时间字符串   
	 * @throws  
	 * 最后修改时间：
	 * 修改人：yh
	 * 修改内容：
	 * 修改注意点：
	 * @throws IOException
	 */
	public static JSONObject importExcelFilterEmpty(MultipartFile excelFilePath,String[] titlesMap){
		//返回的结果集合
		JSONObject resultJson =  new JSONObject();
		resultJson.put("status", "y");
		if(titlesMap ==null || titlesMap.length==0){
			resultJson.put("status", "n");
			resultJson.put("info", "实际内容不能为空!"); 
			return resultJson;
		}
		String[] titles = new String[titlesMap.length];
		String[] realTitles = new String[titlesMap.length];
		for(int i=0;i<titlesMap.length;i++){
			String[] title = titlesMap[i].split(Pattern.quote(":"));
			titles[i] = title[0];
			realTitles[i] = title[1];
		}
		Workbook  wb = null;
		try{
			//如果是xlsx的则是2007版本的
			wb = (Workbook) new HSSFWorkbook(excelFilePath.getInputStream());//new XSSFWorkbook(excelFilePath.getInputStream());
		}catch(Exception e){
			try {
				wb =new XSSFWorkbook(excelFilePath.getInputStream());
			} catch (IOException e1) {
				logger.warn(e.getMessage(),e);
			}
		}
		Sheet sheet= wb.getSheetAt(0);	
		int sumRow=sheet.getPhysicalNumberOfRows();								//得到所有的row行数
		Row row0=sheet.getRow(0);  
		//得到行头  并解析
		for(int i=0;i<titles.length;i++){
			Cell cell=row0.getCell(i);											//得到对应的抬头
			if (cell.getCellType() != HSSFCell.CELL_TYPE_NUMERIC)
			{
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);						//设置cell为字符串类型
			}
			if(!cell.toString().equals(titles[i])){
				resultJson.put("status", "n");
				resultJson.put("info", "表头与实际不匹配!"); 
				return resultJson;
			}
		}
		//用来存储数据的List
		List<Map> jsonList=new ArrayList<Map>();
		//得到表具体内容并解析
		for(int i=1;i<sumRow;i++){
			Row row = sheet.getRow(i);
			Map baseMap=new HashMap();
			for(int j=0;j<realTitles.length;j++){
				if(row.getCell(j) == null || "".equals(getCellValue(row.getCell(j)))){
					row.getCell(j).setCellValue("");
				}
				if(row.getCell(j).getCellType() != Cell.CELL_TYPE_STRING && HSSFDateUtil.isCellDateFormatted(row.getCell(j))){
					String time = "";
					//用于转化为日期格式
					Date d = row.getCell(j).getDateCellValue();
					DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
					time = formater.format(d);
					baseMap.put(realTitles[j],time);
				}
				else
				{
					row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
					baseMap.put(realTitles[j],getCellValue(row.getCell(j)));
				}
			}
			jsonList.add(baseMap);
		}
		resultJson.put("rows", jsonList);
		resultJson.put("sheetName",wb.getSheetName(0));
		return resultJson;
	}
	/**
	 * 
	 * 功能说明：根据导入的文件进行解析		
	 * yh  2014-11-29
	 * @param excelFilePath 文件地址   titles  表头  realTitles 对应的表值		
	 * @return String 时间字符串   
	 * @throws  
	 * 最后修改时间：
	 * 修改人：yh
	 * 修改内容：
	 * 修改注意点：
	 * @throws IOException 
	 */
	public static String getCellValue(Cell cell) {  
	        String ret;  
	        switch (cell.getCellType()) {  
	        case Cell.CELL_TYPE_BLANK:  
	            ret = "";  
	            break;  
	        case Cell.CELL_TYPE_BOOLEAN:  
	            ret = String.valueOf(cell.getBooleanCellValue());  
	            break;  
	        case Cell.CELL_TYPE_ERROR:  
	            ret = null;  
	            break;  
	        case Cell.CELL_TYPE_FORMULA:  
	            Workbook wb = cell.getSheet().getWorkbook();  
	            CreationHelper crateHelper = wb.getCreationHelper();  
	            FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();  
	            ret = getCellValue(evaluator.evaluateInCell(cell));  
	            break;  
	       /* case Cell.CELL_TYPE_NUMERIC:    //时间的暂时注释掉
	            if (DateUtil.isCellDateFormatted(cell)) {   
	                Date theDate = cell.getDateCellValue();  
	                ret = simpleDateFormat.format(theDate);  
	            } else {   
	                ret = NumberToTextConverter.toText(cell.getNumericCellValue());  
	            }  
	            break;  */
	        case Cell.CELL_TYPE_STRING:  
	            ret = cell.getRichStringCellValue().getString();  
	            break;  
	        default:  
	            ret = null;  
	        }  
	          
	        return ret; //有必要自行trim  
	    } 
	
	 @SuppressWarnings("deprecation")
	 public static String createExcel(List<Map> dataList,HttpServletRequest request) {
		 String webRootPath = request.getSession().getServletContext().getRealPath("/");
		 String fileToBeRead = webRootPath+"excel/eforder.xls";
		 long time = System.currentTimeMillis();
		 String target = webRootPath+"excel/eforder_"+time+"_copy.xls"; 
	     copy(fileToBeRead,target);
	     String[] str = {"investmentTime","freezeTime","proName","compName","deptName","empName","custName","custIc","orderNunber","clearingChannel"
	    		 ,"eectronicAccount","cardNumber","bankNumber","bankSubbranch","cityName","endTime","periods","principal","interest","money","freezeTime","age","className"
	    		 ,"enrollSource"};
	     try {
	    	 Workbook  workbook = null;
	    	 try {
	    		 workbook = new XSSFWorkbook (new FileInputStream(target));
			} catch (Exception e) {
				workbook = new HSSFWorkbook (new FileInputStream(target));
			}
	    	 
	         Sheet sheet = workbook.getSheet("Sheet1");
	         FileOutputStream out = null;
	       //设置单元格样式
//	         XSSFCellStyle borderStyle = setCellStyle(workbook);
	         //获得数据集合
	         if(dataList!=null && dataList.size()>0){
	        	 for(int i=1;i<(dataList.size()+1);i++){
	        		 Row row = sheet.createRow(i);
	        		 for (int j = 0; j < str.length; j++) {
	        			 Map map = dataList.get(i-1);
	        			 if("null".equals(map.get(str[j]).toString())){
	        				 row.createCell(j).setCellValue("");
	        			 }else{
	        				 row.createCell(j).setCellValue(map.get(str[j]).toString());
	        			 }
//	        			 row.createCell(j).setCellStyle(borderStyle);
					}
	            }
	          }
	         
	         
	         try {
	                out = new FileOutputStream(target);
	                workbook.write(out);
	                out.close();
	            } catch (IOException e) {
	            	logger.warn(e.getMessage(),e);
	            } finally {
	                try {
	                    out.close();
	                } catch (IOException e) {
	                	logger.warn(e.getMessage(),e);
	                }
	          }
		} catch (FileNotFoundException e) {
			logger.warn(e.getMessage(),e);
        } catch (IOException e) {
        	logger.warn(e.getMessage(),e);
        }
		 return target;
	 }
	 /**
		 * 
		 * 功能说明：excel导出
		 * yh  2014-11-29
		 * @param excelFilePath 文件地址   titles  表头  realTitles 对应的表值		
		 * @return String 时间字符串   
		 * @throws  
		 * 最后修改时间：
		 * 修改人：yh
		 * 修改内容：
		 * 修改注意点：
		 * @throws IOException 
		 */
	 @SuppressWarnings("deprecation")
	 public static String createExcel(List<Map> dataList,HttpServletRequest request,String[] str,String fromName) {
		 String webRootPath = request.getSession().getServletContext().getRealPath("/");
		 String fileToBeRead = webRootPath+"excel/"+fromName;
		 long time = System.currentTimeMillis();
		 String target = webRootPath+"excel/"+fromName+time+"_copy.xls"; 
	     copy(fileToBeRead,target);
	     try {
	    	 Workbook  workbook = null;
	    	 try {
	    		 workbook = new XSSFWorkbook (new FileInputStream(target));
			} catch (Exception e) {
				workbook = new HSSFWorkbook (new FileInputStream(target));
			}
	    	 
	         Sheet sheet = workbook.getSheetAt(0);
	         FileOutputStream out = null;
	         //获得数据集合
	         if(dataList!=null && dataList.size()>0){
	        	 for(int i=1;i<(dataList.size()+1);i++){
	        		 Row row = sheet.createRow(i);
	        		 for (int j = 0; j < str.length; j++) {
	        			 Map map = dataList.get(i-1);
	        			 if(null==map.get(str[j]) || "null".equals(map.get(str[j]).toString())){
	        				 row.createCell(j).setCellValue("");
	        			 }else{
							if (map.get(str[j]) instanceof Double) {
								Double pd = (Double) map.get(str[j]);
								row.createCell(j).setCellValue(pd);
							} else if (map.get(str[j]) instanceof Integer) {
								Integer pd = (Integer) map.get(str[j]);
								row.createCell(j).setCellValue(pd);
							} else {
								row.createCell(j).setCellValue(map.get(str[j]).toString());
							}
	        			 }
					}
	            }
	          }
	         try {
	                out = new FileOutputStream(target);
	                workbook.write(out);
	                out.close();
	            } catch (IOException e) {
	            	logger.warn(e.getMessage(),e);
	            } finally {
	                try {
	                    out.close();
	                } catch (IOException e) {
	                	logger.warn(e.getMessage(),e);
	                }
	          }
		} catch (FileNotFoundException e) {
			logger.warn(e.getMessage(),e);
        } catch (IOException e) {
        	logger.warn(e.getMessage(),e);
        }
		 return target;
	 }
	 /**
	  * 
	  * 功能说明：excel导出
	  * yh  2014-11-29
	  * @param excelFilePath 文件地址   titles  表头  realTitles 对应的表值		
	  * @return String 时间字符串   
	  * @throws  
	  * 最后修改时间：
	  * 修改人：yh
	  * 修改内容：
	  * 修改注意点：
	  * @throws IOException 
	  */
	 @SuppressWarnings("deprecation")
	 public static String createExcelXlsx(List<Map> dataList,HttpServletRequest request,String[] str,String fromName) {
		 String webRootPath = request.getSession().getServletContext().getRealPath("/");
		 String fileToBeRead = webRootPath+"excel/"+fromName;
		 long time = System.currentTimeMillis();
		 String target = webRootPath+"excel/"+fromName+time+"_copy.xlsx"; 
		 copy(fileToBeRead,target);
		 XSSFWorkbook baseworkbook=null;
		try {
			baseworkbook = new XSSFWorkbook (new FileInputStream(target));
		} catch (IOException e1) {
		}
		 SXSSFWorkbook workbook = new SXSSFWorkbook(baseworkbook,1000);
		 Sheet sheet = workbook.getSheetAt(0);
		 FileOutputStream out = null;
		 //获得数据集合
		 if(dataList!=null && dataList.size()>0){
			 for(int i=1;i<(dataList.size()+1);i++){
				 Row row = sheet.createRow(i);
				 for (int j = 0; j < str.length; j++) {
					 Map map = dataList.get(i-1);
					 if(null==map.get(str[j]) || "null".equals(map.get(str[j]).toString())){
						 row.createCell(j).setCellValue("");
					 }else{
						 row.createCell(j).setCellValue(map.get(str[j]).toString());
					 }
				 }
			 }
		 }
		 try {
			 out = new FileOutputStream(target);
			 workbook.write(out);
			 out.close();
		 } catch (IOException e) {
			 logger.warn(e.getMessage(),e);
		 } finally {
			 try {
				 out.close();
			 } catch (IOException e) {
				 logger.warn(e.getMessage(),e);
			 }
		 }
		 return target;
	 }
	 /**
	  * 
	  * 功能说明：excel导出
	  * yh  2014-11-29
	  * @param excelFilePath 文件地址   titles  表头  realTitles 对应的表值		
	  * @return String 时间字符串   
	  * @throws  
	  * 最后修改时间：
	  * 修改人：yh
	  * 修改内容：
	  * 修改注意点：
	  * @throws IOException 
	  */
	 @SuppressWarnings("deprecation")
	 public static String createExcelXlsxWithNoTemplete(String[] titles,String[] columns,List<com.alibaba.fastjson.JSONObject> dataList,HttpServletRequest request) {
		 String webRootPath = request.getSession().getServletContext().getRealPath("/");
		 long time = System.currentTimeMillis();
		 String target = webRootPath+"excel/temp"+time+"_copy.xlsx"; 
		 XSSFWorkbook baseworkbook=null;
		 try {
			 baseworkbook = new XSSFWorkbook (new FileInputStream(target));
		 } catch (IOException e1) {
		 }
		 SXSSFWorkbook workbook = new SXSSFWorkbook(baseworkbook,1000);
		 Sheet sheet = workbook.createSheet();
		 FileOutputStream out = null;
		 Row firstRow = sheet.createRow(0);
		 for (int i = 0; i < titles.length; i++) {
			 firstRow.createCell(i).setCellValue(titles[i]);
		 }
		 //获得数据集合
		 if(dataList!=null && dataList.size()>0){
			 for(int i=1;i<(dataList.size()+1);i++){
				 Row row = sheet.createRow(i);
				 for (int j = 0; j < columns.length; j++) {
					 Map map = dataList.get(i-1);
					 if(null==map.get(columns[j]) || "null".equals(map.get(columns[j]).toString())){
						 row.createCell(j).setCellValue("");
					 }else{
						 row.createCell(j).setCellValue(map.get(columns[j]).toString());
					 }
				 }
			 }
		 }
		 try {
			 out = new FileOutputStream(target);
			 workbook.write(out);
			 out.close();
		 } catch (IOException e) {
			 logger.warn(e.getMessage(),e);
		 } finally {
			 try {
				 out.close();
			 } catch (IOException e) {
				 logger.warn(e.getMessage(),e);
			 }
		 }
		 return target;
	 }
	 /**
	  * 
	  * 功能说明：excel导出
	  * yh  2014-11-29
	  * @param excelFilePath 文件地址   titles  表头  realTitles 对应的表值		
	  * @return String 时间字符串   
	  * @throws  
	  * 最后修改时间：
	  * 修改人：yh
	  * 修改内容：
	  * 修改注意点：
	  * @throws IOException 
	  */
	 @SuppressWarnings("deprecation")
	 public static String createExcelXlsx(List<Map> dataList,HttpServletRequest request,String[] str,String fromName,int beginH) {
		 String webRootPath = request.getSession().getServletContext().getRealPath("/");
		 String fileToBeRead = webRootPath+"excel/"+fromName;
		 long time = System.currentTimeMillis();
		 String target = webRootPath+"excel/"+fromName+time+"_copy.xlsx"; 
		 copy(fileToBeRead,target);
		 XSSFWorkbook baseworkbook=null;
		 try {
			 baseworkbook = new XSSFWorkbook (new FileInputStream(target));
		 } catch (IOException e1) {
		 }
		 SXSSFWorkbook workbook = new SXSSFWorkbook(baseworkbook,1000);
		 Sheet sheet = workbook.getSheetAt(0);
		 FileOutputStream out = null;
		 //获得数据集合
		 if(dataList!=null && dataList.size()>0){
			 for(int i=beginH;i<(dataList.size()+beginH);i++){
				 Row row = sheet.createRow(i);
				 for (int j = 0; j < str.length; j++) {
					 Map map = dataList.get(i-beginH);
					 if(null==map.get(str[j]) || "null".equals(map.get(str[j]).toString())){
						 row.createCell(j).setCellValue("");
					 }else{
						 row.createCell(j).setCellValue(map.get(str[j]).toString());
					 }
				 }
			 }
		 }
		 try {
			 out = new FileOutputStream(target);
			 workbook.write(out);
			 out.close();
		 } catch (IOException e) {
			 logger.warn(e.getMessage(),e);
		 } finally {
			 try {
				 out.close();
			 } catch (IOException e) {
				 logger.warn(e.getMessage(),e);
			 }
		 }
		 return target;
	 }
	    /**
	     * 文件拷贝
	     * @param oldPath 源文件
	     * @param newPath 目标文件
	     */
	    public static void copy(String oldPath,String newPath) {   
	           try{   
	               int bytesum = 0;   
	               int byteread = 0;   
	               File oldfile = new File(oldPath);   
	               if(oldfile.exists()){     
	                   InputStream  inStream = new FileInputStream(oldPath);    
	                   FileOutputStream fs = new FileOutputStream(newPath);   
	                   byte[] buffer = new byte[1444];   
	                   int length;   
	                   while ((byteread = inStream.read(buffer)) != -1)     {   
	                           bytesum += byteread;       
	                           fs.write(buffer,0,byteread);   
	                   }   
	                   inStream.close();  
	                   fs.close();
	               }   
	           }   
	           catch(Exception e)     {   
	               System.out.println( "error  ");   
	               logger.warn(e.getMessage(),e);
	           }   
	     }
	    
	    /**
	     * 设置单元格样式
	     * @param workbook
	     * @return
	     */
		private static XSSFCellStyle setCellStyle(Workbook workbook) {
			XSSFCellStyle borderStyle = (XSSFCellStyle)workbook.createCellStyle();
			XSSFColor  borderColor=new XSSFColor();
			byte[] bytecolor={(byte) 127,(byte)127,(byte)127};
			borderColor.setRgb(bytecolor);
			borderStyle.setBorderColor(BorderSide.RIGHT, borderColor);
			borderStyle.setBorderColor(BorderSide.LEFT, borderColor);
			borderStyle.setBorderColor(BorderSide.TOP, borderColor);
			borderStyle.setBorderColor(BorderSide.BOTTOM, borderColor);
			borderStyle.setBorderRight(CellStyle.BORDER_THIN);//设置细边框
			borderStyle.setBorderLeft(CellStyle.BORDER_THIN);
			borderStyle.setBorderTop(CellStyle.BORDER_THIN);
			borderStyle.setBorderBottom(CellStyle.BORDER_THIN);
			borderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			return borderStyle;
		}
		
	    /**
	     * 将生成好的excel文件下载  
	     */
	    public static void DownLoadExcel(HttpServletResponse response,String filename,String targetFilePath){
	    	//写入流
		    try{
		    	File file=new File(targetFilePath);
				BufferedInputStream br = new BufferedInputStream(new FileInputStream(file));
				//缓冲
				byte[] buf = new byte[1024];
				int len = 0;
				//response.reset(); // 重置
				response.setContentType("application/x-msdownload;charset=utf-8");
				String fname= "";
		    	fname = new String(filename.getBytes("gb2312"),"iso-8859-1");
		    	
				response.setHeader("Content-Disposition", "attachment; filename="+ fname+".xls");
				// 创建输出流对象
				OutputStream outStream = response.getOutputStream();
				// 开始输出
				while ((len = br.read(buf)) > 0)
					outStream.write(buf, 0, len);
				// 关闭流对象
				br.close();
				outStream.flush(); 
				outStream.close();
				if(file.exists()){
					file.delete();
				}
			    file.deleteOnExit();
			} catch (Exception e) {
				logger.warn(e.getMessage(),e);
			}
	    }
	    /**
	     * 将生成好的excel文件下载  
	     */
	    public static void DownLoadExcelXlsx(HttpServletResponse response,String filename,String targetFilePath){
	    	//写入流
	    	try{
	    		File file=new File(targetFilePath);
	    		BufferedInputStream br = new BufferedInputStream(new FileInputStream(file));
	    		//缓冲
	    		byte[] buf = new byte[1024];
	    		int len = 0;
	    		//response.reset(); // 重置
	    		response.setContentType("application/x-msdownload;charset=utf-8");
	    		String fname= "";
	    		fname = new String(filename.getBytes("gb2312"),"iso-8859-1");
	    		
	    		response.setHeader("Content-Disposition", "attachment; filename="+ fname+".xlsx");
	    		// 创建输出流对象
	    		OutputStream outStream = response.getOutputStream();
	    		// 开始输出
	    		while ((len = br.read(buf)) > 0)
	    			outStream.write(buf, 0, len);
	    		// 关闭流对象
	    		br.close();
	    		outStream.flush(); 
	    		outStream.close();
	    		if(file.exists()){
	    			file.delete();
	    		}
	    		file.deleteOnExit();
	    	} catch (Exception e) {
	    		logger.warn(e.getMessage(),e);
	    	}
	    }
		/**
		 * 
		 * 功能说明：根据导入的文件进行解析		
		 * yh  2014-11-29
		 * @param excelFilePath 文件地址   titles 表头  realTitles 对应的表值		
		 * @return String 时间字符串   
		 * @throws  
		 * 最后修改时间：
		 * 修改人：yh
		 * 修改内容：
		 * 修改注意点：
		 * @throws IOException
		 */
		public static JSONObject importExcel(MultipartFile excelFilePath,String[][] titlesMap){
			//返回的结果集合
			JSONObject resultJson =  new JSONObject();
			resultJson.put("status", "y");
			if(titlesMap ==null || titlesMap.length==0){
				resultJson.put("status", "n");
				resultJson.put("info", "实际内容不能为空!"); 
				return resultJson;
			}
			Workbook  wb = null;
			try{
				//如果是xlsx的则是2007版本的
				wb = (Workbook) new HSSFWorkbook(excelFilePath.getInputStream());//new XSSFWorkbook(excelFilePath.getInputStream());
			}catch(Exception e){
				try {
					wb =new XSSFWorkbook(excelFilePath.getInputStream());
				} catch (IOException e1) {
					logger.warn(e.getMessage(),e);
				}
			}
			int sheetTotal = wb.getNumberOfSheets();
			resultJson.put("sheetTotal", sheetTotal);
			JSONArray jsonArray = new JSONArray();
			resultJson.put("data", jsonArray);
			if(sheetTotal>0){
				JSONObject currentJson = new JSONObject();
				String infoCount="";
				for(int sheetIndex=0;sheetIndex<sheetTotal;sheetIndex++){
					String[] titles = new String[titlesMap[sheetIndex].length];
					String[] realTitles = new String[titlesMap[sheetIndex].length];
//					for (int j = 0; j < titlesMap.length; j++) {
						for(int i=0;i<titlesMap[sheetIndex].length;i++){
							String[] title = titlesMap[sheetIndex][i].split(Pattern.quote(":"));
							titles[i] = title[0];
							realTitles[i] = title[1];
						}
//					}
					Sheet sheet= wb.getSheetAt(sheetIndex);	
					int sumRow=sheet.getPhysicalNumberOfRows();								//得到所有的row行数
					Row row0=sheet.getRow(0);  
					//得到行头  并解析
					for(int i=0;i<titles.length;i++){
						Cell cell=row0.getCell(i);											//得到对应的抬头
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);						//设置cell为字符串类型
						if(!cell.toString().equals(titles[i])){
							resultJson.put("status", "n");
							resultJson.put("info", "表头与实际不匹配!"); 
							return resultJson;
						}
					}
					//用来存储数据的List
					List<Map> jsonList=new ArrayList<Map>();
					//得到表具体内容并解析
					for(int i=1;i<sumRow;i++){
						Row row = sheet.getRow(i);
						//如果发现某一行的第一列为空，不继续导入
						if(row==null||row.getCell(0) == null || "".equals(getCellValue(row.getCell(0)))){
							infoCount+="第"+(sheetIndex+1)+"sheet,"+"第"+(i+1)+"行,第1列的数据为空,后续数据不继续处理";
							break;
						}
						
						Map baseMap=new HashMap();
						for(int j=0;j<realTitles.length;j++){
							if(row.getCell(j) == null || "".equals(getCellValue(row.getCell(j)))){
								infoCount+="第"+(sheetIndex+1)+"sheet,"+"第"+(i+1)+"行,第"+(j+1)+"列的数据为空操作终止、";
								resultJson.put("info", infoCount); 
//								return resultJson;
								baseMap.put(realTitles[j],"");
							}else {
								row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
								baseMap.put(realTitles[j],getCellValue(row.getCell(j)));
							}
							resultJson.put("status", "y");
						}
						jsonList.add(baseMap);
					}
					currentJson.put("rows", jsonList);
					currentJson.put("sheetName",wb.getSheetName(sheetIndex));
					jsonArray.add(currentJson);
				}
			}
			resultJson.put("data", jsonArray);
			return resultJson;
		}
}
