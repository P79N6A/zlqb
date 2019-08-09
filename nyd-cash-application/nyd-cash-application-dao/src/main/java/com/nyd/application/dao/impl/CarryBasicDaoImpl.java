package com.nyd.application.dao.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.nyd.application.dao.CarrybasicDao;
import com.nyd.application.model.call.CarryBasic;
import com.tasfe.framework.crud.core.CrudTemplate;
@Repository
public class CarryBasicDaoImpl implements CarrybasicDao {

	@Resource(name="mysql")
    private CrudTemplate crudTemplate;

	@Override
	public void save(CarryBasic carryBasic) throws Exception {
		carryBasic.setCreateTime(new Date());
		carryBasic.setUpdateTime(new Date());
		crudTemplate.save(carryBasic);
		
	}
	

}
