package com.nyd.application.api;

import com.nyd.application.model.FlowConfigModel;
import com.tasfe.framework.support.model.ResponseData;
/**
 * 
 * @author chengjf
 *
 */
public interface FlowConfigContract {
	/**
	 * 根据channelId得到flowConfig
	 * @param channelId
	 * @return
	 */
	 ResponseData<FlowConfigModel> getFlowConfigByChannelId(String channelId); 

}
