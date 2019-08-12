package com.nyd.batch.service.impls;

import com.nyd.batch.dao.ds.DataSourceContextHolder;
import com.nyd.batch.dao.mapper.CuiShouMapper;
import com.nyd.batch.entity.Bill;
import com.nyd.batch.entity.FriendCircle;
import com.nyd.batch.entity.MobileUtity;
import com.nyd.batch.entity.OverdueBill;
import com.nyd.batch.model.AddressBook;
import com.nyd.batch.model.CuimiInfo;
import com.nyd.batch.model.OverdueStatus;
import com.nyd.batch.service.CuimiService;
import com.nyd.batch.service.MiddleService;
import com.nyd.batch.service.aspect.RoutingDataSource;
import com.nyd.batch.service.excel.ExcelKit;
import com.nyd.batch.service.util.DateUtil;
import com.nyd.batch.service.util.MongoApi;
import com.nyd.order.api.OrderContract;
import com.nyd.order.api.OrderDetailContract;
import com.nyd.order.model.OrderDetailInfo;
import com.nyd.order.model.OrderInfo;
import com.nyd.user.api.UserContactContract;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.api.UserJobContract;
import com.nyd.user.model.ContactInfos;
import com.nyd.user.model.JobInfo;
import com.nyd.user.model.UserDetailInfo;
import com.nyd.user.model.UserInfo;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Cong Yuxiang
 * 2018/1/3
 **/
@Service
public class CuimiServiceImpl implements CuimiService {

    Logger logger = LoggerFactory.getLogger(CuimiServiceImpl.class);

    @Autowired
    private CuiShouMapper cuiShouMapper;

//    @Autowired
//    private CuimiMail cuimiMail;

    @Autowired
    private OrderDetailContract orderDetailContract;

    @Autowired
    private UserIdentityContract userIdentityContract;

    @Autowired
    private UserContactContract userContactContract;

    @Autowired
    private UserJobContract userJobContract;

    @Autowired
    private OrderContract orderContract;

    @Autowired
    private MiddleService middleService;

    @Autowired
    private MongoApi mongoApi;

