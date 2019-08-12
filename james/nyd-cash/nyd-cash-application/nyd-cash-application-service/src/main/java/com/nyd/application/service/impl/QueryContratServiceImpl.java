package com.nyd.application.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nyd.application.api.call.QueryContratService;
import com.nyd.application.model.common.ChkUtil;
import com.nyd.application.service.AgreeMentService;
import com.tasfe.framework.support.model.ResponseData;

@Service(value="queryContratService")
public class QueryContratServiceImpl implements QueryContratService{

	@Autowired
	private AgreeMentService agreeMentService;
	
    private static Logger LOGGER = LoggerFactory.getLogger(AgreeMentServiceImpl.class);

	
	@Override
	public ResponseData getSignAgreement(String userId, String orderId) {
        LOGGER.info(" userId =  "+userId + " orderId:" + orderId);
        if (ChkUtil.isEmpty(userId)&&ChkUtil.isEmpty(orderId)) {
            return ResponseData.success();
        }
        try {
            return agreeMentService.getSignAgreement(userId,orderId);
        } catch (Exception e) {
            LOGGER.error(" 获取协议异常" + e);
            return ResponseData.error();
        }
    }

}
