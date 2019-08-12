package com.nyd.admin.service.impl;

import com.nyd.admin.api.UserMessageContract;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.UserMapper;
import com.nyd.admin.dao.mapper.UserRoleRelMapper;
import com.nyd.admin.entity.power.User;
import com.nyd.admin.entity.power.UserRoleRel;
import com.nyd.admin.model.power.dto.UserDto;
import com.nyd.admin.model.power.dto.UserRoleRelDto;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yifeng.qiu
 * @date 2018/4/24
 */
@Service(value = "userMessageContract")
public class UserMessageContractImpl implements UserMessageContract {
    private static Logger LOGGER = LoggerFactory.getLogger(UserMessageContractImpl.class);
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleRelMapper userRoleRelMapper;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData<List<UserDto>> queryUserInfoByRoleId(Integer userType, Integer roleId) {
        ResponseData responseData = ResponseData.success();
        List<UserDto> userVoList = new ArrayList<>();
        try {
            List<User> userList = userMapper.queryUserInfoByRoleId(userType, roleId);

            if (CollectionUtils.isNotEmpty(userList)) {
                userList.forEach(user -> {
                    UserDto userDto = new UserDto();
                    BeanUtils.copyProperties(user, userDto);
                    userDto.setId(user.getId().intValue());
                    userVoList.add(userDto);
                });
            }
            responseData.setData(userVoList);
        } catch (Exception e) {
            LOGGER.error("queryUserInfoByRoleId has exception", e);
            responseData = ResponseData.error("服务器开小差了");
        }
        return responseData;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData<List<UserRoleRelDto>> getUserRoleInfoByAccountNo(String accountNo) {
        ResponseData responseData = ResponseData.success();

        List<UserRoleRelDto> roleVoList = new ArrayList<>();
        try {
            List<UserRoleRel> userRoleRelList = userRoleRelMapper.getUserRoleInfoByAccountNo(accountNo);

            if (CollectionUtils.isNotEmpty(userRoleRelList)) {
                userRoleRelList.forEach(userRoleRel -> {
                    UserRoleRelDto userRoleRelDto = new UserRoleRelDto();
                    BeanUtils.copyProperties(userRoleRel, userRoleRelDto);
                    roleVoList.add(userRoleRelDto);
                });
            }
            responseData.setData(roleVoList);
        } catch (Exception e) {
            LOGGER.error("getUserRoleInfoByAccountNo has exception", e);
            responseData = ResponseData.error("服务器开小差了");
        }
        return responseData;
    }
}
