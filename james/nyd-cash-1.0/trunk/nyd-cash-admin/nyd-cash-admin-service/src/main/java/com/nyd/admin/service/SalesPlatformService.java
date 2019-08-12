package com.nyd.admin.service;

import com.nyd.admin.model.dto.SalesPlatformDto;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by hwei on 2018/12/3.
 */
public interface SalesPlatformService {

    /**
     * 注册未填写信息
     * @return
     */
    ResponseData findRegisterUnfilledData(SalesPlatformDto salesPlatformDto);

    /**
     * 资料不完整
     * @return
     */
    ResponseData findDataIncomplete(SalesPlatformDto salesPlatformDto);

    /**
     * 放款成功
     * @return
     */
    ResponseData findLoadSuccess(SalesPlatformDto salesPlatformDto);

    /**
     * 根据手机号查找
     * @param mobile
     * @return
     */
    boolean checkUser(String mobile);
}
