package com.tasfe.sis.auth.service.impls;

import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.sis.auth.entity.LoginRole;
import com.tasfe.sis.auth.entity.Role;
import com.tasfe.sis.auth.model.AccountInfos;
import com.tasfe.sis.auth.service.AccountRoleService;
import com.tasfe.sis.auth.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lait on 2017/8/1.
 */
@Service
public class AccountRoleServiceImpl implements AccountRoleService {

    //@Autowired
    //private CrudService crudService;

    @Autowired
    private CrudTemplate crudTemplate;


    @Autowired
    private RoleService roleService;

    /**
     * 获取角色id集合
     *
     * @param aid
     * @return
     */
    @Override
    public List<Long> getRoleIdsByAccountId(Long aid) throws Exception {
        LoginRole accountRole = new LoginRole();
        accountRole.setAid(aid);
        List<LoginRole> accountRoleList = crudTemplate.find(accountRole, Criteria.from(LoginRole.class));
        List<Long> rids = new ArrayList<>();
        accountRoleList.stream().forEach(ars -> {
            rids.add(ars.getRid());
        });
        return rids;
    }

    /**
     * 获取角色集合
     *
     * @param aid
     * @return
     */
    @Override
    public List<Role> getRolesByAccountId(Long aid) throws Exception {
        List<Role> roles = new ArrayList<>();
        List<Long> rids = getRoleIdsByAccountId(aid);
        if ((rids != null) && (rids.size() > 0)) {
            Object[] objects = rids.toArray();
            Long[] ids = new Long[rids.size()];
            for (int i = 0; i < objects.length; i++) {
                ids[i] = (Long) objects[i];
            }
            roles = roleService.getRoles(ids);
        }
        return roles;
    }

    @Override
    public void correlateRoleAndAccount(AccountInfos accountInfos) throws Exception {
        List<Role> roles = roleService.getRoleByName(accountInfos.getRoleNames());
        if (roles.size() == 0) {
            throw new Exception();
        }
        for (Role role : roles) {
            LoginRole accountRole = new LoginRole();
            accountRole.setRid(role.getId());
            accountRole.setAid(new Long(accountInfos.getAccountId()));
            crudTemplate.save(accountRole);
        }
    }
}
