package com.nyd.user.api.zzl;

import com.nyd.user.model.BankUser;

public interface UserForWHServise {
	
	public BankUser queryBankInfoByUserId(String userId, String soure);

}
