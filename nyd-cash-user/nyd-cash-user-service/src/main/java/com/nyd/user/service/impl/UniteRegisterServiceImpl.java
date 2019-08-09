package com.nyd.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.nyd.application.api.FlowConfigContract;
import com.nyd.application.model.FlowConfigModel;
import com.nyd.library.api.LibraryContract;
import com.nyd.user.dao.AccountDao;
import com.nyd.user.dao.HitAccountDao;
import com.nyd.user.dao.LoginLogDao;
import com.nyd.user.dao.PasswordDao;
import com.nyd.user.dao.mapper.UserSourceMapper;
import com.nyd.user.entity.Account;
import com.nyd.user.entity.HitAccount;
import com.nyd.user.entity.LoginLog;
import com.nyd.user.entity.UserSource;
import com.nyd.user.entity.UserTarget;
import com.nyd.user.model.AccountInfo;
import com.nyd.user.model.FlowMerchantInfo;
import com.nyd.user.model.dto.AccountDto;
import com.nyd.user.model.dto.NydUniteRegisterDto;
import com.nyd.user.model.dto.UniteRegisterDto;
import com.nyd.user.model.enums.UniteRegisterRespCode;
import com.nyd.user.model.request.HitRequest;
import com.nyd.user.model.uniteregister.UniteRegisterResponseData;
import com.nyd.user.service.AccountInfoService;
import com.nyd.user.service.NydHitLogicService;
import com.nyd.user.service.UniteRegisterService;
import com.nyd.user.service.UserTargetService;
import com.nyd.user.service.run.LibraryRunnable;
import com.nyd.user.service.util.Md5Util;
import com.nyd.user.service.util.RSAUtil;
import com.nyd.user.service.util.RandomUtil;
import com.nyd.user.service.util.UserProperties;
import com.tasfe.framework.support.model.ResponseData;

@Service(value="uniteRegisterService")
public class UniteRegisterServiceImpl implements UniteRegisterService{
	 
	private static Logger logger = LoggerFactory.getLogger(UniteRegisterServiceImpl.class);
	
	private static final String PHONE_REGEX = "^1\\d{10}$";
	
	@Autowired
	private  AccountDao accountDao;
	
	@Autowired 
	private LoginLogDao loginLogDao;
	
	@Autowired
	private UserSourceMapper  userSourceMapper;
	
	@Autowired
	private HitAccountDao hitAccountDao;
	
