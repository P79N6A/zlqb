package com.nyd.user.service;

import java.math.BigDecimal;
import java.util.List;

import com.nyd.user.model.RefundAmountInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 
 * @author zhangdk
 *
 */
public interface RefundAmountService {
    void save(RefundAmountInfo refund) throws Exception;
	void update(RefundAmountInfo refund) throws Exception;
	ResponseData<List<RefundAmountInfo>> queryRefundAmount(RefundAmountInfo refund) throws Exception;
	ResponseData<RefundAmountInfo> getRefundAmountByCode(RefundAmountInfo info) throws Exception;
	ResponseData<RefundAmountInfo> getCountByAmount(BigDecimal amount) throws Exception;
}
