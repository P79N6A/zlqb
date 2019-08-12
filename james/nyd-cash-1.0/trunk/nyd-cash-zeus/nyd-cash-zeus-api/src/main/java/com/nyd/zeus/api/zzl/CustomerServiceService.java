package com.nyd.zeus.api.zzl;

import java.util.ArrayList;
import java.util.List;

import com.nyd.zeus.model.PaymentRiskExcludeInfoRequest;
import com.nyd.zeus.model.PaymentRiskExcludeInfoResult;
import com.nyd.zeus.model.common.PagedResponse;
import com.tasfe.framework.support.model.ResponseData;
/**
 * 客服管理
 * @author dengqingfeng
 *
 */
public interface CustomerServiceService {
	/**
	 * 新增风控扣款排除表信息
	 * @param orderNo
	 * @return ResponseData
	 */
	ResponseData save(String orderNo)  throws Exception;

    /**
     * 获取风控扣款排除表信息
     * @param qaymentRiskExcludeInfoRequest
     * @return ResponseData
     * @throws Exception
     */
	PagedResponse<List<PaymentRiskExcludeInfoResult>> queryList(PaymentRiskExcludeInfoRequest qaymentRiskExcludeInfoRequest) throws Exception;
	/**
	 * 根据贷款编号删除风控扣款排除表信息
	 * @param orderNo
	 * @return ResponseData
	 */
    ResponseData deleteByOrderNo(String orderNo)  throws Exception;
	/**
	 * 统计风控扣款排除表信息
	 * @param orderNo
	 * @return ResponseData
	 */
    ResponseData queryListCount(PaymentRiskExcludeInfoRequest qaymentRiskExcludeInfoRequest)  throws Exception;
}
