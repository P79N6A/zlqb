package com.nyd.user.service;

import java.util.List;
import java.util.Map;

import com.nyd.user.model.RefundAppCountInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 
 * @author zhangdk
 *
 */
public interface RefundAppCountService {
    void save(RefundAppCountInfo info) throws Exception;
	void update(RefundAppCountInfo info) throws Exception;
	ResponseData updateCount(RefundAppCountInfo info) throws Exception;
	ResponseData<List<RefundAppCountInfo>> queryRefundAppCount(Map<String, Object> param)throws Exception;
}
