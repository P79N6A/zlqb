package com.nyd.user.service;

import com.nyd.user.model.request.HitRequest;
import com.tasfe.framework.support.model.ResponseData;

public interface NydHitLogicService {

    /**
     * 手机号、APP名称、来源 去撞库
     */
    ResponseData HitByMobileAndRule(HitRequest request);
}
