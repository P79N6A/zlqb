package com.nyd.user.api;

import com.nyd.user.entity.UserBlackList;

import com.github.pagehelper.PageInfo;

import java.util.HashMap;
import java.util.Map;


/**
 * 服务实现层接口
 * @author yuxue
 * @date 2019-08-26 17:52:57
 */
public interface UserBlackListService {
	
    UserBlackList getByPrimaryKey(Integer id);
    
    PageInfo<UserBlackList> queryByPage(Integer pageNo, Integer pageSize, Map<String, Object> map);
    
    Map<String, Object> save(UserBlackList userBlackList);

    Integer deleteById(Integer id);

    Integer updateById(UserBlackList userBlackList);
}
