package com.nyd.zeus.api;

import com.nyd.zeus.model.BillInfo;
import com.nyd.zeus.model.OverdueBillInfo;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

/**
 * Created by zhujx on 2017/11/18.
 */
public interface BillContract {

    ResponseData<List<BillInfo>> queryBillInfoByUserId(String userId);

    ResponseData<List<BillInfo>> queryBillInfoByOrderNO(String orderNo);

    ResponseData<List<BillInfo>> queryBillInfoByYmtOrderNo(String ibankOrderNo);

    ResponseData<List<OverdueBillInfo>> queryOverdueBillInfoByUserId(String userId);

    ResponseData<List<OverdueBillInfo>> queryOverdueBillInfoByOrderNO(String orderNo);

    /**
     * 查询未还清账单信息
     * @param userId
     * @return
     */
    ResponseData<Integer> getBillInfos(String userId);

    ResponseData<BillInfo> getBillInfo(String billInNo);

    ResponseData saveBillInfo(BillInfo billInfo);

    ResponseData updateBillInfoByBillNo(BillInfo billInfo);

    ResponseData saveOverdueBillInfo(OverdueBillInfo overdueBillInfo);

    ResponseData updateOverdueBillInfoByBillNo(OverdueBillInfo overdueBillInfo);

    ResponseData<BillInfo> getBillInfoByUid(String userId);

}
