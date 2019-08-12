package com.nyd.user.service;

import com.nyd.user.model.IdentityInfo;
import com.nyd.user.model.vo.IdentityVo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by Dengw on 17/11/3.
 */
public interface IdentityInfoService {

    ResponseData saveUserInfo(IdentityInfo identityInfo) throws Exception;

    ResponseData<IdentityVo> getIdentityInfo(String userId);
}
