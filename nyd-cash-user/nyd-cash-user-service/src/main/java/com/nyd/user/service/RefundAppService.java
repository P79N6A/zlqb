package com.nyd.user.service;

import java.util.List;

import com.nyd.user.model.RefundAppInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 
 * @author zhangdk
 *
 */
public interface RefundAppService {
    void save(RefundAppInfo info) throws Exception;
	void update(RefundAppInfo info) throws Exception;
	void resetRealRecomNum() throws Exception;
	ResponseData<List<RefundAppInfo>> queryRefundApp(RefundAppInfo info)throws Exception;
	ResponseData<List<RefundAppInfo>> getTaskListByUserId(RefundAppInfo info)throws Exception;
}
