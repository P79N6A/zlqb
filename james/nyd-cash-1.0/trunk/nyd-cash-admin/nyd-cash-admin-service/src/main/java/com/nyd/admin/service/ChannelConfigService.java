package com.nyd.admin.service;


import java.util.List;
import java.util.Map;

import com.nyd.order.model.ChannelInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 18:26 2018/10/1
 */
public interface ChannelConfigService {
	ResponseData getBankList();
    ResponseData channelConfigQuery(ChannelInfo channel);
    ResponseData channelConfigSave(ChannelInfo channel);
    ResponseData getChannelConfigByCode(ChannelInfo channel);
    ResponseData channelConfigUpdate(ChannelInfo channel);
}
