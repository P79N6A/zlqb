package com.nyd.user.dao.mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nyd.user.entity.Account;
import com.nyd.user.model.AccountCache;

/**
 * Created by hwei on 2017/12/8.
 */
public interface AccountMapper {

    Set<String> queryAccountByAccountList(List<String> accountNumbers);
    
    List<AccountCache> getAccountList(Map<String, Object> params);
    
    List<Account> selectFaceFlagInfo(Map<String, Object> params) throws Exception;
    
    void updateByUserId(Account account);
}
