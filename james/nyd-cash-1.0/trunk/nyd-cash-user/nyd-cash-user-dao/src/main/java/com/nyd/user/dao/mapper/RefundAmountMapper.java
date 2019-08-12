package com.nyd.user.dao.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nyd.user.model.RefundAmountInfo;


public interface RefundAmountMapper {
	void save(RefundAmountInfo refund);
	void update(RefundAmountInfo refund);
	List<RefundAmountInfo> queryRefundAmount(RefundAmountInfo refund);
	RefundAmountInfo getRefundAmountByCode(RefundAmountInfo info);
	RefundAmountInfo getCountByAmount(@Param("amount") BigDecimal amount);
}
