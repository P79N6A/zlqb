package com.nyd.user.api;

import com.nyd.user.entity.UserJx;
import com.tasfe.framework.support.model.ResponseData;


/**
 * @author liuqiu
 */
public interface UserJxContract {
    /**
     * 根据用户id查询即信信息
     * @param userId
     * @return
     */
    ResponseData getUserJxByUserId(String userId);

    /**
     * 更新即信用户
     * @param userJx
     * @return
     */
    ResponseData updateUserJx(UserJx userJx);
}
