package com.zhiwang.zfm.controller.webapp.order;

import io.swagger.annotations.Api;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nyd.order.api.zzl.OrderForZQServise;
import com.nyd.user.api.zzl.UserForZQServise;
import com.nyd.user.model.enums.ContratNameEnum;
import com.zhiwang.zfm.controller.task.Auto2Controller;

@RestController
@RequestMapping("/web/order")
@Api(description="查看pdf")
public class DownPdfController {

	private Logger logger = LoggerFactory.getLogger(Auto2Controller.class);

	@Autowired
	private OrderForZQServise orderForZQServise;
	
	@Autowired
	private UserForZQServise userForZQServise;
	
	@GetMapping("/viewContract")
	public void viewContract(HttpServletRequest reqeust,HttpServletResponse response,String url,String code) {
		try {
			
			//输出字符转换编码
			response.setContentType("text/html;charset=UTF-8");
			//在线预览
			String contratName = ContratNameEnum.fromDesc(code);
			downLoad(url,contratName,response,true);
		} catch (Exception e) {
			logger.error("合同在线查看异常，参数 :url{}", new Object[]{ url });
			logger.error(e.getMessage(), e);
//			throw new BusinessException();
		}
	}
	

	private void downLoad(String filePath,String fileName, HttpServletResponse response, boolean isOnLine) throws Exception {
		
		URL url = new URL(filePath);
		URLConnection conn = url.openConnection();
		// 合同中心token
//		JSONObject resultJson = BusiCtrlUtil.contractCenterToken();
//		conn.setRequestProperty("Authorization", resultJson.getString("data"));
	    InputStream inStream = conn.getInputStream();
        BufferedInputStream br = new BufferedInputStream(inStream);
        byte[] buf = new byte[1024];
        int len = 0;
        response.reset(); // 非常重要
        
        if (isOnLine) { // 在线打开方式
        		URL u = new URL(filePath); 
            response.setContentType(u.openConnection().getContentType());
            response.setContentType("application/pdf");  //  application/x-msdownload
            response.setHeader("Content-Disposition", "inline; filename=" +new String(fileName.getBytes(),"ISO8859-1")+".pdf");
            // 文件名应该编码成UTF-8
        } else { // 纯下载方式
            response.setContentType("application/octet-stream");  //  application/x-msdownload
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes(),"ISO8859-1")+".pdf");
        }
        OutputStream out = response.getOutputStream();
        while ((len = br.read(buf)) > 0)
        out.write(buf, 0, len);
        br.close();
        out.close();
    }
}
