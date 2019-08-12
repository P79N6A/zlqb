package com.zhiwang.zfm.config.shiro;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.servlet.AdviceFilter;

import com.alibaba.fastjson.JSONObject;
import com.zhiwang.zfm.common.response.CommonResponse;
import com.zhiwang.zfm.common.response.bean.sys.UserVO;

public class ShiroLoginFilter extends AdviceFilter {
	static List<String> ignoreList=null;
	static {
		ignoreList=new ArrayList<>();
		ignoreList.add("/login");
		ignoreList.add("/swagger");
        ignoreList.add("/webjars/");
        ignoreList.add("/v2/api-docs");
        ignoreList.add("/swagger-resources/configuration/ui");
        ignoreList.add("/web/");
        ignoreList.add("/js/");
        ignoreList.add("/WEB-INF/views/");
        ignoreList.add("/common/");
        ignoreList.add("/api/upload/");
        ignoreList.add("/api/app/");
        ignoreList.add("/api/sys/user/login");
        ignoreList.add("/api/asynchronous/");
	}

	/**
	 * 在访问controller前判断是否登录，返回json，不进行重定向。
	 * 
	 * @param request
	 * @param response
	 * @return true-继续往下执行，false-该filter过滤器已经处理，不继续执行其他过滤器
	 * @throws Exception
	 */
	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		UserVO userVO=(UserVO) ShiroUtils.getSessionAttribute(ShiroUtils.EMPLOYEE_SESSION);
		String url=httpServletRequest.getRequestURI();
		String projectName=httpServletRequest.getContextPath();
		url=url.replace(projectName, "");
		if (null == userVO && !checkUrl(url)) {
			if(isAjaxRequest(httpServletRequest)) {	
				httpServletResponse.setStatus(401);
				httpServletResponse.setCharacterEncoding("UTF-8");
				httpServletResponse.setContentType("application/json");
				httpServletResponse.getWriter().write(JSONObject.toJSONString(CommonResponse.error("会话已失效,请重新登录", "401")));
				return false;
			} else {// 不是ajax进行重定向处理
				httpServletResponse.sendRedirect(projectName+"/login");
				return false;
			}
		}
		return true;
	}
	
	public static boolean checkUrl(String url) {
        for (String ignoreStr : ignoreList) {
        	if(url.startsWith(ignoreStr)) {
        		return true;
        	}
		}
        return false;
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
