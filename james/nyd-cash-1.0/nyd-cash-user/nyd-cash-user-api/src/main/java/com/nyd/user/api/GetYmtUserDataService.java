package com.nyd.user.api;

import com.nyd.user.model.dto.YmtUserDto;
import com.tasfe.framework.support.model.ResponseData;

/**
 *同步银码头用户数据到侬要贷
 * @author chaiming
 *
 */

public interface GetYmtUserDataService {


    /**
     * 银码头用户数据同步到侬要贷相关表中
     */
    ResponseData saveYmtUserData(YmtUserDto ymtUserDto) throws Exception;
    /**
     * 银码头用户数据同步到侬要贷相关表中
     */
    ResponseData saveYmtUserDataAsStep(YmtUserDto ymtUserDto) throws Exception;
}
