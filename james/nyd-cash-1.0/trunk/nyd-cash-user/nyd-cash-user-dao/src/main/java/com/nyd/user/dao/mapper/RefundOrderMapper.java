package com.nyd.user.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.nyd.user.model.RefundOrderInfo;

@Mapper
public interface RefundOrderMapper {

	void save(RefundOrderInfo refundUser) throws Exception;

	Integer queryRefundOrderTotal(RefundOrderInfo Param) throws Exception;

	List<RefundOrderInfo> queryRefundOrder(RefundOrderInfo Param) throws Exception;

}
