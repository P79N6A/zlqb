package com.nyd.user.dao;

import com.nyd.user.entity.UserJx;
import com.nyd.user.model.UserInfo;

import java.util.List;

/**
 * @author liuqiu
 */
public interface UserJxDao {

    /**
     * 通过userId查询用户即信信息
     * @param userId
     * @return
     * @throws Exception
     */
    List<UserJx> getUserJxByUserId(String userId) throws Exception;


    void updateUserJx(UserJx userJx) throws Exception;
}
