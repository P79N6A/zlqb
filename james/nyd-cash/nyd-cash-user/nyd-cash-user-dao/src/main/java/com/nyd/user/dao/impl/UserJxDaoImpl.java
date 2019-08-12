package com.nyd.user.dao.impl;

import com.nyd.user.dao.UserJxDao;
import com.nyd.user.entity.UserJx;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liuqiu
 */
@Repository
public class UserJxDaoImpl implements UserJxDao {
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;
    @Override
    public List<UserJx> getUserJxByUserId(String userId) throws Exception {
        UserJx step = new UserJx();
        Criteria criteria = Criteria.from(UserJx.class)
                .where()
                .and("user_id", Operator.EQ, userId)
                .endWhere();
        List<UserJx> stepList = crudTemplate.find(step,criteria);
        return stepList;
    }

    @Override
    public void updateUserJx(UserJx userJx) throws Exception {
        Criteria criteria = Criteria.from(UserJx.class)
                .where()
                .and("user_id", Operator.EQ, userJx.getUserId())
                .endWhere();
        userJx.setUserId(null);
        crudTemplate.update(userJx,criteria);
    }
}
