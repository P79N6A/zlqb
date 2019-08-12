package com.nyd.application.service.impl.call;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nyd.application.api.ApplicationSqlService;
import com.nyd.application.api.call.CallInfoOperateService;
import com.nyd.application.model.mongo.CallInfo;

@Service
public class CallInfoOperateServiceImpl implements CallInfoOperateService {

	@Autowired
	private ApplicationSqlService<T> applicationSqlService;
	
	@Override
	public void save(CallInfo callInfo) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(" ");
		sb.append(" ");
		sb.append(" ");
		sb.append(" ");
		sb.append(" ");
		sb.append(" ");
		sb.append(" ");
		sb.append(" ");
		sb.append(" ");
		sb.append(" ");


	}

}
