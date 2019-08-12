package com.nyd.user.dao;

import com.nyd.user.entity.Account;
import com.nyd.user.model.AccountInfo;
import com.nyd.user.model.dto.AccountDto;

import java.util.List;

/**
 * Created by hwei on 2017/11/2.
 */
public interface AccountDao  {

    List<Account> getAccountsByAccountNumber(String accountNumber) throws Exception;
    
    List<Account> getAccountsByUserId(String userId) throws Exception;

    void save(Account account) throws Exception;

    void updateAccountByAccountNumber(Account account) throws Exception;
    void updateAccountByUserId(Account account) throws Exception;

    List<AccountDto> getAccountByuserId(String userId) throws Exception;

    List<AccountDto> getAccountByIbankUserId(String iBankUserId) throws Exception;

    List<AccountDto> queryBalance(String accountNumber) throws Exception;


    List<AccountDto> findByAccountNumber(String accountNumber) throws Exception;

    void updateAccountTest(String accountNumber,String updateMobile) throws Exception;
}
