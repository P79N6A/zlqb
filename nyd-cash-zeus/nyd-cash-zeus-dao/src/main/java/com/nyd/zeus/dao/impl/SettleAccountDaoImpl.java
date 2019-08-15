package com.nyd.zeus.dao.impl;

import com.nyd.zeus.dao.SettleAccountDao;
import com.nyd.zeus.dao.mapper.SettleAccountMapper;
import com.nyd.zeus.model.SettleAccount;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class SettleAccountDaoImpl implements SettleAccountDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;
    @Override
    public void save(SettleAccount settleAccount) throws Exception{
        crudTemplate.save(settleAccount);
    }
}
