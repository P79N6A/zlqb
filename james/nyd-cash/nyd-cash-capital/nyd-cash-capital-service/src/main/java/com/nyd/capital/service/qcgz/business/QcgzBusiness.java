package com.nyd.capital.service.qcgz.business;

import com.nyd.capital.model.qcgz.LoanApplyRequest;
import com.nyd.capital.model.qcgz.LoanSuccessNotifyRequest;
import com.tasfe.framework.support.model.ResponseData;

/**
 * @author liuqiu
 */
public interface QcgzBusiness {
    ResponseData submitLoanApply(LoanApplyRequest request);

    /**
     * 放款通知后进行相应后续操作
     * @param request
     * @return
     */
    String callBack(LoanSuccessNotifyRequest request);

    String pushZues(LoanSuccessNotifyRequest request);
}
