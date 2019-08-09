package com.nyd.application.dao.impl;

import com.nyd.application.dao.VersionDao;
import com.nyd.application.entity.Version;
import com.nyd.application.model.request.VersionModel;
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
public class VersionDaoImpl implements VersionDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public List<Version> getVersion(String os,String appName) throws Exception {
        Version version = new Version();
        Criteria criteria = Criteria.from(Version.class)
                .where()
                .and("os",Operator.EQ,os)
                .and("delete_flag",Operator.EQ,0)
                .and("app_name",Operator.EQ,appName)
                .endWhere()
                .orderBy("create_time desc")
                .limit(0,1);
        return crudTemplate.find(version,criteria);
    }
}
