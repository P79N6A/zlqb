package com.nyd.user.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.nyd.user.entity.RefundUser;
import com.nyd.user.model.RefundUserInfo;

@Mapper
public interface RefundUserMapper {

	void saveRefundUser(RefundUser refundUser) throws Exception;

	void updateRefundUser(RefundUser refundUser) throws Exception;

	Integer queryRefundUserInfoCount(Map<String,Object> Param) throws Exception;

	Integer haveInWhiteList(RefundUserInfo info) throws Exception;
	
	RefundUserInfo getRefundUserByUserId(RefundUserInfo info) throws Exception;

	List<RefundUser> queryRefundUserInfo(Map<String,Object> Param) throws Exception;

}
