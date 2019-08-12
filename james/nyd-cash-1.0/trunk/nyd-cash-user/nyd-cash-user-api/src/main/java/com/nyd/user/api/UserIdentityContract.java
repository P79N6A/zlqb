package com.nyd.user.api;

import com.nyd.user.model.PayFeeInfo;
import com.nyd.user.model.UserDetailInfo;
import com.nyd.user.model.UserInfo;
import com.nyd.user.model.dto.UserDto;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

/**
 * Created by Dengw on 2017/11/13
 */
public interface UserIdentityContract {
    ResponseData<UserInfo> getUserInfo(String userId);

    ResponseData<List<UserInfo>> getUserInfoByIdNo(String idNumber);

    ResponseData<UserDetailInfo> getUserDetailInfo(String userId);

    ResponseData<List<UserInfo>> getUserInfos(UserDto userDto);

    ResponseData<List<PayFeeInfo>> getPayFeeByBusiness(String business);

    }
