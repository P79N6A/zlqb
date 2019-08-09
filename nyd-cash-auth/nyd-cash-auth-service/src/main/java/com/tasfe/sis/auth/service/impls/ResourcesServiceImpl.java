package com.tasfe.sis.auth.service.impls;

import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.params.Page;
import com.tasfe.framework.crud.core.CrudTemplate;
import com.tasfe.sis.auth.entity.LoginRole;
import com.tasfe.sis.auth.entity.Resources;
import com.tasfe.sis.auth.entity.RoleResources;
import com.tasfe.sis.auth.model.*;
import com.tasfe.sis.auth.service.ResourcesService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lait on 2017/7/31.
 */
@Service
public class ResourcesServiceImpl implements ResourcesService {

    //@Autowired
    //private CrudService crudService;

    @Autowired
    private CrudTemplate crudTemplate;

    /**
     * 创建菜单
     *
     * @param resources
     */
    @Override
    public void createMenu(Resources resources) {
        //Resources resources = new Resources();
        //BeanUtils.copyProperties(resources,resources);
        // 设置类型为菜单类型
        //resources.setTyp(ResourcesType.menu.toString());
        // 执行插入
        //crudService.insert(resources);
    }

    @Override
    public void createResources(Resources resources) throws Exception {
        crudTemplate.save(resources);
    }

    @Override
    public void updResources(Resources resources) throws Exception {
        crudTemplate.update(resources, Criteria.from(Resources.class));
    }

    /**
     * 获取用户权限所对应的菜单
     *
     * @param roleResources
     * @return
     */
    @Override
    public List<MenuInfos> getMenuInfos(RoleResources roleResources) {
        List<MenuInfos> menuInfosList = new ArrayList<>();

        //1. 首先获取用户的角色
        LoginRole accountRole = new LoginRole();
        BeanUtils.copyProperties(roleResources, accountRole);
        //crudService.getEntitys(accountRole);

        //2. 根据角色获取角色所对应的资源


        //3. 找到资源进行过滤，找出所有的菜单资源


        return menuInfosList;
    }


    @Override
    public List<ResourcesInfos> getResourcesInfos(RoleResources roleResources) {
        return null;
    }


    @Override
    public Page<Resources> pagingRosources(ResourcesInfos resourcesInfos, Criteria criteria) throws Exception {
        Page page = crudTemplate.paging(resourcesInfos, criteria);
        return page;
    }


    @Override
    public List<Resources> listResources(Resources resources) throws Exception {
        return crudTemplate.find(resources, Criteria.from(Resources.class));
    }

    @Override
    public List<ResourcesTree> listResourcesTree(Resources resources) throws Exception {
        List<Resources> resources1 = crudTemplate.find(resources, Criteria.from(Resources.class));
        List<ResourcesTree> treeList = treeMenuList(resources1, 0);
        return treeList;
    }


    private List<ResourcesTree> treeMenuList(List<Resources> list, long parentId) {
        List<ResourcesTree> treeNodes = new ArrayList<>();
        for (Resources object : list) {
            ResourcesTree treeNode = new ResourcesTree();
            BeanUtils.copyProperties(object, treeNode);
            Long menuId = treeNode.getId();
            Long pid = treeNode.getPid();
            if (pid != null && parentId == pid) {
                List<ResourcesTree> c_node = treeMenuList(list, menuId);
                treeNode.setChilds(c_node);
                treeNodes.add(treeNode);
            }
        }
        return treeNodes;
    }
}
