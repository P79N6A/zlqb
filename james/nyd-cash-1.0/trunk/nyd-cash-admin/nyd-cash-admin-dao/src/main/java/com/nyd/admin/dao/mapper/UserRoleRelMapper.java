package com.nyd.admin.dao.mapper;

import com.nyd.admin.entity.power.UserRoleRel;

import java.util.List;

/**
 * Created by zhujx on 2018/1/5.
 */
public interface UserRoleRelMapper {

    void insertUserRoleList(List<UserRoleRel> list);

    /**
     * 通过accountNo查询用户角色信息
     *
     * @param accountNo 账号
     * @return List<UserRoleRel>
     */
    List<UserRoleRel> getUserRoleInfoByAccountNo(String accountNo);
}
