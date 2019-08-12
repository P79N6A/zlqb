package com.nyd.admin.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.nyd.admin.model.RefundOrderInfo;

@Mapper
public interface RefundOrderMapper {

	void save(RefundOrderInfo refundUser) throws Exception;

	Integer queryRefundOrderTotal(RefundOrderInfo Param) throws Exception;

	List<RefundOrderInfo> queryRefundOrder(RefundOrderInfo Param) throws Exception;

}
