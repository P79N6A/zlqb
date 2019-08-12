package com.zhiwang.zfm.service.impl.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.zhiwang.zfm.common.response.bean.sys.UserVO;
import com.zhiwang.zfm.dao.sys.UserRoleMapper;
import com.zhiwang.zfm.service.api.sys.SqlService;
import com.zhiwang.zfm.service.api.sys.UserRoleService;

@Service
@Transactional
public class UserRoleServiceImpl<T> implements UserRoleService<T> {

	@Autowired
    private UserRoleMapper<T> mapper;
	
	@Autowired
	private SqlService	 sqlService;

	public UserRoleMapper<T> getMapper() {
		return mapper;
	}

	@Override
	public List<UserVO> getCreditTrialUsersList(String roleId) {
		
		List<UserVO> list = sqlService.query("SELECT su.id,su.LOGIN_NAME as loginName  from sys_user su,sys_role sr,sys_user_role sur where sur.ROLE_ID=sr.ID and sur.USER_ID=su.id and su.STATUS=1 and sr.STATUS=1 and sr.id='"+roleId+"'");
		return list;
	}
}
