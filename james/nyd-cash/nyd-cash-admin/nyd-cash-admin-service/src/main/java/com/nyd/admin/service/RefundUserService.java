package com.nyd.admin.service;


import com.nyd.admin.model.AdminRefundUserDto;
import com.tasfe.framework.support.model.ResponseData;

public interface RefundUserService {
	

	/**
	 * 查询用户列表
	 * @param dto
	 * @return
	 */
	ResponseData selectRefundUser(AdminRefundUserDto dto);


}
