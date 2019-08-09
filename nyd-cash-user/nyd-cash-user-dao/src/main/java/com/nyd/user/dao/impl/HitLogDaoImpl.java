package com.nyd.user.dao.impl;

import com.nyd.user.dao.HitLogDao;
import com.nyd.user.entity.HitLog;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class HitLogDaoImpl implements HitLogDao {

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(HitLog hitLog) throws Exception {
        crudTemplate.save(hitLog);
    }
}
