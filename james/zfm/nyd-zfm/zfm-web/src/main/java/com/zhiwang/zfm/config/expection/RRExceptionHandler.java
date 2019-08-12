package com.zhiwang.zfm.config.expection;

import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


import com.zhiwang.zfm.common.response.CommonResponse;

/**
 * 异常处理器
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年10月27日 下午10:16:19
 */
@ControllerAdvice
public class RRExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 自定义异常
	 */
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public CommonResponse handleRRException(RRException e){
		CommonResponse r = new CommonResponse();
		System.out.println(e.getMessage());
		r.setCode(e.getCode());
		r.setMsg(e.getMessage());
		return r;
	}

	@ResponseBody
	@ExceptionHandler(DuplicateKeyException.class)
	public CommonResponse handleDuplicateKeyException(DuplicateKeyException e){
		logger.error(e.getMessage(), e);
		CommonResponse r = new CommonResponse();
		r.setCode("500");
		r.setMsg("数据库中已存在该记录");
		return r;
	}

	@ResponseBody
	@ExceptionHandler(AuthorizationException.class)
	public CommonResponse handleAuthorizationException(AuthorizationException e){
		logger.error(e.getMessage(), e);
		CommonResponse r = new CommonResponse();
		r.setCode("500");
		r.setMsg("没有权限，请联系管理员授权");
		return r;
	}

	@ResponseBody
	@ExceptionHandler(Exception.class)
	public CommonResponse handleException(Exception e){
		logger.error(e.getMessage(), e);
		CommonResponse r = new CommonResponse();
		r.setCode("500");
		r.setMsg(e.getMessage());
		return r;
	}
}
