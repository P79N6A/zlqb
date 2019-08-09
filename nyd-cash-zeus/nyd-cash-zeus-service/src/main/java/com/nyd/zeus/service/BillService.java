package com.nyd.zeus.service;

import com.nyd.zeus.model.BillDetail;
import com.nyd.zeus.model.BillInfo;
import com.nyd.zeus.model.OverdueBillInfo;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

/**
 * Created by zhujx on 2017/11/18.
 */
public interface BillService {

    ResponseData saveBillInfo(BillInfo billInfo);

    ResponseData<BillInfo> getBillInfo(String billNo);

    ResponseData updateBillInfoByBillNo(BillInfo billNo);

    ResponseData<OverdueBillInfo> getOverdueBillInfo(String billNo);

    ResponseData updateOverdueBillInfoByBillNo(OverdueBillInfo overdueBillInfo);

    ResponseData<List<BillDetail>> getBillInfoLs(String userId);

    ResponseData<BillDetail> getBillDetailInfo(String billNo);

    ResponseData queryHlb(BillDetail billDetail);

}
