package com.nyd.capital.service;

import com.nyd.capital.entity.UserJx;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

/**
 * @author liuqiu
 */
public interface UserJxService {
    /**
     * 保存用户密码
     * @param userJx
     */
    void savePassword(UserJx userJx);

    String selectPasswordByUserId(String userId);

    /**
     * 保存用户在即信的id
     * @param memberId
     * @param userId
     */
    void updateMember(String memberId,String userId);

    /**
     * 保存用户在即信的标ID
     * @param loanId
     * @param userId
     */
    void updateLoanId(String loanId,String userId);

    /**
     * 查询已经推单外审确认的所有用户
     * @return
     */
    List<UserJx> selectUserStageTwo();
    /**
     * 查询
     * @param userId
     * @return
     */
    List<UserJx> getUserJxByUserId(String userId);

    void updateUserJx(UserJx userJx);

    ResponseData<UserJx> selectUserJxByMemberId(String memberId);
}
