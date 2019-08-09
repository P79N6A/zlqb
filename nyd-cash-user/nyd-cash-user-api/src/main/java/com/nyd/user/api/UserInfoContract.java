package com.nyd.user.api;

import java.util.List;

import com.nyd.user.model.UserInfo;
import com.nyd.user.model.dto.UserDto;
import com.tasfe.framework.support.model.ResponseData;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 9:35 2018/3/30
 */
public interface UserInfoContract {
    /**
     * 根据用户手机号和身份证号查找用户信息
     * @param userDto
     * @return
     */
    ResponseData<UserInfo> getExistUserInfo(UserDto userDto);
    public ResponseData<List<com.nyd.user.model.t.UserInfo>> getInfoByParam(String userName,String phone,String userId);
}
