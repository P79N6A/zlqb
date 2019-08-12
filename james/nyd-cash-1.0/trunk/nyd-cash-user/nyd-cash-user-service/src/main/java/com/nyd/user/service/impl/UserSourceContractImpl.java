package com.nyd.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.nyd.user.api.UserSourceContract;
import com.nyd.user.dao.mapper.LoginLogMapper;
import com.nyd.user.dao.mapper.UserSourceMapper;
import com.nyd.user.entity.LoginLog;
import com.nyd.user.entity.UserSource;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liuqiu
 */
@Service(value = "userSourceContract")
public class UserSourceContractImpl implements UserSourceContract {

    private static Logger logger = LoggerFactory.getLogger(UserSourceContractImpl.class);

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Override
    public ResponseData selectUserSourceByMobile(String mobile) {
        ResponseData responseData = ResponseData.success();
        try {
            if (StringUtils.isBlank(mobile)) {
                logger.error("手机号为空");
                return ResponseData.error("手机号为空");
            }
            logger.info("开始查询用户最后登录app,手机号为:" + mobile);
            LoginLog loginLog = loginLogMapper.selectSource(mobile);
            responseData.setData(loginLog);

        }catch (Exception e){
            logger.error("查询用户最后登录app发生异常");
            return ResponseData.error("查询用户最后登录app发生异常");
        }
        logger.info("查询用户最后登录app的结果为:" + JSON.toJSONString(responseData));
        return responseData;
    }
}