	@Autowired
	private FlowConfigContract flowConfigContract;	
	@Autowired
	private LibraryContract libraryContract;
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	@Autowired
	UserProperties userProperties;
	@Autowired
	private NydHitLogicService nydHitLogicService;
	@Autowired
	private UserTargetService userTargetService;	
	@Autowired
	private AccountInfoService accountInfoService;	
	@Autowired
	private PasswordDao passwordDao;
	@Override
	public UniteRegisterResponseData dealUniteRegisterReq(UniteRegisterDto dto) {
		//撞库规则重新调整
		UniteRegisterResponseData responseData = UniteRegisterResponseData.success();
		HitRequest request = new HitRequest();
		request.setAppName(dto.getAppName());
		request.setMobile(dto.getMobile());
		request.setMobileType("1");
		request.setSource(dto.getChannelId());
		nydHitLogicService.HitByMobileAndRule(request);	
		//参数判空	
		logger.info("联合注册请求处理uniteRegisterDto"+ToStringBuilder.reflectionToString(dto));
		if(dto ==null  || StringUtils.isBlank(dto.getMobile()) || StringUtils.isBlank(dto.getChannelId())|| StringUtils.isBlank(dto.getAppName())) {
			logger.error("参数异常");
			responseData = UniteRegisterResponseData.error(UniteRegisterRespCode.REGISTER_FAIL,"请求参数异常");
			return responseData;
		}else {		
			if (!dto.getMobile().matches(PHONE_REGEX)) {
				logger.error("手机号不符合要求，手机号" + dto.getMobile());
				responseData = UniteRegisterResponseData.error(UniteRegisterRespCode.REGISTER_FAIL, "请求参数异常");
				return responseData;
			}
		}
		
		try {
			ResponseData<FlowConfigModel> configModelResponseData = flowConfigContract.getFlowConfigByChannelId(dto.getChannelId());
			if (null == configModelResponseData || !"0".equals(configModelResponseData.getStatus())) {
				logger.error("调用flowConfigContract dubbo api异常：" + dto.getChannelId());
				responseData = UniteRegisterResponseData.error(UniteRegisterRespCode.SERVER_ERROR, "服务器内部异常");
				return responseData;
			}
			FlowConfigModel flowConfigModel = configModelResponseData.getData();
			if (null == flowConfigModel) {
				logger.warn("联合注册商请求");
				responseData = UniteRegisterResponseData.error(UniteRegisterRespCode.REGISTER_FAIL, "联合注册请求,无引流配置");
				return responseData;
			}
			List<AccountDto>  accountList = new  ArrayList<AccountDto>();
			try {
				 accountList =	accountDao.findByAccountNumber(dto.getMobile());
			} catch (Exception e) {		
				logger.info(e.getMessage(),e);
			}
			if(accountList != null && accountList.size() >0) {
				logger.info("联合注册用户已经存在 channelId:" + dto.getChannelId());
				responseData = UniteRegisterResponseData.error(UniteRegisterRespCode.REGISTER_OLD_SUCCESS);
				return responseData;			
			}
			
			
			logger.info("联合注册mobile:"+dto.getMobile()+"channelId:"+dto.getChannelId());
			if(!this.registerUser(dto.getMobile(), dto.getChannelId(),dto.getAppName())) {
				logger.info("联合注册失败 mobile:" + dto.getMobile());
				responseData = UniteRegisterResponseData.error(UniteRegisterRespCode.SERVER_ERROR);
				return responseData;
			}			
		}catch(Exception e) {
			logger.error("联合注册异常 channelId:" + dto.getChannelId(), e);
			responseData = UniteRegisterResponseData.error(UniteRegisterRespCode.SERVER_ERROR);
		}
		logger.info("联合注册丢进撞库线程进行处理,手机号为: " + dto.getMobile());
		LibraryRunnable runnable = new LibraryRunnable(libraryContract, dto.getMobile(),
				dto.getAppName(), dto.getChannelId());
		threadPoolTaskExecutor.execute(runnable);	
		return responseData;
	}
	
	/**
	 * 联合注册
	 * @param mobile
	 * @param source
	 * @return
	 */
	private boolean registerUser(String mobile,String source,String appName) {
		boolean isSuccess;	
		try {		
			Account account = new Account();
			account.setAccountNumber(mobile);
			account.setSource(source);
			accountDao.save(account);
			logger.info("uniteRegisterService 保存account成功 mobile:" + mobile);
			AccountInfo accountInfo = new AccountInfo();
			accountInfo.setAccountNumber(mobile);
			String pwd = RandomUtil.flowPasswd(6);
			accountInfo.setPassword(Md5Util.getMD5(pwd));
			passwordDao.save(accountInfo);
			logger.info("uniteRegisterService 保存account_password成功 mobile:" + mobile);		
			//保存账户信息至redis中
			accountInfoService.saveAccountInRedis(account, null);
			LoginLog loginLog = new LoginLog();
			loginLog.setAccountNumber(mobile);
			loginLog.setSource(source);
			loginLog.setStatus("0");//登录成功
			loginLog.setAppName(appName);
			loginLogDao.save(loginLog);
			logger.info("uniteRegisterService 保存loginLog成功 mobile:" + mobile);		
			UserSource userSource = new UserSource();
			userSource.setAccountNumber(mobile);
			if (StringUtils.isBlank(appName)) {
				appName = "nyd";
			}
			userSource.setAppName(appName);
			userSource.setSource(source);		
			userSourceMapper.save(userSource);
			logger.info("uniteRegisterService 保存userSource成功 mobile:" + mobile);			
			String sha256Str = DigestUtils.sha256Hex(mobile.getBytes());
	        String md5Str = Md5Util.getMD5To32LowCaseSign(mobile);
	        HitAccount  hitAccount = new HitAccount();
            hitAccount.setAccountNumber(mobile);
            hitAccount.setSecretShaStr(sha256Str);
            hitAccount.setSecretMdStr(md5Str);
            hitAccountDao.insert(hitAccount);
            logger.info("HitAccount 添加成功，accountNumber："+mobile);
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
    		logger.info("userTarget 添加成功，accountNumber："+mobile);
			isSuccess = true;
		}catch(Exception e) {
			isSuccess = false;
			logger.info(e.getMessage(),e);
		}		
		return isSuccess;
	}

