package com.nyd.user.dao;

import com.nyd.user.model.UserDetailInfo;

import java.util.List;

/**
 * Created by dengw on 2017/11/2.
 */
public interface UserDetailDao {
    void save(UserDetailInfo detailInfo) throws Exception;

    void update(UserDetailInfo detailInfo) throws Exception;

    void delete(String userId) throws Exception;
    
    List<UserDetailInfo> getUserDetailsByUserId(String userId) throws Exception;

    /**
     * 根据身份证号查找用户详情信息
     * @param IdNumber
     * @return
     * @throws Exception
     */
    List<UserDetailInfo> getUserDetailsByIdNumber(String IdNumber) throws Exception;

    void updateUserDetailTest(String idNumber,String updateIdNumber) throws Exception;

}
