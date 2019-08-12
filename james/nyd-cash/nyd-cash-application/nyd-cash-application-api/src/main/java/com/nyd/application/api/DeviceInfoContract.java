package com.nyd.application.api;

import com.nyd.application.model.mongo.AddressBook;

import java.util.List;

/**
 * 获取用户设置信息
 * @author shaoqing.liu
 * @date 2018/7/11 15:55
 */
public interface DeviceInfoContract {

    /**
     * 获取身份证对应照片
     * @param userId
     * @param type 1：正面照 2 反面照 3 活体照片
     * @return
     */
    String getAttachmentModelUrl(String userId, String type);
}
