package com.nyd.admin.service;


import com.nyd.admin.model.UserSourceInfo;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 18:25 2018/10/1
 */
public interface UserSourceManagerService {

    UserSourceInfo getByMobile(String mobile);
}
