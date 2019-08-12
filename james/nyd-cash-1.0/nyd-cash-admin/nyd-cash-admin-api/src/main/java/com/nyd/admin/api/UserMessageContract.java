package com.nyd.admin.api;

import com.nyd.admin.model.power.dto.UserDto;
import com.nyd.admin.model.power.dto.UserRoleRelDto;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

/**
 * @author yifeng.qiu
 * @date 2018/4/24
 */
public interface UserMessageContract {
    /**
     * 查询对应角色的所有账户信息
     *
     * @param roleId   角色Id
     * @param userType 角色类型
     * @return 符合条件结果集
     */
    ResponseData<List<UserDto>> queryUserInfoByRoleId(Integer userType, Integer roleId);

    /**
     * 根据用户accountNo查询该用户所属角色信息
     *
     * @param accountNo 上送条件
     * @return List<RoleVo>
     */
    ResponseData<List<UserRoleRelDto>> getUserRoleInfoByAccountNo(String accountNo);
}
