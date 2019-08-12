package com.nyd.admin.dao.impl;

import com.nyd.admin.dao.UserSourceDao;
import com.nyd.admin.entity.UserSource;
import com.nyd.admin.model.UserSourceInfo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 18:28 2018/10/1
 */
@Repository
public class UserSourceDaoImpl implements UserSourceDao {
    @Autowired
    private CrudTemplate crudTemplate;

    @Override
    public List<UserSourceInfo> findByMobile(String mobile) throws Exception{
        Criteria criteria =Criteria.from(UserSource.class)
                .where()
                .and("account_number",Operator.EQ,mobile)
                .and("delete_flag", Operator.EQ,0)
                .endWhere();
        UserSourceInfo userSourceInfo = new UserSourceInfo();
        return crudTemplate.find(userSourceInfo,criteria);
    }
}
