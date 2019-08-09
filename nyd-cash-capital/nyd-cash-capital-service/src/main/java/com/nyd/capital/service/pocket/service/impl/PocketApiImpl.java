package com.nyd.capital.service.pocket.service.impl;

import com.nyd.capital.api.service.PocketApi;
import com.nyd.capital.model.pocket.PocketAccountDetailDto;
import com.nyd.capital.service.UserPocketService;
import com.nyd.capital.service.pocket.business.Pocket2Business;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("pocketApi")
public class PocketApiImpl implements PocketApi {


    @Autowired
    private Pocket2Business pocket2Business;
    @Autowired
    private UserPocketService userPocketService;

    @Override
    public ResponseData selectUserPocket(String userId) {
        return ResponseData.success(userPocketService.selectPocketUserByUserId(userId));
    }

    @Override
    public ResponseData selectUserOpenDetail(PocketAccountDetailDto dto,boolean ifAuto) {
        return pocket2Business.selectUserOpenDetail(dto,ifAuto);
    }
}
