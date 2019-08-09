package com.nyd.zeus.api.zzl;

import com.nyd.zeus.model.BillInfo;
import com.nyd.zeus.model.common.CommonResponse;

import java.util.List;

public interface ZeusForLXYService {
    CommonResponse<BillInfo> queryBillInfoByOrderNO(String orderNo);
}
