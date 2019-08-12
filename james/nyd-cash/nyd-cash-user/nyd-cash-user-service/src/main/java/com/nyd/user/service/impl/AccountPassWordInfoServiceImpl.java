package com.nyd.user.service.impl;

import com.nyd.user.dao.PasswordDao;
import com.nyd.user.entity.Password;
import com.nyd.user.service.AccountPassWordInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountPassWordInfoServiceImpl implements AccountPassWordInfoService {

    @Autowired
    private PasswordDao passwordDao;

    @Override
    public void updateAccountPassword(Password password) throws Exception {
        passwordDao.updatPassword(password);

    }
    @Override
    public void updateAccountPassword(Password password,String accountNumber) throws Exception {
    	passwordDao.updatPassword(password,accountNumber);
    	
    }

    @Override
    public void saveAccountPassword(Password password) throws Exception {
        passwordDao.savePassword(password);
    }
}
