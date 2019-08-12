package com.nyd.user.service;

import com.nyd.user.entity.User;
import com.nyd.user.model.UserInfo;

import java.util.List;

public interface UserService {
    List<UserInfo> findByUserId(String userId) throws Exception;

    void updateUser(UserInfo userInfo) throws Exception;

    void saveUser(UserInfo userInfo) throws Exception;

    List<UserInfo> findByIdNumber(String idNumber) throws Exception;
}
