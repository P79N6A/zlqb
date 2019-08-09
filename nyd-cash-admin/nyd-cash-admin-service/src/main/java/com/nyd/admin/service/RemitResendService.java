package com.nyd.admin.service;

import java.text.ParseException;
import java.util.Map;

import com.nyd.admin.model.Info.OrderExceptionRequst;
import com.tasfe.framework.support.model.ResponseData;

public interface RemitResendService {

	ResponseData audit(OrderExceptionRequst req);
	ResponseData query(OrderExceptionRequst req) throws ParseException;
	ResponseData export(Map<String,Object> req);
}
