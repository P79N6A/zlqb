package com.nyd.admin.dao.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 9:36 2018/10/2
 */
@Mapper
public interface AccountMapper {
    /**
     * 根据创建时间查找未登陆的用户
     * @param paramMap
     * @return
     */
    List<String> selectUnloginByTime(Map<String,String> paramMap);
}
