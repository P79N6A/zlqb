package com.nyd.admin.service;

import com.nyd.admin.model.dto.FundCreateDto;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by hwei on 2017/12/29.
 */
public interface FundManageService {

    ResponseData queryFundConfig();

    ResponseData createFundInfo(FundCreateDto fundCreateDto) throws Exception;
}
