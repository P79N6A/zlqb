package com.nyd.settlement.dao.impl;

import com.nyd.settlement.dao.RefundDao;
import com.nyd.settlement.entity.refund.Refund;
import com.tasfe.framework.crud.core.CrudTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by hwei on 2018/1/17.
 */
@Service
public class RefundDaoImpl implements RefundDao{

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void saveRefund(Refund refund) throws Exception {
        crudTemplate.save(refund);
    }
}
