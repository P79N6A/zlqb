package com.nyd.user.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.nyd.application.api.FlowConfigContract;
import com.nyd.application.model.FlowConfigModel;
import com.nyd.user.dao.AccountDao;
import com.nyd.user.dao.PasswordDao;
import com.nyd.user.dao.mapper.HitLogMapper;
import com.nyd.user.entity.Account;
import com.nyd.user.entity.HitLog;
import com.nyd.user.entity.UserTarget;
import com.nyd.user.model.AccountInfo;
import com.nyd.user.model.dto.UniteRegisterDto;
import com.nyd.user.model.enums.UniteRegisterRespCode;
import com.nyd.user.model.mq.RegisterToHitAccountMessage;
import com.nyd.user.model.mq.UserSourceLogMessage;
import com.nyd.user.model.request.HitRequest;
import com.nyd.user.model.uniteregister.UniteRegisterResponseData;
import com.nyd.user.service.AccountInfoService;
import com.nyd.user.service.NydHitLogicService;
import com.nyd.user.service.UserTargetService;
import com.nyd.user.service.WwjLomoRegisterService;
import com.nyd.user.service.mq.HitAccountProducer;
import com.nyd.user.service.mq.UserToUserSourceLogProducer;
import com.nyd.user.service.util.Md5Util;
import com.nyd.user.service.util.RandomUtil;
import com.tasfe.framework.support.model.ResponseData;

@Service(value = "wwjLomoRegisterService")
public class WwjLomoRegisterServiceImpl implements WwjLomoRegisterService{
	
	private static Logger logger = LoggerFactory.getLogger(WwjLomoRegisterServiceImpl.class);
	
	public static String APPNAME = "wwj";
	
	public static String CHANNELID = "wwj_lomo";
	
	public static String NYD_CHANNELID = "nyd_lomo";
	
	private static final String PHONE_REGEX = "^1\\d{10}$";
	
	@Autowired
	private NydHitLogicService nydHitLogicService;
	
	@Autowired
	private FlowConfigContract flowConfigContract;
	
	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	private PasswordDao passwordDao;
	
	@Autowired
	private AccountInfoService accountInfoService;
	
	@Autowired
	private HitAccountProducer hitAccountProducer;
	
	@Autowired
	private UserTargetService userTargetService;
	
	@Autowired
	private UserToUserSourceLogProducer userToUserSourceLogProducer;
	
	@Autowired
	private HitLogMapper hitLogMapper;
	
	@Override
	public UniteRegisterResponseData lomoWwjRegisterReq(UniteRegisterDto dto) {
		UniteRegisterResponseData responseData = UniteRegisterResponseData.success();
		logger.info("wwj lomo register param"+JSON.toJSONString(dto));
		if(dto ==null  || StringUtils.isBlank(dto.getMobile()) || StringUtils.isBlank(dto.getChannelId())|| StringUtils.isBlank(dto.getAppName())) {
			logger.error("参数异常");
			responseData = UniteRegisterResponseData.error(UniteRegisterRespCode.REGISTER_FAIL,"请求参数异常");
			return responseData;
		}
		if(!APPNAME.equals(dto.getAppName()) || !dto.getChannelId().contains(CHANNELID)) {
			logger.error("参数异常");
			responseData = UniteRegisterResponseData.error(UniteRegisterRespCode.REGISTER_FAIL,"请求参数异常");
			return responseData;
		}
		if(!dto.getMobile().matches(PHONE_REGEX)) {
			logger.error("手机号不符合要求，手机号" + dto.getMobile());
			responseData = UniteRegisterResponseData.error(UniteRegisterRespCode.REGISTER_FAIL, "手机号不符合要求");
			return responseData;
		}
		ResponseData<FlowConfigModel> configModelResponseData = flowConfigContract.getFlowConfigByChannelId(dto.getChannelId());
		if (null == configModelResponseData || !"0".equals(configModelResponseData.getStatus())) {
			logger.error("调用flowConfigContract dubbo api异常：" + dto.getChannelId());
			responseData = UniteRegisterResponseData.error(UniteRegisterRespCode.SERVER_ERROR, "服务器内部异常");
			return responseData;
		}
		FlowConfigModel flowConfigModel = configModelResponseData.getData();
		if (null == flowConfigModel) {
			logger.error("联合注册请求,无引流配置");
			responseData = UniteRegisterResponseData.error(UniteRegisterRespCode.REGISTER_FAIL, "联合注册请求,无引流配置");
			return responseData;
		}
		HitRequest request = new HitRequest();
		request.setAppName(dto.getAppName());
		request.setMobile(dto.getMobile());
		request.setMobileType("1");
		request.setSource(dto.getChannelId());
		nydHitLogicService.HitByMobileAndRule(request);	
		List<Account> accountList = null;
		try {
			accountList = accountDao.getAccountsByAccountNumber(dto.getMobile());
		} catch (Exception e) {
			logger.info("selct accout error",e);
			responseData = UniteRegisterResponseData.error(UniteRegisterRespCode.SERVER_ERROR);
			return responseData;
		} 
		if (null != accountList && accountList.size() > 0) {
             logger.info("lomo引流 该用户已被注册 phone:" + dto.getMobile());
             Account account = accountList.get(0);
             if (StringUtils.isNotBlank(account.getSource()) && account.getSource().contains(CHANNELID)) {
            	responseData = UniteRegisterResponseData.error(UniteRegisterRespCode.REGISTER_OLD_SUCCESS);
 				return responseData;
             }
             if(StringUtils.isNotBlank(account.getSource()) && account.getSource().contains(NYD_CHANNELID)) {
            	List<HitLog> hitList =hitLogMapper.hitLogByMobile(dto.getMobile());
     			int count = 0;
     			for(HitLog hit :hitList) {
     				if(StringUtils.isNotBlank(hit.getSource()) && hit.getSource().contains(CHANNELID)) {
     					count = count + 1;       					
     				}
     			}
	 			if(count ==1) {
	 				 responseData = UniteRegisterResponseData.success();
	   				 return responseData;
				}
	 			if(count > 1) {
	 			  responseData = UniteRegisterResponseData.error(UniteRegisterRespCode.REGISTER_OLD_SUCCESS);
	 			  return responseData;
	 			}
             }            
             responseData = UniteRegisterResponseData.success();
			 return responseData;
		}	
		try {		
			if(!this.registerUser(dto.getMobile(), dto.getChannelId(),dto.getAppName())) {
				logger.error("联合注册失败 mobile:" + dto.getMobile());
				responseData = UniteRegisterResponseData.error(UniteRegisterRespCode.SERVER_ERROR);
				return responseData;
			}			
	   }catch(Exception e) {
		logger.error("联合注册异常 channelId:" + dto.getChannelId(), e);
		responseData = UniteRegisterResponseData.error(UniteRegisterRespCode.SERVER_ERROR);
	    }		
		return responseData;		
	}
	
