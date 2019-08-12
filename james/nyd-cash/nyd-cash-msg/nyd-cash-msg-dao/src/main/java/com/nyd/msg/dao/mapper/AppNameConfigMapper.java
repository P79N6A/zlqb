package com.nyd.msg.dao.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author liuqiu
 *
 **/
@Mapper
public interface AppNameConfigMapper {

    /**
     * 通过code查询app中文名
     * @param appNameCode
     * @return
     */
    String selectNameByCode(String appNameCode);

}
