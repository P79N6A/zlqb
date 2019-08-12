package com.nyd.user.service.mq;

import com.alibaba.fastjson.JSON;
import com.nyd.user.model.HitAccountInfo;
import com.nyd.user.model.mq.RegisterToHitAccountMessage;
import com.nyd.user.service.HitAccountService;
import com.nyd.user.service.util.Md5Util;
import com.tasfe.framework.rabbitmq.RabbitmqMessageProcesser;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @Author: zhangp
 * @Description:
 * @Date: 18:59 2018/9/6
 */
public class HitAccountMqProcesser implements RabbitmqMessageProcesser<RegisterToHitAccountMessage>{

    private static final Logger LOGGER = LoggerFactory.getLogger(HitAccountMqProcesser.class);

    @Autowired
    private HitAccountService hitAccountService;


    @Override
    public void processMessage(RegisterToHitAccountMessage message) {
        LOGGER.info("HitAccountMqProcesser start,param is "+ JSON.toJSONString(message));
        String accountNumber = message.getAccountNumber();
        if(StringUtils.isBlank(accountNumber)){
            LOGGER.warn("HitAccountMqProcesser 手机号为null");
            return;
        }
        try {
            String sha256Str = DigestUtils.sha256Hex(accountNumber.getBytes());
            String md5Str = Md5Util.getMD5To32LowCaseSign(accountNumber);
            HitAccountInfo  hitAccount = new HitAccountInfo();
            hitAccount.setAccountNumber(accountNumber);
            hitAccount.setSecretShaStr(sha256Str);
            hitAccount.setSecretMdStr(md5Str);
            if(StringUtils.isNotBlank(message.getToken())){
                hitAccount.setDescription(message.getToken());
            }
            hitAccountService.saveHitAccount(hitAccount);
            LOGGER.info("saveHitAccount 添加成功，accountNumber："+accountNumber);
        }catch (Exception e){
            LOGGER.error("HitAccountMqProcesser exception , accountNumber :"+accountNumber,e);
        }
    }

}
