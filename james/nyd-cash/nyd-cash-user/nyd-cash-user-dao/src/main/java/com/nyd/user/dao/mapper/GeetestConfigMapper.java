package com.nyd.user.dao.mapper;

import com.nyd.user.entity.GeetestConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liuqiu
 */
@Mapper
public interface GeetestConfigMapper {

    /**
     * 通过应用code和类型查询极验配置
     * @param code
     * @param type
     * @return
     */
    GeetestConfig selectByCodeAndType(@Param("code") String code,@Param("type") String type);

    /**
     * 查询使用极验验证的应用
     * @return
     */
    List<String> selectAllAppCode();
}
