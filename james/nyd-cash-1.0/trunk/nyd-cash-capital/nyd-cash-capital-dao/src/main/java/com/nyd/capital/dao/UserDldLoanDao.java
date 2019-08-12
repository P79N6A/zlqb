package com.nyd.capital.dao;

import java.util.List;

import com.nyd.capital.entity.UserDldLoan;

/**
 * 
 * 
 * @author zhangdk
 *
 */
public interface UserDldLoanDao {
	List<UserDldLoan> getUserDldLoan(String orderNo) throws Exception ;
}
