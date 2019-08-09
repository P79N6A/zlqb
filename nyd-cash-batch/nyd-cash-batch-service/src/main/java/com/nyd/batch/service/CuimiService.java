package com.nyd.batch.service;

/**
 * Cong Yuxiang
 * 2018/1/3
 **/
public interface CuimiService {
    void generateCuimiExcel();
    void generateOverdueReturnStatus();
    void generateCuimiExcelAll();

    void generateOverdueReturnStatusTest(String startDate,String endDate);
}
