package com.nyd.batch.service.impls;

import com.nyd.batch.dao.ds.DataSourceContextHolder;
import com.nyd.batch.dao.mapper.*;
import com.nyd.batch.entity.*;
import com.nyd.batch.service.ReportService;
import com.nyd.batch.service.aspect.RoutingDataSource;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cong Yuxiang
 * 2017/12/27
 **/
@Service
public class ReportServiceImpl implements ReportService{

    @Autowired
    private TRemitMapper tRemitMapper;

    @Autowired
    private TRepayMapper tRepayMapper;

    @Autowired
    private RemitReportMapper remitReportMapper;

    @Autowired
    private RepayReportMapper repayReportMapper;

    @Autowired
    private BillMapper billMapper;


    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public List<TRemit> getRemitList() {
        Map timeMap = new HashMap();
        String flagDate = DateFormatUtils.format(DateUtils.addDays(new Date(),-1),"yyyy-MM-dd");
        timeMap.put("flagDate", flagDate);
        return tRemitMapper.queryByTimeRange(timeMap);
    }
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public List<TRepay> getRepayList() {
        Map timeMap = new HashMap();
        String flagDate = DateFormatUtils.format(DateUtils.addDays(new Date(),-1),"yyyy-MM-dd");
        timeMap.put("flagDate", flagDate);
        return tRepayMapper.queryByTimeRange(timeMap);
    }
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public void saveRemitReport(RemitReport report) {
        remitReportMapper.insert(report);
    }
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public void saveRepayReport(RepayReport report) {
        repayReportMapper.insert(report);
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public List<Bill> getBillByOrderNo(String orderNo) {
        Map orderMap = new HashMap();
        orderMap.put("orderNo",orderNo);
        return billMapper.getBillByOrderNo(orderMap);
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public List<Bill> getBillByBillNo(String billNo) {
        Map billMap = new HashMap();
        billMap.put("billNo",billNo);
        return billMapper.getBillByBillNo(billMap);
    }
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public OverdueBill getOverDueBillByBillNo(String billNo) {
        Map billMap = new HashMap();
        billMap.put("billNo",billNo);
        return billMapper.getOverDueBillByBillNo(billMap);
    }
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public List<TRemit> getRemitList(String cdate) {
        Map timeMap = new HashMap();
        timeMap.put("flagDate", cdate);
        return tRemitMapper.queryByTimeRange(timeMap);
    }
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public List<TRepay> getRepayList(String cdate) {
        Map timeMap = new HashMap();
        timeMap.put("flagDate", cdate);
        return tRepayMapper.queryByTimeRange(timeMap);
    }
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public void deleteRemitReport(String cdate) throws ParseException {
        Date a = DateUtils.parseDate(cdate,"yyyy-MM-dd");
        Date b = DateUtils.addDays(a,1);
        Map map = new HashMap();
        map.put("cdate",DateFormatUtils.format(b,"yyyy-MM-dd"));
        remitReportMapper.deleteRemitReport(map);
    }
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public void deleteRepayReport(String cdate) {
        Map map = new HashMap();
        map.put("cdate",cdate);
        repayReportMapper.deleteRepayReport(map);
    }
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public AmountOfHistory queryRepayAmountHistory(String billNo, String repayTime) {
        Map map = new HashMap();
        map.put("billNo",billNo);
        map.put("repayTime",repayTime);
        AmountOfHistory amount = billMapper.queryRepayAmountHistory(map);
        if(amount==null){
            amount = new AmountOfHistory(new BigDecimal(0),new BigDecimal(0));
        }
        return amount;
    }
}
