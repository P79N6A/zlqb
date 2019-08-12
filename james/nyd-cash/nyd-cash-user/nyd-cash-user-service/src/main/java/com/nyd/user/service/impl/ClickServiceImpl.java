package com.nyd.user.service.impl;

import com.nyd.user.dao.ClickDao;
import com.nyd.user.model.ClickInfo;
import com.nyd.user.service.ClickService;
import com.nyd.user.service.consts.UserConsts;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhujx on 2017/12/12.
 */
@Service
public class ClickServiceImpl implements ClickService{
    private static Logger LOGGER = LoggerFactory.getLogger(ClickServiceImpl.class);

    @Autowired
    ClickDao clickDao;

    /**
     * 保存引流点击数据
     * @param clickInfo
     * @return
     */
    @Override
    public ResponseData saveClickInfo(ClickInfo clickInfo) {
        LOGGER.info("begin to save clickInfo, channelNo is"+clickInfo.getChannelNo());
        ResponseData responseData = ResponseData.success();
        try {
            clickDao.save(clickInfo);
            LOGGER.info("save clickInfo success");
        } catch (Exception e) {
            responseData = ResponseData.error(UserConsts.DB_ERROR_MSG);
            LOGGER.error("save clickInfo error! ChannelNo = " + clickInfo.getChannelNo(), e);
        }
        return responseData;
    }
}
