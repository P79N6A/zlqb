package com.nyd.user.service;



import com.nyd.user.model.UserDetailInfo;

public interface UserDetailService {

    void saveUserDetail(UserDetailInfo detailInfo) throws Exception;
    
    void updateUserDetailByUserId(UserDetailInfo userDetailInfo) throws Exception;
}
