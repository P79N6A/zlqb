package com.tasfe.sis.auth.service;

import com.tasfe.sis.auth.entity.Role;
import com.tasfe.sis.auth.model.AccountInfos;

import java.util.List;

/**
 * Created by Lait on 2017/8/1.
 */
public interface AccountRoleService {

    /**
     * 根据账户id获取账户所对应的角色
     *
     * @param aid
     */
    List<Long> getRoleIdsByAccountId(Long aid) throws Exception;


    List<Role> getRolesByAccountId(Long aid) throws Exception;

    void correlateRoleAndAccount(AccountInfos accountInfos) throws Exception;

}
