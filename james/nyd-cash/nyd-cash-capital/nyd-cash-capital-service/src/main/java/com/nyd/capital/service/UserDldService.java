package com.nyd.capital.service;

import com.nyd.capital.entity.UserDld;
import com.nyd.capital.entity.UserDldLoan;

/**
 *
 * @author zhangdk
 *
 */
public interface UserDldService {
    /**
     * 保存用户
     * @param
     */
    void save(UserDld userDld) throws Exception;

    void update(UserDld userDld) throws Exception;
    UserDld getUserDldByUserId(String userId) throws Exception;

    void saveLoan(UserDldLoan loan)throws Exception;

    void updateLoan(UserDldLoan loan)throws Exception;

    UserDldLoan getUserDldLoanByOrderNo(String orderNo);

}
