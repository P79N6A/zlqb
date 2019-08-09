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

import com.alibaba.fastjson.JSON;
import com.nyd.msg.service.ISendSmsService;
import com.nyd.user.dao.AccountDao;
import com.nyd.user.dao.HitAccountDao;
import com.nyd.user.dao.PasswordDao;
import com.nyd.user.dao.mapper.HitLogMapper;
import com.nyd.user.entity.Account;
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
import com.nyd.user.service.RegisterGeiNiHuaService;
import com.nyd.user.service.UserTargetService;
import com.nyd.user.service.mq.HitAccountProducer;
import com.nyd.user.service.mq.UserToUserSourceLogProducer;
import com.nyd.user.service.util.Md5Util;
import com.nyd.user.service.util.RSAUtil;
import com.nyd.user.service.util.RandomUtil;
import com.nyd.user.service.util.UserProperties;
import com.tasfe.framework.support.model.ResponseData;

@Service
public class RegisterGeiNiHuaServiceImpl implements RegisterGeiNiHuaService{

	private Logger logger=LoggerFactory.getLogger(RegisterGeiNiHuaServiceImpl.class);
	
	@Autowired
	private UserToUserSourceLogProducer userToUserSourceLogProducer;
	
	@Autowired
	private HitAccountProducer hitAccountProducer;
	
	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	private PasswordDao passwordDao;
	
	@Autowired
	private ISendSmsService sendSmsService;
	
	@Autowired
	private HitAccountDao hitAccountDao;
	
	@Autowired
	private UserProperties userProperties;
	
	@Autowired
	private NydHitLogicService nydHitLogicService;
	
	@Autowired
	private HitLogMapper hitLogMapper;
	
	@Autowired
	private UserTargetService userTargetService;
	
	@Autowired
	private AccountInfoService accountInfoService;
	
	@Value("${geinihua.xiangqianjie.luodi.url}")
    private String URL;
	
	@Value("${geinihua.wwj.luodi.url}")
	private String WWJURL;
	
    private static final String geinihuaChannel = "51gnh";
    
    private static final String geinihuaChannel_wwj = "51gnh_wwj";
    
    private static final String APPID_NYD="1809700001";
    
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
	
