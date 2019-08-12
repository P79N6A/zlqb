package com.nyd.admin.dao.impl;

import com.nyd.admin.dao.CustomerRefundDao;
import com.nyd.admin.entity.RefundAuditLog;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @Author: wucx
 * @Date: 2018/11/2 19:15
 */
@Repository
public class CustomerRefundDaoImpl implements CustomerRefundDao {

    @Autowired
    private CrudTemplate crudTemplate;

    @Override
    public void save(RefundAuditLog refundAuditLog) throws Exception {
        crudTemplate.save(refundAuditLog);
    }
}
