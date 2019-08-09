package com.nyd.capital.dao.mappers;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.nyd.capital.dao.CrudDao;
import com.nyd.capital.entity.Fund;
import com.nyd.capital.entity.UserDldLoan;

/**
 * 
 * @author zhangdk
 *
 */
@Mapper
public interface UserDldLoanMapper extends CrudDao<Fund> {

	public void save(UserDldLoan dld);
	public void update(UserDldLoan dld);
	public UserDldLoan getUserDldLoanByOrderNo(@Param("orderNo") String orderNo);
}
