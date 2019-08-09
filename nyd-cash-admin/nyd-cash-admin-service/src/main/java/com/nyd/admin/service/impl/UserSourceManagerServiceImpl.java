package com.nyd.admin.service.impl;

import com.nyd.admin.dao.UserSourceDao;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.model.UserSourceInfo;
import com.nyd.admin.service.UserSourceManagerService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 19:03 2018/10/1
 */
@Service
public class UserSourceManagerServiceImpl implements UserSourceManagerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSourceManagerServiceImpl.class);

    @Autowired
    private UserSourceDao userSourceDao;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public UserSourceInfo getByMobile(String mobile) {
        LOGGER.info("UserSourceManagerServiceImpl getByMobile start,mobile:{}",mobile);
        try {
            List<UserSourceInfo> userSourceInfos = userSourceDao.findByMobile(mobile);
            if(null!=userSourceInfos && userSourceInfos.size()>0){
                return userSourceInfos.get(0);
            }
        } catch (Exception e) {
            LOGGER.error("getByMobile exception,mobile:{}",e);
        }
        return null;
    }
}
