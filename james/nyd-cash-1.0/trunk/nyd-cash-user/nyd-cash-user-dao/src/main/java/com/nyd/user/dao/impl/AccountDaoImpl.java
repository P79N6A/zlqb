package com.nyd.user.dao.impl;

import com.nyd.user.dao.AccountDao;
import com.nyd.user.entity.Account;
import com.nyd.user.model.dto.AccountDto;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by hwei on 2017/11/2.
 */
@Repository
public class AccountDaoImpl implements AccountDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public List<Account> getAccountsByAccountNumber(String accountNumber) throws Exception {
        Account account = new Account();
        Criteria criteria = Criteria.from(Account.class)
                .where()
                .and("account_number",Operator.EQ,accountNumber)
                .and("delete_flag",Operator.EQ,0)
                .endWhere()
                .orderBy("create_time desc");
        List<Account> accountList =crudTemplate.find(account,criteria);
        return accountList;
    }
    @Override
    public List<Account> getAccountsByUserId(String userId) throws Exception {
    	Account account = new Account();
    	Criteria criteria = Criteria.from(Account.class)
    			.where()
    			.and("user_id",Operator.EQ,userId)
    			.and("delete_flag",Operator.EQ,0)
    			.endWhere()
    			.orderBy("create_time desc");
    	List<Account> accountList =crudTemplate.find(account,criteria);
    	return accountList;
    }

    @Override
    public void save(Account account) throws Exception {
        crudTemplate.save(account);
    }

    @Override
    public void updateAccountByAccountNumber(Account account) throws Exception {
        Criteria criteria = Criteria.from(Account.class)
                .whioutId()
                .where()
                .and("account_number",Operator.EQ,account.getAccountNumber())
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        account.setAccountNumber(null);
        crudTemplate.update(account,criteria);
    }
    @Override
    public void updateAccountByUserId(Account account) throws Exception {
    	Criteria criteria = Criteria.from(Account.class)
    			.whioutId()
    			.where()
    			.and("user_id",Operator.EQ,account.getUserId())
    			.and("delete_flag",Operator.EQ,0)
    			.endWhere();
    	account.setUserId(null);
    	crudTemplate.update(account,criteria);
    }

    @Override
    public List<AccountDto> getAccountByuserId(String userId) throws Exception {
        AccountDto accountDto = new AccountDto();
        Criteria criteria = Criteria.from(Account.class)
                .where()
                .and("user_id",Operator.EQ,userId)
                .and("delete_flag",Operator.EQ,0)
                .endWhere()
                .orderBy("create_time desc");
        List<AccountDto> accountList =crudTemplate.find(accountDto,criteria);
        return accountList;
    }

    @Override
    public List<AccountDto> getAccountByIbankUserId(String iBankUserId) throws Exception {
        AccountDto accountDto = new AccountDto();
        Criteria criteria = Criteria.from(Account.class)
                .where()
                .and("i_bank_user_id",Operator.EQ,iBankUserId)
                .and("delete_flag",Operator.EQ,0)
                .endWhere()
                .orderBy("create_time desc");
        List<AccountDto> accountList =crudTemplate.find(accountDto,criteria);
        return accountList;
    }

    @Override
    public List<AccountDto> queryBalance(String accountNumber) throws Exception{
        AccountDto accountDto = new AccountDto();
        Criteria criteria = Criteria.from(Account.class)
                .where()
                .and("account_number",Operator.EQ,accountNumber)
                .and("delete_flag",Operator.EQ,0)
                .endWhere()
                .orderBy("create_time desc");
        List<AccountDto> accountList =crudTemplate.find(accountDto,criteria);
        return accountList;
    }

    @Override
    public List<AccountDto> findByAccountNumber(String accountNumber) throws Exception{
        AccountDto accountDto = new AccountDto();
        Criteria criteria = Criteria.from(Account.class)
                .where()
                .and("account_number",Operator.EQ,accountNumber)
                .and("delete_flag",Operator.EQ,0)
                .endWhere()
                .orderBy("create_time desc");
        List<AccountDto> accountList =crudTemplate.find(accountDto,criteria);
        return accountList;
    }

    @Override
    public void updateAccountTest(String accountNumber, String updateMobile) throws Exception {
        Criteria criteria = Criteria.from(Account.class)
                .whioutId()
                .where()
                .and("account_number",Operator.EQ,accountNumber)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        Account account = new Account();
        account.setAccountNumber(updateMobile);
        crudTemplate.update(account,criteria);
    }
}
