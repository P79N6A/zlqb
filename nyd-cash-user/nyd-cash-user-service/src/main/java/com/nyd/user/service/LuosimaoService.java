package com.nyd.user.service;

import com.nyd.user.model.AccountInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by zhangp on 2018/8/8.
 */
public interface LuosimaoService {
    /**
     * 校验人机结果集
     * @param accountInfo
     * @return
     */
    ResponseData verifyResult(AccountInfo accountInfo);
}
