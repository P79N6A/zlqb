package com.tasfe.sis.auth.service.impls;

import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.enums.Operator;
import com.tasfe.framework.crud.api.params.Page;
import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.sis.auth.entity.Role;
import com.tasfe.sis.auth.model.RoleInfos;
import com.tasfe.sis.auth.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lait on 2017/7/31.
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private CrudTemplate crudTemplate;

    @Override
    public void createRole(Role role) throws Exception {
        crudTemplate.save(role);
    }

    @Override
    public void updateRole(Role role) throws Exception {
        crudTemplate.update(role, Criteria.from(Role.class));
    }

    @Override
    public Role getRole(Long id) throws Exception {
        return crudTemplate.get(Role.class, id);
    }

    @Override
    public List<Role> getRoles(Long... ids) throws Exception {
        return crudTemplate.list(Role.class, ids);
    }

    @Override
    public List<Role> listRole(Role role) throws Exception {
        List<Role> roleList = crudTemplate.list(Role.class);
        return roleList;
    }

    @Override
    public void delRole(Long id) throws Exception {
        crudTemplate.delete(Role.class, id);
    }

    @Override
    public Page<Role> pagingRole(RoleInfos role, Criteria criteria) throws Exception {
        Page page = crudTemplate.paging(role, criteria);
        return page;
    }

    @Override
    public List<Role> getRoleByName(String roleNames) throws Exception {
        String[] names = roleNames.split(",");
        List<Role> resList = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            Role record = new Role();
            record.setName(names[i]);
            List<Role> roles = crudTemplate.find(record, Criteria.from(Role.class).where().and("name", Operator.EQ).endWhere());
            if (roles.size() > 0) {
                resList.add(roles.get(0));
            }
        }
        return resList;
    }


}
