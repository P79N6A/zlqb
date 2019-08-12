package com.nyd.member.api;

import com.nyd.member.model.MemberConfigModel;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

/**
 * Created by Dengw on 2017/12/6
 */
public interface MemberConfigContract {
    /**
     * 查询会员配置信息
     * @param business 业务线
     * @return ResponseData
     */
    ResponseData<List<MemberConfigModel>> getMemberConfig(String business);

    ResponseData<MemberConfigModel> getAsseFee();
}
