package com.nyd.user.dao;

import com.nyd.user.entity.ExtraInfo;

import java.util.List;

/**
 * Created by Dengw on 17/11/2.
 */
public interface ExtraInfoDao {
    void saveExtraInfo(ExtraInfo extraInfo) throws Exception;

    List<ExtraInfo> getExtraInfosByUserId(String userId) throws Exception;
}
