package com.nyd.admin.service;

import com.nyd.admin.model.RemitReportVo;
import com.nyd.admin.model.RepayReportVo;

import java.util.List;

/**
 * Cong Yuxiang
 * 2017/12/27
 **/
public interface FinanceService {
    List<RemitReportVo> queryRemitReportVo(String startDate,String endDate);
    List<RepayReportVo> queryRepayReportVo(String startDate, String endDate);
}
