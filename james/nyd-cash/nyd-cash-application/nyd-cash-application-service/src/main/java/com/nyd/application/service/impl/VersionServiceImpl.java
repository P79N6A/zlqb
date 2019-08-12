package com.nyd.application.service.impl;

import com.nyd.application.dao.VersionDao;
import com.nyd.application.entity.Version;
import com.nyd.application.model.request.VersionModel;
import com.nyd.application.service.VersionService;
import com.nyd.application.service.consts.ApplicationConsts;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Dengw on 2017/11/27
 */
@Service
public class VersionServiceImpl implements VersionService {
    private static Logger LOGGER = LoggerFactory.getLogger(VersionServiceImpl.class);

    @Autowired
    VersionDao versionDao;


    @Override
    public ResponseData getVersionInfo(Map<String, String> map) {
        ResponseData responseData = ResponseData.success();
        LOGGER.info("begin to get versionInfo !");
        try {
            String os = map.get("os");
            String appName = map.get("appName");
            VersionModel model = new VersionModel();
            List<Version> list = null;
            if (StringUtils.isNotBlank(appName)){
                list = versionDao.getVersion(os,appName);
            }else {
                list = versionDao.getVersion(os,"nyd");
            }
            if(list != null && list.size()>0){
                int ver = 0;
                Version version = list.get(0);
                BeanUtils.copyProperties(version,model);
                if("ios".equals(os)){
                    String versionName = map.get("versionName").replace(".","");
                    ver = Integer.valueOf(versionName);
                    String versionNameDB = version.getVersionName().replace(".","");
                    if(Integer.valueOf(versionNameDB) > ver){
                        model.setUpdated(1);
                    }
                }else if("android".equals(os)){
                    ver = Integer.valueOf(map.get("version"));
                    if(version.getVersion() > ver){
                        model.setUpdated(1);
                    }
                    model.setForc(model.getForce());
                }
                responseData.setData(model);
                LOGGER.info("get versionInfo success !");
            }
        } catch (Exception e) {
            LOGGER.error("get version info error ! os = "+ map.get("os"),e);
            return ResponseData.error(ApplicationConsts.ERROR_MSG);
        }
        return responseData;
    }
}
