package com.nyd.user.dao.mapper;

import com.nyd.user.entity.HitRuleConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface HitRuleConfigMapper {

    List<HitRuleConfig> selectByAppName(Map<String, String> map);

    List<HitRuleConfig> selectByAppNameAndSource(Map<String, String> params);
}
