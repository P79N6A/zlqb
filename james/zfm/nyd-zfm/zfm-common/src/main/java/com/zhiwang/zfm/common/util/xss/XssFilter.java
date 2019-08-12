package com.zhiwang.zfm.common.util.xss;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * XSS过滤
 * @author chenbo
 * @email 381756915@qq.com
 * @date 2018-01-5 19:22
 */
public class XssFilter implements Filter {

	@Override
	public void init(FilterConfig config) throws ServletException {
		System.out.println("进入xxsfilter-init："+config.getFilterName());
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
		XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(
				(HttpServletRequest) request);
		chain.doFilter(xssRequest, response);
		System.out.println("进入xxsfilter-do");
	}

	@Override
	public void destroy() {
		System.out.println("进入xxsfilter-detroy");
	}

}