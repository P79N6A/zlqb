package com.nyd.admin.dao;

import com.nyd.admin.entity.UnLoginMsgLog;
import com.nyd.admin.model.UnLoginMsgLogInfo;

import java.util.List;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 14:36 2018/10/1
 */
public interface UnLoginMsgLogDao {

    void save(UnLoginMsgLog unLoginMsgLog) throws Exception;

    void update(UnLoginMsgLogInfo unLoginMsgLogInfo) throws Exception;

    List<UnLoginMsgLogInfo> getByMobile(String mobile) throws Exception;
}
