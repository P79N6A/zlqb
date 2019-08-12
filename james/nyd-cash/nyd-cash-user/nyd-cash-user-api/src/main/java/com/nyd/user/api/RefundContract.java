package com.nyd.user.api;

import java.util.List;

import com.nyd.user.model.RefundAppInfo;
import com.nyd.user.model.RefundInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 
 * @author zhangdk
 *
 */
public interface RefundContract {
    void save(RefundInfo info) throws Exception;
	void update(RefundInfo info) throws Exception;
	ResponseData<List<RefundInfo>> queryRefund(RefundInfo info)throws Exception;
	ResponseData<RefundInfo> getRefundByRefundNo(String refundNo)throws Exception;
	ResponseData<List<RefundAppInfo>> getRefundAppListByRefundNo(String refundNo) throws Exception;
	
	ResponseData<List<RefundInfo>> getRefundByUserId(String userId) throws Exception;
	
}
