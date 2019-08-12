package com.tasfe.sis.auth.service;

import com.tasfe.sis.auth.entity.Resources;
import com.tasfe.sis.auth.entity.RoleResources;

import java.util.List;

/**
 * Created by Lait on 2017/8/21.
 */
public interface RoleResourcesService {

    void save(RoleResources roleResources) throws Exception;

    void addResourcesWithRole(Long roleId, List<Long> resourcesIds) throws Exception;

    List<Resources> getResourcesWithRoleIdAndType(Long roleId, Integer type) throws Exception;


}
