package com.tasfe.sis.auth.service.impls;

import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.sis.auth.entity.Resources;
import com.tasfe.sis.auth.entity.RoleResources;
import com.tasfe.sis.auth.model.RoleResourcesInfos;
import com.tasfe.sis.auth.service.RoleResourcesService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lait on 2017/8/21.
 */
@Service
public class RoleResourcesServiceImpl implements RoleResourcesService {

    @Autowired
    private CrudTemplate crudTemplate;

    @Override
    public void save(RoleResources roleResources) throws Exception {
        crudTemplate.save(roleResources);
    }

    @Override
    public void addResourcesWithRole(Long roleId, List<Long> resourcesIds) throws Exception {
        List<RoleResources> list = new ArrayList<>();
        for (Long rid : resourcesIds) {
            RoleResources roleResources = new RoleResources();
            roleResources.setRoleId(roleId);
            roleResources.setResourceId(rid);
            list.add(roleResources);
        }
        crudTemplate.save(list);
    }

    @Override
    public List<Resources> getResourcesWithRoleIdAndType(Long roleId, Integer type) throws Exception {
        RoleResources roleResources = new RoleResources();
        roleResources.setRoleId(roleId);
        List<RoleResources> list = crudTemplate.find(roleResources, Criteria.from(RoleResources.class));
        if (list.size() > 0) {
            List<Resources> rList = new ArrayList<>();
            for (RoleResources temptRoleResources : list) {
                Resources record = new Resources();
                record.setId(temptRoleResources.getResourceId());
                record.setTyp(type);
                List<Resources> resources = crudTemplate.find(record, Criteria.from(Resources.class));
                if (!CollectionUtils.isEmpty(resources)) {
                    rList.add(resources.get(0));
                }
            }
            return rList;
        }
        return null;
    }
}
