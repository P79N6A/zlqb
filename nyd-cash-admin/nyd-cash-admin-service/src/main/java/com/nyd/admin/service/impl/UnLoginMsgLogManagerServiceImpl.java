package com.nyd.admin.service.impl;

import com.nyd.admin.dao.UnLoginMsgLogDao;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.entity.UnLoginMsgLog;
import com.nyd.admin.model.UnLoginMsgLogInfo;
import com.nyd.admin.service.UnLoginMsgLogManagerService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 18:46 2018/10/1
 */
@Service
public class UnLoginMsgLogManagerServiceImpl implements UnLoginMsgLogManagerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnLoginMsgLogManagerServiceImpl.class);
    @Autowired
    private UnLoginMsgLogDao unLoginMsgLogDao;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public void saveUnLoginMsgLog(UnLoginMsgLogInfo unLoginMsgLogInfo) {
        LOGGER.info("saveUnLoginMsgLog start param  mobile:{}",unLoginMsgLogInfo.getMobile());
        try {
            UnLoginMsgLog unLoginMsgLog = new UnLoginMsgLog();
            BeanUtils.copyProperties(unLoginMsgLogInfo,unLoginMsgLog);
            unLoginMsgLogDao.save(unLoginMsgLog);
            LOGGER.info("save UnLoginMsgLog success  mobile:{}",unLoginMsgLogInfo.getMobile());
        } catch (Exception e) {
            LOGGER.error("saveUnLoginMsgLog exception,mobile:{}",unLoginMsgLogInfo.getMobile(),e);
        }
    }
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public void updateUnLoginMsgLog(UnLoginMsgLogInfo unLoginMsgLogInfo) {
        LOGGER.info("updateUnLoginMsgLog start param  mobile:{}",unLoginMsgLogInfo.getMobile());
        try {
            unLoginMsgLogDao.update(unLoginMsgLogInfo);
            LOGGER.info("update UnLoginMsgLog success  mobile:{}",unLoginMsgLogInfo.getMobile());
        } catch (Exception e) {
            LOGGER.error("updateUnLoginMsgLog exception,mobile:{}",unLoginMsgLogInfo.getMobile(),e);
        }
    }
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public UnLoginMsgLogInfo getByMobile(String mobile) {
        LOGGER.info("getByMobile start param  mobile:{}",mobile);
        try {
            List<UnLoginMsgLogInfo> unLoginMsgLogInfos = unLoginMsgLogDao.getByMobile(mobile);
            if(null!=unLoginMsgLogInfos && unLoginMsgLogInfos.size()>0){
                return unLoginMsgLogInfos.get(0);
            }
        } catch (Exception e) {
            LOGGER.error("getByMobile exception,mobile:{}",mobile,e);
        }
        return null;
    }
}
