package com.nyd.user.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.nyd.user.entity.Step;


@Mapper
public interface UserStepMapper {

	 List<Step> userStepFindByUserId(List<String> list);
}
