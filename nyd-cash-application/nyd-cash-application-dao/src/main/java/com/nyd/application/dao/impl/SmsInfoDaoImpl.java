package com.nyd.application.dao.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import com.nyd.application.dao.SmsInfoDao;
import com.nyd.application.entity.SmsInfo;
import com.tasfe.framework.crud.core.CrudTemplate;

@Repository
public class SmsInfoDaoImpl implements SmsInfoDao{

	@Resource(name="mysql")
    private CrudTemplate crudTemplate;
	
	@Override
	public void save(com.nyd.application.model.mongo.SmsInfo message) throws Exception {
		SmsInfo smsPO = new SmsInfo();
		smsPO.setCreateTime(new Date());
		smsPO.setUpdateTime(new Date());
		BeanUtils.copyProperties(message,smsPO);
		crudTemplate.save(smsPO);
	
	}

}
