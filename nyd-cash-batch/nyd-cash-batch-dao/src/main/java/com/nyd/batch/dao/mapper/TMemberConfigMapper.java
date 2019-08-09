package com.nyd.batch.dao.mapper;

import com.nyd.batch.entity.TMemberConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface TMemberConfigMapper {


    TMemberConfig selectByMemberType(Map map);

}