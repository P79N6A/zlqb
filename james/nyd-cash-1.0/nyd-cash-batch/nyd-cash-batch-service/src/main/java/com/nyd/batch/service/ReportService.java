package com.nyd.batch.service;

import com.nyd.batch.entity.*;

import java.text.ParseException;
import java.util.List;

/**
 * Cong Yuxiang
 * 2017/12/27
 **/
public interface ReportService {
    List<TRemit> getRemitList();
    List<TRepay> getRepayList();
    void saveRemitReport(RemitReport report);
    void saveRepayReport(RepayReport report);

    List<Bill> getBillByOrderNo(String orderNo);
    List<Bill> getBillByBillNo(String billNo);
    OverdueBill getOverDueBillByBillNo(String billNo);


    //用于人工跑单

    List<TRemit> getRemitList(String cdate);
    List<TRepay> getRepayList(String cdate);

    void deleteRemitReport(String cdate) throws ParseException;
    void deleteRepayReport(String cdate);

    AmountOfHistory queryRepayAmountHistory(String billNo,String repayTime);

}
