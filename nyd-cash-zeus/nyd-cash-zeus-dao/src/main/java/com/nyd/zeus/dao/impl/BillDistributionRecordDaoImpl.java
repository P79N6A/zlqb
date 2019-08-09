package com.nyd.zeus.dao.impl;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import com.nyd.zeus.dao.BillDistributionRecordDao;
import com.nyd.zeus.entity.BillDistributionRecord;
import com.nyd.zeus.model.BillDistributionRecordVo;
import com.tasfe.framework.crud.core.CrudTemplate;

/**
 * Created by zhujx on 2017/11/18.
 */
@Repository
public class BillDistributionRecordDaoImpl implements BillDistributionRecordDao{

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(BillDistributionRecordVo vo) throws Exception {
    	BillDistributionRecord record=  new BillDistributionRecord();
        BeanUtils.copyProperties(vo,record);
        record.setStatus(1);
        crudTemplate.save(record);
    }

}
