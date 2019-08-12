package com.nyd.application.dao.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import com.nyd.application.dao.CarryCallsDao;
import com.nyd.application.model.call.CarryCalls;
import com.tasfe.framework.crud.core.CrudTemplate;
@Repository
public class CarryCallsDaoImpl implements CarryCallsDao {

	@Resource(name="mysql")
    private CrudTemplate crudTemplate;
	
	@Override
	public void save(CarryCalls carryCalls) throws Exception {
		carryCalls.setCreateTime(new Date());
		carryCalls.setUpdateTime(new Date());
        crudTemplate.save(carryCalls);
		
	}

}
