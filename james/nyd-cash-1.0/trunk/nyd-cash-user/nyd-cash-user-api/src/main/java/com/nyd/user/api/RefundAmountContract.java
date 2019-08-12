package com.nyd.user.api;

import java.util.List;

import com.nyd.user.model.RefundAmountInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 
 * @author zhangdk
 *
 */
public interface RefundAmountContract {
    void save(RefundAmountInfo refund) throws Exception;
	void update(RefundAmountInfo refund) throws Exception;
	ResponseData<List<RefundAmountInfo>> queryRefundAmount(RefundAmountInfo refund) throws Exception;
	ResponseData<RefundAmountInfo> getRefundAmountByCode(RefundAmountInfo info) throws Exception;
}
