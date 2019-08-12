package com.nyd.admin.service;

import com.nyd.admin.model.dto.FundCreateDto;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by hwei on 2018/1/2.
 */
public interface FundBussinessService {
    ResponseData createFundInfo(FundCreateDto fundCreateDto) throws Exception;
}
