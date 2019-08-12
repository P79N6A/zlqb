package com.nyd.capital.dao.mappers;

import com.nyd.capital.entity.UserPocket;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liuqiu
 */
@Mapper
public interface UserPocketMapper {

    /**
     * 保存用户密码
     *
     * @param userPocket
     */
    void savePassword(UserPocket userPocket);

    /**
     * 根据用户id查询密码
     *
     * @param userId
     * @return
     */
    UserPocket selectPasswordByUserId(String userId);


    /**
     * 查询已经推单外审确认的所有用户
     * @return
     */
    List<UserPocket> selectUserStageTwo();

    /**
     * 根据userId查询口袋理财用户信息
     * @param userId
     * @return
     */
    List<UserPocket> getUserPocketByUserId(String userId);

    /**
     * 修改
     * @param userPocket
     */
    void updateUserPocket(UserPocket userPocket);

    /**
     * 通过电子账户查询用户
     * @param memberId
     * @return
     */
    UserPocket selectUserPocketByMemberId(String memberId);

    /**
     * 通过进度查询口袋理财用户
     * @param code
     * @return
     */
    List<UserPocket> selectPocketByStage(Integer code);
}
