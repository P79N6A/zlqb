package com.nyd.capital.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.nyd.capital.dao.ds.DataSourceContextHolder;
import com.nyd.capital.dao.mappers.UserDldLoanMapper;
import com.nyd.capital.dao.mappers.UserDldMapper;
import com.nyd.capital.entity.UserDld;
import com.nyd.capital.entity.UserDldLoan;
import com.nyd.capital.service.UserDldService;
import com.nyd.capital.service.aspect.RoutingDataSource;

/**
 *
 * @author zhangdk
 *
 */
@Service
public class UserDldServiceImpl implements UserDldService {

    final Logger logger = LoggerFactory.getLogger(UserDldServiceImpl.class);
    @Autowired
    private UserDldMapper userDldMapper;
    @Autowired
    private UserDldLoanMapper userDldLoanMapper;


    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public void save(UserDld userDld) throws Exception{
        try {
            userDldMapper.save(userDld);
            logger.info(JSON.toJSONString(userDld)+"保存成功");
        }catch (Exception e){
            logger.error(JSON.toJSONString(userDld)+"保存失败",e);
            throw new Exception(e.getMessage());
        }
    }
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public UserDld getUserDldByUserId(String userId) throws Exception{
        try {
            return userDldMapper.getUserDldByUserId(userId);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public void update(UserDld userDld) throws Exception{
        try {
            userDldMapper.update(userDld);
            logger.info(JSON.toJSONString(userDld)+"保存成功");
        }catch (Exception e){
            logger.error(JSON.toJSONString(userDld)+"保存失败",e);
            throw new Exception(e.getMessage());
        }
    }
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public void saveLoan(UserDldLoan loan) throws Exception{
        try {
            userDldLoanMapper.save(loan);
            logger.info(JSON.toJSONString(loan)+"保存成功");
        }catch (Exception e){
            logger.error(JSON.toJSONString(loan)+"保存失败",e);
            throw new Exception(e.getMessage());
        }
    }
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public void updateLoan(UserDldLoan loan) throws Exception{
        try {
            userDldLoanMapper.update(loan);
            logger.info(JSON.toJSONString(loan)+"保存成功");
        }catch (Exception e){
            logger.error(JSON.toJSONString(loan)+"保存失败",e);
            throw new Exception(e.getMessage());
        }
    }
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public UserDldLoan getUserDldLoanByOrderNo(String orderNo) {
        return userDldLoanMapper.getUserDldLoanByOrderNo(orderNo);
    }
}
