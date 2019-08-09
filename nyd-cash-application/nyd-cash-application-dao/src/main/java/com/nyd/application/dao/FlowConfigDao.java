package com.nyd.application.dao;

import java.util.List;

import com.nyd.application.model.FlowConfigModel;

public interface FlowConfigDao {
	
	List<FlowConfigModel>  getFlowConfigByChannelId(String channelId) throws Exception;

}
