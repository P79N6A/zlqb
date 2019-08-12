package com.nyd.user.dao.impl;


import com.nyd.user.dao.UserSourceDao;
import com.nyd.user.entity.UserSource;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by hwei on 2018/11/26.
 */
@Repository
public class UserSourceDaoImpl implements UserSourceDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;


    @Override
    public void updateUserSourceTest(String accountNumber, String updateMobile) throws Exception {
        Criteria criteria = Criteria.from(UserSource.class)
                .whioutId()
                .where()
                .and("account_number", Operator.EQ,accountNumber)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        UserSource userSource = new UserSource();
        userSource.setAccountNumber(updateMobile);
        crudTemplate.update(userSource,criteria);
    }
}
