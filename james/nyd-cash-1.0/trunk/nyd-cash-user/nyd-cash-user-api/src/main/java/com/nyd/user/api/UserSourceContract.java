package com.nyd.user.api;

import com.tasfe.framework.support.model.ResponseData;

/**
 * @author liuqiu
 */
public interface UserSourceContract {

    /**
     * 通过手机号查询用户注册来源
     * @param mobile
     * @return
     */
    ResponseData selectUserSourceByMobile(String mobile);
}
