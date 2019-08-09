package com.nyd.user.dao.impl;

import com.nyd.user.dao.LoginLogDao;
import com.nyd.user.entity.LoginLog;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by hwei on 2018/5/8.
 */
@Repository
public class LoginLogDaoImpl implements LoginLogDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(LoginLog loginLog) throws Exception {
        crudTemplate.save(loginLog);
    }
}
