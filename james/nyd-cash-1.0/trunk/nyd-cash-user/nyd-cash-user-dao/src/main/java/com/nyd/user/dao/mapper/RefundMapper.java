package com.nyd.user.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.nyd.user.model.RefundInfo;

@Mapper
public interface RefundMapper {
	
	void save(RefundInfo refund) throws Exception;
	
	void update(RefundInfo refund) throws Exception;
	
	Integer queryRefundTotal(RefundInfo refund) throws Exception;
	
	List<RefundInfo> queryRefund(RefundInfo refund) throws Exception;
	
	List<RefundInfo> queryAllRefund() throws Exception;
	
	RefundInfo getRefundByRefundNo(@Param("refundNo") String refundNo) throws Exception;
	List<RefundInfo> getRefundByUserId(@Param("userId") String refundNo) throws Exception;

}
