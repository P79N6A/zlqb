package com.nyd.order.service;

import com.nyd.order.dao.mapper.ProportionMapper;
import com.nyd.order.entity.ChannelProportionConfig;
import com.nyd.order.model.ChannelInfo;
import com.nyd.order.service.impl.OrderChannelContractImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shaoqing.liu
 * @date 2018/10/12 10:40
 */
@Service
public class ChannelProportionService {

    final Logger LOGGER = LoggerFactory.getLogger(ChannelProportionService.class);
    @Autowired
    private ProportionMapper proportionMapper;


    /**
     * 获取所有有效资金渠道
     * @return
     */
    public List<ChannelProportionConfig> getChannelConfig(){
        List<ChannelProportionConfig> proportions = proportionMapper.selectChannelList();
        return proportions;
    }
    /**
     * 获取所有资金渠道
     * @return
     */
    public List<ChannelInfo> getAllChannelConfig(ChannelInfo info){
    	List<ChannelInfo> proportions = proportionMapper.selectAllChannelList(info);
    	return proportions;
    }

    public int updateChannelConfig(ChannelProportionConfig channelProportionConfig){
        return proportionMapper.updateChannelConfig(channelProportionConfig);
    }

}
