package com.nyd.user.dao;

import com.nyd.user.model.UserKzjrInfo;

import java.util.List;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 12:19 2018/6/21
 */
public interface UserKzjrDao {

    List<UserKzjrInfo> getByUserId(String userId) throws Exception;
}
