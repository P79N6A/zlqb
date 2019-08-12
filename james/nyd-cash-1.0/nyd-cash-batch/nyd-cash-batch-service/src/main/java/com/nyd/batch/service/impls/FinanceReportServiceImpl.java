package com.nyd.batch.service.impls;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.batch.entity.*;
import com.nyd.batch.service.FinanceReportService;
import com.nyd.batch.service.MemberService;
import com.nyd.batch.service.ReportService;
import com.nyd.batch.service.tmp.KzjrConfig;
import com.nyd.batch.service.tmp.KzjrService;
import com.nyd.batch.service.util.AllocationAmount;
import com.nyd.batch.service.util.Utils;
import com.nyd.capital.model.kzjr.QueryAssetRequest;
import com.nyd.order.api.OrderContract;
import com.nyd.order.api.OrderDetailContract;
import com.nyd.order.model.OrderDetailInfo;
import com.nyd.order.model.OrderInfo;
import com.nyd.user.api.UserAccountContract;
import com.nyd.user.model.dto.AccountDto;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Cong Yuxiang
 * 2017/12/25
 **/
@Service
public class FinanceReportServiceImpl implements FinanceReportService{


    Logger logger = LoggerFactory.getLogger(FinanceReportServiceImpl.class);


    @Autowired
    private OrderContract orderContract;

    @Autowired
    private OrderDetailContract orderDetailContract;




    @Autowired
    private MemberService memberService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private KzjrConfig kzjrConfig;

    @Autowired
    private KzjrService kzjrService;

    @Autowired
    private UserAccountContract userAccountContract;

