package com.nyd.user.dao.impl;

import com.nyd.user.dao.InnerWhiteUserDao;
import com.nyd.user.dao.PhoneRegionDao;
import com.nyd.user.entity.InnerWhiteUser;
import com.nyd.user.entity.PhoneRegion;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;


@Repository
public class InnerWhiteUserDaoImpl implements InnerWhiteUserDao {

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public List<InnerWhiteUser> getObjectsByMobile(String mobile) throws Exception {
        Criteria criteria = Criteria.from(InnerWhiteUser.class)
                .where().and("is_in_use",Operator.EQ,0)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        InnerWhiteUser user = new InnerWhiteUser();
        user.setMobile(mobile);
        return crudTemplate.find(user,criteria);
    }
}
