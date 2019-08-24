package com.nyd.user.api.zzl;

import com.nyd.user.model.common.CommonResponse;
import com.nyd.user.model.t.UserInfo;
import com.nyd.user.model.vo.UserBankInfo;
import com.nyd.user.model.vo.UserBankVo;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

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
	/**
	 * 根据用户id、银行卡类型获取绑卡信息
	 * @param userId
	 * @param soure
	 * @return ResponseData<UserBankInfo>
	 */
	public ResponseData<List<UserBankInfo>> getUserBankByUserIdAndSoure(String userId, String soure);
	/**
	 * 根据用户id、银行卡类型获取绑卡信息
	 * @param userId
	 * @param soure
	 * @return ResponseData<UserBankInfo>
	 */
	public CommonResponse<List<UserBankVo>> getUserBankListByUserIdAndSoure(String userId, String soure);
}
