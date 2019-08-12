package com.nyd.capital.api.service;

import java.math.BigDecimal;

import com.nyd.order.model.msg.OrderMessage;
import com.tasfe.framework.support.model.ResponseData;

/**
 * @author liuqiu
 */
public interface CapitalApi {

    /**
     * 签约推送资产
     * @param message
     * @return
     */
    ResponseData sendCapital(OrderMessage message);
    /**
     * 提送资产
     * @param message
     * @return
     */
    ResponseData sendCapitalAfterRisk(OrderMessage message);
    
    /**
     * 资金风控通过后重新推送资产
     * @param message
     * @return
     */
    ResponseData reSendCapitalAfterRiskByOrderNo(String orderNo) throws Exception;

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
    ResponseData withholdTaskRetry1Hour();
    /**
     * 放款成功后的代扣跑批
     * @return
     */
    ResponseData withholdTask10Min();
    
    /**
     * 查询口袋理财待放款的数据
     */
    ResponseData queryKdlcWaitLoan(String funCode);
    
    /**
     * 查询口袋理财创建订单处理中的订单
     */
	void kdlcQueryCreateOrder();
	
	 /**
     * 放款拒绝后的代扣跑批
     * @return
     */
	ResponseData withholdRefusedTask();
	
	 /**
     * 放款拒绝后的代扣跑批
     * @return
     */
    ResponseData withholdRefusedTaskRetry1Hour();
    /**
     * 放款拒绝后的代扣跑批
     * @return
     */
    ResponseData withholdRefusedTask10Min();

    ResponseData pushPocketOrder(String order);
}
