package com.nyd.user.service.impl.zzl;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

import com.nyd.zeus.model.SettleAccount;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.StringUtil;
import com.nyd.user.api.zzl.UserForSLHServise;
import com.nyd.user.api.zzl.UserSqlService;
import com.nyd.user.dao.ContactDao;
import com.nyd.user.dao.UserDao;
import com.nyd.user.dao.mapper.UserSourceMapper;
import com.nyd.user.entity.UserBind;
import com.nyd.user.model.t.UserInfo;
import com.nyd.user.model.vo.UserBankInfo;
import org.springframework.transaction.annotation.Transactional;

@Service("userForSLHServise")
public class UserForSLHServiseImpl implements UserForSLHServise{
	private Logger log = LoggerFactory.getLogger(UserForSLHServiseImpl.class);
	@Autowired
	private UserSourceMapper mapper;
	@Autowired
	private UserSqlService userSqlService;
	@Override
	public UserInfo queryOrderByUser(String userId) {
	    UserInfo info = new UserInfo();
		try {
			info = mapper.getInfoByUserId(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}
	@Override
	public boolean queryUserByUserId(String userId) {
		String sql="select * from t_user_bank where  user_id = '"+userId+"' and soure = 2 ";
		List<UserBind> data =new ArrayList<>();
	    data=userSqlService.queryT(sql,UserBind.class);
	if(!data.isEmpty()) {
		return true;
	}else {
		return false;
	}
		 
	}
	@Override
	public UserBankInfo getuserByUserId(String userId) {
		String sql="select * from t_user_bank where  user_id = '"+userId+"' and soure !=2 order by create_time desc limit 1 ";
		List<UserBankInfo> data =new ArrayList<>();
		data = userSqlService.queryT(sql, UserBankInfo.class);
		if(!data.isEmpty()) {
			return data.get(0);
		}else {
			return null;
		}
	}
	@Override
	public String getUserIdByPhone(String phone) {
		String sql = "select user_id from t_account where account_number = '"+phone+"'  ";
		String userId = userSqlService.queryOne(sql).getString("user_id");
		if(StringUtils.isBlank(userId)) {
			return null;
		}else {
			return userId;
		}
		
	}
	@Override
	public UserInfo getInfoUserId(String userId) {
		 UserInfo info = new UserInfo();
			try {
				info = mapper.getInfoUserId(userId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return info;
	}

	/**
	 * 根据用户id、银行卡类型获取绑卡信息
	 * @param userId
	 * @param soure
	 * @return ResponseData<UserBankInfo>
	 */
	public ResponseData<List<UserBankInfo>> getUserBankByUserIdAndSoure(String userId, String soure) {
		String sql="select * from t_user_bank where  user_id = '"+userId+"'";
		if(StringUtil.isNotEmpty(soure)){
			sql+=" and soure = '"+soure+"' ";
		}
		sql+=" order by create_time desc limit 1 ";
		List<UserBankInfo> data =new ArrayList<>();
		try {
			data = userSqlService.queryT(sql, UserBankInfo.class);
			ResponseData<List<UserBankInfo>> responseData = new ResponseData<List<UserBankInfo>>();
			responseData.setStatus("0");
			responseData.setMsg("查询成功");
			responseData.setData(data);
			return responseData;
		}catch (Exception e){
			ResponseData<List<UserBankInfo>> responseData = new ResponseData<List<UserBankInfo>>();
			responseData.setStatus("1");
			responseData.setMsg("系统异常，请联系管理员");
			log.error("UserForSLHServise getUserBankByUserId e="+e.getMessage());
			return responseData;
		}
	}

}
