package com.nyd.admin.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.nyd.admin.model.RefundUserInfo;

@Mapper
public interface RefundUserMapper {

	
	Integer queryRefundUserInfoCount(Map<String,Object> Param) throws Exception;


	List<RefundUserInfo> queryRefundUserInfo(Map<String,Object> Param) throws Exception;

}
