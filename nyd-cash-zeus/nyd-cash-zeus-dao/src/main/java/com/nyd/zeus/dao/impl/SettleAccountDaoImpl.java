package com.nyd.zeus.dao.impl;

import com.nyd.zeus.dao.SettleAccountDao;
import com.nyd.zeus.dao.mapper.SettleAccountMapper;
import com.nyd.zeus.model.SettleAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SettleAccountDaoImpl implements SettleAccountDao {
    @Autowired
    private SettleAccountMapper settleAccountMapper;
    @Override
    public void save(SettleAccount settleAccount) throws Exception{
        settleAccountMapper.save(settleAccount);
    }
}
