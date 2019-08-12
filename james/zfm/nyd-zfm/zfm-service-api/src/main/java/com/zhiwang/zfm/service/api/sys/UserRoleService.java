package com.zhiwang.zfm.service.api.sys;

import java.util.List;

import com.zhiwang.zfm.common.response.bean.sys.UserVO;

public interface UserRoleService<T> {

	List<UserVO>  getCreditTrialUsersList(String roleId);

}
