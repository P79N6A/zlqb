package com.nyd.application.service.impl;

import com.nyd.application.dao.AppInfoDao;
import com.nyd.application.dao.VersionDao;
import com.nyd.application.entity.AppInfo;
import com.nyd.application.entity.Version;
import com.nyd.application.model.request.VersionModel;
import com.nyd.application.service.AppInfoService;
import com.nyd.application.service.VersionService;
import com.nyd.application.service.consts.ApplicationConsts;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Dengw on 2017/11/27
 */
@Service
public class AppInfoServiceImpl implements AppInfoService {
    private static Logger LOGGER = LoggerFactory.getLogger(AppInfoServiceImpl.class);

    @Autowired
    AppInfoDao appInfoDao;


    @Override
    public ResponseData getAppInfo(Map<String, String> map) {
        ResponseData responseData = ResponseData.success();
        LOGGER.info("begin to get appInfo !");
        try {
            String os = map.get("os");
            String appName = map.get("appName");
            List<AppInfo> list = new ArrayList<>();
            if (StringUtils.isNotBlank(appName)){
                list = appInfoDao.getAppInfo(os,appName);
            }else {
                list = appInfoDao.getAppInfo(os,"xxd");
            }
            if(list != null && list.size()>0){
                AppInfo appInfo = list.get(0);
                responseData.setData(appInfo);
                LOGGER.info("get appInfo success !");
            }
        } catch (Exception e) {
            LOGGER.error("get appInfo info error ! os = "+ map.get("os"),e);
            return ResponseData.error(ApplicationConsts.ERROR_MSG);
        }
        return responseData;
    }
}
