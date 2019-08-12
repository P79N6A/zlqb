package com.nyd.batch.dao.mapper;


import com.nyd.batch.entity.TMember;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TMemberMapper {

    List<TMember> selectByUserId(Map map);

}