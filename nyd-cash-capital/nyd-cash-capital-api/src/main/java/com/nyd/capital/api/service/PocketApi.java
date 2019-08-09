package com.nyd.capital.api.service;

import com.nyd.capital.model.pocket.PocketAccountDetailDto;
import com.tasfe.framework.support.model.ResponseData;

/**
 * @author liuqiu
 */
public interface PocketApi {

    /**
     * 查询用户口袋理财开户情况
     *
     * @param dto
     * @param ifAgain
     * @return
     */
    ResponseData selectUserOpenDetail(PocketAccountDetailDto dto,boolean ifAgain);


    /**
     * 查询口袋理财用户
     *
     * @param userId
     * @return
     */
    ResponseData selectUserPocket(String userId);
}
