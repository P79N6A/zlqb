package com.nyd.user.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.nyd.user.entity.HitLog;

@Mapper
public interface HitLogMapper {
	
	List<HitLog> hitLogByMobile(String mobile); 

}
