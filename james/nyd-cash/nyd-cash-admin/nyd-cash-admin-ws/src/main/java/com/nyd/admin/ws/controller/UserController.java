package com.nyd.admin.ws.controller;

import com.nyd.admin.model.power.dto.UserDto;
import com.nyd.admin.model.power.dto.UserRoleDto;
import com.nyd.admin.service.UserService;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zhujx on 2018/1/3.
 */
@RestController
@RequestMapping("/admin/user")
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    /**
     * 查询该用户的所有权限
     * @param userDto
     * @return
     */
    @RequestMapping("/queryAllPower")
    @ResponseBody
    public ResponseData queryAllPower(@RequestBody UserDto userDto) {
        try {
            return ResponseData.success(userService.getUserPowerVoByUserId(userDto));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseData.error();
    }


    /**
     * 新建用户
     * @param userDto
     * @return
     */
    @RequestMapping("/saveUser")
    @ResponseBody
    public ResponseData saveUserInfo(@RequestBody UserDto userDto) {
        try {
            return userService.saveUserInfo(userDto);
        } catch (Exception e) {
            return ResponseData.error();
        }
    }


    /**
     * 编辑用户
     * @param userDto
     * @return
     */
    @RequestMapping("/updateUser")
    @ResponseBody
    public ResponseData updateUserInfo(@RequestBody UserDto userDto) {
        boolean flag = userService.updateUserInfo(userDto);
        if(flag) {
            return ResponseData.success();
        }else {
            return ResponseData.error();
        }
    }


    /**
     * 分页查询用户
     * @param userDto
     * @return
     */
    @RequestMapping("/getUserLs")
    @ResponseBody
    public ResponseData getUserLs(@RequestBody UserDto userDto) {
        try {
            return ResponseData.success(userService.getUserLs(userDto));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseData.error();
    }

    /**
     * 根据用户id查询用户包含的角色
     */
    @RequestMapping(value = "/findRolesByUser", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseData findPowersByRole(@RequestBody UserDto userDto) {
        try {
            return userService.findRolesByUser(userDto);
        } catch (Exception e) {
            return ResponseData.error("服务器开小差了");
        }
    }


    /**
     * 编辑用户角色
     */
    @RequestMapping(value = "/editUserRole", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseData editUserRole(@RequestBody UserRoleDto userRoleDto) {
        try {
            return userService.editUserRole(userRoleDto);
        } catch (Exception e) {
            return ResponseData.error("服务器开小差了");
        }
    }


    /**
     *根据用户名查找具体用户信息
     */
    @RequestMapping(value = "/findByName",method = RequestMethod.POST,produces = "application/json")
    public ResponseData findByUserName(@RequestBody UserDto userDto){
        logger.info("根据用户姓名查找用户具体信息,请求参数:"+userDto.getUserName());
        ResponseData responseData = userService.findUserByName(userDto.getUserName());
        return  responseData;
    }

}
