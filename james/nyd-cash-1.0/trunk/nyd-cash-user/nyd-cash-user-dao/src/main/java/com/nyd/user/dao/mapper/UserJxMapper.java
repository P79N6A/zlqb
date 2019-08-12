package com.nyd.user.dao.mapper;

import com.nyd.user.entity.UserJx;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liuqiu
 */
@Mapper
public interface UserJxMapper {
    /**
     * 查询已经推单外审确认的所有用户
     * @param userId
     * @return
     */
    List<UserJx> getUserJxByUserId(String userId);
}
