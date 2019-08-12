package com.nyd.capital.dao.mappers;


import com.nyd.capital.dao.CrudDao;
import com.nyd.capital.entity.Fund;
import com.nyd.capital.entity.UserDld;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author zhangdk
 *
 */
@Mapper
public interface UserDldMapper extends CrudDao<Fund> {

	public void save(UserDld dld);
	public void update(UserDld dld);
	public UserDld getUserDldByUserId(@Param("userId") String userId);
}
