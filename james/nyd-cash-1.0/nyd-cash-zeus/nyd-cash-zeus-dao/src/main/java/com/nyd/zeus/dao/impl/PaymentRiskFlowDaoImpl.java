package com.nyd.zeus.dao.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.nyd.zeus.dao.PaymentRiskFlowDao;
import com.nyd.zeus.entity.PaymentRiskFlow;
import com.tasfe.framework.crud.core.CrudTemplate;

/**
 * Created by zhujx on 2017/11/18.
 */
@Repository
public class PaymentRiskFlowDaoImpl implements PaymentRiskFlowDao{

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(PaymentRiskFlow vo) throws Exception {
        crudTemplate.save(vo);
    }

}
