package com.nyd.user.dao.impl;


import com.nyd.user.dao.UnbindUserDao;
import com.nyd.user.entity.UnbindUser;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by hwei on 2018/11/12.
 */
@Repository
public class UnbindUserDaoImpl implements UnbindUserDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(UnbindUser unbindUser) throws Exception {
        crudTemplate.save(unbindUser);
    }

    @Override
    public void updateByOriginMobile(String originMobile,UnbindUser unbindUser) throws Exception {
        Criteria criteria = Criteria.from(UnbindUser.class)
                .whioutId()
                .where()
                .and("origin_mobile", Operator.EQ,originMobile)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        unbindUser.setUpdateTime(null);
        unbindUser.setCreateTime(null);
        crudTemplate.update(unbindUser,criteria);
    }

    @Override
    public List<UnbindUser> getUnbindUserByOriginMobile(String originMobile) throws Exception {
        Criteria criteria = Criteria.from(UnbindUser.class)
                .where()
                .and("origin_mobile",Operator.EQ,originMobile)
                .and("delete_flag",Operator.EQ,0)
                .endWhere()
                .orderBy("create_time desc");
        UnbindUser unbindUser = new UnbindUser();
        return crudTemplate.find(unbindUser,criteria);
    }
}
