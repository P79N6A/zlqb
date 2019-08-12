package com.nyd.capital.service.jx;

import com.nyd.capital.model.jx.*;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 即信API
 * @author liuqiu
 */
public interface JxService {

    /**
     * 开户/设置交易密码/缴费授权/还款授权五合一接口
     */
    ResponseData jxFiveComprehensive(JxFiveComprehensiveRequest request);

    /**
     * 即信获取验证码
     */
    ResponseData jxGetCheckCode(JxGetCheckCodeRequest request);


    /**
     * 即信提现
     */
    ResponseData jxWithDraw(JxWithDrawRequest request);

    /**
     * 即信放款查询
     */
    ResponseData jxLoanQuery (JxLoanQueryRequest request);

    /**
	 * 1.推单查询
	 */
    ResponseData queryPushStatus(JxQueryPushStatusRequest jxQueryPushStatusRequest);
	/**
	 * 2.推单外审接口
	 */
    ResponseData pushAudit(JxPushAuditRequest jxPushAuditRequest);
	/**
	 * 3.推单外审确认
	 */
    ResponseData pushAuditConfirm(JxPushAuditConfirmRequest jxpushAuditConfirmRequest);
	/**
	 * 4.推单外审结果查询
	 */
    ResponseData queryPushAuditResult(JxQueryPushAuditResultRequest jxQueryPushAuditResultRequest);

    /**
     * 还款计划查询
     * @param jxQueryLoanPhasesRequest
     * @return
     */
    ResponseData queryLoanPhases(JxQueryLoanPhasesRequest jxQueryLoanPhasesRequest);

    /**
     * 还款
     * @param jxRepaymentsRequest
     * @return
     */
    ResponseData repayments(JxRepaymentsRequest jxRepaymentsRequest);


}
