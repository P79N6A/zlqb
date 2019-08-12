package com.nyd.admin.dao.impl;

import com.nyd.admin.dao.CustomerServiceLogDao;
import com.nyd.admin.entity.CustomerServiceLog;
import com.nyd.admin.model.Info.CustomerServiceLogInfo;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class CustomerServiceLogDaoImpl implements CustomerServiceLogDao {

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(CustomerServiceLog customerServiceLog) throws Exception {
        crudTemplate.save(customerServiceLog);
    }

    @Override
    public List<CustomerServiceLogInfo> findCustomerServiceListByUserId(String userId) throws Exception {
        Criteria criteria = Criteria.from(CustomerServiceLog.class)
                .where().and("user_id", Operator.EQ,userId)
                .and("delete_flag",Operator.EQ,0)
                .endWhere()
                .orderBy("create_time desc");
        CustomerServiceLogInfo customerServiceLogInfo = new CustomerServiceLogInfo();
        return crudTemplate.find(customerServiceLogInfo,criteria);
    }
}
