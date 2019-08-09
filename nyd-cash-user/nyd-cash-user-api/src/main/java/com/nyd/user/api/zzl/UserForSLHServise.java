package com.nyd.user.api.zzl;

import com.nyd.user.model.t.UserInfo;
import com.nyd.user.model.vo.UserBankInfo;

public interface UserForSLHServise {
	 UserInfo queryOrderByUser(String userId);
	 boolean queryUserByUserId(String userId);
	 /**
	  * 查询畅杰 绑卡信息
	  * @param userId
	  * @return
	  */
	 public UserBankInfo getuserByUserId(String userId);
	 
	 public String getUserIdByPhone(String phone);
	 public UserInfo getInfoUserId(String userId);
}
