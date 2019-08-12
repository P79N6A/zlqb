package com.nyd.user.service.mq;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.nyd.user.dao.LoginLogDao;
import com.nyd.user.entity.LoginLog;
import com.nyd.user.model.HitAccountInfo;
import com.nyd.user.service.HitAccountService;
import com.nyd.user.service.util.Md5Util;
import com.tasfe.framework.rabbitmq.RabbitmqMessageProcesser;

/**
 * 
 * @author zhangdk
 *
 */
public class UserLoginLogMqProcesser implements RabbitmqMessageProcesser<LoginLog>{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginLogMqProcesser.class);

    @Autowired
	private LoginLogDao loginLogDao;


    @Override
    public void processMessage(LoginLog message) {
        LOGGER.info("Login Log Message:"+ message.getAccountNumber());
        try {
        	loginLogDao.save(message);
            LOGGER.info("Login Log save success，accountNumber："+message.getAccountNumber());
        }catch (Exception e){
            LOGGER.error("Login Log save exception , accountNumber :"+message.getAccountNumber(),e);
        }
    }

}
