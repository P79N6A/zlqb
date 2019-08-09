package com.nyd.admin.service;

import com.nyd.admin.model.RefundOrderInfo;
import com.tasfe.framework.support.model.ResponseData;

public interface RefundOrderService {

	ResponseData save(RefundOrderInfo req);
	ResponseData query(RefundOrderInfo req);
	ResponseData queryExport(RefundOrderInfo req);
}
