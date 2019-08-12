package com.nyd.order.service.YmtKzjr;

import com.nyd.order.model.YmtKzjrBill.BillYmtInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * @author cm
 */
public interface BillYmtService {

    /**
     *
     * 根据资产找到账单信息
     * @param assetCode
     * @return
     */
    ResponseData<BillYmtInfo> findByAssetCode(String assetCode);

}
