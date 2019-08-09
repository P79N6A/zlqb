package com.nyd.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.AccountMapper;
import com.nyd.admin.service.AccountManagerService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 18:40 2018/10/1
 */
@Service
public class AccountManagerServiceImpl implements AccountManagerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountManagerServiceImpl.class);
    @Autowired
    private AccountMapper accountMapper;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public List<String> findByTime(Map<String, String> timeParam) {
        LOGGER.info("AccountManagerServiceImpl findByTime start param:{}", JSON.toJSONString(timeParam));
        List<String> mobiles= accountMapper.selectUnloginByTime(timeParam);
        return mobiles;
    }
}
