package com.nyd.application.api.call;

import com.nyd.application.model.mongo.SmsInfo;

public interface SmsInfoOperateService {
	public void save(SmsInfo smsInfo) throws Exception;

}
