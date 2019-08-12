package com.nyd.batch.service;

import com.nyd.batch.entity.TMember;
import com.nyd.batch.entity.TMemberConfig;
import com.nyd.batch.entity.TMemberLog;

import java.util.List;

/**
 * Cong Yuxiang
 * 2017/12/27
 **/
public interface MemberService {
    List<TMemberLog> getMemberLog();
    TMember getMemberByUserId(String userid);
    TMemberConfig getMemberConfigByType(String type);

    List<TMemberLog> selectByUserId(String userId);
    List<TMemberLog> selectByOrderNo(String orderNo);

    TMemberLog selectByMemberId(String memberId);

    void selectInnerTestAop();
}
