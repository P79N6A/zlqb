package com.nyd.user.ws.controller;

import com.alibaba.fastjson.JSON;
import com.nyd.user.api.zzl.UserForSLHServise;
import com.nyd.user.model.*;
import com.nyd.user.service.UserLoginService;
import com.nyd.user.ws.util.ControllerUntil;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dengw on 2017/11/2.
 */
@RestController
@RequestMapping("/user")
public class UserLoginController {
	private static Logger LOGGER = LoggerFactory.getLogger(UserLoginController.class);

	@Autowired
	private UserLoginService userLoginService;
	@Autowired
	private UserForSLHServise userForSLHServise;
	@RequestMapping(value = "/smscode", method = RequestMethod.POST, produces = "application/json")
	public ResponseData sendMsgCode(@RequestBody SmsInfo smsRequest) {
		ResponseData responseData = userLoginService.sendMsgCode(smsRequest);
		return responseData;
	}

    @RequestMapping(value = "/sendSms", method = RequestMethod.POST, produces = "application/json")
    public ResponseData sendMsgCodeNew(@RequestBody SmsInfo smsRequest) {
        ResponseData responseData = userLoginService.sendMsgCode(smsRequest);
        return responseData;
    }

	@RequestMapping(value = "/logout", method = RequestMethod.POST, produces = "application/json")
	public ResponseData<BaseInfo> logout(@RequestBody AccountInfo accountInfo) {
		ResponseData responseData = userLoginService.logout(accountInfo.getAccountNumber());
		return responseData;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
	public ResponseData<BaseInfo> login(@RequestBody AccountInfo accountInfo, HttpServletRequest request) {
		String ip = ControllerUntil.getIPAddr(request);
		ResponseData responseData = userLoginService.login(accountInfo, ip);
		return responseData;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
	public ResponseData register(@RequestBody AccountInfo accountInfo) {
		ResponseData responseData = userLoginService.register(accountInfo);
		return responseData;
	}

	@RequestMapping(value = "/query", method = RequestMethod.POST, produces = "application/json")
	public ResponseData query(@RequestBody AccountInfo accountInfo) {
		ResponseData responseData = userLoginService.query(accountInfo);
		return responseData;
	}


	/**
	 * 
	* @author chenjqt
	* @Description: 免密登陆
	* @param @param accountInfo
	* @param @param request
	* @param @return
	* @return ResponseData<BaseInfo>
	* @throws
	 */
	@RequestMapping(value = "/messageRegisterOrLogin", method = RequestMethod.POST, produces = "application/json")
	public ResponseData<BaseInfo> messageRegisterOrLogin(@RequestBody AccountInfo accountInfo,HttpServletRequest request) {
		LOGGER.info("geeTest request params : " + JSON.toJSONString(accountInfo));
		String ip = ControllerUntil.getIPAddr(request);
		ResponseData responseData = userLoginService.messageRegisterOrLogin(accountInfo, ip);
		return responseData;
	}

	@RequestMapping(value = "/smsRegisterOrLogin", method = RequestMethod.POST, produces = "application/json")
	public ResponseData<BaseInfo> messageRegisterOrLoginNew(@RequestBody AccountInfo accountInfo,HttpServletRequest request) {
		LOGGER.info("geeTest request params : " + JSON.toJSONString(accountInfo));
		String ip = ControllerUntil.getIPAddr(request);
		ResponseData responseData = userLoginService.messageRegisterOrLogin(accountInfo, ip);
		return responseData;
	}

	@RequestMapping(value = "/geeTest/prepare", method = RequestMethod.POST, produces = "application/json")
	public ResponseData geeTestPrepare(@RequestBody GeeTestDto dto, HttpServletRequest request) {
		LOGGER.info("geeTest request params : " + JSON.toJSONString(dto));
		if (StringUtils.isBlank(dto.getIfNative()) || StringUtils.isBlank(dto.getAppName())){
			return ResponseData.error("参数错误");
		}
		if (StringUtils.isBlank(dto.getDeviceId())){
			return ResponseData.error("参数错误");
		}
		String ip = ControllerUntil.getIPAddr(request);
		String[] split = ip.split(",");
		dto.setIp(split[0]);
		ResponseData responseData = userLoginService.geeTestPrepare(dto);
		return responseData;
	}

	@RequestMapping(value = "/geeTest/second", method = RequestMethod.POST, produces = "application/json")
	public ResponseData geeTestSecond(@RequestBody GeeTestDto dto, HttpServletRequest request) {
		if (StringUtils.isBlank(dto.getIfNative()) || StringUtils.isBlank(dto.getAppName()) || StringUtils.isBlank(dto.getChallenge()) || StringUtils.isBlank(dto.getSecCode()) || StringUtils.isBlank(dto.getValidate())){
			return ResponseData.error("参数错误");
		}
		if (StringUtils.isBlank(dto.getDeviceId())){
			return ResponseData.error("参数错误");
		}
		String ip = ControllerUntil.getIPAddr(request);
		String[] split = ip.split(",");
		dto.setIp(split[0]);
		ResponseData responseData = userLoginService.geeTestSecond(dto);
		return responseData;
	}

	@RequestMapping(value = "/gateway/pass", method = RequestMethod.POST, produces = "application/json")
	public ResponseData geeTestOnePass(@RequestBody GeeTestDto dto) {
		if (StringUtils.isBlank(dto.getAccesscode()) || StringUtils.isBlank(dto.getProcess_id())) {
			return ResponseData.error("参数错误");
		}
		ResponseData responseData = userLoginService.geeTestOnePass(dto);
		return responseData;
	}

	/**
	 * 
	* @author chenjqt
	* @Description: 免密登陆设置密码
	* @param @param accountInfo
	* @param @return
	* @param @throws Throwable
	* @return ResponseData
	* @throws
	 */
	@RequestMapping(value = "/passwordSet", method = RequestMethod.POST, produces = "application/json")
	public ResponseData passwordSet(@RequestBody AccountInfo accountInfo, HttpServletRequest request) {
		String ip = ControllerUntil.getIPAddr(request);
		ResponseData responseData = userLoginService.passwordSet(accountInfo, ip);
		return responseData;
	}

	@RequestMapping(value = "/pwd", method = RequestMethod.POST, produces = "application/json")
	public ResponseData passwordSetNew(@RequestBody AccountInfo accountInfo, HttpServletRequest request) {
		String ip = ControllerUntil.getIPAddr(request);
		ResponseData responseData = userLoginService.passwordSet(accountInfo, ip);
		return responseData;
	}


	@RequestMapping(value = "/modify/password/auth", method = RequestMethod.POST, produces = "application/json")
	public ResponseData modifyPassword(@RequestBody AccountInfo accountInfo) throws Throwable {
		ResponseData responseData = userLoginService.modifyPassword(accountInfo);
		return responseData;
	}

    @RequestMapping(value = "/modify/pwd", method = RequestMethod.POST, produces = "application/json")
    public ResponseData modifyPasswordNew(@RequestBody AccountInfo accountInfo) throws Throwable {
        ResponseData responseData = userLoginService.modifyPassword(accountInfo);
        return responseData;
    }

	@RequestMapping(value = "/forget/password", method = RequestMethod.POST, produces = "application/json")
	public ResponseData forgetPassword(@RequestBody AccountInfo accountInfo) {
		ResponseData responseData = userLoginService.forgetPassword(accountInfo);
		return responseData;
	}

    @RequestMapping(value = "/forget/pwd", method = RequestMethod.POST, produces = "application/json")
    public ResponseData forgetPasswordNew(@RequestBody AccountInfo accountInfo) {
        ResponseData responseData = userLoginService.forgetPassword(accountInfo);
        return responseData;
    }
    @RequestMapping(value = "/getUserIdByPhone", method = RequestMethod.POST, produces = "application/json")
    public ResponseData getUserIdByPhone(@RequestBody Phone phone) {
    	ResponseData data =new ResponseData<>();
    	String userId = userForSLHServise.getUserIdByPhone(phone.getPhone());
    	if(StringUtils.isBlank(userId)) {
    		data.setData(null);
        	data.setStatus("1");
        	data.setCode("400");
    	}else {
    		data.setData(userId);
        	data.setStatus("0");
        	data.setCode("200");
    	}
    	
        return data;
    }
}
