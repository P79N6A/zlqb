package com.nyd.admin.dao.impl;

import com.nyd.admin.dao.FundInfoDao;
import com.nyd.admin.entity.FundInfo;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by hwei on 2018/1/2.
 */
@Repository
public class FundInfoDaoImpl implements FundInfoDao{
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void saveFundInfo(FundInfo fundInfo) throws Exception {
        crudTemplate.save(fundInfo);
    }
}
