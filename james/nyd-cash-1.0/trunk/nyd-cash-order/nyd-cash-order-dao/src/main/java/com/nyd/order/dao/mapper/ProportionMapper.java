package com.nyd.order.dao.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nyd.order.entity.ChannelProportionConfig;
import com.nyd.order.entity.Proportion;
import com.nyd.order.model.ChannelInfo;

@Repository
public interface ProportionMapper {


    List<Proportion> selectDate();

    Proportion selectDefaultRadio();

    int updateChannelConfig(ChannelProportionConfig channelProportionConfig);

    /**
     * 查询资金渠道集合
     * @return
     */
    List<ChannelProportionConfig> selectChannelList();
    /**
     * 查询资金渠道集合
     * @return
     */
    List<ChannelInfo> selectAllChannelList(ChannelInfo info);

    void save(ChannelInfo channel);
    void update(ChannelInfo channel);

    /**
     * 通过code查询渠道配置信息
     * @param channel
     * @return
     */
    ChannelProportionConfig selectChannelConfigByCode(String channel);
}