	@Override
	public UniteRegisterResponseData dealNydUniteRegisterReq(NydUniteRegisterDto dto) {
		logger.info("联合注册请求处理NydUniteRegisterDto"+ToStringBuilder.reflectionToString(dto));
		UniteRegisterResponseData data = UniteRegisterResponseData.success();
		//撞库规则重新调整
		HitRequest request = new HitRequest();
		request.setAppName(dto.getAppName());
		request.setMobile(dto.getMobile());
		request.setMobileType("1");
		request.setSource(dto.getChannelId());
		nydHitLogicService.HitByMobileAndRule(request);		
		//参数判空
		UniteRegisterResponseData paramData  = this.paramIsBlank(dto);
		if(String.valueOf(UniteRegisterRespCode.REGISTER_FAIL.getCode()).equals(String.valueOf(paramData.getCode()))) {
			return paramData;
		}
		
		if(!"nyd".equals(dto.getAppName())) {
			data = UniteRegisterResponseData.error(UniteRegisterRespCode.REGISTER_FAIL,"appName传参错误");
			logger.info("appName传参错误"+dto.getAppName());
			return data;
			
		}
		//手机号不符合标准要求
		if (!dto.getMobile().matches(PHONE_REGEX)) {
			logger.error("手机号不符合要求，手机号:" + dto.getMobile());
			data = UniteRegisterResponseData.error(UniteRegisterRespCode.REGISTER_FAIL, "手机号不符合要求");
			return data;
		}		
		String publicKey = null;
		if(FlowMerchantInfo.BAO_JIE.equals(dto.getChannelId())) {
			 publicKey =userProperties.getBJUniteRegisterPublicKey();	
		}else if(FlowMerchantInfo.SHAN_FT.equals(dto.getChannelId())) {
			publicKey =userProperties.getShanFTUniteRegisterPublicKey();
		}
		if(StringUtils.isBlank(publicKey)) {
			logger.info("公钥为null");
			data = UniteRegisterResponseData.error(UniteRegisterRespCode.REGISTER_FAIL,"获取公钥为null");
			return data;
		}
		
		//调用私有方法获取明文参数
		String mingwen =this.packageParam(dto);	
		try {
			if(!RSAUtil.verify(mingwen.getBytes(), publicKey, dto.getSign())) {
				logger.info("签名验证错误");
				data = UniteRegisterResponseData.error(UniteRegisterRespCode.SIGN_ERROR,"验证签名错误");
				return data;
			}
		} catch (Exception e) {
			logger.info(dto.getAppId()+"联合注册sign验签异常",e);
			data = UniteRegisterResponseData.error(UniteRegisterRespCode.SERVER_ERROR,"服务内部错误");
			return data;
		}
	    try {
		ResponseData<FlowConfigModel> configModelResponseData = flowConfigContract.getFlowConfigByChannelId(dto.getChannelId());
		if (null == configModelResponseData || !"0".equals(configModelResponseData.getStatus())) {
			logger.error("调用flowConfigContract dubbo api异常" + dto.getChannelId());
			data = UniteRegisterResponseData.error(UniteRegisterRespCode.SERVER_ERROR, "服务器内部异常");
			return data;
		}
		FlowConfigModel flowConfigModel = configModelResponseData.getData();
		if (null == flowConfigModel) {
			logger.info("channelId不合法,未找到channelId"+dto.getChannelId());
			data = UniteRegisterResponseData.error(UniteRegisterRespCode.REGISTER_FAIL, "联合注册请求,无引流配置");
			return data;
		}	
		List<AccountDto>  accountList = new ArrayList<AccountDto>();
		try {
			 accountList =	accountDao.findByAccountNumber(dto.getMobile());
		} catch (Exception e) {		
			logger.info(e.getMessage(),e);
		}
		if(accountList != null && accountList.size() >0) {
			logger.info("该用户已经注册"+dto.getMobile());
			data = UniteRegisterResponseData.error(UniteRegisterRespCode.REGISTER_OLD_SUCCESS);
			return data;			
		}	
		if(!this.registerUser(dto.getMobile(), dto.getChannelId(),dto.getAppName())) {
			logger.info(dto.getAppId()+"联合注册失败 mobile:" + dto.getMobile());
			data = UniteRegisterResponseData.error(UniteRegisterRespCode.SERVER_ERROR);
			return data;
		}
		logger.info("联合注册丢进撞库线程进行处理,手机号为: " + dto.getMobile());
		LibraryRunnable runnable = new LibraryRunnable(libraryContract, dto.getMobile(),
				dto.getAppName(), dto.getChannelId());
		threadPoolTaskExecutor.execute(runnable);
	    }catch(Exception e){
	    	logger.error(dto.getAppId()+"联合注册异常 channelId:" + dto.getChannelId(), e);
			data = UniteRegisterResponseData.error(UniteRegisterRespCode.SERVER_ERROR);    
	    }
		return data;
	}
	
	
	private UniteRegisterResponseData paramIsBlank(NydUniteRegisterDto dto) {
		UniteRegisterResponseData data = new UniteRegisterResponseData();
		if( StringUtils.isBlank(dto.getAppId())) {
			data = UniteRegisterResponseData.error(UniteRegisterRespCode.REGISTER_FAIL,"appId参数为空");			
		}
		if( StringUtils.isBlank(dto.getAppName())) {
			data = UniteRegisterResponseData.error(UniteRegisterRespCode.REGISTER_FAIL,"appName参数为空");		
		}
		if( StringUtils.isBlank(dto.getChannelId())) {
			data = UniteRegisterResponseData.error(UniteRegisterRespCode.REGISTER_FAIL,"channelId参数为空");			
		}
		if( StringUtils.isBlank(dto.getMobile())) {
			data = UniteRegisterResponseData.error(UniteRegisterRespCode.REGISTER_FAIL,"mobile参数为空");
		}
		if( StringUtils.isBlank(dto.getTimestamp())) {
			data = UniteRegisterResponseData.error(UniteRegisterRespCode.REGISTER_FAIL,"timestamp参数为空");
		}
		if( StringUtils.isBlank(dto.getSign())) {
			data = UniteRegisterResponseData.error(UniteRegisterRespCode.REGISTER_FAIL,"sign必传参数为空");
		}	
		return data;
		
	}
	
	private String  packageParam(NydUniteRegisterDto dto) {		
		String appId = dto.getAppId();
		String appName =dto.getAppName();
		String mobile =dto.getMobile();
		String channelId =dto.getChannelId();
		String timestamp =dto.getTimestamp();
		String mingwen = "appId=" + appId + "&appName=" + appName +"&channelId="+channelId+ "&mobile=" + mobile +"&timestamp=" + timestamp;
		return mingwen ;
	}
	
	
	
	

}
