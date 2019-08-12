package com.nyd.capital.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.nyd.capital.dao.UserDldLoanDao;
import com.nyd.capital.entity.UserDldLoan;
import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.core.CrudTemplate;

/**
 * 
 * @author zhangdk
 *
 */
@Repository
public class UserDldLoanDaoImpl implements UserDldLoanDao{
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;
    @Override
    public List<UserDldLoan> getUserDldLoan(String orderNo) throws Exception {
    	Criteria criteria = Criteria.from(UserDldLoan.class)
                .where().and("mer_order_no", Operator.EQ,orderNo)
                .and("delete_flag",Operator.EQ,0)
                .endWhere();
        return crudTemplate.find(new UserDldLoan(),criteria);
    }
}
