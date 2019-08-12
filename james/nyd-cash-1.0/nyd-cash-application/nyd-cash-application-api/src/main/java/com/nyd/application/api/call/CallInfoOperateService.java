package com.nyd.application.api.call;

import com.nyd.application.model.mongo.CallInfo;

public interface CallInfoOperateService {
	public void save(CallInfo callInfo) throws Exception;

}
