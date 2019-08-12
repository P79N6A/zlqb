package com.nyd.capital.service.qcgz;


import com.nyd.capital.model.qcgz.*;
import com.tasfe.framework.support.model.ResponseData;


/**
 * 七彩格子相关接口
 * @author cm
 */

public interface QcgzService {

    /**
     * 资产提交
     */
    ResponseData assetSubmit(SubmitAssetRequest request);



    /**
     * 申请放款
     */
    ResponseData submitLoanApply(LoanApplyRequest request);


    /**
     *查询放款状态
     */
    ResponseData queryLoanApplyResult(QueryLoanApplyResultRequest request);


    /**
     * 放款成功通知
     */
    ResponseData loanSucceesNotify(LoanSuccessNotifyRequest request);

    /**
     * 七彩格子数据修复
     * @param request
     * @return
     */
    ResponseData callbackForFail(LoanFailListRequest request);

}
