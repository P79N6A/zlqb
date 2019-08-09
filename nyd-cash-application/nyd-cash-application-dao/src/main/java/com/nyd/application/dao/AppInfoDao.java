package com.nyd.application.dao;


import com.nyd.application.entity.AppInfo;
import com.nyd.application.entity.Version;

import java.util.List;

/**
 * Created by Dengw on 2017/11/27
 */
public interface AppInfoDao {
    List<AppInfo> getAppInfo(String os, String appName) throws Exception;

}
