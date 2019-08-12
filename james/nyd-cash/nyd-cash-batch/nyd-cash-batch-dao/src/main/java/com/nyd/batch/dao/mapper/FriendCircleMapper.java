package com.nyd.batch.dao.mapper;


import com.nyd.batch.entity.FriendCircle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * yuxiang.cong
 */
@Mapper
public interface FriendCircleMapper {
    List<FriendCircle> selectByMobile(@Param(value = "mobile") String mobile);
}