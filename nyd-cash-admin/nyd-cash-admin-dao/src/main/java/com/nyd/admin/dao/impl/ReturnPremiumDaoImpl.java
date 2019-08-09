package com.nyd.admin.dao.impl;

import com.nyd.admin.dao.ReturnPremiumDao;
import com.nyd.admin.entity.ReturnPremium;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class ReturnPremiumDaoImpl implements ReturnPremiumDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(ReturnPremium returnPremium) throws Exception {
        crudTemplate.save(returnPremium);
    }
}
