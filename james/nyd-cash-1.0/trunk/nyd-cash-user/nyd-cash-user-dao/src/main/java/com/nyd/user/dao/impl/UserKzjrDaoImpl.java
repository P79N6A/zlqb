package com.nyd.user.dao.impl;

import com.nyd.user.dao.UserKzjrDao;
import com.nyd.user.entity.UserKzjr;
import com.nyd.user.model.UserKzjrInfo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 12:21 2018/6/21
 */
@Repository
public class UserKzjrDaoImpl implements UserKzjrDao {
    @Autowired
    private CrudTemplate crudTemplate;

    @Override
    public List<UserKzjrInfo> getByUserId(String userId) throws Exception {
        Criteria criteria = Criteria.from(UserKzjr.class)
                .where()
                .and("user_id",Operator.EQ,userId)
                .endWhere();
        UserKzjrInfo userKzjrInfo = new UserKzjrInfo();
        return crudTemplate.find(userKzjrInfo ,criteria);
    }
}
