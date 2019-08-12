package com.nyd.capital.service;

import com.nyd.capital.entity.UserPocket;

import java.util.List;

/**
 * @author liuqiu
 */
public interface UserPocketService {
    /**
     * 保存用户密码
     * @param userPocket
     */
    void savePassword(UserPocket userPocket);

    /**
     * 修改用户
     * @param userPocket
     */
    void update(UserPocket userPocket);

    /**
     * 查询密码
     * @param userId
     * @return
     */
    String selectPasswordByUserId(String userId);

    /**
     * 查询口袋理财用户
     * @param userId
     * @return
     */
    UserPocket selectPocketUserByUserId(String userId);

    /**
     * 通过进度查询口袋理财用户
     * @param code
     * @return
     */
    List<UserPocket> selectPocketByStage(Integer code);
}
