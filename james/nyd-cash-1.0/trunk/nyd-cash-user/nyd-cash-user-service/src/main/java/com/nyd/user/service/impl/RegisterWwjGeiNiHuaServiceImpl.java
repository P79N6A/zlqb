package com.nyd.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nyd.user.dao.AccountDao;
import com.nyd.user.dao.PasswordDao;
import com.nyd.user.dao.mapper.HitLogMapper;
import com.nyd.user.entity.Account;
import com.nyd.user.entity.HitLog;
import com.nyd.user.entity.UserTarget;
import com.nyd.user.model.AccountInfo;
import com.nyd.user.model.GeinihuaResponseData;
import com.nyd.user.model.dto.AccountDto;
import com.nyd.user.model.dto.GeiNiHuaDTO;
import com.nyd.user.model.enums.RegisterGeiNiHuaEnums;
import com.nyd.user.model.mq.RegisterToHitAccountMessage;
import com.nyd.user.model.mq.UserSourceLogMessage;
import com.nyd.user.model.request.HitRequest;
import com.nyd.user.model.response.HitResponse;
import com.nyd.user.service.AccountInfoService;
import com.nyd.user.service.NydHitLogicService;
import com.nyd.user.service.RegisterWwjGeiNiHuaService;
import com.nyd.user.service.UserTargetService;
import com.nyd.user.service.mq.HitAccountProducer;
import com.nyd.user.service.mq.UserToUserSourceLogProducer;
import com.nyd.user.service.util.Md5Util;
import com.nyd.user.service.util.RSAUtil;
import com.nyd.user.service.util.RandomUtil;
import com.nyd.user.service.util.UserProperties;
import com.tasfe.framework.support.model.ResponseData;

@Service(value ="registerWwjGeiNiHuaService")
public class RegisterWwjGeiNiHuaServiceImpl implements RegisterWwjGeiNiHuaService{
	
	private Logger logger=LoggerFactory.getLogger(RegisterWwjGeiNiHuaServiceImpl.class);
		
	@Autowired
	private UserProperties userProperties;
	
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
	private HitLogMapper hitLogMapper;
	
	@Autowired
	private UserToUserSourceLogProducer userToUserSourceLogProducer;
	
	@Value("${geinihua.wwj.luodi.url}")	
	private String WWJ_51GNH_URL;
	
	private static final String geinihuaChannel_wwj = "51gnh_wwj";
	
	private static final String GNH="51gnh";
	        
    private static final String APPID_WWJ="UN1812170001";   
    //手机正则验证
    private static final String PHONE_REGEX = "^1\\d{10}$";    
	// 商户唯一标示
	private static final String APP_ID = "appId";
	// 引流商户号
	private static final String CHANNEL_ID = "channelId";
	// 手机号
	private static final String MOBILE = "mobile";
	// md5加密 32位小写
	private static final String MOBILE_MD5 = "mobileMd5";
	// 时间戳
	private static final String TIME_STAMP = "timestamp";
	
	@Autowired
	private NydHitLogicService nydHitLogicService;

