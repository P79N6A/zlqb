package com.nyd.zeus.api;

import com.nyd.zeus.model.RepayInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by zhujx on 2017/11/21.
 */
public interface RepayContract {

    ResponseData save(RepayInfo repayInfo) throws Exception;

    ResponseData getRepayInfoByBillNo(String billNo) throws Exception;

}
