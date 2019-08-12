package com.nyd.user.ws.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.nyd.user.model.dto.NydUniteRegisterDto;
import com.nyd.user.model.dto.UniteRegisterDto;
import com.nyd.user.model.enums.UniteRegisterRespCode;
import com.nyd.user.model.uniteregister.UniteRegisterResponseData;
import com.nyd.user.service.FmhLomoRegisterService;
import com.nyd.user.service.UniteRegisterService;
import com.nyd.user.service.WwjLomoRegisterService;
/**
 * 
 * @author chengjf
 *
 */
@RestController
@RequestMapping("/user")
public class UniteRegisterController {
	
	private static Logger logger=LoggerFactory.getLogger(UniteRegisterController.class);

	
	@Autowired 
	private UniteRegisterService uniteRegisterService;
	
	
	@Autowired
	private WwjLomoRegisterService wwjLomoRegisterService;
	
	@Autowired
	private FmhLomoRegisterService fmhLomoRegisterService;

	/**
	 * 乐摩API对接
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/flownotice",method=RequestMethod.POST,produces = "application/json", consumes = "application/x-www-form-urlencoded")
	public @ResponseBody UniteRegisterResponseData  uniteRegisterNotice(HttpServletRequest request){
		UniteRegisterDto  uniteRegisterDto = this.getUniteRegisterDto(request);	
		return uniteRegisterService.dealUniteRegisterReq(uniteRegisterDto);
	}
	
	/***
	 * 万万借乐摩API对接
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/wwj/registgerflow",method=RequestMethod.POST,produces = "application/json", consumes = "application/x-www-form-urlencoded")
	public  @ResponseBody UniteRegisterResponseData  lomoWwjRegister(HttpServletRequest request) {
		UniteRegisterDto  dto = this.getUniteRegisterDto(request);
		UniteRegisterResponseData  responseData = wwjLomoRegisterService.lomoWwjRegisterReq(dto);
		logger.info("wwj lomo register"+JSON.toJSONString(responseData));
		return responseData;
	}
	
	/***
	 * 分秒花乐摩API对接
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/fmh/registgerflow",method=RequestMethod.POST,produces = "application/json",consumes = "application/x-www-form-urlencoded")
	public @ResponseBody UniteRegisterResponseData lomoFmhRegister(HttpServletRequest request) {
		UniteRegisterDto  dto = this.getUniteRegisterDto(request);
		logger.info("fmh lomo register{}",JSON.toJSON(dto));
		UniteRegisterResponseData responseData  =fmhLomoRegisterService.fmhLomoRegisterReq(dto);
		logger.info("fmh lomo register result{}",JSON.toJSON(responseData));
		return  responseData;
	}
	
	
	/**
	 * 引流联合注册对接API
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/registgerflow",method=RequestMethod.POST,produces = "application/json", consumes = "application/x-www-form-urlencoded")
	public @ResponseBody UniteRegisterResponseData	bJUniteRegister(HttpServletRequest request) {
		NydUniteRegisterDto  nydUniteRegisterDto = this.getParameter(request);
		String channelId =nydUniteRegisterDto.getChannelId();
		logger.info("channelId:"+channelId);
		UniteRegisterResponseData data = new UniteRegisterResponseData();
		if(channelId == null) {
			data = UniteRegisterResponseData.error(UniteRegisterRespCode.REGISTER_FAIL,"channelId为空");
			logger.info("侬要贷引流联合注册返回结果"+JSON.toJSONString(data));
			return data;
		}	
		data = uniteRegisterService.dealNydUniteRegisterReq(nydUniteRegisterDto);		
		logger.info("侬要贷引流联合注册返回结果"+JSON.toJSONString(data));
		return data;
	}
	
	
	private NydUniteRegisterDto  getParameter(HttpServletRequest request) {
		NydUniteRegisterDto  dto = new NydUniteRegisterDto();
		dto.setAppId(request.getParameter("appId"));
		dto.setAppName(request.getParameter("appName"));
		dto.setMobile(request.getParameter("mobile"));
		dto.setChannelId(request.getParameter("channelId"));
		dto.setTimestamp(request.getParameter("timestamp"));
		dto.setSign(request.getParameter("sign"));
		return dto;
	}
	
	
	private   UniteRegisterDto  getUniteRegisterDto(HttpServletRequest request) {
		String mobile = request.getParameter("mobile");
		String channelId = request.getParameter("channelId");
		String appName=request.getParameter("appName");
		UniteRegisterDto dto = new UniteRegisterDto();
		dto.setMobile(mobile);
		dto.setChannelId(channelId);
		dto.setAppName(appName);
		return dto;
	}


}
