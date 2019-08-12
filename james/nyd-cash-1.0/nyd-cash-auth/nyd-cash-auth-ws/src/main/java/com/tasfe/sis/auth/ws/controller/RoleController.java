package com.tasfe.sis.auth.ws.controller;

import com.tasfe.framework.crud.api.criteria.Criteria;
import com.tasfe.framework.crud.api.params.Page;
import com.tasfe.sis.auth.entity.Resources;
import com.tasfe.sis.auth.entity.Role;
import com.tasfe.sis.auth.entity.RoleResources;

import com.tasfe.sis.auth.model.RoleInfos;
import com.tasfe.sis.auth.service.RoleResourcesService;
import com.tasfe.sis.auth.service.RoleService;
import com.tasfe.zh.base.model.response.ResponseData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Lait on 2017/8/17.
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleResourcesService roleResourcesService;

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
    public ResponseData addRole(@RequestBody RoleInfos roleInfos) throws Exception {
        Role role = new Role();
        BeanUtils.copyProperties(roleInfos, role);
        ResponseData<Role> responseData = new ResponseData();
        roleService.createRole(role);
        if (roleInfos.getResourcesIds() != null) {
            roleResourcesService.addResourcesWithRole(role.getId(), roleInfos.getResourcesIds());
        }
        responseData.setCode("");
        responseData.setMsg("");
        responseData.setData(role);
        return responseData;
    }

    /**
     * 增加角色并关联角色所对应的资源
     *
     * @param roleResources
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addRoleResources", method = RequestMethod.POST, produces = "application/json")
    public ResponseData addRoleResources(@RequestBody RoleResources roleResources) throws Exception {
        roleResourcesService.save(roleResources);
        return null;
    }


    @RequestMapping("/upd")
    public void updRole(@RequestBody RoleInfos roleInfos) throws Exception {
        Role role = new Role();
        BeanUtils.copyProperties(roleInfos, role);
        roleService.updateRole(role);
        if (roleInfos.getResourcesIds() != null) {
            roleResourcesService.addResourcesWithRole(roleInfos.getId(), roleInfos.getResourcesIds());
        }
    }


    /**
     * 删除role
     *
     * @param id
     * @throws Exception
     */
    @RequestMapping("/del")
    public void delRole(@RequestParam Long id) throws Exception {
        roleService.delRole(id);
    }


    @RequestMapping(value = "/selectRole", method = RequestMethod.POST, produces = "application/json")
    public ResponseData selectRole(@RequestBody RoleInfos roleInfos) throws Exception {
        ResponseData<List> responseData = new ResponseData();
        Role role = new Role();
        List<Role> roleList = roleService.listRole(role);
        List<String> stringList = new ArrayList<>();
        roleList.forEach(new Consumer<Role>() {
            @Override
            public void accept(Role s) {
                stringList.add(s.getName());
            }
        });
        responseData.setData(stringList);
        responseData.setCode("100000");
        responseData.setMsg("success");
        return responseData;
    }


    /**
     * 分页列出角色
     *
     * @param roleInfos
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json")
    public ResponseData listRole(@RequestBody RoleInfos roleInfos) throws Exception {
        ResponseData<Page> responseData = new ResponseData();
        Page<Role> page = roleService.pagingRole(roleInfos, Criteria.from(Role.class).limit(Page.resolve(roleInfos.getPageNo(), roleInfos.getPageSize())));
        responseData.setData(page);
        responseData.setCode("100000");
        responseData.setMsg("success");
        return responseData;
    }


    @RequestMapping(value = "/listResourcesByRoleId", method = RequestMethod.POST, produces = "application/json")
    public ResponseData listResourcesByRoleId(@RequestBody RoleInfos roleInfos) throws Exception {
        ResponseData<List> responseData = new ResponseData();
        List<Resources> resourcesList = roleResourcesService.getResourcesWithRoleIdAndType(roleInfos.getId(), null);
        responseData.setData(resourcesList);
        responseData.setCode("100000");
        responseData.setMsg("success");
        return responseData;
    }


}
