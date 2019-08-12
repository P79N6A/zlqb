package com.nyd.admin.dao;

import com.nyd.admin.model.power.dto.UserDto;
import com.nyd.admin.model.power.vo.UserRoleRelVo;
import com.nyd.admin.model.power.vo.UserVo;

import java.util.List;

/**
 * @author Peng
 * @create 2017-12-19 15:17
 **/
public interface UserDao {

    /**
     * 登录
     * @param vo
     * @return
     * @throws Exception
     */
    List<UserVo> getUserInfo(UserVo vo) throws Exception;

    /**
     * 新建用户
     * @param vo
     * @throws Exception
     */
    void save(UserVo vo) throws Exception;

    /**
     * 编辑用户信息
     * @param vo
     * @throws Exception
     */
    void update(UserVo vo) throws Exception;

    /**
     * 根据userId删除所有用户角色
     * @param userId
     * @throws Exception
     */
    void deleteRolesByUserId(Integer userId) throws Exception;

    List<UserRoleRelVo> findRolesByUser(UserDto userDtoUserRoleRelVo) throws Exception;

    List<UserRoleRelVo> findRolesByRoleId(Integer roleId) throws Exception;

    /**
     * 更新催收人员信息
     * @param userVo
     * @return
     */
    void updateCollectionUser(UserVo userVo) throws Exception;

    List<UserVo> getUserInfoByUserName(UserVo voquery) throws Exception;
}
