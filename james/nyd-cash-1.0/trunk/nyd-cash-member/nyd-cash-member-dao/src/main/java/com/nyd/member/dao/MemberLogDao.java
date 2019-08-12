package com.nyd.member.dao;

import com.nyd.member.entity.MemberLog;
import com.nyd.member.model.MemberLogModel;

import java.util.List;

/**
 * Created by Dengw on 2017/12/6
 */
public interface MemberLogDao {
    void save(MemberLog memberLog) throws Exception;

    void update(String userId) throws Exception;

    MemberLogModel getMemberLogByUserId(String userId) throws Exception;
    
    MemberLogModel getMemberLogWithMemberIdByUserId(String userId) throws Exception;

    void updateMemberLogByUserId(MemberLog memberLog) throws Exception;

    MemberLogModel getMemberLogByUserIdAndDibitFlag(String userId,String debitFlag) throws Exception;


    List<MemberLogModel> getMemberLog(String userId, String debitFlag) throws Exception;
    
    MemberLogModel findMemberLogByUserId(String userId) throws Exception;
}
