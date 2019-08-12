package com.nyd.user.dao;

import com.nyd.user.entity.User;
import com.nyd.user.model.AccountInfo;
import com.nyd.user.model.PayFee;
import com.nyd.user.model.PayFeeInfo;
import com.nyd.user.model.UserInfo;

import java.util.List;

/**
 * Created by dengw on 17/11/3.
 */
public interface UserDao {
    void save(UserInfo userInfo) throws Exception;

    void update(UserInfo userInfo) throws Exception;

    void delete(String userId) throws Exception;

    List<UserInfo> getUsersByIdNumber(String idNumber) throws Exception;

    List<UserInfo> getUsersByUserId(String userId) throws Exception;

    List<AccountInfo> getMobileByUserId(String userId) throws Exception;

    List<UserInfo> getUsers(UserInfo userInfo) throws Exception;

    List<UserInfo> getUserByIdNumber(UserInfo userInfo) throws Exception;

    void updateUserTest(String idNumber,String updateIdNumber) throws Exception;

    List<PayFeeInfo> getPayFeeByBusiness(String business) throws Exception;
}
