package com.nyd.member.dao;

import com.nyd.member.entity.MemberConfig;
import com.nyd.member.model.MemberConfigModel;

import java.util.List;

/**
 * Created by Dengw on 2017/12/6
 */
public interface MemberConfigDao {
    void save(MemberConfig memberConfig) throws Exception;

    List<MemberConfigModel> getMermberConfigByBusiness(String business) throws Exception;

    MemberConfigModel getMermberConfigByType(String type) throws Exception;
    
    MemberConfigModel getMermberConfigByTypeBusiness(String type,String business)throws Exception;
}
