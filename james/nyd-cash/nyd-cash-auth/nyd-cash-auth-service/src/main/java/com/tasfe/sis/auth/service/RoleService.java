package com.tasfe.sis.auth.service;

import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.params.Page;
import com.tasfe.sis.auth.entity.Role;
import com.tasfe.sis.auth.model.RoleInfos;

import java.util.List;

/**
 * Created by Lait on 2017/7/31.
 */
public interface RoleService {

    /**
     * 创建role
     *
     * @param role
     */
    void createRole(Role role) throws Exception;

    /**
     * 更新角色
     *
     * @param role
     */
    void updateRole(Role role) throws Exception;

    /**
     * 获取角色
     *
     * @param id
     */
    Role getRole(Long id) throws Exception;

    /**
     * 获取角色集合
     *
     * @param ids
     */
    List<Role> getRoles(Long... ids) throws Exception;

    List<Role> listRole(Role role) throws Exception;

    void delRole(Long id) throws Exception;

    Page<Role> pagingRole(RoleInfos roleInfos, Criteria criteria) throws Exception;

    List<Role> getRoleByName(String roleName) throws Exception;
}