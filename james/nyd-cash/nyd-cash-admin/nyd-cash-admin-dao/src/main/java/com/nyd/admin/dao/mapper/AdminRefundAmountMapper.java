package com.nyd.admin.dao.mapper;

import java.util.List;

import com.nyd.admin.model.AdminRefundAmountInfo;


public interface AdminRefundAmountMapper {

	List<AdminRefundAmountInfo> queryRefundAmount(AdminRefundAmountInfo refund);
	Integer queryRefundAmountTotal(AdminRefundAmountInfo refund);

}
