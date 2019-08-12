package com.nyd.user.dao.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.nyd.user.model.AccountResetInfo;

@Mapper
public interface AccountResetMapper {

	void save(AccountResetInfo info);
}
