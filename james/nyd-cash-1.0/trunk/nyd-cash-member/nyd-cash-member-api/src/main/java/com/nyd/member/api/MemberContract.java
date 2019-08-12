package com.nyd.member.api;

import com.nyd.member.model.MemberModel;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

/**
 * Created by Dengw on 2017/12/6
 */
public interface MemberContract {
    /**
     * 保存用户对应会员信息
     * @param memberModel 会员信息
     * @return ResponseData
     */
    ResponseData saveMember(MemberModel memberModel);

    /**
     * 查询用户对应会员信息
     * @param userId 用户ID
     * @return ResponseData
     */
    ResponseData<MemberModel> getMember(String userId);
    ResponseData<MemberModel> getMemberWithMemberId(String userId);

    ResponseData<List<String>> doAssessTask();

}
