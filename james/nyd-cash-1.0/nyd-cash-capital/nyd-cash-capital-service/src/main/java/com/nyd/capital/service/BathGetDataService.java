package com.nyd.capital.service;

import com.nyd.capital.model.kzjr.BathFundResult;
import com.nyd.order.model.OrderDetailInfo;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.msg.OrderMessage;
import com.tasfe.framework.support.model.ResponseData;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 21:13 2018/5/14
 */
public interface BathGetDataService {

    ResponseData<OrderInfo> getByOrderNo(String OrderNo);

    ResponseData<OrderDetailInfo> getDetailByOrderNo(String OrderNo);

    ResponseData assetSubmit(OrderMessage message , BathFundResult bathFundResult);
}
