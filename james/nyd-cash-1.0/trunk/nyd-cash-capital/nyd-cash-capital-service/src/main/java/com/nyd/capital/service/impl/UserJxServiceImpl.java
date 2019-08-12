package com.nyd.capital.service.impl;

import com.alibaba.fastjson.JSON;
import com.nyd.capital.dao.LogKzjrDao;
import com.nyd.capital.dao.ds.DataSourceContextHolder;
import com.nyd.capital.dao.mappers.UserJxMapper;
import com.nyd.capital.entity.UserJx;
import com.nyd.capital.service.UserJxService;
import com.nyd.capital.service.aspect.RoutingDataSource;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liuqiu
 */
@Service
public class UserJxServiceImpl implements UserJxService {

    final Logger logger = LoggerFactory.getLogger(UserJxServiceImpl.class);
    @Autowired
    private UserJxMapper userJxMapper;
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public void savePassword(UserJx userJx) {
        try {
            userJxMapper.savePassword(userJx);
            logger.info(JSON.toJSONString(userJx)+"保存成功");
        }catch (Exception e){
            logger.error(JSON.toJSONString(userJx)+"保存失败",e);
        }
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public String selectPasswordByUserId(String userId) {
        try {
            UserJx password = userJxMapper.selectPasswordByUserId(userId);
            logger.info(JSON.toJSONString(userId)+"查询成功");
            return password.getPassword();
        }catch (Exception e){
            logger.error(JSON.toJSONString(userId)+"查询失败",e);
            return null;
        }
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public void updateMember(String memberId,String userId) {
        try {
            userJxMapper.updateMember(memberId,userId);
            logger.info(JSON.toJSONString(memberId)+"保存成功");
        }catch (Exception e){
            logger.error(JSON.toJSONString(memberId)+"保存失败",e);
        }
    }
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public void updateLoanId(String loanId, String userId) {
        try {
            userJxMapper.updateLoanId(loanId,userId);
            logger.info(JSON.toJSONString(loanId)+"保存成功");
        }catch (Exception e){
            logger.error(JSON.toJSONString(loanId)+"保存失败",e);
        }
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public List<UserJx> selectUserStageTwo() {
        try {
            List<UserJx> list = userJxMapper.selectUserStageTwo();
            logger.info("定时查询推单外审确认的用户成功");
            return list;
        }catch (Exception e){
            logger.error("定时查询推单外审确认的用户失败",e);
        }
        return null;
    }
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public List<UserJx> getUserJxByUserId(String userId) {
        try {
            List<UserJx> list = userJxMapper.getUserJxByUserId(userId);
            logger.info("查询jx的用户成功");
            return list;
        }catch (Exception e){
            logger.error("查询jx的用户失败",e);
        }
        return null;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public void updateUserJx(UserJx userJx) {
        try {
            userJxMapper.updateUserJx(userJx);
            logger.info("修改jx的用户成功");
        }catch (Exception e){
            logger.error("修改jx的用户失败",e);
        }
    }
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public ResponseData<UserJx> selectUserJxByMemberId(String memberId) {
        ResponseData<UserJx> responseData = (ResponseData<UserJx>) ResponseData.success();
        try {
            UserJx userJx = userJxMapper.selectUserJxByMemberId(memberId);
            logger.info("查询jx的用户成功");
            responseData.setData(userJx);
            return responseData;
        }catch (Exception e){
            logger.error("查询jx的用户失败",e);
        }
        responseData.setData(null);
        return responseData;
    }
}
