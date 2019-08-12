package com.nyd.admin.service;

import com.nyd.user.model.RefundAppInfo;
import com.tasfe.framework.support.model.ResponseData;

public interface RefundAppService {

	ResponseData save(RefundAppInfo req);
	ResponseData update(RefundAppInfo req);
	ResponseData query(RefundAppInfo req);
}
