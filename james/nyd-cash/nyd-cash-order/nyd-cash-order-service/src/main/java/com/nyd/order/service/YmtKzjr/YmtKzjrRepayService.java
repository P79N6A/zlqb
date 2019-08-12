package com.nyd.order.service.YmtKzjr;

import com.nyd.order.model.YmtKzjrBill.KzjrRepayDetail;
import com.tasfe.framework.support.model.ResponseData;

public interface YmtKzjrRepayService {

    /**
     * 根据子订单号找到还款详情页面所需信息
     * @param orderSno
     * @return
     */
    ResponseData<KzjrRepayDetail> findKzjrRepayDetail(String orderSno);
}
