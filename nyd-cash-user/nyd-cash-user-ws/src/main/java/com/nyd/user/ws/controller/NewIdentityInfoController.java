package com.nyd.user.ws.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.nyd.user.model.BaseInfo;
import com.nyd.user.model.IdentityInfo;
import com.nyd.user.model.PersonInfo;
import com.nyd.user.model.StepInfo;
import com.nyd.user.service.NewIdentityInfoService;
import com.nyd.user.service.consts.UserConsts;
import com.tasfe.framework.support.model.ResponseData;

@RestController
@RequestMapping("/user")
public class NewIdentityInfoController {
	
	private static Logger logger = LoggerFactory.getLogger(JobInfoController.class);
	
	@Autowired
	private NewIdentityInfoService newIdentityInfoService;
	
	
	/**
	 * 面部认证
	 * @param identityInfo
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping(value = "/identity/face/auth", method = RequestMethod.POST, produces = "application/json")
	public ResponseData seveFaceIdentity(@RequestBody IdentityInfo identityInfo) throws Throwable{		
		logger.info("刷脸认证前端传入参数:{},{},{}",identityInfo.getAccountNumber(),identityInfo.getFileJson(),identityInfo.getDeviceId());
		ResponseData responseData = null;
		try{
			responseData =newIdentityInfoService.saveFaceIdentity(identityInfo);
		}catch(Exception e) {
			responseData =ResponseData.error(UserConsts.DB_ERROR_MSG);
		}
		return responseData;
	}

	@RequestMapping(value = "/face/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseData seveFaceIdentityNew(@RequestBody IdentityInfo identityInfo) throws Throwable{
		logger.info("刷脸认证前端传入参数:{},{},{}",identityInfo.getAccountNumber(),identityInfo.getFileJson(),identityInfo.getDeviceId());
		ResponseData responseData = null;
		try{
			responseData =newIdentityInfoService.saveFaceIdentity(identityInfo);
		}catch(Exception e) {
			responseData =ResponseData.error(UserConsts.DB_ERROR_MSG);
		}
		return responseData;
	}
	
	/**
	 * 身份证信息
	 * @param identityInfo
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping(value = "/save/identity/auth",method = RequestMethod.POST, produces = "application/json")
	public ResponseData saveIdentityinfo(@RequestBody IdentityInfo identityInfo) throws Throwable{		
		logger.info("身份证信息认证前端传入参数"+JSON.toJSONString(identityInfo));
		ResponseData responseData = null;
		try{
			responseData =newIdentityInfoService.saveIdentityInfo(identityInfo);
		}catch(Exception e) {
			responseData =ResponseData.error(UserConsts.DB_ERROR_MSG);
		}
		return responseData;
	}

	@RequestMapping(value = "/idCard/save",method = RequestMethod.POST, produces = "application/json")
	public ResponseData saveIdentityinfoNew(@RequestBody IdentityInfo identityInfo) throws Throwable{
		logger.info("身份证信息认证前端传入参数"+JSON.toJSONString(identityInfo));
		ResponseData responseData = null;
		try{
			responseData =newIdentityInfoService.saveIdentityInfo(identityInfo);
		}catch(Exception e) {
			responseData =ResponseData.error(UserConsts.DB_ERROR_MSG);
		}
		return responseData;
	}
	
	
	/**
	 * 运营商认证
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/save/operator/auth",method = RequestMethod.POST, produces = "application/json")
	public ResponseData saveMobileFlag(@RequestBody StepInfo stepInfo) {
		logger.info("运营商认证前端传入参数"+JSON.toJSONString(stepInfo));
		return newIdentityInfoService.saveMobileFlag(stepInfo);
	}

	@RequestMapping(value = "/operator/save",method = RequestMethod.POST, produces = "application/json")
	public ResponseData saveMobileFlagNew(@RequestBody StepInfo stepInfo) {
		logger.info("运营商认证前端传入参数"+JSON.toJSONString(stepInfo));
		return newIdentityInfoService.saveMobileFlag(stepInfo);
	}
	
	
	/**
	 * 个人信息认证
	 * @param personInfo
	 * @return
	 */
	@RequestMapping(value = "/save/person/auth",method = RequestMethod.POST, produces = "application/json")
	public ResponseData savePersonInfo(@RequestBody PersonInfo personInfo) throws Throwable{
		logger.info("个人信息认证前端传入参数"+JSON.toJSONString(personInfo));
		ResponseData responseData = null;
		try {
			responseData =newIdentityInfoService.savePersonInfo(personInfo);
		}catch(Exception e){
			responseData =ResponseData.error(UserConsts.DB_ERROR_MSG);
		}
		return responseData;
	}

    @RequestMapping(value = "/person/save",method = RequestMethod.POST, produces = "application/json")
    public ResponseData savePersonInfoNew(@RequestBody PersonInfo personInfo) throws Throwable{
        logger.info("个人信息认证前端传入参数"+JSON.toJSONString(personInfo));
        ResponseData responseData = null;
        try {
            responseData =newIdentityInfoService.savePersonInfo(personInfo);
        }catch(Exception e){
            responseData =ResponseData.error(UserConsts.DB_ERROR_MSG);
        }
        return responseData;
    }
			
	
	/**
	 * 获取资料是否完整得到step信息
	 * @param baseInfo
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping(value = "/new/score/fetch/auth", method = RequestMethod.POST, produces = "application/json")
    public ResponseData fetchScore(@RequestBody BaseInfo baseInfo) throws Throwable{
		logger.info("获取资料是否完整得到step信息前端传入参数"+JSON.toJSONString(baseInfo));
		ResponseData responseData =newIdentityInfoService.getStepInfo(baseInfo.getAccountNumber(),baseInfo.getAppName());
        return responseData;
    }

    @RequestMapping(value = "/new/score/query", method = RequestMethod.POST, produces = "application/json")
    public ResponseData fetchScoreNew(@RequestBody BaseInfo baseInfo) throws Throwable{
        logger.info("获取资料是否完整得到step信息前端传入参数"+JSON.toJSONString(baseInfo));
        ResponseData responseData =newIdentityInfoService.getStepInfo(baseInfo.getAccountNumber(),baseInfo.getAppName());
        return responseData;
    }
	
	/**
	 * 获取现金红包
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/find/activity/fee", method = RequestMethod.POST, produces = "application/json")
	public ResponseData getAtivityFee(@RequestBody BaseInfo baseInfo) {
		ResponseData responseData = ResponseData.success();	
		logger.info("获取获取现金红包记录传入参数"+baseInfo);
		return  newIdentityInfoService.getCashList(baseInfo);		
	}
	

}