	 // 注册登录用户
	  private boolean registerUser(String mobile, String channelId, String appName) {
		boolean isSuccess;
		try {
			if (!mobile.matches(PHONE_REGEX)) {
				logger.error("registerUser 手机号不符合要求，手机号：" + mobile);
				return false;
			}
			Account account = new Account();
			account.setAccountNumber(mobile);
			account.setSource(channelId);				
			accountDao.save(account);
			logger.info("flowService 保存account成功 mobile:" + mobile);
			AccountInfo accountInfo = new AccountInfo();
			accountInfo.setAccountNumber(mobile);
			String pwd = RandomUtil.flowPasswd(6);
			accountInfo.setPassword(Md5Util.getMD5(pwd));
			passwordDao.save(accountInfo);
			//保存账户信息至redis缓存
			accountInfoService.saveAccountInRedis(account, accountInfo.getPassword());
			// 添加至撞库信息中(注册成功发送mq 进行处理)
			RegisterToHitAccountMessage message = new RegisterToHitAccountMessage();
			message.setAccountNumber(accountInfo.getAccountNumber());
			message.setToken(null);
			hitAccountProducer.sendAddHitAccountMsg(message);
			logger.info("flowService 保存passwordDao成功 mobile:" + mobile);				
			sendMsgToUserSourceLog(mobile, channelId);
			UserTarget userTarget = new UserTarget();
			userTarget.setMobile(mobile);
			userTarget.setMd5Mobile(Md5Util.getMD5To32LowCaseSign(mobile));
			userTarget.setShaMobile(DigestUtils.sha256Hex(mobile));
			userTarget.setIfRegister(0);
	 		userTarget.setTargetOne(0);
	 		userTarget.setTargetTwo(0);
	 		userTarget.setTargetThree(0);
	 		userTarget.setTargetFour(0);
	 		userTarget.setTargetFive(0);
			userTargetService.save(userTarget);
			isSuccess = true;
		} catch (Exception e) {
			logger.error("注册用户信息异常：mobile:" + mobile, e);
			isSuccess = false;
		}
		return isSuccess;
	}
	  
	 // 记录结算费用表
		private void sendMsgToUserSourceLog(String mobile, String source) {
			try {
				UserSourceLogMessage msg = new UserSourceLogMessage();
				msg.setAccountNumber(mobile);
				msg.setSource(source);				
			    msg.setAppName("wwj");				
				msg.setCreateTime(new Date());
				userToUserSourceLogProducer.sendMsg(msg);
			} catch (Exception e) {
				logger.error("send msg to userSourceLog has exception!", e);
			}
		}

}
