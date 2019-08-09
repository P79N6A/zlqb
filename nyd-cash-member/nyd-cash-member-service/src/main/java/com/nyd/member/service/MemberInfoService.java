package com.nyd.member.service;

import com.nyd.member.model.BaseInfo;
import com.nyd.member.model.MemberInfo;
import com.nyd.member.model.MemberModel;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by hwei on 2017/12/7.
 */
public interface MemberInfoService {
    ResponseData<MemberInfo> fetchMemberInfo(BaseInfo baseInfo);
    ResponseData<MemberModel> getMemberWithMemberId(String userId);
}
