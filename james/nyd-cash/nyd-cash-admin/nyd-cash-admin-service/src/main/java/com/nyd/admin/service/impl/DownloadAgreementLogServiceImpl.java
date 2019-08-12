package com.nyd.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.nyd.admin.dao.DownloadAgreementLogDao;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.entity.DownloadAgreementLog;
import com.nyd.admin.model.dto.DownloadAgreementLogDto;
import com.nyd.admin.service.DownloadAgreementLogService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DownloadAgreementLogServiceImpl implements DownloadAgreementLogService {
    private static final Logger logger = LoggerFactory.getLogger(DownloadAgreementLogServiceImpl.class);

    @Autowired
    private DownloadAgreementLogDao downloadAgreementLogDao;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData saveDownloadAgreementLog(DownloadAgreementLogDto downloadAgreementLogDto) {
        ResponseData response = ResponseData.success();
        DownloadAgreementLog downloadAgreementLog = new DownloadAgreementLog();
        try {
            downloadAgreementLog.setAgreementId(downloadAgreementLogDto.getAgreementId());
            downloadAgreementLog.setDownloadPerson(downloadAgreementLogDto.getDownloadPerson());
            logger.info("代扣协议下载记录,保存对象:"+ JSON.toJSON(downloadAgreementLog));
            downloadAgreementLogDao.save(downloadAgreementLog);
            logger.info("代扣协议下载记录,保存成功");
        }catch (Exception e){
            logger.error("代扣协议下载记录,保存出错",e);
            return response.error("服务器开小差了！");
        }
        return response;
    }
}
