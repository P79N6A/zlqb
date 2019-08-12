package com.nyd.admin.dao.mapper;

import com.nyd.admin.dao.CrudDao;
import com.nyd.admin.entity.power.User;
import com.nyd.admin.model.power.UserPowerInfo;
import com.nyd.admin.model.power.vo.UserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zhujx on 2018/1/3.
 */
public interface UserMapper extends CrudDao<User> {

    List<UserPowerInfo> getUserPowerVoByUserId(Integer id);

    List<UserPowerInfo> getUserPowerVoByPowerId(List<Integer> powerList);

    List<User> queryUserInfoByRoleId(@Param("userType") Integer userType, @Param("roleId") Integer roleId);

    /**
     * 根据用户姓名模糊查找
     * @param voquery
     * @return
     */
    List<User> getUserInfoByUserName(UserVo voquery);
}
