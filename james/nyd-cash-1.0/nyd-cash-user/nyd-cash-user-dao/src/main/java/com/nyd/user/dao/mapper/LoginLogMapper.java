package com.nyd.user.dao.mapper;

import com.nyd.user.entity.LoginLog;
import com.nyd.user.entity.UserSource;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author liuqiu
 *
 **/
@Mapper
public interface LoginLogMapper {

    /**
     * 查询用户最后登录app
     * @param mobile
     * @return
     */
    LoginLog selectSource(String mobile);
}
