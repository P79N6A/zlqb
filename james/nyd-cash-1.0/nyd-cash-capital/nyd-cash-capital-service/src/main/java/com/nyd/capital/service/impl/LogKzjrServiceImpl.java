package com.nyd.capital.service.impl;

import com.alibaba.fastjson.JSON;
import com.nyd.capital.dao.LogKzjrDao;
import com.nyd.capital.dao.ds.DataSourceContextHolder;
import com.nyd.capital.entity.LogKzjr;
import com.nyd.capital.service.LogKzjrService;
import com.nyd.capital.service.aspect.RoutingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Cong Yuxiang
 * 2018/5/2
 **/
@Service
public class LogKzjrServiceImpl implements LogKzjrService{
    final Logger logger = LoggerFactory.getLogger(LogKzjrServiceImpl.class);
    @Autowired
    private LogKzjrDao logKzjrDao;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_FINANCE)
    @Override
    public void save(LogKzjr logKzjr) {
        try {
            logKzjrDao.save(logKzjr);
            logger.info(JSON.toJSONString(logKzjr)+"保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(JSON.toJSONString(logKzjr)+"保存异常",e);
        }
    }
}
