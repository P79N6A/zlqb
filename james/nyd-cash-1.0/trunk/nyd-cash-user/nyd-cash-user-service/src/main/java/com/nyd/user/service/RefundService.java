package com.nyd.user.service;

import java.util.List;

import com.nyd.user.model.RefundAppInfo;
import com.nyd.user.model.RefundInfo;
import com.nyd.user.model.vo.RefundAppVo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 
 * @author zhangdk
 *
 */
public interface RefundService {
	void save(RefundInfo info) throws Exception;

	void update(RefundInfo info) throws Exception;

	ResponseData<List<RefundInfo>> queryRefund(RefundInfo info)throws Exception;

	ResponseData<RefundInfo> getRefundByRefundNo(String  refundNo)throws Exception;
	
	ResponseData<List<RefundAppInfo>> getRefundAppListByRefundNo(String refundNo) throws Exception;

	ResponseData<List<RefundAppInfo>> getRefundAppListByRefundNoAuth(String refundNo) throws Exception;
	
	ResponseData uploadRefundImge(RefundAppVo vo)throws Exception;
	
	ResponseData<List<RefundInfo>> getRefundByUserId(String userId) throws Exception;
}
