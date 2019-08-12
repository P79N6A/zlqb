package com.nyd.admin.service;

import com.nyd.admin.model.AdminRefundInfo;
import com.nyd.user.model.RefundInfo;
import com.tasfe.framework.support.model.ResponseData;

public interface RefundService {

	ResponseData audit(RefundInfo req);
	ResponseData query(AdminRefundInfo req);
	ResponseData getRefundDetail(RefundInfo param);
}
