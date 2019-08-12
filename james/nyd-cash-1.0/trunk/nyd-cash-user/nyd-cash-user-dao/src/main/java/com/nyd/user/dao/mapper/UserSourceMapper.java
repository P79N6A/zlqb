package com.nyd.user.dao.mapper;

import com.nyd.user.entity.UserSource;
import com.nyd.user.model.t.UserInfo;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author liuqiu
 *
 **/
@Mapper
public interface UserSourceMapper {
    /**
     * 保存
     * @param userSource
     */
    void save(UserSource userSource);

    /**
     * 查询用户注册来源
     * @param mobile
     * @return
     */
    UserSource selectSource(String mobile);
    UserInfo getInfoByUserId(String userId);
    
    UserInfo getInfoUserId(String userId);
    List<UserInfo> getInfoByParam(Map<String,String> params);
}