    @Override
    public void generateReport() {

        List<TRemit> remitList = reportService.getRemitList();
        List<TRepay> repayList = reportService.getRepayList();

        for(TRemit remit:remitList){
            RemitReport remitReport = new RemitReport();
            String orderNo = remit.getOrderNo();
            int loopCount = 10;
            OrderInfo orderInfo = orderContract.getOrderByOrderNo(orderNo).getData();
            while (orderInfo == null&&loopCount>0){
                orderInfo = orderContract.getOrderByOrderNo(orderNo).getData();
                loopCount--;
            }
            loopCount = 10;
            OrderDetailInfo orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(orderNo).getData();
            while (orderDetailInfo == null&&loopCount>0){
                orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(orderNo).getData();
                loopCount--;
            }
            String userId = orderInfo.getUserId();

            loopCount = 10;
            List<OrderInfo> orderInfoList = orderContract.getOrdersByUserId(userId).getData();
            while (orderInfoList == null&&loopCount>0){
                loopCount--;
                orderInfoList = orderContract.getOrdersByUserId(orderInfo.getUserId()).getData();
            }

            if(orderInfoList!=null){
                int i=0;
                for(OrderInfo info:orderInfoList){
                    if(info.getOrderStatus()==50){
                        i++;
                    }
                }
                remitReport.setDebitCount(i);
            }

            /**
             * 通过userId到表t_account中找到用户来源(备注：对放款来源处理)
             */
            ResponseData<AccountDto> accountData = userAccountContract.getAccount(userId);
            AccountDto accountDto = accountData.getData();
            remitReport.setSource(accountDto.getSource());  //放款来源
            logger.info("放款来源："+accountDto.getSource());

            /**
             * 资方名称
             */
            if (StringUtils.isNotBlank(remit.getInvestorName())){
                remitReport.setCapitalName(remit.getInvestorName());
                logger.info("资方名称："+remit.getInvestorName());
            }



            List<Bill> bill = reportService.getBillByOrderNo(orderNo);
            if(remit.getContractLink()==null||remit.getContractLink().length()==0){
//                QueryAssetRequest request = new QueryAssetRequest();
//                request.setChannelCode(kzjrConfig.getChannelCode());
//                request.setOrderId(orderNo);
//                JSONObject obj = kzjrService.queryAsset(request);
//                logger.info(JSON.toJSONString(request)+"查询资产code 报表"+JSONObject.toJSONString(obj));
//                if(obj.getInteger("status")==0){
//                    remitReport.setProductCode(obj.getJSONObject("data").getString("productCode"));
//                }else {
                    remitReport.setProductCode("111111");
//                }
            }else {
                remitReport.setProductCode(remit.getContractLink());
            }
            remitReport.setOrderNo(orderNo);
            remitReport.setRemitNo(orderNo);
            remitReport.setCustomerName(orderDetailInfo.getRealName());
            remitReport.setProductType(1);
            remitReport.setMobile(orderDetailInfo.getMobile());
            remitReport.setDepositBank(orderInfo.getBankName());
            remitReport.setTestStatus(orderInfo.getTestStatus()==null?1:orderInfo.getTestStatus());

            remitReport.setRemitChannel(remit.getFundCode());
            remitReport.setMemberFeeId(orderInfo.getMemberId());
            remitReport.setMemberFee(orderInfo.getMemberFee());
            //先设为0  后续 如果是测试标 则设置为其他
            remitReport.setMemberFeeDrawBack(new BigDecimal("0"));

            TMemberLog log = memberService.selectByMemberId(orderInfo.getMemberId());
            if(log!=null) {
                if ("1".equals(log.getDebitFlag())) {
                    if(orderInfo.getMemberFee().compareTo(new BigDecimal(0))<=0){
                        remitReport.setMemberFeePay(new BigDecimal(0));
                    }else {
                        remitReport.setMemberFeePay(log.getMemberFee());
                        if(orderInfo.getTestStatus()==0){
                            remitReport.setMemberFeeDrawBack(log.getMemberFee());
                        }
                    }
                    remitReport.setMemberFeePayDate(log.getStartTime());
                } else {
                    remitReport.setMemberFeePay(new BigDecimal(0));
                }
            }else {
                remitReport.setMemberFeePay(new BigDecimal(-1));
            }


            if(bill!=null&&bill.size()>0) {
                remitReport.setContractEndDate(bill.get(0).getPromiseRepaymentDate());

                remitReport.setContractStartDate(DateUtils.addDays(bill.get(0).getCreateTime(),1));

            }


            remitReport.setBorrowTime(orderInfo.getBorrowTime());
            remitReport.setBorrowAmount(orderInfo.getLoanAmount());
            remitReport.setActivityDerate(new BigDecimal("0"));

//            TMember tMember = memberService.getMemberByUserId(userId);
//            if(tMember != null){
//                remitReport.setMemberFeeId(tMember.getId()+"");
//                remitReport.setMemberFee(orderInfo.getMemberFee());
//            }



//            remitReport.setFeeItem();
//            remitReport.setFeeTotal();
//            remitReport.setCapitalName(null);
//            remitReport.setFeeChannel();

            reportService.saveRemitReport(remitReport);

        }
        for (TRepay repay:repayList){
                if(!repay.getBillNo().contains("_")) {
                    int loopCount = 10;
                    //历史金额
                    AmountOfHistory amountOfHistory = reportService.queryRepayAmountHistory(repay.getBillNo(),DateFormatUtils.format(repay.getRepayTime(),"yyyy-MM-dd HH:mm:ss"));


                    List<Bill> bills = reportService.getBillByBillNo(repay.getBillNo());
                    if(bills==null||bills.size()==0){
                        logger.error("还款报告 没有billNo"+repay.getBillNo());
                        continue;
                    }
                    Bill bill = bills.get(0);
                    OrderDetailInfo orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(bill.getOrderNo()).getData();
                    while (orderDetailInfo == null&&loopCount>0){
                        orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(bill.getOrderNo()).getData();
                        loopCount--;
                    }

                    loopCount=10;
                    OrderInfo orderInfo = orderContract.getOrderByOrderNo(bill.getOrderNo()).getData();
                    while (orderInfo == null&&loopCount>0){
                        orderInfo = orderContract.getOrderByOrderNo(bill.getOrderNo()).getData();
                        loopCount--;
                    }

                    RepayReport repayReport = new RepayReport();

                    repayReport.setOrderNo(repay.getBillNo());
                    repayReport.setCustomerName(orderDetailInfo.getRealName());
                    repayReport.setMobile(orderDetailInfo.getMobile());
                    repayReport.setLoanNo(bill.getOrderNo());

//                    repayReport.setCapitalName();先空着
                    repayReport.setProductType(1);
                    repayReport.setLabel("");
                    repayReport.setTestStatus(orderInfo.getTestStatus()==null?1:orderInfo.getTestStatus());
                    repayReport.setContractStartDate(DateUtils.addDays(bill.getCreateTime(),1));
                    repayReport.setContractEndDate(bill.getPromiseRepaymentDate());

                    OverdueBill overdueBill = reportService.getOverDueBillByBillNo(repay.getBillNo());
                    if(overdueBill==null) {
                        repayReport.setOverdueDay(0);
//                        repayReport.setOverdueShouldAmount(new BigDecimal("0"));
//                    判断
                        repayReport.setOverduePeriod("M0");
                    }else{
                        repayReport.setOverdueDay(overdueBill.getOverdueDays());
//                        repayReport.setOverdueShouldAmount(overdueBill.getOverdueAmount());
                        if(overdueBill.getOverdueDays()<=30){
                            repayReport.setOverduePeriod("M1");
                        }else if(overdueBill.getOverdueDays()<=60&&overdueBill.getOverdueDays()>30){
                            repayReport.setOverduePeriod("M2");
                        }else {
                            repayReport.setOverduePeriod("M3");
                        }

                    }
                    BigDecimal receivableAmount = bill.getReceivableAmount();
                    repayReport.setContractTime(orderInfo.getBorrowTime());
                    repayReport.setSerialNo(repay.getRepayNo());
                    repayReport.setRepayChannel(repay.getRepayChannel());
                    repayReport.setActualReceiptDay(repay.getRepayTime());
                    repayReport.setContractAmount(orderInfo.getLoanAmount());
                    repayReport.setRemitAmount(orderInfo.getLoanAmount());
//                    repayReport.setFeeItem();

                    /**
                     * 通过userId到表t_account中找到用户来源(备注：对放款来源处理)
                     */
                    String userId = orderInfo.getUserId();
                    ResponseData<AccountDto> accountData = userAccountContract.getAccount(userId);
                    AccountDto accountDto = accountData.getData();
                    repayReport.setSource(accountDto.getSource());  //放款来源
                    logger.info("放款来源："+accountDto.getSource());


                    repayReport.setReceiveAmount(repay.getRepayAmount());
                    repayReport.setAccumRepayAmount(bill.getAlreadyRepayAmount());
//                    repayReport.setThirdPartyPoundage();


                    AllocationAmount allocationAmount;
                    //拆账
                    if(orderInfo.getTestStatus()==0){
                        allocationAmount = Utils.calAllocation(amountOfHistory, repay, bill, overdueBill,true);
                    }else {
                        allocationAmount = Utils.calAllocation(amountOfHistory, repay, bill, overdueBill,false);
                    }
                    repayReport.setThisContractAmount(allocationAmount.getThisContractAmount());
                    repayReport.setThisFeeService(new BigDecimal(0));
                    repayReport.setThisInterestAmount(allocationAmount.getThisInterestAmount());
                    repayReport.setThisFeeLate(allocationAmount.getThisFeeLate());
                    repayReport.setThisOverdueFaxi(allocationAmount.getThisOverdueFaxi());
                    repayReport.setDrawbackAmount(allocationAmount.getDrawbackAmount());
//                    repayReport.setCompensatoryAmount();
//                    repayReport.setActivityDerate();
                    repayReport.setCollectionDerate(repay.getDerateAmount());

//                    repayReport.setOtherIncome();


                    repayReport.setMemberFeeId(orderInfo.getMemberId());
                    repayReport.setMemberFeeAmount(orderInfo.getMemberFee());

                    TMemberLog log = memberService.selectByMemberId(orderInfo.getMemberId());

                    if(log!=null) {
                        if ("1".equals(log.getDebitFlag())) {
                            if(orderInfo.getMemberFee().compareTo(new BigDecimal(0))<=0){
                                repayReport.setMemberFeePay(new BigDecimal(0));
                            }else {
                                repayReport.setMemberFeePay(log.getMemberFee());
                            }

                            repayReport.setMemberFeePayDate(log.getStartTime());
                            repayReport.setMemberFeePayChannel(log.getDebitChannel());
                            TMember tMember = memberService.getMemberByUserId(orderInfo.getUserId());
                            if(tMember!=null) {
                                repayReport.setExpireTime(tMember.getExpireTime());
                            }
                        } else {
                            repayReport.setMemberFeePay(new BigDecimal(0));
                        }
                    }else {
                        repayReport.setMemberFeePay(new BigDecimal(-1));
                    }





                    reportService.saveRepayReport(repayReport);


                }

        }

//        List<TMemberLog> memberLogList = getMemberLog();
//        for (TMemberLog memberLog:memberLogList){
//            MemberFeeReport report = new MemberFeeReport();
////            re
//            String userId = memberLog.getUserId();
//            report.setMemberFeeId(memberLog.getId());
//            OrderInfo orderInfo = orderContract.g
//
//        }

    }

