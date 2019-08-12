package com.nyd.user.service;

import com.nyd.user.entity.Account;
import com.nyd.user.model.AccountCache;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface AccountInfoService {

    List<Account> findByAccountNumber(String accountNumber) throws Exception;
    
    List<Account> findByUserId(String userId) throws Exception;

    void saveYmtAccount(Account account) throws Exception;

    void updateAccount(Account account) throws Exception;
    
    void updateAccountByUserId(Account account) throws Exception;
    
	AccountCache getAccountCacheFromRedis(String accountNumber);
	
	void saveAccountCacheInRedis(AccountCache cache);
	
	void convertAccountInRedis(Map<String, Object> params);
	
	void saveAccountInRedis(Account account, String password);
	
	void saveAccountInRedis(AccountCache cache);
	
	List<AccountCache> getAccountList(Map<String, Object> params);
	
	String getLoginPrefix(String accountNum);
	
	void removeAccountCacheFromRedis(String accountNumber);
}