	@Override
	public GeinihuaResponseData checkWwjGNHAccountPhone(GeiNiHuaDTO geiNiHuaDTO) {		
		GeinihuaResponseData geinihuaResponseData = new GeinihuaResponseData();			
		if (null == geiNiHuaDTO || StringUtils.isBlank(geiNiHuaDTO.getAppId())
				|| StringUtils.isBlank(geiNiHuaDTO.getMobile()) || StringUtils.isBlank(geiNiHuaDTO.getMobileMd5())
				|| StringUtils.isBlank(geiNiHuaDTO.getChannelId()) || StringUtils.isBlank(geiNiHuaDTO.getTimestamp())
				|| StringUtils.isBlank(geiNiHuaDTO.getSign())) {
			geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.PARAMETER_ERROR.getCode());
			geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.PARAMETER_ERROR.getMsg());
			return geinihuaResponseData;
		}
		String param = APP_ID + "=" + geiNiHuaDTO.getAppId() + "&" + CHANNEL_ID + "=" + geiNiHuaDTO.getChannelId() + "&"
				+ MOBILE + "=" + geiNiHuaDTO.getMobile() + "&" + MOBILE_MD5 + "=" + geiNiHuaDTO.getMobileMd5() + "&"
				+ TIME_STAMP + "=" + geiNiHuaDTO.getTimestamp();
		logger.info("拼装param参数：{}", param);
		//获取公钥
		if (StringUtils.isBlank(userProperties.getPubkeyGnh())) {		
		    logger.error("pubKey公钥为空!");			
			geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.SIGN_ERROR.getCode());
			geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.SIGN_ERROR.getMsg());
			return geinihuaResponseData;
		}			
		try {
			if (!RSAUtil.verify(param.getBytes(), userProperties.getPubkeyGnh(), geiNiHuaDTO.getSign())) {
				logger.error("sign验签失败");
				geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.SIGN_ERROR.getCode());
				geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.SIGN_ERROR.getMsg());
				return geinihuaResponseData;
			}
		} catch (Exception e) {
			logger.error("sign验签error",e);
			geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.SERVER_ERROR.getCode());
			geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.SERVER_ERROR.getMsg());
			return geinihuaResponseData;
		}
		//wwj51gnh渠道号source为51gnh_wwj
		if(APPID_WWJ.equals(geiNiHuaDTO.getAppId())){
			geiNiHuaDTO.setChannelId(geinihuaChannel_wwj);
		}
		HitRequest request = new HitRequest();
		 if(geinihuaChannel_wwj.equals(geiNiHuaDTO.getChannelId())) {
			request.setAppName("wwj");
		}else {			
			request.setAppName("nyd");
		}
		request.setMobile(geiNiHuaDTO.getMobileMd5());
		request.setMobileType("2");
		request.setSource(geiNiHuaDTO.getChannelId());
		//万万借暂时调整为0撞库规则
		ResponseData data =nydHitLogicService.HitByMobileAndRule(request);			
		List<UserTarget> listUserTarget  = new ArrayList<UserTarget>();
		List<AccountDto> accountList = new ArrayList<AccountDto>();			
		if("0".equals(data.getStatus()) &&  data.getData() != null) {
			 HitResponse hitResponse = (HitResponse)data.getData();
			 if(hitResponse.getStatus()==1) {
				 Map<String,String> params = new HashMap<>();
			     params.put("mobile",geiNiHuaDTO.getMobileMd5());
			     listUserTarget =userTargetService.getUserTarget(params);
			     if(listUserTarget != null && listUserTarget.size() > 0) {				    	 
			    	 UserTarget userTarget =listUserTarget.get(0);
			    	 try {
						accountList =accountDao.findByAccountNumber(userTarget.getMobile());
					} catch (Exception e) {
						logger.error("select account error",e);
						geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.SIGN_ERROR.getCode());
						geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.SIGN_ERROR.getMsg());
						return geinihuaResponseData;
					}
			     }
			 }
		}
		if(accountList !=null && accountList.size() >0) {
			for(AccountDto  account :accountList ) {				
				if(geiNiHuaDTO.getChannelId().equals(account.getSource())) {
					geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.OLD_SUCCESS.getCode());
					geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.OLD_SUCCESS.getMsg());
					return geinihuaResponseData;
        		}else if(GNH.equals(account.getSource())) {
        			List<HitLog> hitList =hitLogMapper.hitLogByMobile(geiNiHuaDTO.getMobileMd5());
        			int count = 0;
        			for(HitLog hit :hitList) {
        				if(geinihuaChannel_wwj.equals(hit.getSource())) {
        					count = count + 1;       					
        				}
        			}
        			if(count ==1) {
        				geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.NEW_SUCCESS.getCode());
            			geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.NEW_SUCCESS.getMsg());
            			return geinihuaResponseData;
        			}
        			if(count >1) {
        				geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.OLD_SUCCESS.getCode());
            			geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.OLD_SUCCESS.getMsg());
            			return geinihuaResponseData;
        			} 
        			geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.NEW_SUCCESS.getCode());
        			geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.NEW_SUCCESS.getMsg());
        			return geinihuaResponseData;
        		}else {      		
        			geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.OTHER_CHANNEL_USER.getCode());
					geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.OTHER_CHANNEL_USER.getMsg());
					return geinihuaResponseData;
        		}
        	}			
		}			
		geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.NEW_SUCCESS.getCode());
		geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.NEW_SUCCESS.getMsg());		
		return geinihuaResponseData;
	}

	@Override
	public GeinihuaResponseData registerWwjGeiNiHua(GeiNiHuaDTO geiNiHuaDTO) {
		GeinihuaResponseData geinihuaResponseData = new GeinihuaResponseData();	
		if (null == geiNiHuaDTO || StringUtils.isBlank(geiNiHuaDTO.getAppId())
				|| StringUtils.isBlank(geiNiHuaDTO.getMobile()) || StringUtils.isBlank(geiNiHuaDTO.getChannelId())
				|| StringUtils.isBlank(geiNiHuaDTO.getTimestamp()) || StringUtils.isBlank(geiNiHuaDTO.getSign())) {
			geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.PARAMETER_ERROR.getCode());
			geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.PARAMETER_ERROR.getMsg());
			return geinihuaResponseData;
		}
		String param = APP_ID + "=" + geiNiHuaDTO.getAppId() + "&" + CHANNEL_ID + "=" + geiNiHuaDTO.getChannelId() + "&"
				+ MOBILE + "=" + geiNiHuaDTO.getMobile() + "&" + TIME_STAMP + "=" + geiNiHuaDTO.getTimestamp();		
		// 验证签名		
		try {
			if (!RSAUtil.verify(param.getBytes(), userProperties.getPubkeyGnh(), geiNiHuaDTO.getSign())) {
				geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.SIGN_ERROR.getCode());
				geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.SIGN_ERROR.getMsg());
				return geinihuaResponseData;
			}
		} catch (Exception e) {
			logger.error("签名验证失败",e);
			geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.SERVER_ERROR.getCode());
			geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.SERVER_ERROR.getMsg());
			return geinihuaResponseData;
		}
		//wwj51gnh渠道号source为51gnh_wwj
		if(APPID_WWJ.equals(geiNiHuaDTO.getAppId())){
			geiNiHuaDTO.setChannelId(geinihuaChannel_wwj);
		}		
		List<Account> accountList = null;
		try {
			accountList = accountDao.getAccountsByAccountNumber(geiNiHuaDTO.getMobile());
		} catch (Exception e) {
			logger.error("select account error",e);
			geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.SERVER_ERROR.getCode());
			geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.SERVER_ERROR.getMsg());
			return geinihuaResponseData;
		}
		if (null != accountList && accountList.size() > 0) {
			logger.warn("给你花引流 该用户已被注册 phone：" + geiNiHuaDTO.getMobile());
			if(geiNiHuaDTO.getChannelId().equals(accountList.get(0).getSource())) {
				geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.OLD_SUCCESS.getCode());
				geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.OLD_SUCCESS.getMsg());
				geinihuaResponseData.setUrl(WWJ_51GNH_URL);
				return geinihuaResponseData;								
			}else if(GNH.equals(accountList.get(0).getSource())) {
				String mobileMd5 = null;
				try {
					mobileMd5 =Md5Util.getMD5(geiNiHuaDTO.getMobile());
				} catch (Exception e) {
					logger.error("md5 加密 error",e);
					geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.SERVER_ERROR.getCode());
					geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.SERVER_ERROR.getMsg());
					return geinihuaResponseData;
				}			
				List<HitLog> hitList =hitLogMapper.hitLogByMobile(mobileMd5);
    			int count = 0;
				for(HitLog hit :hitList) {
    				if(geinihuaChannel_wwj.equals(hit.getSource())) {
    					count = count + 1;       					
    				} 				
    			}
				if(count ==1) {
    				geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.SUCCESS_CODE.getCode());
        			geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.SUCCESS_CODE.getMsg());
        			geinihuaResponseData.setUrl(WWJ_51GNH_URL);
        			return geinihuaResponseData;
    			}
    			if(count >1) {
    				geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.OLD_SUCCESS.getCode());
        			geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.OLD_SUCCESS.getMsg());
        			geinihuaResponseData.setUrl(WWJ_51GNH_URL);
        			return geinihuaResponseData;
    			} 
				geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.SUCCESS_CODE.getCode());
				geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.SUCCESS_CODE.getMsg());
				geinihuaResponseData.setUrl(WWJ_51GNH_URL);
				return geinihuaResponseData;	
    		}else {      		
    			geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.OTHER_CHANNEL_USER.getCode());
				geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.OTHER_CHANNEL_USER.getMsg());
				geinihuaResponseData.setUrl(WWJ_51GNH_URL);
				return geinihuaResponseData;
    		}					
	  }
		if (!registerUser(geiNiHuaDTO.getMobile(),geiNiHuaDTO, null)) {
			geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.SERVER_ERROR.getCode());
			geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.SERVER_ERROR.getMsg());
			return geinihuaResponseData;
		}
		geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.SUCCESS_CODE.getCode());
		geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.SUCCESS_CODE.getMsg());				
	    geinihuaResponseData.setUrl(WWJ_51GNH_URL);		
		return geinihuaResponseData;
	
	}	
	    // 注册登录用户
		private boolean registerUser(String mobile, GeiNiHuaDTO geiNiHuaDTO, String token) {
			boolean isSuccess;
			try {
				if (!mobile.matches(PHONE_REGEX)) {
					logger.error("registerUser 手机号不符合要求，手机号：" + mobile);
					return false;
				}
				Account account = new Account();
				account.setAccountNumber(mobile);
				account.setSource(geiNiHuaDTO.getChannelId());				
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
				message.setToken(token);
				hitAccountProducer.sendAddHitAccountMsg(message);
				logger.info("flowService 保存passwordDao成功 mobile:" + mobile);				
				sendMsgToUserSourceLog(mobile, geiNiHuaDTO.getChannelId());
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
