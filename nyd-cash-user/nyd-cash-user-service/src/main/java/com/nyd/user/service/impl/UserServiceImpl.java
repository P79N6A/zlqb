package com.nyd.user.service.impl;

import com.nyd.user.dao.UserDao;
import com.nyd.user.entity.User;
import com.nyd.user.model.UserInfo;
import com.nyd.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public List<UserInfo> findByUserId(String userId) throws Exception {
        return userDao.getUsersByUserId(userId);
    }

    @Override
    public void updateUser(UserInfo userInfo) throws Exception {
        userDao.update(userInfo);
    }

    @Override
    public void saveUser(UserInfo userInfo) throws Exception {
        userDao.save(userInfo);
    }

    @Override
    public List<UserInfo> findByIdNumber(String idNumber) throws Exception {
        return userDao.getUsersByIdNumber(idNumber);
    }
}
