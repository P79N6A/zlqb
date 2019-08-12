package com.nyd.application.api.call;

import com.tasfe.framework.support.model.ResponseData;

public interface QueryContratService {
	
	public ResponseData getSignAgreement(String userId, String orderId);
	}
