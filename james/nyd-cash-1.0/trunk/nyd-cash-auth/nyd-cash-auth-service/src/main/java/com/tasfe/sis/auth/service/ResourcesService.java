package com.tasfe.sis.auth.service;

import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.params.Page;
import com.tasfe.sis.auth.entity.Resources;
import com.tasfe.sis.auth.entity.Role;
import com.tasfe.sis.auth.entity.RoleResources;
import com.tasfe.sis.auth.model.*;

import java.util.List;

/**
 * Created by Lait on 2017/7/31.
 */
public interface ResourcesService {

    /**
     * 创建菜单
     *
     * @param resources
     */
    void createMenu(Resources resources);

    /**
     * 创建资源
     *
     * @param resources
     */
    void createResources(Resources resources) throws Exception;


    void updResources(Resources resources) throws Exception;

    /**
     * 获取菜单
     *
     * @param roleResources
     * @return
     */
    List<MenuInfos> getMenuInfos(RoleResources roleResources);


    /**
     * 获取权限对应的资源
     *
     * @param roleResources
     * @return
     */
    List<ResourcesInfos> getResourcesInfos(RoleResources roleResources);


    Page<Resources> pagingRosources(ResourcesInfos roleInfos, Criteria criteria) throws Exception;


    List<Resources> listResources(Resources resources) throws Exception;


    List<ResourcesTree> listResourcesTree(Resources resources) throws Exception;


}
