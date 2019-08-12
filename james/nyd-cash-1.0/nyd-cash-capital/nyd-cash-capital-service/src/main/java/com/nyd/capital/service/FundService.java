package com.nyd.capital.service;

import com.nyd.capital.model.WsmQuery;
import com.nyd.capital.model.enums.RemitStatus;
import com.nyd.capital.service.validate.ValidateException;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

/**
 * Cong Yuxiang
 * 2017/11/13
 **/
public interface FundService {
    /**
     *  提交订单
     * @return
     */
    ResponseData sendOrder(List orderList);

    /**
     * 结果信息保存
     */
    boolean saveLoanResult(String result);

    /**
     *  查询订单信息
     * @return
     */
    String queryOrderInfo(WsmQuery query);

    /**
     * 生成订单
     * @return
     */
    List generateOrdersTest();

    /**
     * 创建资金端放款的订单列表
     * @param userId UserId
     * @param orderNo 订单号
     * @return List
     * @throws ValidateException ValidateException
     */
    List generateOrders(String userId,String orderNo,Integer channel) throws ValidateException;
}
