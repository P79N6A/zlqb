package com.nyd.user.dao;

import com.nyd.user.entity.LoginLog;

/**
 * Created by hwei on 2018/5/8.
 */
public interface LoginLogDao {
    void save(LoginLog loginLog) throws Exception;
}
