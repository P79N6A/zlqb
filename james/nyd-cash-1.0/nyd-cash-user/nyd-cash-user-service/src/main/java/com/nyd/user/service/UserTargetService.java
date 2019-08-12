package com.nyd.user.service;

import com.nyd.user.entity.UserTarget;

import java.util.List;
import java.util.Map;

public interface UserTargetService {
    /**
     * 根据userId查找打标对象
     * @param mobile
     * @return
     */
    List<UserTarget> findByMobile(String mobile);

    /**
     * 更新打标对象
     * @param userTarget
     */
    void updateUserTarget(UserTarget userTarget);

    /**
     * 往打标表插入记录
     * @param userTarget
     */
    void save(UserTarget userTarget);

    /**
     * 手机号根据规则撞库
     * @param params
     * @return
     */
    List toHitByRuleCodeAndMobile(Map<String, String> params);
    
    
    List<UserTarget> getUserTarget(Map<String, String> params);
}
