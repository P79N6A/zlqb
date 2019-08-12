package com.nyd.pay.dao;

import com.nyd.pay.entity.YsbUser;

import java.util.List;


public interface YsbUserDao {


    List<YsbUser> selectByUserId(String userid, String cardno) throws Exception;

    void save(YsbUser user) throws Exception;
}
