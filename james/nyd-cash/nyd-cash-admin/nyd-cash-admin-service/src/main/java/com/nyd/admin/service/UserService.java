package com.nyd.admin.service;

import com.github.pagehelper.PageInfo;
import com.nyd.admin.model.power.UserPowerInfo;
import com.nyd.admin.model.power.dto.UserDto;
import com.nyd.admin.model.power.dto.UserRoleDto;
import com.nyd.admin.model.power.vo.UserVo;
import com.tasfe.framework.support.model.ResponseData;

import java.text.ParseException;
import java.util.List;

/**
 * @author Peng
 * @create 2017-12-19 15:31
 **/
public interface UserService {

    /**
     * 登录
     * @param userDto
     * @return
     */
    ResponseData getUserInfo(UserDto userDto);

    /**
     * 保存用户
     * @param userDto
     * @return
     */
    ResponseData saveUserInfo(UserDto userDto);

    /**
     * 更新用户信息
     * @param userDto
     * @return
     */
    boolean updateUserInfo(UserDto userDto);

    /**
     * 分页查询
     */
    PageInfo<UserVo> getUserLs(UserDto userDto) throws ParseException;

    /**
     * 根据用户id查询该用户所有权限
     * @param userDto
     * @return
     */
    List<UserPowerInfo> getUserPowerVoByUserId(UserDto userDto);

    ResponseData findRolesByUser(UserDto userDto);

    /**
     * 编辑用户角色
     * @param userRoleDto
     * @return
     */
    ResponseData editUserRole(UserRoleDto userRoleDto);


    ResponseData findUserByName(String userName);
}
