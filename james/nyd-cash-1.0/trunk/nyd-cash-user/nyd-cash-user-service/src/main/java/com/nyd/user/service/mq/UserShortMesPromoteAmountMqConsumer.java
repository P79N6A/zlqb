package com.nyd.user.service.mq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.nyd.msg.model.SmsRequest;
import com.nyd.msg.service.ISendSmsService;
import com.nyd.user.dao.AccountDao;
import com.nyd.user.dao.UserDao;
import com.nyd.user.model.UserInfo;
import com.nyd.user.model.dto.AccountDto;
import com.nyd.user.model.dto.ShortMesPromoteAmountDto;
import com.tasfe.framework.rabbitmq.RabbitmqMessageProcesser;
import com.tasfe.framework.support.model.ResponseData;

@Component
public class UserShortMesPromoteAmountMqConsumer implements RabbitmqMessageProcesser<String>{
	
	private static final Logger logger = LoggerFactory.getLogger(UserShortMesPromoteAmountMqConsumer.class);
	
	@Autowired
	private UserDao UserDao;
	
	@Autowired
	private ISendSmsService sendSmsService;
	
	@Autowired
	private AccountDao accountDao;
	
	private static String APPNAME="侬要贷";
	
	@Override
	public void processMessage(String message) {
		if(StringUtils.isBlank(message)) {
			logger.info("接受到风控推送过过来的用户提额信息为null");
			return;
		}
		logger.info("接受到风控用户提额信息"+message);
		
		List<ShortMesPromoteAmountDto> messageList = JSON.parseArray(message, ShortMesPromoteAmountDto.class);
		
		for(ShortMesPromoteAmountDto  dto :messageList ) {
			UserInfo  userInfo = new UserInfo();
			try {			
				List<UserInfo> userList =UserDao.getUsersByIdNumber(dto.getIdCardNo());
				if(userList != null && userList.size() > 0) {
					userInfo =userList.get(0);
				}else {
					logger.info(dto.getIdCardNo()+"没有查到用户信息");
					continue;
				}
			} catch (Exception e) {
				logger.info("数据库查询失败",e);		
			}
			AccountDto  account = new AccountDto();
			try {
				 List<AccountDto> accountList =accountDao.getAccountByuserId(userInfo.getUserId());
				 if(accountList != null && accountList.size() > 0) {
					account = accountList.get(0); 
				 }else {
					logger.info(dto.getIdCardNo()+"查询到的手机号码为空");
					continue; 
				 }
			} catch (Exception e) {		
				logger.info("数据库account表查询失败",e);
			}			
			SmsRequest vo = new SmsRequest();
			vo.setAppName("nyd");
			vo.setCellphone(account.getAccountNumber());
			vo.setSmsType(33);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("userName", userInfo.getRealName());
			map.put("limitAmount", dto.getLimitAmount());
			map.put("appNmae", APPNAME);
			vo.setMap(map);
			ResponseData data =sendSmsService.sendSingleSms(vo);
			logger.info("短信推送返回信息"+JSON.toJSONString(data));			
		 }
		}
	
}
