package com.nyd.user.service.impl;

import com.nyd.user.api.UserJxContract;
import com.nyd.user.dao.UserJxDao;
import com.nyd.user.dao.mapper.UserJxMapper;
import com.nyd.user.entity.UserJx;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liuqiu
 */
@Service("userJxContract")
public class UserJxContractImpl implements UserJxContract {
    private static Logger LOGGER = LoggerFactory.getLogger(UserJxContractImpl.class);
    @Autowired
    private UserJxDao userJxDao;
    @Autowired
    private UserJxMapper userJxMapper;
    @Override
    public ResponseData getUserJxByUserId(String userId) {
        ResponseData responseData = ResponseData.success();
        try {
            List<UserJx> userJxByUserId = userJxMapper.getUserJxByUserId(userId);
            responseData.setData(userJxByUserId);
        }catch (Exception e){
            LOGGER.error("查询用户即信信息发生异常",e);
            return ResponseData.error("查询用户即信信息发生异常");
        }
       return responseData;
    }

    @Override
    public ResponseData updateUserJx(UserJx userJx) {
        ResponseData responseData = ResponseData.success();
        try {
            userJxDao.updateUserJx(userJx);
        }catch (Exception e){
            LOGGER.error("查询用户即信信息发生异常",e);
            return ResponseData.error("查询用户即信信息发生异常");
        }
        return responseData;
    }
}
