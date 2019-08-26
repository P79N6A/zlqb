package com.nyd.user.dao.mapper;

import com.nyd.user.entity.UserBlackList;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserBlackListMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserBlackList record);

    int insertSelective(UserBlackList record);

    UserBlackList selectByPrimaryKey(Integer id);

    List<UserBlackList> selectByCondistion(Map map);

    int updateByPrimaryKeySelective(UserBlackList record);

    int updateByPrimaryKey(UserBlackList record);
}