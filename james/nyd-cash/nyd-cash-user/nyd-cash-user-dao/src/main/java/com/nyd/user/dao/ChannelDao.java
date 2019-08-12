package com.nyd.user.dao;

import com.nyd.user.model.ChannelInfo;

/**
 * Created by zhujx on 2017/12/5.
 */
public interface ChannelDao {

    void save(ChannelInfo channelInfo) throws Exception;

    /**
     * 根据url查询渠道信息
     * @param url
     * @return
     * @throws Exception
     */
    ChannelInfo getChannelInfoByUrl(String url)throws Exception;

}
