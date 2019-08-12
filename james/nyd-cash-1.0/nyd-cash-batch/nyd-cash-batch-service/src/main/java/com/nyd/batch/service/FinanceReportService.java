package com.nyd.batch.service;

/**
 * Cong Yuxiang
 * 2017/12/25
 **/
public interface FinanceReportService {

    void generateReport();
    void generateReportDate(String cdate,String remitflag,String repayflag);
}
