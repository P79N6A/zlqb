package com.nyd.zeus.service;

import com.nyd.zeus.model.RepayInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by zhujx on 2018/1/8.
 */
public interface RepayService {

    ResponseData save(RepayInfo repayInfo) throws Exception;

    ResponseData getRepayInfoByBillNo(String billNo) throws Exception;
}
