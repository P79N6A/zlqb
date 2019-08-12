package com.nyd.capital.service.impl;

import com.nyd.capital.dao.ds.DataSourceContextHolder;
import com.nyd.capital.dao.mappers.UserPocketMapper;
import com.nyd.capital.entity.UserPocket;
import com.nyd.capital.service.UserPocketService;
import com.nyd.capital.service.aspect.RoutingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liuqiu
 */
@Service
public class UserPocketServiceImpl implements UserPocketService {

    private static final Logger logger = LoggerFactory.getLogger(UserPocketServiceImpl.class);
    @Autowired
    private UserPocketMapper userPocketMapper;
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public void savePassword(UserPocket userPocket) {
        try {
            userPocketMapper.savePassword(userPocket);
            logger.info(userPocket.toString()+"保存成功");
        }catch (Exception e){
            logger.error(userPocket.toString()+"保存失败",e);
        }
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public void update(UserPocket userPocket) {
        try {
            userPocketMapper.updateUserPocket(userPocket);
            logger.info(userPocket.toString()+"保存成功");
        }catch (Exception e){
            logger.error(userPocket.toString()+"保存失败",e);
        }
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public String selectPasswordByUserId(String userId) {
        try {
            UserPocket password = userPocketMapper.selectPasswordByUserId(userId);
            if (password == null){
                return null;
            }
            logger.info(userId+"查询成功");
            return password.getPassword();
        }catch (Exception e){
            logger.error(userId+"查询失败",e);
            return null;
        }
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_USER)
    @Override
    public UserPocket selectPocketUserByUserId(String userId) {
        try {
            List<UserPocket> list = userPocketMapper.getUserPocketByUserId(userId);
            if (list.size() == 0){
                return null;
            }
            logger.info(userId+"查询成功");
            return list.get(0);
        }catch (Exception e){
            logger.error(userId+"查询失败",e);
            return null;
        }
    }

    @Override
    public List<UserPocket> selectPocketByStage(Integer code) {
        try {
            List<UserPocket> list = userPocketMapper.selectPocketByStage(code);
            logger.info("查询成功");
            return list;
        }catch (Exception e){
            logger.error("查询失败",e);
            return null;
        }
    }


}
