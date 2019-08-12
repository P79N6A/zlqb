package com.nyd.zeus.dao.impl;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import com.nyd.zeus.dao.PaymentRiskRecordExtendDao;
import com.nyd.zeus.entity.PaymentRiskRecordExtend;
import com.nyd.zeus.model.PaymentRiskRecordExtendVo;
import com.tasfe.framework.crud.core.CrudTemplate;

/**
 * Created by zhujx on 2017/11/18.
 */
@Repository
public class PaymentRiskRecordDaoImpl implements PaymentRiskRecordExtendDao{
    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(PaymentRiskRecordExtendVo vo) throws Exception {
    	PaymentRiskRecordExtend record = new PaymentRiskRecordExtend();
        BeanUtils.copyProperties(vo,record);
        crudTemplate.save(record);
    }

   
}
