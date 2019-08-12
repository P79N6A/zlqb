package com.nyd.admin.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.nyd.admin.model.AdminRefundInfo;

@Mapper
public interface AdminRefundMapper {
	
	void save(AdminRefundInfo refund) throws Exception;
	
	void update(AdminRefundInfo refund) throws Exception;
	
	Integer queryRefundTotal(AdminRefundInfo refund) throws Exception;
	
	List<AdminRefundInfo> queryRefund(AdminRefundInfo refund) throws Exception;
	
	List<AdminRefundInfo> queryAllRefund() throws Exception;
	
	AdminRefundInfo getRefundByRefundNo(@Param("refundNo") String refundNo) throws Exception;

}
