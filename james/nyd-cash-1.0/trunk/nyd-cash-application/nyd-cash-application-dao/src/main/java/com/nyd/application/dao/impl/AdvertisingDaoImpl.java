package com.nyd.application.dao.impl;

import com.nyd.application.dao.AdvertisingDao;
import com.nyd.application.entity.Advertising;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dengw on 2017/11/28
 */
@Repository
public class AdvertisingDaoImpl implements AdvertisingDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public List<Advertising> getAdvertisings() throws Exception {
        Advertising advertising = new Advertising();
        advertising.setDeleteFlag(0);
        Criteria criteria = Criteria.from(Advertising.class)
                .orderBy("create_time desc");
        return crudTemplate.find(advertising,criteria);
    }
}
