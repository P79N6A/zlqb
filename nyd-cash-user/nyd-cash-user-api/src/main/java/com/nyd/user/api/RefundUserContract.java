package com.nyd.user.api;

import com.nyd.user.model.RefundUserDto;
import com.nyd.user.model.RefundUserInfo;
import com.tasfe.framework.support.model.ResponseData;

public interface RefundUserContract {
	
	/**
	 * 白名单金额查找
	 * @param dto
	 * @return
	 */
	ResponseData findRefundCash(RefundUserDto dto);
	
	/**
	 * 添加用户
	 * @param dto
	 * @return
	 */
	ResponseData addUser(RefundUserDto dto);
	
	/**
	 * 更新用户状态
	 * @param dto
	 * @return
	 */
	ResponseData updateUser(RefundUserDto dto);
	
	/**
	 * 查询用户列表
	 * @param dto
	 * @return
	 */
	ResponseData selectRefundUser(RefundUserDto dto);
	/**
	 * 查询用户是否在白名单
	 * @param info
	 * @return
	 */
	ResponseData haveInWhiteList(RefundUserInfo info);
	
	/**
	 * 查询用户万花筒是否展示退款入口
	 * @param info
	 * @return
	 * @throws Exception
	 */
	ResponseData ifShowRefund(RefundUserInfo info) throws Exception;
	/**
	 *更新点击vip新口子标识
	 * @param info
	 * @return
	 * @throws Exception
	 */
	ResponseData updateClickVip(RefundUserInfo info) throws Exception;
	
	void stopWithhold(String accountNumber);

}
