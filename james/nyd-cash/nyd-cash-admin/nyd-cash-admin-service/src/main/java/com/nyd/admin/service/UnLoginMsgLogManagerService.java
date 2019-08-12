package com.nyd.admin.service;


import com.nyd.admin.model.UnLoginMsgLogInfo;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 18:27 2018/10/1
 */
public interface UnLoginMsgLogManagerService {


    void saveUnLoginMsgLog(UnLoginMsgLogInfo unLoginMsgLogInfo);

    void updateUnLoginMsgLog(UnLoginMsgLogInfo unLoginMsgLogInfo);

    UnLoginMsgLogInfo getByMobile(String mobile);
}