    @Override
    public void generateCuimiExcel() {
        List<OverdueBill> cuishouBills = middleService.getCuishouBills();
        if (cuishouBills == null || cuishouBills.size() == 0) {
            return;
        }


        List<CuimiInfo> resultM1 = new ArrayList<>();
//        List<CuimiInfo> resultM1B = new ArrayList<>();
        List<CuimiInfo> resultM2 = new ArrayList<>();
        List<CuimiInfo> resultM3 = new ArrayList<>();

        for (OverdueBill cuishouBill : cuishouBills) {
            CuimiInfo cuimiInfo = new CuimiInfo();
            String orderNo = cuishouBill.getOrderNo();

            cuimiInfo.setSerialNo(orderNo);
            cuimiInfo.setOverDuePenalInterest(cuishouBill.getOverdueAmount().toString());
            cuimiInfo.setContractNo(cuishouBill.getOrderNo());
            cuimiInfo.setPeriodCount("1");
            cuimiInfo.setPeriodReturn("0");

            if (cuishouBill.getOverdueDays() >= 0 && cuishouBill.getOverdueDays() <= 30) {
                cuimiInfo.setEntrustStopDate(DateFormatUtils.format(DateUtils.addDays(new Date(), 30 - cuishouBill.getOverdueDays()), "yyyy-MM-dd"));
            } else if (cuishouBill.getOverdueDays() >= 31 && cuishouBill.getOverdueDays() <= 60) {
                cuimiInfo.setEntrustStopDate(DateFormatUtils.format(DateUtils.addDays(new Date(), 60 - cuishouBill.getOverdueDays()), "yyyy-MM-dd"));
            } else {
                cuimiInfo.setEntrustStopDate(DateFormatUtils.format(DateUtils.addDays(new Date(), 30), "yyyy-MM-dd"));
            }

            cuimiInfo.setEntrustmentAmount(cuishouBill.getOverdueAmount().toString());


            Map map = new HashMap();
            map.put("billNo", cuishouBill.getBillNo());
            Bill bill = middleService.getBillByBillNo(map);

            cuimiInfo.setLoanDate(DateFormatUtils.format(bill.getCreateTime(), "yyyy-MM-dd"));
            cuimiInfo.setOverDueDate(DateFormatUtils.format(DateUtils.addDays(bill.getPromiseRepaymentDate(), 1), "yyyy-MM-dd"));
            cuimiInfo.setEntrustDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
            cuimiInfo.setOverDueDays(cuishouBill.getOverdueDays() + "");

            //根据orderno 获取orderDetail
            OrderDetailInfo orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(orderNo).getData();
            int loopCount = 10;
            while (orderDetailInfo == null && loopCount > 0) {
                orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(orderNo).getData();
                loopCount--;
            }
            if (orderDetailInfo == null) {
                logger.error("orderDetailInfo为null,orderno:" + orderNo);
                continue;
            }

            OrderInfo orderInfo = orderContract.getOrderByOrderNo(orderNo).getData();
            loopCount = 10;
            while (orderInfo == null && loopCount > 0) {
                orderInfo = orderContract.getOrderByOrderNo(orderNo).getData();
                loopCount--;
            }
            if (orderInfo == null) {
                logger.error("orderInfo为null,orderno:" + orderNo);
                continue;
            }
            cuimiInfo.setTotalAmount(orderInfo.getLoanAmount().toString());
            cuimiInfo.setRemainAmount(bill.getWaitRepayPrinciple().toString());
            cuimiInfo.setRepayBankName(orderInfo.getBankName());
            cuimiInfo.setRepayBankAcc(orderInfo.getBankAccount());
            cuimiInfo.setEntrustmentAmount(bill.getWaitRepayAmount().toString());
            cuimiInfo.setPeriodAmount(orderInfo.getLoanAmount().add(bill.getRepayInterest()).toString());
            cuimiInfo.setPenalty(cuishouBill.getOverdueFine().toString());
            cuimiInfo.setOverDueInterest(bill.getRepayInterest().toString());

            cuimiInfo.setName(orderDetailInfo.getRealName());
            cuimiInfo.setIdCard(orderDetailInfo.getIdNumber());
            cuimiInfo.setMobile(orderDetailInfo.getMobile());


            FriendCircle friendCircle = middleService.selectByMobile(orderDetailInfo.getMobile());

            List<AddressBook> addressBookList = new ArrayList<>();

            try {
                addressBookList = mongoApi.getAddressBooks(orderDetailInfo.getMobile());
            } catch (Exception e) {
                e.printStackTrace();
            }

            MobileUtity mobileUtity = addName(friendCircle, addressBookList);


            Map<String, String> mobileMap = mobileUtity.getAddressBookMap();

            int size = 0;
            for (Map.Entry<String, String> entry : mobileMap.entrySet()) {
                if ("10000".equals(entry.getKey()) || "10010".equals(entry.getKey()) || "10086".equals(entry.getKey()) || (entry.getValue() != null && entry.getValue().contains("彩票"))) {
                    continue;
                }
                if (entry.getKey() == null || entry.getKey().trim().length() != 11) {
                    continue;
                }
                size++;
                if (size == 1) {
                    cuimiInfo.setContactName3(entry.getValue());
                    cuimiInfo.setContactCellphone3(entry.getKey());
                }
                if (size == 2) {
                    cuimiInfo.setContactName4(entry.getValue());
                    cuimiInfo.setContactCellphone4(entry.getKey());
                }
                if (size == 3) {
                    cuimiInfo.setContactName5(entry.getValue());
                    cuimiInfo.setContactCellphone5(entry.getKey());
                }
                if (size == 4) {
                    cuimiInfo.setContactName6(entry.getValue());
                    cuimiInfo.setContactCellphone6(entry.getKey());
                }
                if (size == 5) {
                    cuimiInfo.setContactName7(entry.getValue());
                    cuimiInfo.setContactCellphone7(entry.getKey());
                }
                if (size == 6) {
                    cuimiInfo.setContactName8(entry.getValue());
                    cuimiInfo.setContactCellphone8(entry.getKey());
                }
                if (size == 7) {
                    cuimiInfo.setContactName9(entry.getValue());
                    cuimiInfo.setContactCellphone9(entry.getKey());
                }
                if (size == 8) {
                    cuimiInfo.setContactName10(entry.getValue());
                    cuimiInfo.setContactCellphone10(entry.getKey());
                    break;
                }
            }
            FriendCircle circle = mobileUtity.getFriendCircle();
            if (circle != null) {
                if (circle.getPeerNumTop33mPeerNumber1() != null && circle.getPeerNumTop33mPeerNumber1().trim().length() == 11) {
                    cuimiInfo.setContactName3(circle.getPeerNumTop33mPeerNumLoc1());
                    cuimiInfo.setContactCellphone3(circle.getPeerNumTop33mPeerNumber1());
                }
                if (circle.getPeerNumTop33mPeerNumber2() != null && circle.getPeerNumTop33mPeerNumber2().trim().length() == 11) {
                    cuimiInfo.setContactName4(circle.getPeerNumTop33mPeerNumLoc2());
                    cuimiInfo.setContactCellphone4(circle.getPeerNumTop33mPeerNumber2());
                }
                if (circle.getPeerNumTop33mPeerNumber3() != null && circle.getPeerNumTop33mPeerNumber3().trim().length() == 11) {
                    cuimiInfo.setContactName5(circle.getPeerNumTop33mPeerNumLoc3());
                    cuimiInfo.setContactCellphone5(circle.getPeerNumTop33mPeerNumber3());
                }
                if (circle.getPeerNumTop36mPeerNumber1() != null && circle.getPeerNumTop36mPeerNumber1().trim().length() == 11) {
                    cuimiInfo.setContactName6(circle.getPeerNumTop36mPeerNumLoc1());
                    cuimiInfo.setContactCellphone6(circle.getPeerNumTop36mPeerNumber1());
                }
                if (circle.getPeerNumTop36mPeerNumber2() != null && circle.getPeerNumTop36mPeerNumber2().trim().length() == 11) {
                    cuimiInfo.setContactName7(circle.getPeerNumTop36mPeerNumLoc2());
                    cuimiInfo.setContactCellphone7(circle.getPeerNumTop36mPeerNumber2());
                }
                if (circle.getPeerNumTop36mPeerNumber3() != null && circle.getPeerNumTop36mPeerNumber3().trim().length() == 11) {
                    cuimiInfo.setContactName8(circle.getPeerNumTop36mPeerNumLoc3());
                    cuimiInfo.setContactCellphone8(circle.getPeerNumTop36mPeerNumber3());
                }
            }
            String userid = orderDetailInfo.getUserId();

            UserDetailInfo userDetailInfo = userIdentityContract.getUserDetailInfo(userid).getData();

            loopCount = 10;
            while (userDetailInfo == null && loopCount > 0) {
                userDetailInfo = userIdentityContract.getUserDetailInfo(userid).getData();
                loopCount--;
            }
            if (userDetailInfo == null) {
                logger.error("userDetailInfo为null,userid:" + userid);
                continue;
            }
            cuimiInfo.setFamilyAddress(userDetailInfo.getLivingAddress());
            cuimiInfo.setResidenceAddress(userDetailInfo.getIdAddress());
            cuimiInfo.setProvince(userDetailInfo.getLivingProvince());
            cuimiInfo.setCity(userDetailInfo.getLivingCity());
            cuimiInfo.setCounty(userDetailInfo.getLivingDistrict());

            UserInfo userInfo = userIdentityContract.getUserInfo(userid).getData();
            loopCount = 10;
            while (userInfo == null && loopCount > 0) {
                userInfo = userIdentityContract.getUserInfo(userid).getData();
                loopCount--;
            }
            if (userInfo == null) {
                logger.error("userInfo为null,userid:" + userid);
                continue;
            }
            cuimiInfo.setGender(userInfo.getGender());

            JobInfo jobInfo = userJobContract.getJobInfo(userid).getData();
            loopCount = 10;
            while (jobInfo == null && loopCount > 0) {
                jobInfo = userJobContract.getJobInfo(userid).getData();
                loopCount--;
            }
            if (jobInfo != null) {
                cuimiInfo.setCompanyName(jobInfo.getCompany());
                cuimiInfo.setCompanyAddress(jobInfo.getCompanyAddress());
                cuimiInfo.setCompanyPhone(jobInfo.getTelephone());
                cuimiInfo.setJob(jobInfo.getProfession());
            }

            ContactInfos contactInfos = userContactContract.getContactInfo(userid).getData();
            loopCount = 10;
            while (contactInfos == null && loopCount > 0) {
                contactInfos = userContactContract.getContactInfo(userid).getData();
                loopCount--;
            }
            if (contactInfos != null) {
                cuimiInfo.setContactName1(contactInfos.getDirectContactName());
                cuimiInfo.setContactRelation1(contactInfos.getDirectContactRelation());
                cuimiInfo.setContactCellphone1(contactInfos.getDirectContactMobile());

                cuimiInfo.setContactName2(contactInfos.getMajorContactName());
                cuimiInfo.setContactRelation2(contactInfos.getMajorContactRelation());
                cuimiInfo.setContactCellphone2(contactInfos.getMajorContactMobile());
            }
            if (cuishouBill.getOverdueDays() >= 1 && cuishouBill.getOverdueDays() <= 30) {
                cuimiInfo.setOverduePeriod("M1");
                resultM1.add(cuimiInfo);
            } else if (cuishouBill.getOverdueDays() <= 60 && cuishouBill.getOverdueDays() > 30) {
                cuimiInfo.setOverduePeriod("M2");
                resultM2.add(cuimiInfo);
            } else {
                cuimiInfo.setOverduePeriod("M3");
                resultM3.add(cuimiInfo);
            }


        }

        if (resultM1.size() > 0) {
            storeFile(resultM1, "侬要贷催收单", "M1");
        }

        if (resultM2.size() > 0) {
            storeFile(resultM2, "侬要贷催收单", "M2");
        }
        if (resultM3.size() > 0) {
            storeFile(resultM3, "侬要贷催收单", "M3");
        }

    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public void generateOverdueReturnStatus() {
        List<OverdueStatus> result = new ArrayList<>();
        Map map1 = new HashMap();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
//        map1.put("starDate",DateFormatUtils.format(c,"yyyy-MM-dd"));
        map1.put("endDate", DateFormatUtils.format(DateUtils.addDays(new Date(), -1), "yyyy-MM-dd"));
        List<OverdueBill> bills = cuiShouMapper.getOverdueStatusAll(map1);
        for (OverdueBill overdueBill : bills) {

            OverdueStatus overdueStatus = new OverdueStatus();
            String billNo = overdueBill.getBillNo();
            Map map = new HashMap();
            map.put("billNo", billNo);
            Bill bill = cuiShouMapper.getBillByBillNo(map);
            if (bill == null) {
                logger.error(billNo + "没查出bill");
                continue;
            }
            Date promiseDate = bill.getPromiseRepaymentDate();
//            if(days(promiseDate)){
            OrderDetailInfo orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(overdueBill.getOrderNo()).getData();
            int loopCount = 10;
            while (orderDetailInfo == null && loopCount > 0) {
                orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(overdueBill.getOrderNo()).getData();
                loopCount--;
            }
            if (orderDetailInfo == null) {
                logger.error("over orderDetailInfo null:" + overdueBill.getOrderNo());
                continue;
            }

            overdueStatus.setName(orderDetailInfo.getRealName());
            overdueStatus.setMobile(orderDetailInfo.getMobile());
            overdueStatus.setIdCard(orderDetailInfo.getIdNumber());
            overdueStatus.setBillNo(overdueBill.getBillNo());
            overdueStatus.setOrderNo(overdueBill.getOrderNo());
            overdueStatus.setNotRecvAmount(bill.getWaitRepayAmount().toString());
            overdueStatus.setReceiveAmount(bill.getAlreadyRepayAmount().toString());
            overdueStatus.setRepayTime(DateFormatUtils.format(bill.getPromiseRepaymentDate(), "yyyy-MM-dd"));
            if ("B003".equals(overdueBill.getBillStatus())) {
                overdueStatus.setDone("是");
//                if()
                if(DateUtil.getDay(DateUtils.addDays(c.getTime(),-1),bill.getActualSettleDate())<0){
                    continue;
                }
                overdueStatus.setSettleTime(DateFormatUtils.format(bill.getActualSettleDate(), "yyyy-MM-dd"));
            } else {
                overdueStatus.setDone("否");
            }
            int overdueDays = overdueBill.getOverdueDays();

            if (overdueDays >= 1 && overdueDays <= 15) {
                overdueStatus.setPeriod("M1A");
            } else if (overdueDays <= 30 && overdueDays >= 16) {
                overdueStatus.setPeriod("M1B");
            } else if (overdueDays <= 60 && overdueDays > 30) {
                overdueStatus.setPeriod("M2");
            } else {
                overdueStatus.setPeriod("M3");
            }
            result.add(overdueStatus);

//            }
        }


        File file = new File("/data/cuimi/逾期还款状态" + DateFormatUtils.format(DateUtils.addDays(new Date(), -1), "yyyy-MM-dd") + ".xlsx");
        file.delete();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            ExcelKit.$Builder(OverdueStatus.class).toExcel(result, "侬要贷", outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (outputStream == null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private boolean days(Date promiseDate) {
        try {
            Date promiseDateNormal = DateUtils.parseDate(DateFormatUtils.format(promiseDate, "yyyy-MM-dd"), "yyyy-MM-dd");
            Date nowDateNormal = DateUtils.parseDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
            long days = (nowDateNormal.getTime() - promiseDateNormal.getTime()) / (1000 * 3600 * 24);
            logger.info("计算天数" + days);
            if (days == 2 || days == 9 || days == 32 || days == 62) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("判断时间间隔异常" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public void generateCuimiExcelAll() {
        List<OverdueBill> cuishouBills = middleService.getCuishouBillsAll();
        if (cuishouBills == null || cuishouBills.size() == 0) {
            return;
        }

//        List<CuimiInfo> resultM1A = new ArrayList<>();
        List<CuimiInfo> resultM1 = new ArrayList<>();
        List<CuimiInfo> resultM2 = new ArrayList<>();
        List<CuimiInfo> resultM3 = new ArrayList<>();

        for (OverdueBill cuishouBill : cuishouBills) {
            CuimiInfo cuimiInfo = new CuimiInfo();
            String orderNo = cuishouBill.getOrderNo();

            cuimiInfo.setSerialNo(orderNo);
            cuimiInfo.setOverDuePenalInterest(cuishouBill.getOverdueAmount().toString());
            cuimiInfo.setContractNo(cuishouBill.getOrderNo());
            cuimiInfo.setPeriodCount("1");
            cuimiInfo.setPeriodReturn("0");


//            cuimiInfo.setEntrustStopDate();
            if (cuishouBill.getOverdueDays() >= 0 && cuishouBill.getOverdueDays() <= 30) {
                cuimiInfo.setEntrustStopDate(DateFormatUtils.format(DateUtils.addDays(new Date(), 30 - cuishouBill.getOverdueDays()), "yyyy-MM-dd"));
            } else if (cuishouBill.getOverdueDays() >= 31 && cuishouBill.getOverdueDays() <= 60) {
                cuimiInfo.setEntrustStopDate(DateFormatUtils.format(DateUtils.addDays(new Date(), 60 - cuishouBill.getOverdueDays()), "yyyy-MM-dd"));
            } else {
                cuimiInfo.setEntrustStopDate(DateFormatUtils.format(DateUtils.addDays(new Date(), 30), "yyyy-MM-dd"));
            }

            cuimiInfo.setEntrustmentAmount(cuishouBill.getOverdueAmount().toString());


            Map map = new HashMap();
            map.put("billNo", cuishouBill.getBillNo());
            Bill bill = middleService.getBillByBillNo(map);

            cuimiInfo.setLoanDate(DateFormatUtils.format(bill.getCreateTime(), "yyyy-MM-dd"));
            cuimiInfo.setOverDueDate(DateFormatUtils.format(DateUtils.addDays(bill.getPromiseRepaymentDate(), 1), "yyyy-MM-dd"));
            cuimiInfo.setEntrustDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
            cuimiInfo.setOverDueDays(cuishouBill.getOverdueDays() + "");

            //根据orderno 获取orderDetail
            OrderDetailInfo orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(orderNo).getData();
            int loopCount = 10;
            while (orderDetailInfo == null && loopCount > 0) {
                orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(orderNo).getData();
                loopCount--;
            }
            if (orderDetailInfo == null) {
                logger.error("orderDetailInfo为null,orderno:" + orderNo);
                continue;
            }

            OrderInfo orderInfo = orderContract.getOrderByOrderNo(orderNo).getData();
            loopCount = 10;
            while (orderInfo == null && loopCount > 0) {
                orderInfo = orderContract.getOrderByOrderNo(orderNo).getData();
                loopCount--;
            }
            if (orderInfo == null) {
                logger.error("orderInfo为null,orderno:" + orderNo);
                continue;
            }
            cuimiInfo.setTotalAmount(orderInfo.getLoanAmount().toString());
            cuimiInfo.setRemainAmount(bill.getWaitRepayPrinciple().toString());
            cuimiInfo.setRepayBankName(orderInfo.getBankName());
            cuimiInfo.setRepayBankAcc(orderInfo.getBankAccount());
            cuimiInfo.setEntrustmentAmount(bill.getWaitRepayAmount().toString());
            cuimiInfo.setPeriodAmount(orderInfo.getLoanAmount().add(bill.getRepayInterest()).toString());
            cuimiInfo.setPenalty(cuishouBill.getOverdueFine().toString());
            cuimiInfo.setOverDueInterest(bill.getRepayInterest().toString());

            cuimiInfo.setName(orderDetailInfo.getRealName());
            cuimiInfo.setIdCard(orderDetailInfo.getIdNumber());
            cuimiInfo.setMobile(orderDetailInfo.getMobile());


            FriendCircle friendCircle = middleService.selectByMobile(orderDetailInfo.getMobile());

            List<AddressBook> addressBookList = new ArrayList<>();

            try {
                addressBookList = mongoApi.getAddressBooks(orderDetailInfo.getMobile());
            } catch (Exception e) {
                e.printStackTrace();
            }

            MobileUtity mobileUtity = addName(friendCircle, addressBookList);


            Map<String, String> mobileMap = mobileUtity.getAddressBookMap();

            int size = 0;
            for (Map.Entry<String, String> entry : mobileMap.entrySet()) {
                if ("10000".equals(entry.getKey()) || "10010".equals(entry.getKey()) || "10086".equals(entry.getKey()) || (entry.getValue() != null && entry.getValue().contains("彩票"))) {
                    continue;
                }
                if (entry.getKey() == null || entry.getKey().trim().length() != 11) {
                    continue;
                }
                size++;
                if (size == 1) {
                    cuimiInfo.setContactName3(entry.getValue());
                    cuimiInfo.setContactCellphone3(entry.getKey());
                }
                if (size == 2) {
                    cuimiInfo.setContactName4(entry.getValue());
                    cuimiInfo.setContactCellphone4(entry.getKey());
                }
                if (size == 3) {
                    cuimiInfo.setContactName5(entry.getValue());
                    cuimiInfo.setContactCellphone5(entry.getKey());
                }
                if (size == 4) {
                    cuimiInfo.setContactName6(entry.getValue());
                    cuimiInfo.setContactCellphone6(entry.getKey());
                }
                if (size == 5) {
                    cuimiInfo.setContactName7(entry.getValue());
                    cuimiInfo.setContactCellphone7(entry.getKey());
                }
                if (size == 6) {
                    cuimiInfo.setContactName8(entry.getValue());
                    cuimiInfo.setContactCellphone8(entry.getKey());
                }
                if (size == 7) {
                    cuimiInfo.setContactName9(entry.getValue());
                    cuimiInfo.setContactCellphone9(entry.getKey());
                }
                if (size == 8) {
                    cuimiInfo.setContactName10(entry.getValue());
                    cuimiInfo.setContactCellphone10(entry.getKey());
                    break;
                }
            }
            FriendCircle circle = mobileUtity.getFriendCircle();
            if (circle != null) {
                if (circle.getPeerNumTop33mPeerNumber1() != null && circle.getPeerNumTop33mPeerNumber1().trim().length() == 11) {
                    cuimiInfo.setContactName3(circle.getPeerNumTop33mPeerNumLoc1());
                    cuimiInfo.setContactCellphone3(circle.getPeerNumTop33mPeerNumber1());
                }
                if (circle.getPeerNumTop33mPeerNumber2() != null && circle.getPeerNumTop33mPeerNumber2().trim().length() == 11) {
                    cuimiInfo.setContactName4(circle.getPeerNumTop33mPeerNumLoc2());
                    cuimiInfo.setContactCellphone4(circle.getPeerNumTop33mPeerNumber2());
                }
                if (circle.getPeerNumTop33mPeerNumber3() != null && circle.getPeerNumTop33mPeerNumber3().trim().length() == 11) {
                    cuimiInfo.setContactName5(circle.getPeerNumTop33mPeerNumLoc3());
                    cuimiInfo.setContactCellphone5(circle.getPeerNumTop33mPeerNumber3());
                }
                if (circle.getPeerNumTop36mPeerNumber1() != null && circle.getPeerNumTop36mPeerNumber1().trim().length() == 11) {
                    cuimiInfo.setContactName6(circle.getPeerNumTop36mPeerNumLoc1());
                    cuimiInfo.setContactCellphone6(circle.getPeerNumTop36mPeerNumber1());
                }
                if (circle.getPeerNumTop36mPeerNumber2() != null && circle.getPeerNumTop36mPeerNumber2().trim().length() == 11) {
                    cuimiInfo.setContactName7(circle.getPeerNumTop36mPeerNumLoc2());
                    cuimiInfo.setContactCellphone7(circle.getPeerNumTop36mPeerNumber2());
                }
                if (circle.getPeerNumTop36mPeerNumber3() != null && circle.getPeerNumTop36mPeerNumber3().trim().length() == 11) {
                    cuimiInfo.setContactName8(circle.getPeerNumTop36mPeerNumLoc3());
                    cuimiInfo.setContactCellphone8(circle.getPeerNumTop36mPeerNumber3());
                }
            }
            String userid = orderDetailInfo.getUserId();

            UserDetailInfo userDetailInfo = userIdentityContract.getUserDetailInfo(userid).getData();

            loopCount = 10;
            while (userDetailInfo == null && loopCount > 0) {
                userDetailInfo = userIdentityContract.getUserDetailInfo(userid).getData();
                loopCount--;
            }
            if (userDetailInfo == null) {
                logger.error("userDetailInfo为null,userid:" + userid);
                continue;
            }
            cuimiInfo.setFamilyAddress(userDetailInfo.getLivingAddress());
            cuimiInfo.setResidenceAddress(userDetailInfo.getIdAddress());
            cuimiInfo.setProvince(userDetailInfo.getLivingProvince());
            cuimiInfo.setCity(userDetailInfo.getLivingCity());
            cuimiInfo.setCounty(userDetailInfo.getLivingDistrict());

            UserInfo userInfo = userIdentityContract.getUserInfo(userid).getData();
            loopCount = 10;
            while (userInfo == null && loopCount > 0) {
                userInfo = userIdentityContract.getUserInfo(userid).getData();
                loopCount--;
            }
            if (userInfo == null) {
                logger.error("userInfo为null,userid:" + userid);
                continue;
            }
            cuimiInfo.setGender(userInfo.getGender());

            JobInfo jobInfo = userJobContract.getJobInfo(userid).getData();
            loopCount = 10;
            while (jobInfo == null && loopCount > 0) {
                jobInfo = userJobContract.getJobInfo(userid).getData();
                loopCount--;
            }
            if (jobInfo != null) {
                cuimiInfo.setCompanyName(jobInfo.getCompany());
                cuimiInfo.setCompanyAddress(jobInfo.getCompanyAddress());
                cuimiInfo.setCompanyPhone(jobInfo.getTelephone());
                cuimiInfo.setJob(jobInfo.getProfession());
            }

            ContactInfos contactInfos = userContactContract.getContactInfo(userid).getData();
            loopCount = 10;
            while (contactInfos == null && loopCount > 0) {
                contactInfos = userContactContract.getContactInfo(userid).getData();
                loopCount--;
            }
            if (contactInfos != null) {
                cuimiInfo.setContactName1(contactInfos.getDirectContactName());
                cuimiInfo.setContactRelation1(contactInfos.getDirectContactRelation());
                cuimiInfo.setContactCellphone1(contactInfos.getDirectContactMobile());

                cuimiInfo.setContactName2(contactInfos.getMajorContactName());
                cuimiInfo.setContactRelation2(contactInfos.getMajorContactRelation());
                cuimiInfo.setContactCellphone2(contactInfos.getMajorContactMobile());
            }
            if (cuishouBill.getOverdueDays() >= 1 && cuishouBill.getOverdueDays() <= 30) {
                cuimiInfo.setOverduePeriod("M1");
                resultM1.add(cuimiInfo);
            } else if (cuishouBill.getOverdueDays() <= 60 && cuishouBill.getOverdueDays() > 30) {
                cuimiInfo.setOverduePeriod("M2");
                resultM2.add(cuimiInfo);
            } else {
                cuimiInfo.setOverduePeriod("M3");
                resultM3.add(cuimiInfo);
            }


        }

        if (resultM1.size() > 0) {
            storeFile(resultM1, "nydall催收单", "M1");
        }
        if (resultM2.size() > 0) {
            storeFile(resultM2, "nydall催收单", "M2");
        }
        if (resultM3.size() > 0) {
            storeFile(resultM3, "nydall催收单", "M3");
        }


    }

    private void storeFile(List<CuimiInfo> result, String filename, String flag) {
        File file = new File("/data/cuimi/" + filename + flag + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + ".xlsx");
        file.delete();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            ExcelKit.$Builder(CuimiInfo.class).toExcel(result, "侬要贷", outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (outputStream == null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private MobileUtity addName(FriendCircle friendCircle, List<AddressBook> books) {

        Map<String, String> map = new HashMap<>();
        for (AddressBook addressBook : books) {
            map.put(addressBook.getTel(), addressBook.getName());
        }
        if (friendCircle != null) {
            friendCircle.setPeerNumTop33mPeerNumLoc1(null);
            friendCircle.setPeerNumTop33mPeerNumLoc2(null);
            friendCircle.setPeerNumTop33mPeerNumLoc3(null);
            friendCircle.setPeerNumTop36mPeerNumLoc1(null);
            friendCircle.setPeerNumTop36mPeerNumLoc2(null);
            friendCircle.setPeerNumTop36mPeerNumLoc3(null);

            if (map.containsKey(friendCircle.getPeerNumTop33mPeerNumber1())) {
                friendCircle.setPeerNumTop33mPeerNumLoc1(map.get(friendCircle.getPeerNumTop33mPeerNumber1()));
                map.remove(friendCircle.getPeerNumTop33mPeerNumber1());
            }
            if (map.containsKey(friendCircle.getPeerNumTop33mPeerNumber2())) {
                friendCircle.setPeerNumTop33mPeerNumLoc2(map.get(friendCircle.getPeerNumTop33mPeerNumber2()));
                map.remove(friendCircle.getPeerNumTop33mPeerNumber2());
            }

            if (map.containsKey(friendCircle.getPeerNumTop33mPeerNumber3())) {
                friendCircle.setPeerNumTop33mPeerNumLoc3(map.get(friendCircle.getPeerNumTop33mPeerNumber3()));
                map.remove(friendCircle.getPeerNumTop33mPeerNumber3());
            }
            if (map.containsKey(friendCircle.getPeerNumTop36mPeerNumber1())) {
                friendCircle.setPeerNumTop36mPeerNumLoc1(map.get(friendCircle.getPeerNumTop36mPeerNumber1()));
                map.remove(friendCircle.getPeerNumTop36mPeerNumber1());
            }
            if (map.containsKey(friendCircle.getPeerNumTop36mPeerNumber2())) {
                friendCircle.setPeerNumTop36mPeerNumLoc2(map.get(friendCircle.getPeerNumTop36mPeerNumber2()));
                map.remove(friendCircle.getPeerNumTop36mPeerNumber2());
            }
            if (map.containsKey(friendCircle.getPeerNumTop36mPeerNumber3())) {
                friendCircle.setPeerNumTop36mPeerNumLoc3(map.get(friendCircle.getPeerNumTop36mPeerNumber3()));
                map.remove(friendCircle.getPeerNumTop36mPeerNumber3());
            }
        }
        MobileUtity mobileUtity = new MobileUtity();
        mobileUtity.setAddressBookMap(map);
        mobileUtity.setFriendCircle(friendCircle);
        return mobileUtity;

    }

    @Override
    public void generateOverdueReturnStatusTest(String startDate, String endDate) {
        List<OverdueStatus> result = new ArrayList<>();
        Map map1 = new HashMap();
        map1.put("startDate", startDate);
        map1.put("endDate", endDate);
        List<OverdueBill> bills = cuiShouMapper.getOverdueStatusByTime(map1);
        for (OverdueBill overdueBill : bills) {

            OverdueStatus overdueStatus = new OverdueStatus();
            String billNo = overdueBill.getBillNo();
            Map map = new HashMap();
            map.put("billNo", billNo);
            Bill bill = cuiShouMapper.getBillByBillNo(map);
            if (bill == null) {
                logger.error(billNo + "没查出bill");
                continue;
            }

            OrderDetailInfo orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(overdueBill.getOrderNo()).getData();
            int loopCount = 10;
            while (orderDetailInfo == null && loopCount > 0) {
                orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(overdueBill.getOrderNo()).getData();
                loopCount--;
            }
            if (orderDetailInfo == null) {
                logger.error("over orderDetailInfo null:" + overdueBill.getOrderNo());
                continue;
            }

            overdueStatus.setName(orderDetailInfo.getRealName());
            overdueStatus.setMobile(orderDetailInfo.getIdNumber());
            overdueStatus.setIdCard(orderDetailInfo.getIdNumber());
            overdueStatus.setBillNo(overdueBill.getBillNo());
            overdueStatus.setOrderNo(overdueBill.getOrderNo());
            overdueStatus.setNotRecvAmount(bill.getWaitRepayAmount().toString());
            overdueStatus.setReceiveAmount(bill.getAlreadyRepayAmount().toString());
            overdueStatus.setRepayTime(DateFormatUtils.format(bill.getPromiseRepaymentDate(), "yyyy-MM-dd HH:mm:ss"));
            if ("B003".equals(overdueBill.getBillStatus())) {
                overdueStatus.setDone("是");
                overdueStatus.setSettleTime(DateFormatUtils.format(bill.getActualSettleDate(), "yyyy-MM-dd HH:mm:ss"));
            } else {
                overdueStatus.setDone("否");
            }
            result.add(overdueStatus);


        }


        File file = new File("/data/cuimi/逾期还款状态" + startDate + endDate + ".xlsx");
        file.delete();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            ExcelKit.$Builder(OverdueStatus.class).toExcel(result, "侬要贷", outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (outputStream == null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
