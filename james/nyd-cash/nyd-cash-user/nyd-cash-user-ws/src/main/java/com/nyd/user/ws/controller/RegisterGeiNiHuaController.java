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
import com.nyd.user.service.RegisterGeiNiHuaService;

@RestController
@RequestMapping("/user")
public class RegisterGeiNiHuaController {
	
	private Logger logger=LoggerFactory.getLogger(RegisterGeiNiHuaController.class);
	
	@Autowired
	private RegisterGeiNiHuaService registerGeiNiHuaController;
	
	
	/**
	 * 
	* @author chenjqt
	* @Description: 给你花撞库接口
	* @param @return
	* @return GeinihuaResponseData
	* @throws
	 */
	@RequestMapping(value = "/geinihua/check", method = RequestMethod.POST, produces = "application/json", consumes = "application/x-www-form-urlencoded")
	public GeinihuaResponseData checkPhoneToGeiNiHua(HttpServletRequest request) {
		GeiNiHuaDTO geiNiHuaDTO = new GeiNiHuaDTO();
		geiNiHuaDTO.setAppId(request.getParameter("appId"));
		geiNiHuaDTO.setMobile(request.getParameter("mobile"));
		geiNiHuaDTO.setMobileMd5(request.getParameter("mobileMd5"));
		geiNiHuaDTO.setChannelId(request.getParameter("channelId"));
		geiNiHuaDTO.setTimestamp(request.getParameter("timestamp"));
		geiNiHuaDTO.setSign(request.getParameter("sign"));
		logger.info("FlowContrioller geinihua checkXiaoheiyu start,param is：" + JSON.toJSONString(geiNiHuaDTO));
		return registerGeiNiHuaController.checkGNHAccountPhone(geiNiHuaDTO);
	}

	/**
	 * 
	* @author chenjqt
	* @Description: 给你花注册接口
	* @param @param geiNiHuaDTO
	* @param @return
	* @return GeinihuaResponseData
	* @throws
	 */
	@RequestMapping(value = "/geinihua/register", method = RequestMethod.POST, produces = "application/json", consumes = "application/x-www-form-urlencoded")
	public GeinihuaResponseData regUrl(HttpServletRequest request) {
		GeiNiHuaDTO geiNiHuaDTO = new GeiNiHuaDTO();
		geiNiHuaDTO.setAppId(request.getParameter("appId"));
		geiNiHuaDTO.setMobile(request.getParameter("mobile"));
		geiNiHuaDTO.setChannelId(request.getParameter("channelId"));
		geiNiHuaDTO.setTimestamp(request.getParameter("timestamp"));
		geiNiHuaDTO.setSign(request.getParameter("sign"));
		logger.info("FlowContrioller geinihua registgerXiaoheiyu start,param is：" + JSON.toJSONString(geiNiHuaDTO));
		return registerGeiNiHuaController.registerGeiNiHua(geiNiHuaDTO);
	}
	
}
