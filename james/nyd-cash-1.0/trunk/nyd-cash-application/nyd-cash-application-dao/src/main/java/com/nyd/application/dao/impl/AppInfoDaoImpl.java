package com.nyd.application.dao.impl;

import com.nyd.application.dao.AppInfoDao;
import com.nyd.application.dao.VersionDao;
import com.nyd.application.entity.AppInfo;
import com.nyd.application.entity.Version;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dengw on 2017/11/27
 */
@Repository
public class AppInfoDaoImpl implements AppInfoDao {

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public List<AppInfo> getAppInfo(String os,String appName) throws Exception {
        AppInfo appInfo = new AppInfo();
        Criteria criteria = Criteria.from(AppInfo.class)
                .where()
                .and("os",Operator.EQ,os)
                .and("app_name",Operator.EQ,appName)
                .endWhere()
                .orderBy("create_time desc")
                .limit(0,1);
        return crudTemplate.find(appInfo,criteria);
    }
}
