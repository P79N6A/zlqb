package com.nyd.user.dao;

import java.util.List;

import com.nyd.user.entity.UserBind;


public interface UserBindDao {


    List<UserBind> selectByUserId(String userid, String cardno) throws Exception;

    void save(UserBind user) throws Exception;
    
    void update(UserBind user);
}

