package com.nyd.user.service.impl;

import com.nyd.user.dao.ChannelDao;
import com.nyd.user.model.ChannelInfo;
import com.nyd.user.service.ChannelInfoService;
import com.nyd.user.service.consts.UserConsts;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by zhujx on 2017/12/5.
 */
@Service
public class ChannelInfoServiceImpl implements ChannelInfoService{
    private static Logger LOGGER = LoggerFactory.getLogger(ChannelInfoServiceImpl.class);

    @Autowired
    private ChannelDao channelDao;

    /**
     * 查询渠道信息
     * @param url
     * @return
     */
    @Override
    public ResponseData<ChannelInfo> getChannelInfoByUrl(String url) {
        LOGGER.info("begin to get channelInfo, url is"+url);
        ResponseData responseData = ResponseData.success();
        try {
            ChannelInfo channelInfo = channelDao.getChannelInfoByUrl(url);
            responseData.setData(channelInfo);
            LOGGER.info("get channelInfo success");
        } catch (Exception e) {
            responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
            LOGGER.error("getChannelInfoByUrl error! url = " + url, e);
        }
        return responseData;
    }
}
