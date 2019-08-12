package com.nyd.capital.dao.mappers;

import com.nyd.capital.entity.UserJx;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liuqiu
 */
@Mapper
public interface UserJxMapper {

    /**
     * 保存用户密码
     *
     * @param userJx
     */
    void savePassword(UserJx userJx);

    /**
     * 根据用户id查询密码
     *
     * @param userId
     * @return
     */
    UserJx selectPasswordByUserId(String userId);

    /**
     * 保存用户在即信的id
     *
     * @param memberId
     * @param userId
     */
    void updateMember(@Param("memberId") String memberId, @Param("userId") String userId);

    /**
     * 保存用户在即信的标ID
     *
     * @param loanId
     * @param userId
     */
    void updateLoanId(@Param("loanId") String loanId, @Param("userId") String userId);

    /**
     * 查询已经推单外审确认的所有用户
     * @return
     */
    List<UserJx> selectUserStageTwo();

    /**
     * 查询已经推单外审确认的所有用户
     * @param userId
     * @return
     */
    List<UserJx> getUserJxByUserId(String userId);

    void updateUserJx(UserJx userJx);

    UserJx selectUserJxByMemberId(String memberId);
}
