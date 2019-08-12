package com.nyd.admin.ws.controller;


import com.nyd.admin.model.power.dto.RoleDto;
import com.nyd.admin.model.power.dto.RolePowerDto;
import com.nyd.admin.model.power.dto.UserDto;
import com.nyd.admin.service.RoleService;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * hwei
 * 2018/1/4
 **/
@RestController
@RequestMapping("/admin/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    /**
     * 新建角色
     * @return
     */
    @RequestMapping(value = "/saveRole", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseData saveRole(@RequestBody RoleDto roleDto) {
        try {
            return roleService.saveRole(roleDto);
        } catch (Exception e) {
            return ResponseData.error("服务器开小差了");
        }
    }

    /**
     * 更新角色
     * @return
     */
    @RequestMapping(value = "/updateRole", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseData updateRole(@RequestBody RoleDto roleDto) {
        try {
            return roleService.updateRole(roleDto);
        } catch (Exception e) {
            return ResponseData.error("服务器开小差了");
        }
    }

    /**
     * 分页查询角色
     * @param roleDto
     * @return
     */
    @RequestMapping("/getRoleLs")
    @ResponseBody
    public ResponseData getRoleLs(@RequestBody RoleDto roleDto) {
        try {
            return ResponseData.success(roleService.getRoleLs(roleDto));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseData.error();
    }

    /**
     * 查询所有角色
     * @param roleDto
     * @return
     */
    @RequestMapping(value = "/findRoles", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseData findRole(@RequestBody RoleDto roleDto) {
        try {
            return roleService.findRoles(roleDto);
        } catch (Exception e) {
            return ResponseData.error("服务器开小差了");
        }
    }

    /**
     * 根据角色id查询角色包含的权限
     */
    @RequestMapping(value = "/findPowersByRole", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseData findPowersByRole(@RequestBody RoleDto roleDto) {
        try {
            return roleService.findPowersByRole(roleDto);
        } catch (Exception e) {
            return ResponseData.error("服务器开小差了");
        }
    }

    /**
     * 编辑角色权限
     */
    @RequestMapping(value = "/editRolePower", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseData editRolePower(@RequestBody RolePowerDto rolePowerDto) {
        try {
            return roleService.editRolePower(rolePowerDto);
        } catch (Exception e) {
            return ResponseData.error("服务器开小差了");
        }
    }
}
