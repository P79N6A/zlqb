package com.nyd.admin.service.impl;

import com.nyd.admin.dao.SendMsgRecordDao;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.model.Info.SendMsgRecordInfo;
import com.nyd.admin.service.SendMsgRecordService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SendMsgRecordServiceImpl implements SendMsgRecordService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendMsgRecordServiceImpl.class);

    @Autowired
    private SendMsgRecordDao sendMsgRecordDao;


    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public List<SendMsgRecordInfo> findByStatus() {
        try {
            return sendMsgRecordDao.findByStatus();
        } catch (Exception e) {
            LOGGER.error("findByStatus 根据状态查找短信异常",e);
        }
        return null;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public void updateByPhone(SendMsgRecordInfo sendMsgRecordInfo) {
        try {
            sendMsgRecordDao.update(sendMsgRecordInfo);
        } catch (Exception e) {
            LOGGER.error("updateByPhone 异常",e);
        }

    }
}
