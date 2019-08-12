package com.nyd.application.service;

import com.tasfe.framework.support.model.ResponseData;

import java.util.Map;

/**
 * Created by Dengw on 2017/11/27
 */
public interface AppInfoService {
    ResponseData getAppInfo(Map<String, String> map);
}
