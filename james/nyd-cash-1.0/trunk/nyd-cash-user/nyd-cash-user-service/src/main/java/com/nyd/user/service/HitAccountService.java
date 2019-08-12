package com.nyd.user.service;

import com.nyd.user.model.HitAccountInfo;
import com.tasfe.framework.support.model.ResponseData;


/**
 * @Author: zhangp
 * @Description:
 * @Date: 11:35 2018/9/6
 */
public interface HitAccountService {

    /**
     * 保存撞库信息
     * @param hitAccountInfo
     * @return
     */
    ResponseData saveHitAccount(HitAccountInfo hitAccountInfo);

    /**
     * 根据类型查找撞库信息
     * @param hitAccountInfo
     * @param type 默认/0 根据手机号  1:根据sha256加密串查找 2：根据md5查找
     * @return
     */
    ResponseData<HitAccountInfo> getByStr(HitAccountInfo hitAccountInfo , Integer type);
}
