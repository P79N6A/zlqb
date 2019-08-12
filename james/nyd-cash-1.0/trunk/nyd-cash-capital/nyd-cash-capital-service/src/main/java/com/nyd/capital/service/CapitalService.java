package com.nyd.capital.service;

import java.math.BigDecimal;
import java.util.List;

import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.msg.OrderMessage;
import com.tasfe.framework.support.model.ResponseData;


/**
 * @author liuqiu
 */
public interface CapitalService {

    /**
     * 提送资产
     * @param message
     * @param ifAuto 是否自动推送
     * @return
     */
    ResponseData newSendCapital(OrderMessage message,boolean ifAuto);
    /**
     * 提送资产
     * @param message
     * @return
     */
    ResponseData sendCapitalAfterRisk(OrderMessage message);
    /**
     * 提送资产
     * @param message
     * @return
     */
    ResponseData reSendCapitalAfterRiskWithCheck(OrderMessage message);
    /**
     * 资金风控通过后重新推送资产
     * @param orderInfos
     * @return
     */
    ResponseData reSendCapitalAfterRisk(List<OrderInfo> orderInfos) throws Exception;


    /**
     * 查询放款
     * @return
     */
    ResponseData queryLoan(String fundCode);
    /**
     * 查询放款
     * @return
     */
    BigDecimal queryDldUserBalance();

    /**
     * 放款成功后的代扣跑批
     * @return
     */
    ResponseData withholdTask();
    /**
     * 放款成功后的代扣跑批
     * @return
     */
    ResponseData withholdTask10Min();
    /**
     * 放款成功后的代扣跑批
     * @return
     */
    ResponseData withholdTaskRetry1Hour();
    
    /**
     * 查询口袋理财待放款的数据
     */
    ResponseData queryKdlcWaitLoan(String funCode);
    
    
}
