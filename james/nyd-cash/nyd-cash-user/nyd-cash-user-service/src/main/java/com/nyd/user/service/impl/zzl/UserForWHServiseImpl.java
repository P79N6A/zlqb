package com.nyd.user.service.impl.zzl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nyd.user.api.zzl.UserForGYTServise;
import com.nyd.user.api.zzl.UserForWHServise;
import com.nyd.user.api.zzl.UserForOrderPayBackServise;
import com.nyd.user.model.BankUser;

@Service("userForWHServise")
public class UserForWHServiseImpl implements UserForWHServise{
	
	@Autowired
	private UserForOrderPayBackServise userForOrderPayBackServise;

	@Override
	public BankUser queryBankInfoByUserId(String userId, String soure) {
		
		return userForOrderPayBackServise.queryBankInfoByUserId(userId, soure);
	}

}
