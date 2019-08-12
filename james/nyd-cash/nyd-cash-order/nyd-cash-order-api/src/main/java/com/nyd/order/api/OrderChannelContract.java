package com.nyd.order.api;

import java.math.BigDecimal;
import java.util.List;

import com.nyd.order.model.ChannelInfo;
import com.tasfe.framework.support.model.ResponseData;


/**
 *
 * @author liuqiu
 */
public interface OrderChannelContract {


    /**
     * 获取资金渠道方
     * @return
     */
    ResponseData getChannel();
    /**
     * 获取资金渠道方
     * @return
     */
    ResponseData getChannelWithAmount(BigDecimal currentAmount);
    /**
     * 根据code获取资金渠道方
     * @return
     */
    ResponseData getChannelByCode(String code);
    
    List<ChannelInfo> getAllChannelConfig(ChannelInfo info);
    
    void save(ChannelInfo channelInfo);
    
    void update(ChannelInfo channelInfo);
    
    boolean ifReachMaxAmount(String code,BigDecimal currentAmount);
}
