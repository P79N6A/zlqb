package com.nyd.capital.dao.impl;

import com.nyd.capital.dao.LogKzjrDao;
import com.nyd.capital.entity.LogKzjr;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Cong Yuxiang
 * 2018/5/2
 **/
@Repository
public class LogKzjrDaoImpl implements LogKzjrDao{
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;
    @Override
    public void save(LogKzjr logKzjr) throws Exception {
        crudTemplate.save(logKzjr);
    }
}
