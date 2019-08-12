package com.nyd.capital.api.service;

import com.nyd.capital.model.qcgz.SubmitAssetRequest;
import com.tasfe.framework.support.model.ResponseData;

/**
 * @author liuqiu
 */
public interface QcgzApi {

    /**
     * 资产提交
     * @param request
     * @return
     */
    ResponseData assetSubmit(SubmitAssetRequest request);
}
