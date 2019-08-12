package com.nyd.application.dao.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import com.nyd.application.dao.CallInfoDao;
import com.nyd.application.entity.CallInfo;
import com.tasfe.framework.crud.core.CrudTemplate;
@Repository
public class CallInfoDaoImpl implements CallInfoDao {
	@Resource(name="mysql")
    private CrudTemplate crudTemplate;
	
	@Override
	public void save(com.nyd.application.model.mongo.CallInfo callInfo) throws Exception {
			CallInfo callInfoPO = new CallInfo();
			callInfoPO.setCreateTime(new Date());
			callInfoPO.setUpdateTime(new Date());
	        BeanUtils.copyProperties(callInfo,callInfoPO);
	        callInfoPO.setCallTime(callInfo.getCalltime());
	        crudTemplate.save(callInfoPO);
	}

}
