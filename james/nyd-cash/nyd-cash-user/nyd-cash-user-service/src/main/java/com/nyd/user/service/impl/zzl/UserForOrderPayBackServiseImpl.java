package com.nyd.user.service.impl.zzl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.nyd.user.api.zzl.UserForOrderPayBackServise;
import com.nyd.user.api.zzl.UserSqlService;
import com.nyd.user.model.BankUser;

@Service("userForOrderPayBackServise")
public class UserForOrderPayBackServiseImpl implements UserForOrderPayBackServise{

	@Autowired
	private UserSqlService<?> userSqlService;
	
	@Override
	public BankUser queryBankInfoByUserId(String userId, String soure) {
		String sql = "SELECT ub.account_name, ub.bank_account, ub.reserved_phone, ub.protocol_no, ub.hlb_user_id, u.id_number "
				+ "from t_user_bank ub, t_user u  where ub.user_id = u.user_id and ub.user_id = '%s' and ub.soure = '%s' LIMIT 1";
		List<BankUser> list = (List<BankUser>) userSqlService.queryT(String.format(sql, userId, soure), BankUser.class);
		if(CollectionUtils.isNotEmpty(list))
			return list.get(0);
		else 
			return null;
	}

}
