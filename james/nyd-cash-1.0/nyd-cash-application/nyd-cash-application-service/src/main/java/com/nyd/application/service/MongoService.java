package com.nyd.application.service;

import com.nyd.application.model.mongo.AddressBook;
import com.nyd.application.model.mongo.CallInfo;
import com.nyd.application.model.mongo.SmsInfo;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;
import java.util.Map;

/**
 * Created by Dengw on 2017/11/23
 */
public interface MongoService {
    /**
     * 保存通讯录
     */
    ResponseData saveAddressBook(List<AddressBook> list);

    /**
     * 保存通话记录
     */
    ResponseData saveCallInfo(List<CallInfo> list);

    /**
     * 保存短信
     */
    ResponseData saveSmsInfo(List<SmsInfo> list);

    /**
     * 保存设备信息
     */
    ResponseData saveDeviceInfo(Map<String, Object> map);

    /**
     * 保存埋点信息
     */
    ResponseData saveBuriedInfo(List<Map<String, Object>> list);

    /**
     * 保存app安装情况信息
     */
    ResponseData saveAppInfo(Map<String, Object> map);
}
