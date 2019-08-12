package com.nyd.user.api.zzl;

import com.nyd.user.model.BankUser;

public interface UserForOrderPayBackServise {

	/**
	 * 根据用户id查询绑卡信息
	 * @param orderNo
	 * @return
	 */
	public BankUser queryBankInfoByUserId(String userId, String soure);
	
}
