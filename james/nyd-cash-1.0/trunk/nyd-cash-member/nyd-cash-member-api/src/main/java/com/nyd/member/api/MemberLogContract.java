package com.nyd.member.api;

import com.nyd.member.entity.MemberLog;
import com.nyd.member.model.MemberLogModel;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

/**
 * Created by Dengw on 2017/12/6
 */
public interface MemberLogContract {
    /**
     * 保存购买会员记录
     * @param memberLogModel 会员信息
     * @return ResponseData
     */
    ResponseData saveMemberLog(MemberLogModel memberLogModel);


    /**
     * 扣款成功更新会员记录
     */
    ResponseData updateMemberLog(String userId);


    /**
     * 更新表t_member_log使用现金券相关记录
     */
    void updateMemberLogByUserId(MemberLog memberLog) throws Exception;

    /**
     * 获取代扣的会员id
     *
     */
    ResponseData<MemberLogModel> getMemberLogByUserIdAndDibitFlag(String userId,String debitFlag);

    /**
     * 获取代扣的会员id
     * @param userId
     * @param debitFlag
     * @return
     */
    ResponseData<List<MemberLogModel>> getMemberLog(String userId, String debitFlag);
    
    
    ResponseData<MemberLogModel> getMemberLogByUserId(String userId);
    
    
}
