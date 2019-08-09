package com.creativearts.nyd.pay.service.zzl.helibao;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.nyd.order.entity.zzl.HelibaoFlowLog;
import com.nyd.pay.api.zzl.HelibaoFlowLogService;
import com.nyd.pay.dao.mapper.HelibaoFlowLogMapper;

@Service
@Transactional
public class HelibaoFlowLogServiceImpl<T> implements HelibaoFlowLogService<T> {
	
	Logger logger = LoggerFactory.getLogger(HelibaoFlowLogServiceImpl.class);

	@Autowired
    private HelibaoFlowLogMapper  mapper;
	



	@Override
	public void saveFlow(String bizType,String orderNumber,Map reqContent, Map respContent,String remark,String custInfoId) {
		HelibaoFlowLog log = new HelibaoFlowLog();
		log.setId(getUUID());
		log.setBizType(bizType);
		log.setOrderNumber(orderNumber);
		log.setCreateTime(new Date());
		log.setReqContent(JSON.toJSONString(reqContent, SerializerFeature.WriteMapNullValue));
		log.setRespContent(JSON.toJSONString(respContent, SerializerFeature.WriteMapNullValue));
		log.setRemark(remark);
		log.setCustInfoId(custInfoId);
		try {
			mapper.insert(log);
		} catch (Exception e) {
			logger.error("HelibaoFlowLog插入异常：" + e.getMessage(), e);
		}
	}


	
	public  String getUUID(){
		return UUID.randomUUID().toString().replaceAll(Pattern.quote("-"),"");
	}
	
	
}
