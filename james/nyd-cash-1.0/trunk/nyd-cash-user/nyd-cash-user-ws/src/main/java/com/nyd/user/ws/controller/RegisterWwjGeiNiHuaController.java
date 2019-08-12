package com.nyd.user.ws.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.nyd.user.model.GeinihuaResponseData;
import com.nyd.user.model.dto.GeiNiHuaDTO;
import com.nyd.user.service.RegisterWwjGeiNiHuaService;

@RestController
@RequestMapping("/user")
public class RegisterWwjGeiNiHuaController {
	
	private Logger logger=LoggerFactory.getLogger(RegisterWwjGeiNiHuaController.class);
	
	@Autowired
	private RegisterWwjGeiNiHuaService registerWwjGeiNiHuaService;
	
	/**
	 * wwj51gnh联合登录撞库请求
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/wwj/51gnh/check", method = RequestMethod.POST, produces ="application/json",consumes = "application/x-www-form-urlencoded")
	public GeinihuaResponseData checkPhoneToGeiWwjNiHua(HttpServletRequest request) {
		GeiNiHuaDTO geiNiHuaDTO = new GeiNiHuaDTO();
		geiNiHuaDTO.setAppId(request.getParameter("appId"));
		geiNiHuaDTO.setMobile(request.getParameter("mobile"));
		geiNiHuaDTO.setMobileMd5(request.getParameter("mobileMd5"));
		geiNiHuaDTO.setChannelId(request.getParameter("channelId"));
		geiNiHuaDTO.setTimestamp(request.getParameter("timestamp"));
		geiNiHuaDTO.setSign(request.getParameter("sign"));
		logger.info("wwj 51gnh check param:"+JSON.toJSONString(geiNiHuaDTO));
		return registerWwjGeiNiHuaService.checkWwjGNHAccountPhone(geiNiHuaDTO);
	}
	
	/**
	 * wwj51gnh联合登录注册请求
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/wwj/51gnh/register", method = RequestMethod.POST, produces = "application/json",consumes = "application/x-www-form-urlencoded")
	public GeinihuaResponseData regWwjUrl(HttpServletRequest request) {
			GeiNiHuaDTO geiNiHuaDTO = new GeiNiHuaDTO();
			geiNiHuaDTO.setAppId(request.getParameter("appId"));
			geiNiHuaDTO.setMobile(request.getParameter("mobile"));
			geiNiHuaDTO.setChannelId(request.getParameter("channelId"));
			geiNiHuaDTO.setTimestamp(request.getParameter("timestamp"));
			geiNiHuaDTO.setSign(request.getParameter("sign"));
			logger.info("wwj 51gnh register param:" + JSON.toJSONString(geiNiHuaDTO));		
		return registerWwjGeiNiHuaService.registerWwjGeiNiHua(geiNiHuaDTO);
	}
	
	
}
