package com.nyd.admin.service;

import java.util.Map;

import com.nyd.admin.model.Info.RefundAppCountRequest;
import com.tasfe.framework.support.model.ResponseData;

public interface RefundAppCountService {

	ResponseData query(RefundAppCountRequest req);
}
