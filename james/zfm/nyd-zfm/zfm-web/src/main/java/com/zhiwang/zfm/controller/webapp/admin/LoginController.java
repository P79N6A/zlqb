package com.zhiwang.zfm.controller.webapp.admin;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.zhiwang.zfm.common.response.CommonResponse;
import com.zhiwang.zfm.config.shiro.ShiroUtils;

import io.swagger.annotations.ApiOperation;

@Controller
public class LoginController {

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String toLogin(HttpServletRequest request,HttpServletResponse response){

		/*response.setStatus(401);
		if(isAjaxRequest(request)) {
			try {
				response.setContentType("text/html; charset=utf-8");
				PrintWriter out= response.getWriter();
				out.print(JSONObject.toJSONString(CommonResponse.error("会话过期,请重新登录","401")));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}*/
		return "login";
	}
	
	
	@RequestMapping(value = "/sys/logout", method = RequestMethod.GET)
	@ApiOperation(value = "登出")
	public String logout(HttpServletResponse response) throws IOException {
		ShiroUtils.logout();
		ShiroUtils.removeSession(ShiroUtils.EMPLOYEE_SESSION);
		return "redirect:/login"; // 跳转SSO loginOut
	} 

	@RequiresPermissions("/loginTest")
	@RequestMapping(value = "/loginTest", method = {RequestMethod.GET,RequestMethod.POST})
	@ApiOperation(value = "测试方法")
	public CommonResponse<?> loginTest(HttpServletResponse response) throws IOException {
		/*ShiroUtils.logout();
		ShiroUtils.removeSession(ShiroUtils.EMPLOYEE_SESSION);*/
		System.out.println("我是真的不懂了");
		return CommonResponse.success("666");
	} 
	
	/**
	 * 是否是Ajax异步请求
     */
    public static boolean isAjaxRequest(HttpServletRequest request)
    {

        String accept = request.getHeader("accept");
        if (accept != null && accept.indexOf("application/json") != -1)
        {
            return true;
        }

        String xRequestedWith = request.getHeader("X-Requested-With");
        if (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1)
        {
            return true;
        }

        /*String uri = request.getRequestURI();
        if (StringUtils.inStringIgnoreCase(uri, ".json", ".xml"))
        {
            return true;
        }

        String ajax = request.getParameter("__ajax");
        if (StringUtils.inStringIgnoreCase(ajax, "json", "xml"))
        {
            return true;
        }*/

        return false;
    }
	
}
