package com.nyd.user.dao;

import com.nyd.user.entity.Password;
import com.nyd.user.model.AccountInfo;

import java.util.List;

/**
 * Created by Dengw on 2017/11/9
 */
public interface PasswordDao {
    void save(AccountInfo accountInfo) throws Exception;

    void update(AccountInfo accountInfo) throws Exception;
    
    void updatPassword(Password password,String accountNumber) throws Exception;

    List<Password> getPasswordsByAccountNumber(String accountId) throws Exception;

    void updatPassword(Password password) throws Exception;

    void savePassword(Password password) throws Exception;

    void updatePassWordTest(String accountNumber,String updateMobile) throws Exception;
}
