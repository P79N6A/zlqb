package com.nyd.user.dao.impl;

import com.nyd.user.dao.BankDao;
import com.nyd.user.entity.Account;
import com.nyd.user.entity.Bank;
import com.nyd.user.model.BankInfo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dengw on 17/11/1.
 */
@Repository
public class BankDaoImpl implements BankDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(BankInfo bankInfo) throws Exception {
        Bank bank = new Bank();
        BeanUtils.copyProperties(bankInfo,bank);
        crudTemplate.save(bank);
    }
    
    @Override
    public void updateBankByBankAccount(Bank bank) throws Exception {
        Criteria criteria = Criteria.from(Bank.class)
                .whioutId()
                .where()
                .and("bank_account",Operator.EQ,bank.getBankAccount())
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        bank.setBankAccount(null);
        crudTemplate.update(bank,criteria);
    }

    @Override
    public List<BankInfo> getBanksByUserId(String userId) throws Exception {
        BankInfo bankInfo = new BankInfo();
        bankInfo.setUserId(userId);
        return getBanks(bankInfo);
    }
    
    @Override
    public List<BankInfo> getBanksByUserIdAndSource(String userId,Integer source) throws Exception {
        BankInfo bankInfo = new BankInfo();
        bankInfo.setUserId(userId);
        bankInfo.setSoure(source);
        return getBanks(bankInfo);
    }

    @Override
    public List<BankInfo> getBanksByBankAccount(String bankAccount) throws Exception {
        BankInfo bankInfo = new BankInfo();
        bankInfo.setBankAccount(bankAccount);
        bankInfo.setSoure(1);
        return getBanks(bankInfo);
    }



    private List<BankInfo> getBanks(BankInfo bankInfo) throws Exception {
        Criteria criteria = Criteria.from(Bank.class)
                .where()
                .and("delete_flag",Operator.EQ,0)
                .endWhere()
                .orderBy("create_time desc")
                .limit(0,1000);
        return crudTemplate.find(bankInfo,criteria);
    }

    @Override
    public void saveBank(Bank bank) throws Exception {
        crudTemplate.save(bank);
    }


}
