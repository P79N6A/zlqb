package com.nyd.zeus.dao.impl;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import com.nyd.zeus.dao.BillExtendInfoDao;
import com.nyd.zeus.entity.BillExtendInfo;
import com.nyd.zeus.model.BillExtendInfoVo;
import com.tasfe.framework.crud.core.CrudTemplate;

/**
 * Created by zhujx on 2017/11/18.
 */
@Repository
public class BillExtendInfoDaoImpl implements BillExtendInfoDao{

    @Resource(name="mysql")
    private CrudTemplate crudTemplate;

    @Override
    public void save(BillExtendInfoVo vo) throws Exception {
    	BillExtendInfo info = new BillExtendInfo();
        BeanUtils.copyProperties(vo,info);
        crudTemplate.save(info);
    }

}