	@Override
	public GeinihuaResponseData checkGNHAccountPhone(GeiNiHuaDTO geiNiHuaDTO) {
		logger.info("flowService unionRegisterToGNH，start param: {}", JSON.toJSONString(geiNiHuaDTO));
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
			if (logger.isErrorEnabled()) {
				logger.error("pubKey公钥为空!");
			}
			geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.SIGN_ERROR.getCode());
			geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.SIGN_ERROR.getMsg());
			return geinihuaResponseData;
		}
		if(APPID_NYD.equals(geiNiHuaDTO.getAppId())) {
			geiNiHuaDTO.setChannelId(geinihuaChannel);
		}else if(APPID_WWJ.equals(geiNiHuaDTO.getAppId())){
			geiNiHuaDTO.setChannelId(geinihuaChannel_wwj);
		}	
		try {
			if (!RSAUtil.verify(param.getBytes(), userProperties.getPubkeyGnh(), geiNiHuaDTO.getSign())) {
				geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.SIGN_ERROR.getCode());
				geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.SIGN_ERROR.getMsg());
				return geinihuaResponseData;
			}					
			//撞库规则重新调整
			HitRequest request = new HitRequest();
			if(geinihuaChannel.equals(geiNiHuaDTO.getChannelId())) {
				request.setAppName("nyd");
			}else if(geinihuaChannel_wwj.equals(geiNiHuaDTO.getChannelId())) {
				request.setAppName("wwj");
			}else {			
				request.setAppName("nyd");
			}
			request.setMobile(geiNiHuaDTO.getMobileMd5());
			request.setMobileType("2");
			request.setSource(geiNiHuaDTO.getChannelId());
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
				    	 accountList =accountDao.findByAccountNumber(userTarget.getMobile());
				     }
				 }
			}
			if(accountList !=null && accountList.size() >0) {
				for(AccountDto  account :accountList ) {				
					if(geiNiHuaDTO.getChannelId().equals(account.getSource())) {
	        			geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.OLD_SUCCESS.getCode());
						geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.OLD_SUCCESS.getMsg());
						return geinihuaResponseData;
	        		}
	        		/*if(geinihuaChannel.equals(account.getSource())) {
	        			geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.OLD_SUCCESS.getCode());
						geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.OLD_SUCCESS.getMsg());
						return geinihuaResponseData;
	        		}*/
	        	}
				geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.OTHER_CHANNEL_USER.getCode());
				geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.OTHER_CHANNEL_USER.getMsg());
				return geinihuaResponseData;
			}			
			geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.NEW_SUCCESS.getCode());
			geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.NEW_SUCCESS.getMsg());
			return geinihuaResponseData;
		} catch (Exception e) {
			e.printStackTrace();
			if (logger.isErrorEnabled()) {
				logger.error("", e);
			}
			geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.SERVER_ERROR.getCode());
			geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.SERVER_ERROR.getMsg());
			return geinihuaResponseData;
		}
	}

	@Override
	public GeinihuaResponseData registerGeiNiHua(GeiNiHuaDTO geiNiHuaDTO) {
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
			if(APPID_NYD.equals(geiNiHuaDTO.getAppId())) {
				geiNiHuaDTO.setChannelId(geinihuaChannel);
			}else if(APPID_WWJ.equals(geiNiHuaDTO.getAppId())){
				geiNiHuaDTO.setChannelId(geinihuaChannel_wwj);
			}
			
			List<Account> accountList = accountDao.getAccountsByAccountNumber(geiNiHuaDTO.getMobile());
			if (null != accountList && accountList.size() > 0) {
				logger.warn("给你花引流 该用户已被注册 phone：" + geiNiHuaDTO.getMobile());
				if(geiNiHuaDTO.getChannelId().equals(accountList.get(0).getSource())) {
					geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.OLD_SUCCESS.getCode());
					geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.OLD_SUCCESS.getMsg());
					if(geinihuaChannel.equals(geiNiHuaDTO.getChannelId())) {			
						geinihuaResponseData.setUrl(URL);
					}else if(geinihuaChannel_wwj.equals(geiNiHuaDTO.getChannelId())) {
						geinihuaResponseData.setUrl(WWJURL);
					}
					return geinihuaResponseData;
				}					
				/*if(geinihuaChannel.equals(accountList.get(0).getSource())) {
					geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.OLD_SUCCESS.getCode());
					geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.OLD_SUCCESS.getMsg());
					geinihuaResponseData.setUrl(URL);
					return geinihuaResponseData;
				}	*/						
				geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.OTHER_CHANNEL_USER.getCode());
				geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.OTHER_CHANNEL_USER.getMsg());
				if(geinihuaChannel.equals(geiNiHuaDTO.getChannelId())) {			
					geinihuaResponseData.setUrl(URL);
				}else if(geinihuaChannel_wwj.equals(geiNiHuaDTO.getChannelId())) {
					geinihuaResponseData.setUrl(WWJURL);
				}
				return geinihuaResponseData;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			if (logger.isErrorEnabled()) {
				logger.error("", e);
			}
			geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.SERVER_ERROR.getCode());
			geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.SERVER_ERROR.getMsg());
			return geinihuaResponseData;
		}
		if (!registerUser(geiNiHuaDTO.getMobile(),geiNiHuaDTO, null)) {
			geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.SERVER_ERROR.getCode());
			geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.SERVER_ERROR.getMsg());
			return geinihuaResponseData;
		}
		geinihuaResponseData.setCode(RegisterGeiNiHuaEnums.SUCCESS_CODE.getCode());
		geinihuaResponseData.setMessage(RegisterGeiNiHuaEnums.SUCCESS_CODE.getMsg());
		if(geinihuaChannel.equals(geiNiHuaDTO.getChannelId())) {			
			geinihuaResponseData.setUrl(URL);
		}else if(geinihuaChannel_wwj.equals(geiNiHuaDTO.getChannelId())) {
			geinihuaResponseData.setUrl(WWJURL);
		}
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
				/*SmsRequest smsRequest = new SmsRequest();
				smsRequest.setSmsType(15);
				smsRequest.setCellphone(mobile);
				Map<String, Object> map = new HashMap<>();
				map.put("passwd", pwd);
				smsRequest.setMap(map);
				sendSmsService.sendSingleSms(smsRequest);*/
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
				if(geinihuaChannel.equals(source)) {
					msg.setAppName("nyd");
				}else if(geinihuaChannel_wwj.equals(source)) {
					msg.setAppName("wwj");
				}else {			
					msg.setAppName("nyd");
				}
				msg.setCreateTime(new Date());
				userToUserSourceLogProducer.sendMsg(msg);
			} catch (Exception e) {
				logger.error("send msg to userSourceLog has exception!", e);
			}
		}

}
