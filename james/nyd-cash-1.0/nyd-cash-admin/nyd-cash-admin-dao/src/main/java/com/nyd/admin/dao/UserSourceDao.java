package com.nyd.admin.dao;


import com.nyd.admin.model.UserSourceInfo;

import java.util.List;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 18:28 2018/10/1
 */
public interface UserSourceDao {

    List<UserSourceInfo> findByMobile(String mobile) throws Exception;
}
