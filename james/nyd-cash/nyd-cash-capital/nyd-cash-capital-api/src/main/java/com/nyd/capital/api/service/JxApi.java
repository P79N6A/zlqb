package com.nyd.capital.api.service;

import com.nyd.capital.entity.UserJx;
import com.nyd.capital.model.jx.JxQueryPushStatusRequest;
import com.tasfe.framework.support.model.ResponseData;

/**
 * @author liuqiu
 */
public interface JxApi {
    /**
     * 1.推单查询
     */
    ResponseData queryPushStatus(JxQueryPushStatusRequest jxQueryPushStatusRequest);


    /**
     *
     * 根据用户userID去查找四要素
     */
    ResponseData getInformationByUserId(String userId);

    /**
     *
     * 根据用户userID去查找四要素
     */
    ResponseData getUserJxByUserId(String userId);

    void updateUserJx(UserJx userJx);
}
