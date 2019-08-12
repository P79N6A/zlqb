package com.nyd.application.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nyd.application.api.FlowConfigContract;
import com.nyd.application.dao.FlowConfigDao;
import com.nyd.application.model.FlowConfigModel;
import com.nyd.application.service.consts.ApplicationConsts;
import com.tasfe.framework.support.model.ResponseData;
@Service(value="flowConfigContract")
public class FlowConfigContractServiceimpl implements FlowConfigContract{
	
    private static Logger logger = LoggerFactory.getLogger(FlowConfigContractServiceimpl.class);

	
	@Autowired
	private FlowConfigDao flowConfigDao;
	
	@Override
	public ResponseData<FlowConfigModel> getFlowConfigByChannelId(String channelId) {
		ResponseData responseData = ResponseData.success();
		try {
			List<FlowConfigModel> list =flowConfigDao.getFlowConfigByChannelId(channelId);
			if(list != null && list.size() >0) {			
				responseData.setData(list.get(0));
			}
		} catch (Exception e) {		
			 responseData = ResponseData.error(ApplicationConsts.ERROR_MSG);
			logger.info(e.getMessage(),e);
		}
		return responseData;
	}

}
