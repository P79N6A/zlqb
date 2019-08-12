package com.nyd.user.dao.impl;

import com.nyd.user.dao.PhoneRegionDao;
import com.nyd.user.entity.PhoneRegion;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;


@Repository
public class PhoneRegionDaoImpl implements PhoneRegionDao {

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public PhoneRegion getPhoneRegionsByMobile(String phoneFlag)throws Exception {
        Criteria criteria = Criteria.from(PhoneRegion.class)
                .where()
                .and("phone_flag", Operator.EQ,phoneFlag)
                .endWhere();
        PhoneRegion phoneRegion = new PhoneRegion();
        List<PhoneRegion> list = crudTemplate.find(phoneRegion,criteria);
        if(list != null && list.size() > 0){
            phoneRegion = list.get(0);
        }
        return phoneRegion;
    }


}
