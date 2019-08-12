package com.nyd.user.service;

import com.nyd.user.entity.HitRuleConfig;
import com.nyd.user.model.request.HitRuleConfigRequest;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

public interface HitRuleConfigService {

    /**
     * 通过AppName和source获取撞库规则
     */
    ResponseData<List<HitRuleConfig>> findByAppNameAndSource(HitRuleConfigRequest hitRuleConfigRequest);
}
