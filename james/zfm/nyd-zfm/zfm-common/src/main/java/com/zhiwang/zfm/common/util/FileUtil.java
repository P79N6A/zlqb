package com.zhiwang.zfm.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 文件操作类
 * 
 * @author Alan
 * 
 */
public class FileUtil {
	
	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * 使用文件通道的方式复制文件 如果新文件不存在 系统默认自己创建
	 * 
	 * @param s
	 *            源文件
	 * @param t
	 *            复制到的新文件
	 */
	public static String fileChannelCopy(File s, File t) {

		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			if (!t.exists()){
				//创建文件夹
				File foldFile = new File(t.getParent());
				if(!foldFile.exists()){
					
					foldFile.mkdirs();
				}
				//创建 文件
				t.createNewFile();
			}

			//复制文件
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道

		} catch (IOException e) {

			e.printStackTrace();
			return "";
		} finally {

			try {
				if(fi != null)
					fi.close();
				
				if(in != null)
					in.close();
				
				if(fo != null)
					fo.close();
				
				if(out != null)
					out.close();
				
			} catch (IOException e) {

				e.printStackTrace();
				return "";
			}

		}
		return t.getPath();
	}

	/**
	 * 删除单个文件
	 * @param sPath   被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {

		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) { 
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			}else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 获得文件后缀名
	 * @param file
	 * @return
	 */
	public static String getFileSuffix(File file,String fileName) {
		if(ChkUtil.isEmpty(fileName))
			fileName = file.getName();
			
	    String prefix = fileName.substring(fileName.lastIndexOf(".")+1);
		return prefix;
	}
	
	/**
	 * 
	 * 功能说明：generateFileName 生成随机文件名
	 * weiyingni  2015-8-30
	 * 最后修改时间：最后修改时间
	 * 修改人：
	 * 修改内容：
	 * 修改注意点：
	 * @throws IOException
	 */
	public static String generateFileName() {
		// 获得当前时间
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		// 转换为字符串
		String formatDate = format.format(new Date());
		// 随机生成文件编号
		int random = new Random().nextInt(10000);
		return new StringBuffer().append(formatDate).append(random).toString();
	}
	
	/** 
	 * @Description: 上传文件
	 * @Author: taohui   
	 * @param server
	 * @param fileUrl
	 * @param fileName
	 * @param inputStream
	 * @throws Exception
	 * @CreateDate: 2018年5月3日 下午6:52:00
	 * @throws Exception 异常
	 */
	public static void uploadFile(String fileUrl,String fileName,InputStream inputStream)throws Exception{
		
		File file = new File(fileUrl);
        if(!file.exists()&&!file.isDirectory()){
            System.out.println("目录或文件不存在！");
            file.mkdir();
        }
        //注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
        //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
        fileName = fileName.substring(fileName.lastIndexOf(File.separator)+1);
        //创建一个文件输出流
        FileOutputStream fos = new FileOutputStream(fileUrl+File.separator+fileName);
        //创建一个缓冲区
        byte buffer[] = new byte[1024];
        //判断输入流中的数据是否已经读完的标识
        int length = 0;
        //循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
        while((length = inputStream.read(buffer))>0){
            //使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
            fos.write(buffer, 0, length);
        }
        //关闭输入流
        inputStream.close();
        //关闭输出流
        fos.close();
	}
	
	/** 
	 * @Description: 下载文件
	 * @Author: taohui   
	 * @param response
	 * @param request
	 * @param fileUrl
	 * @param fileName
	 * @param toName
	 * @throws Exception
	 * @CreateDate: 2018年5月4日 上午9:40:19
	 * @throws Exception 异常
	 */
	public static void downFile(HttpServletResponse response, HttpServletRequest request, String fileUrl,String fileName)throws Exception{
	    //第一步：设置响应类型
		response.setContentType("application/force-download");//应用程序强制下载
	    //第二读取文件
	    String path = fileUrl+fileName;
	    InputStream in = new FileInputStream(path);
	    response.setHeader("Content-Disposition", "attachment;filename="+fileName);   
	    response.setContentLength(in.available());
	    
	    //第三步：老套路，开始copy
	    OutputStream out = response.getOutputStream();
	    byte[] b = new byte[1024];
	    int len = 0;
	    while((len = in.read(b))!=-1){
	      out.write(b, 0, len);
	    }
	    out.flush();
	    out.close();
	    in.close();
	}
	
	/**
	 * 下载文件
	 * @param response
	 * @param request
	 * @param fileUrl
	 * @param fileName
	 * @param toName
	 * @throws Exception
	 */
	public static void downFile(HttpServletResponse response, HttpServletRequest request, String fileUrl,String fileName, String toName)throws Exception{
	    //第一步：设置响应类型
		response.setContentType("application/force-download");//应用程序强制下载
	    //第二读取文件
	    String path = fileUrl+fileName;
	    InputStream in = new FileInputStream(path);
	    //设置响应头，对文件进行url编码
	    toName = URLEncoder.encode(toName, "UTF-8");
	    response.setHeader("Content-Disposition", "attachment;filename="+toName+"."+FileUtil.getFileSuffix(null, fileName));   
	    response.setContentLength(in.available());
	    
	    //第三步：老套路，开始copy
	    OutputStream out = response.getOutputStream();
	    byte[] b = new byte[1024];
	    int len = 0;
	    while((len = in.read(b))!=-1){
	      out.write(b, 0, len);
	    }
	    out.flush();
	    out.close();
	    in.close();
	}
	
	/**
	 * 下载/预览文件
	 * @author guojingjing
	 * @param filePath
	 * @param fileName
	 * @param response
	 * @param isOnLine true 预览 false 下载
	 * @throws Exception
	 */
	public static void downLoad(String filePath, String fileName, HttpServletResponse response, boolean isOnLine) throws Exception {
		URL url = new URL(filePath);
		InputStream br = new BufferedInputStream(url.openStream());
		
        byte[] buf = new byte[1024];
        int len = 0;
        response.reset(); // 非常重要
        response.setHeader("title", fileName);
        if (isOnLine) { // 在线打开方式
        	URL u = new URL(filePath); 
            response.setContentType(u.openConnection().getContentType());
            response.setContentType("application/pdf");  //  application/x-msdownload
            response.setHeader("Content-Disposition", "inline; filename=" +URLEncoder.encode(fileName, "UTF-8")+".pdf");
            // 文件名应该编码成UTF-8
        } else { // 纯下载方式
            response.setContentType("application/octet-stream");  //  application/x-msdownload
            response.setHeader("Content-Disposition", "attachment; filename=" +URLEncoder.encode(fileName, "UTF-8")+".pdf");
        }
        OutputStream out = response.getOutputStream();
        while ((len = br.read(buf)) > 0)
        out.write(buf, 0, len);
        br.close();
        out.close();       
	}

	/**
	 * 多文件打包下载
	 * @param filePath	文件路径
	 * @param fileName	文件名称
	 * @param tmpFileName	压缩包名称（带后缀名如：Demo.zip）
	 * @param response
	 * @throws Exception
	 */
	public static void downLoad(String[] filePath, String[] fileName, String tmpFileName, HttpServletResponse response) throws Exception {
        byte[] buffer = new byte[1024];    
        try {    
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(    
            		tmpFileName));    
            for (int i = 0; i < filePath.length; i++) {
            	URL url = new URL(filePath[i]);
        		InputStream fis = new BufferedInputStream(url.openStream());
                out.putNextEntry(new ZipEntry(fileName[i]));    
                int len;    
                // 读入需要下载的文件的内容，打包到zip文件    
                while ((len = fis.read(buffer)) > 0) {    
                    out.write(buffer, 0, len);    
                }    
                out.closeEntry();    
                fis.close();    
            }    
            out.close();    
            downFile(response, tmpFileName);    
        } catch (Exception e) {    
            logger.error("文件下载出错", e);    
        }    
        return;
	}
	
	 /**   
     * 文件下载   
     * @param response   
     * @param str   
     */    
    private static void downFile(HttpServletResponse response, String str) {    
        try {    
            File file = new File(str);    
            if (file.exists()) {    
                InputStream ins = new FileInputStream(str);    
                BufferedInputStream bins = new BufferedInputStream(ins);// 放到缓冲流里面    
                OutputStream outs = response.getOutputStream();// 获取文件输出IO流    
                BufferedOutputStream bouts = new BufferedOutputStream(outs);    
                response.setContentType("application/x-download");// 设置response内容的类型    
                response.setHeader(    
                        "Content-disposition",    
                        "attachment;filename="    
                                + URLEncoder.encode(str, "UTF-8"));// 设置头部信息    
                int bytesRead = 0;    
                byte[] buffer = new byte[8192];    
                // 开始向网络传输文件流    
                while ((bytesRead = bins.read(buffer, 0, 8192)) != -1) {    
                    bouts.write(buffer, 0, bytesRead);    
                }    
                bouts.flush();// 这里一定要调用flush()方法    
                ins.close();    
                bins.close();    
                outs.close();    
                bouts.close();    
            }
        } catch (IOException e) {    
        	e.printStackTrace();
        	logger.error(e.getMessage(), e);
            logger.error("文件下载出错,请求参数：{}", new Object[]{str});    
        }    
}    
	
	public static void main(String[] args) throws Exception {
//		String sourceFilePath = "F:\\apache-tomcat-6.0.29\\webapps\\erpv3_client\\erpv3\\hr\\fileUpload\\testZip.zip";

//		File s = new File(sourceFilePath);
		
//		System.out.println(getFileSuffix(s));;
//		File t = new File(
//				"F:\\apache-tomcat-6.0.29\\webapps\\erpv3_client\\erpv3\\hr\\fileUpload\\sb.zip");
//		if (t.exists()) {
//			t.createNewFile();
//		}
//		fileChannelCopy(s, t);
	}
}