    @Override
    public void generateReportDate(String cdate,String remitflag,String repayflag) {
        List<TRemit> remitList = null;
        if (!StringUtils.isBlank(remitflag)) {
            remitList = reportService.getRemitList(cdate);
        }
        List<TRepay> repayList = null;
        if (!StringUtils.isBlank(repayflag)) {
            repayList = reportService.getRepayList(cdate);
        }
        if (!StringUtils.isBlank(remitflag)) {
            try {
                reportService.deleteRemitReport(cdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            for (TRemit remit : remitList) {
                RemitReport remitReport = new RemitReport();
                String orderNo = remit.getOrderNo();
                int loopCount = 10;
                OrderInfo orderInfo = orderContract.getOrderByOrderNo(orderNo).getData();
                while (orderInfo == null && loopCount > 0) {
                    orderInfo = orderContract.getOrderByOrderNo(orderNo).getData();
                    loopCount--;
                }
                loopCount = 10;
                OrderDetailInfo orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(orderNo).getData();
                while (orderDetailInfo == null && loopCount > 0) {
                    orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(orderNo).getData();
                    loopCount--;
                }
                String userId = orderInfo.getUserId();

            loopCount = 10;
            List<OrderInfo> orderInfoList = orderContract.getOrdersByUserId(userId).getData();
            while (orderInfoList == null&&loopCount>0){
                loopCount--;
                orderInfoList = orderContract.getOrdersByUserId(orderInfo.getUserId()).getData();
            }

            if(orderInfoList!=null){
                int i=0;
                for(OrderInfo info:orderInfoList){
                    String date = DateFormatUtils.format(info.getLoanTime(),"yyyy-MM-dd");
                    long loantime  = 0;
                    try {
                        loantime = DateUtils.parseDate(date,"yyyy-MM-dd").getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long ctime = 0;
                    try {
                        ctime = DateUtils.parseDate(cdate,"yyyy-MM-dd").getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(info.getOrderStatus()==50&&(ctime-loantime)>=0){
                        i++;
                    }
                }
                remitReport.setDebitCount(i);
            }


                List<Bill> bill = reportService.getBillByOrderNo(orderNo);
                if (remit.getContractLink() == null || remit.getContractLink().length() == 0) {
//                    QueryAssetRequest request = new QueryAssetRequest();
//                    request.setChannelCode(kzjrConfig.getChannelCode());
//                    request.setOrderId(orderNo);
//                    JSONObject obj = kzjrService.queryAsset(request);
//                    logger.info(JSON.toJSONString(request) + "查询资产code 报表" + JSONObject.toJSONString(obj));
//                    if (obj.getInteger("status") == 0) {
//                        remitReport.setProductCode(obj.getJSONObject("data").getString("productCode"));
//                    } else {
                        remitReport.setProductCode("111111");
//                    }
                } else {
                    remitReport.setProductCode(remit.getContractLink());
                }
                remitReport.setOrderNo(orderNo);
                remitReport.setRemitNo(orderNo);
                remitReport.setCustomerName(orderDetailInfo.getRealName());
                remitReport.setProductType(1);
                remitReport.setMobile(orderDetailInfo.getMobile());
                remitReport.setDepositBank(orderInfo.getBankName());
                remitReport.setTestStatus(orderInfo.getTestStatus()==null?1:orderInfo.getTestStatus());
                remitReport.setRemitChannel(remit.getFundCode());
                remitReport.setMemberFeeId(orderInfo.getMemberId());
                remitReport.setMemberFee(orderInfo.getMemberFee());

                remitReport.setMemberFeeDrawBack(new BigDecimal("0"));

                TMemberLog log = memberService.selectByMemberId(orderInfo.getMemberId());
                if(log!=null) {
                    if ("1".equals(log.getDebitFlag())) {
                        if(orderInfo.getMemberFee().compareTo(new BigDecimal(0))<=0){
                            remitReport.setMemberFeePay(new BigDecimal(0));
                        }else {
                            remitReport.setMemberFeePay(log.getMemberFee());
                            if(orderInfo.getTestStatus()==0){
                                remitReport.setMemberFeeDrawBack(log.getMemberFee());
                            }
                        }
                        remitReport.setMemberFeePayDate(log.getStartTime());
                    } else {
                        remitReport.setMemberFeePay(new BigDecimal(0));
                    }
                }else {
                    remitReport.setMemberFeePay(new BigDecimal(-1));
                }

                if (bill != null && bill.size() > 0) {
                    remitReport.setContractEndDate(bill.get(0).getPromiseRepaymentDate());

                    remitReport.setContractStartDate(DateUtils.addDays(bill.get(0).getCreateTime(), 1));

                }

                remitReport.setBorrowTime(orderInfo.getBorrowTime());
                remitReport.setBorrowAmount(orderInfo.getLoanAmount());
                remitReport.setActivityDerate(new BigDecimal("0"));

                /**
                 * 通过userId到表t_account中找到用户来源(备注：对放款来源处理)
                 */
                ResponseData<AccountDto> accountData = userAccountContract.getAccount(userId);
                AccountDto accountDto = accountData.getData();
                remitReport.setSource(accountDto.getSource());  //放款来源
                logger.info("放款来源："+accountDto.getSource());

                /**
                 * 资方名称
                 */
                if (StringUtils.isNotBlank(remit.getInvestorName())){
                    remitReport.setCapitalName(remit.getInvestorName());
                    logger.info("资方名称："+remit.getInvestorName());
                }

//            List<TMemberLog> memberLogList = memberService.selectByUserId(userId);
//
//            if(memberLogList != null){
//                if(Tme)
//                remitReport.setMemberFeeId(tMember.getId()+"");
//                remitReport.setMemberFee(orderInfo.getMemberFee());
//            }


//            remitReport.setFeeItem();
//            remitReport.setFeeTotal();
//            remitReport.setCapitalName(null);
//            remitReport.setFeeChannel();

                reportService.saveRemitReport(remitReport);

            }
        }
        if (!StringUtils.isBlank(repayflag)) {
            reportService.deleteRepayReport(cdate);
            for (TRepay repay : repayList) {
                if (!repay.getBillNo().contains("_")) {
                    int loopCount = 10;
                    AmountOfHistory amountOfHistory = reportService.queryRepayAmountHistory(repay.getBillNo(),DateFormatUtils.format(repay.getRepayTime(),"yyyy-MM-dd HH:mm:ss"));

                    List<Bill> bills = reportService.getBillByBillNo(repay.getBillNo());
                    if (bills == null || bills.size() == 0) {
                        logger.error("还款报告 没有billNo" + repay.getBillNo());
                        continue;
                    }
                    Bill bill = bills.get(0);
                    OrderDetailInfo orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(bill.getOrderNo()).getData();
                    while (orderDetailInfo == null && loopCount > 0) {
                        orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(bill.getOrderNo()).getData();
                        loopCount--;
                    }

                    loopCount = 10;
                    OrderInfo orderInfo = orderContract.getOrderByOrderNo(bill.getOrderNo()).getData();
                    while (orderInfo == null && loopCount > 0) {
                        orderInfo = orderContract.getOrderByOrderNo(bill.getOrderNo()).getData();
                        loopCount--;
                    }

                    RepayReport repayReport = new RepayReport();
                    repayReport.setOrderNo(repay.getBillNo());
                    repayReport.setCustomerName(orderDetailInfo.getRealName());
                    repayReport.setMobile(orderDetailInfo.getMobile());
                    repayReport.setLoanNo(bill.getOrderNo());
//                    repayReport.setCapitalName();先空着
                    repayReport.setProductType(1);
                    repayReport.setLabel("");
                    repayReport.setTestStatus(orderInfo.getTestStatus()==null?1:orderInfo.getTestStatus());
                    repayReport.setContractStartDate(DateUtils.addDays(bill.getCreateTime(), 1));
                    repayReport.setContractEndDate(bill.getPromiseRepaymentDate());
//                repayReport.setOverdueShouldAmount(bill.getWaitRepayAmount());

                    OverdueBill overdueBill = reportService.getOverDueBillByBillNo(repay.getBillNo());
                    if (overdueBill == null) {
                        repayReport.setOverdueDay(0);
//                    repayReport.setOverdueShouldAmount(new BigDecimal("0"));
//                    判断
                        repayReport.setOverduePeriod("M0");
                    } else {
                        repayReport.setOverdueDay(overdueBill.getOverdueDays());
//                    repayReport.set
//                   repayReport.setOverdueShouldAmount(overdueBill.getOverdueAmount());
                        if (overdueBill.getOverdueDays() <= 30) {
                            repayReport.setOverduePeriod("M1");
                        } else if (overdueBill.getOverdueDays() <= 60 && overdueBill.getOverdueDays() > 30) {
                            repayReport.setOverduePeriod("M2");
                        } else {
                            repayReport.setOverduePeriod("M3");
                        }

                    }

                    repayReport.setContractTime(orderInfo.getBorrowTime());
                    repayReport.setSerialNo(repay.getRepayNo());
                    repayReport.setRepayChannel(repay.getRepayChannel());
                    repayReport.setActualReceiptDay(repay.getRepayTime());
                    repayReport.setContractAmount(orderInfo.getLoanAmount());
                    repayReport.setRemitAmount(orderInfo.getLoanAmount());
//                    repayReport.setFeeItem();

                    /**
                     * 通过userId到表t_account中找到用户来源(备注：对放款来源处理)
                     */
                    String userId = orderInfo.getUserId();
                    ResponseData<AccountDto> accountData = userAccountContract.getAccount(userId);
                    AccountDto accountDto = accountData.getData();
                    repayReport.setSource(accountDto.getSource());  //放款来源
                    logger.info("放款来源："+accountDto.getSource());

                    repayReport.setReceiveAmount(repay.getRepayAmount());
                    if(amountOfHistory!=null&&amountOfHistory.getRepayAmountSum()!=null){
                        repayReport.setAccumRepayAmount(repay.getRepayAmount().add(amountOfHistory.getRepayAmountSum()));
                    }else {
                        repayReport.setAccumRepayAmount(repay.getRepayAmount());
                    }
//                    repayReport.setThirdPartyPoundage();
//                    repayReport.setThisContractAmount(orderInfo.getLoanAmount());
                    repayReport.setThisFeeService(new BigDecimal(0));
                    //拆账
                    AllocationAmount allocationAmount;
                    if(orderInfo.getTestStatus()==0){
                        allocationAmount = Utils.calAllocation(amountOfHistory, repay, bill, overdueBill,true);
                    }else {
                        allocationAmount = Utils.calAllocation(amountOfHistory, repay, bill, overdueBill,false);
                    }
                    repayReport.setThisContractAmount(allocationAmount.getThisContractAmount());
                    repayReport.setThisFeeService(new BigDecimal(0));
                    repayReport.setThisInterestAmount(allocationAmount.getThisInterestAmount());
                    repayReport.setThisFeeLate(allocationAmount.getThisFeeLate());
                    repayReport.setThisOverdueFaxi(allocationAmount.getThisOverdueFaxi());
                    repayReport.setDrawbackAmount(allocationAmount.getDrawbackAmount());

                    repayReport.setCollectionDerate(repay.getDerateAmount());


                    repayReport.setMemberFeeId(orderInfo.getMemberId());
                    repayReport.setMemberFeeAmount(orderInfo.getMemberFee());

                    TMemberLog log = memberService.selectByMemberId(orderInfo.getMemberId());

                    if(log!=null) {
                        if ("1".equals(log.getDebitFlag())) {
                            if(orderInfo.getMemberFee().compareTo(new BigDecimal(0))<=0){
                                repayReport.setMemberFeePay(new BigDecimal(0));
                            }else {
                                repayReport.setMemberFeePay(log.getMemberFee());
                            }

                            repayReport.setMemberFeePayDate(log.getStartTime());
                            repayReport.setMemberFeePayChannel(log.getDebitChannel());
                            TMember tMember = memberService.getMemberByUserId(orderInfo.getUserId());
                            if(tMember!=null) {
                                repayReport.setExpireTime(tMember.getExpireTime());
                            }
                        } else {
                            repayReport.setMemberFeePay(new BigDecimal(0));
                        }
                    }else {
                        repayReport.setMemberFeePay(new BigDecimal(-1));
                    }

//                TMember tMember = memberService.getMemberByUserId(orderInfo.getUserId());
//
//                if(tMember!=null) {
//                    TMemberConfig memberConfig = memberService.getMemberConfigByType(tMember.getMemberType());
//
//
//                    repayReport.setMemberFeeId(tMember.getId());
//                    repayReport.setMemberFeeAmount(memberConfig.getDiscountFee());
//                    repayReport.setMemberFeePay(memberConfig.getDiscountFee());
//                    repayReport.setMemberFeePayDate(tMember.getCreateTime());
//                    repayReport.setMemberFeePayChannel("hlb");
//                    repayReport.setExpireTime(tMember.getExpireTime());
//                }

                    reportService.saveRepayReport(repayReport);

                }

            }
        }
//        List<TMemberLog> memberLogList = getMemberLog();
//        for (TMemberLog memberLog:memberLogList){
//            MemberFeeReport report = new MemberFeeReport();
////            re
//            String userId = memberLog.getUserId();
//            report.setMemberFeeId(memberLog.getId());
//            OrderInfo orderInfo = orderContract.g
//
//        }
    }

    public static void main(String[] args) {

        String Da = DateFormatUtils.format(DateUtils.addDays(new Date(),-1),"yyyy-MM-dd");
        try {
            System.out.println(DateUtils.parseDate(Da,"yyyy-MM-dd").getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
