package com.nyd.user.service;

import com.nyd.user.entity.Password;

public interface AccountPassWordInfoService {
    void updateAccountPassword(Password password) throws Exception;
    
    void updateAccountPassword(Password password,String accountNumber) throws Exception;

    void saveAccountPassword(Password password) throws Exception;
}
