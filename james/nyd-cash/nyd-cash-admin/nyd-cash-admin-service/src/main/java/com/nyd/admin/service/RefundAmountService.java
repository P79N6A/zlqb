package com.nyd.admin.service;

import java.math.BigDecimal;

import com.nyd.admin.model.AdminRefundAmountInfo;
import com.nyd.user.model.RefundAmountInfo;
import com.tasfe.framework.support.model.ResponseData;

public interface RefundAmountService {

	ResponseData save(RefundAmountInfo req);
	ResponseData update(RefundAmountInfo req);
	ResponseData query(AdminRefundAmountInfo req);
	boolean queryAndCheckAmount(String amountCode,BigDecimal minAmount,BigDecimal maxAmount);
}
