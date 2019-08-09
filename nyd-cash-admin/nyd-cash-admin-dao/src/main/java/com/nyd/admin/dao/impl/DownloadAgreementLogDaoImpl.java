package com.nyd.admin.dao.impl;

import com.nyd.admin.dao.DownloadAgreementLogDao;
import com.nyd.admin.entity.DownloadAgreementLog;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class DownloadAgreementLogDaoImpl implements DownloadAgreementLogDao {

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(DownloadAgreementLog downloadAgreementLog) throws Exception {
        crudTemplate.save(downloadAgreementLog);
    }
}
