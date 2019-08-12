package com.nyd.batch.service.util;

import com.alibaba.fastjson.JSON;
import com.nyd.batch.entity.AmountOfHistory;
import com.nyd.batch.entity.Bill;
import com.nyd.batch.entity.OverdueBill;
import com.nyd.batch.entity.TRepay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Cong Yuxiang
 * 2018/1/30
 **/
public class Utils {
    static Logger logger = LoggerFactory.getLogger(Utils.class);

    /**
     * 拆账
     * @param amountOfHistory
     * @param tRepay
     * @param bill
     * @param overdueBill
     * @return
     */
    public static AllocationAmount calAllocation(AmountOfHistory amountOfHistory, TRepay tRepay, Bill bill, OverdueBill overdueBill,boolean testFlag){
        AllocationAmount allocationAmount = new AllocationAmount();

        BigDecimal repayAmountSum = new BigDecimal(0);
        BigDecimal derateAmountSum = new BigDecimal(0);

        if(amountOfHistory!=null){
            repayAmountSum = amountOfHistory.getRepayAmountSum();
            derateAmountSum = amountOfHistory.getDerateAmountSum();
        }
        if(repayAmountSum==null){
            repayAmountSum = new BigDecimal(0);
            logger.info("repayAmountSum"+ JSON.toJSONString(amountOfHistory));
        }
        if(derateAmountSum==null){
            derateAmountSum = new BigDecimal(0);
            logger.info("derateAmountSum"+ JSON.toJSONString(amountOfHistory));
        }
        BigDecimal thisRepayAmount = tRepay.getRepayAmount();
        BigDecimal thisDerateAmount = tRepay.getDerateAmount();
        if(thisRepayAmount==null){
            thisRepayAmount = new BigDecimal(0);
            logger.info("thisRepayAmount"+JSON.toJSONString(tRepay));
        }
        if(thisDerateAmount==null){
            thisDerateAmount = new BigDecimal(0);
            logger.info("thisDerateAmount"+JSON.toJSONString(tRepay));
        }

        BigDecimal total = repayAmountSum.add(derateAmountSum);

        if(testFlag){
            allocationAmount.setThisInterestAmount(new BigDecimal("0"));
            allocationAmount.setThisFeeLate(new BigDecimal("0"));
            allocationAmount.setThisOverdueFaxi(new BigDecimal("0"));
            allocationAmount.setDrawbackAmount(new BigDecimal("0"));
            if(repayAmountSum.compareTo(bill.getRepayPrinciple())>=0){
                 allocationAmount.setDrawbackAmount(thisRepayAmount);
            }else {
                if(bill.getRepayPrinciple().compareTo(thisRepayAmount.add(repayAmountSum))>=0){
                    allocationAmount.setThisContractAmount(thisRepayAmount);
                }else {
                    allocationAmount.setThisContractAmount(bill.getRepayPrinciple().subtract(repayAmountSum));
                    allocationAmount.setDrawbackAmount(thisRepayAmount.subtract(allocationAmount.getThisContractAmount()));
                }
            }
            return allocationAmount;
        }else {

            if (total.compareTo(bill.getReceivableAmount()) >= 0) {

                allocationAmount.setDrawbackAmount(thisRepayAmount);
                return allocationAmount;
            }
            //本金
            if (bill.getRepayPrinciple().compareTo(repayAmountSum) >= 0) {
                if (bill.getRepayPrinciple().compareTo(repayAmountSum.add(thisRepayAmount)) >= 0) {
                    allocationAmount.setThisContractAmount(thisRepayAmount);
                } else {
                    allocationAmount.setThisContractAmount(bill.getRepayPrinciple().subtract(repayAmountSum));
                }
            } else {
                allocationAmount.setThisContractAmount(new BigDecimal(0));
            }
            //利息
            if (thisRepayAmount.subtract(allocationAmount.getThisContractAmount()).compareTo(new BigDecimal(0)) > 0) {
                if (bill.getRepayInterest().compareTo(thisRepayAmount.subtract(allocationAmount.getThisContractAmount())) <= 0) {
                    allocationAmount.setThisInterestAmount(bill.getRepayInterest());
                } else {
                    allocationAmount.setThisInterestAmount(thisRepayAmount.subtract(allocationAmount.getThisContractAmount()));
                }
            }
            //滞纳金
            if (overdueBill != null) {
                if ((thisRepayAmount.subtract(allocationAmount.getThisContractAmount()).subtract(allocationAmount.getThisInterestAmount())).compareTo(new BigDecimal(0)) > 0) {
                    if (overdueBill.getOverdueFine().compareTo(thisRepayAmount.subtract(allocationAmount.getThisContractAmount()).subtract(allocationAmount.getThisInterestAmount())) <= 0) {
                        allocationAmount.setThisFeeLate(overdueBill.getOverdueFine());
                    } else {
                        allocationAmount.setThisFeeLate(thisRepayAmount.subtract(allocationAmount.getThisContractAmount()).subtract(allocationAmount.getThisInterestAmount()));
                    }
                }
            }
            //罚息
            if (overdueBill != null) {
                if ((thisRepayAmount.subtract(allocationAmount.getThisContractAmount()).subtract(allocationAmount.getThisInterestAmount()).subtract(allocationAmount.getThisFeeLate())).compareTo(new BigDecimal(0)) > 0) {
                    if (overdueBill.getOverdueAmount().compareTo(thisRepayAmount.subtract(allocationAmount.getThisContractAmount()).subtract(allocationAmount.getThisInterestAmount()).subtract(allocationAmount.getThisFeeLate())) <= 0) {
                        allocationAmount.setThisOverdueFaxi(overdueBill.getOverdueAmount());
                    } else {
                        allocationAmount.setThisOverdueFaxi(thisRepayAmount.subtract(allocationAmount.getThisContractAmount()).subtract(allocationAmount.getThisInterestAmount()).subtract(allocationAmount.getThisFeeLate()));
                    }
                }
            }

            //应退款的
            if ((thisRepayAmount.subtract(allocationAmount.getThisContractAmount()).subtract(allocationAmount.getThisInterestAmount()).subtract(allocationAmount.getThisFeeLate()).subtract(allocationAmount.getThisOverdueFaxi())).compareTo(new BigDecimal(0)) > 0) {
//            if(overdueBill.getOverdueAmount().compareTo(thisRepayAmount.subtract(allocationAmount.getThisContractAmount()).subtract(allocationAmount.getThisInterestAmount()).subtract(allocationAmount.getThisFeeLate()))<=0){
//                allocationAmount.setThisOverdueFaxi(overdueBill.getOverdueAmount());
//            }else {
//                allocationAmount.setThisOverdueFaxi(thisRepayAmount.subtract(allocationAmount.getThisContractAmount()).subtract(allocationAmount.getThisInterestAmount()).subtract(allocationAmount.getThisFeeLate()));
//            }
                allocationAmount.setDrawbackAmount(thisRepayAmount.subtract(allocationAmount.getThisContractAmount()).subtract(allocationAmount.getThisInterestAmount()).subtract(allocationAmount.getThisFeeLate()).subtract(allocationAmount.getThisOverdueFaxi()));
            }

//            BigDecimal totalDerateAmount = derateAmountSum.add(thisDerateAmount);
//            BigDecimal remainDerateAmount = totalDerateAmount;

            return allocationAmount;
        }
        //罚息
//        if(totalDerateAmount.compareTo(allocationAmount.getThisOverdueFaxi())<=0){
//            allocationAmount.setThisOverdueFaxi(allocationAmount.getThisOverdueFaxi().subtract(totalDerateAmount));
//        }else {
//            remainDerateAmount = remainDerateAmount.subtract(allocationAmount.getThisOverdueFaxi());
//            allocationAmount.setThisOverdueFaxi(new BigDecimal(0));
//        }
        //滞纳金
//        if(remainDerateAmount.compareTo(new BigDecimal(0))>0){
//            if(remainDerateAmount.compareTo(allocationAmount.getThisFeeLate())<=0){
//                allocationAmount.setThisFeeLate(allocationAmount.getThisFeeLate().subtract(remainDerateAmount));
//            }else {
//                remainDerateAmount = remainDerateAmount.subtract(allocationAmount.getThisFeeLate());
//                allocationAmount.setThisFeeLate(new BigDecimal(0));
//            }
//        }
        //利息
//        if(remainDerateAmount.compareTo(new BigDecimal(0))>0){
//            if(remainDerateAmount.compareTo(allocationAmount.getThisInterestAmount())<=0){
//                allocationAmount.setThisInterestAmount(allocationAmount.getThisInterestAmount().subtract(remainDerateAmount));
//            }else {
//                remainDerateAmount = remainDerateAmount.subtract(allocationAmount.getThisInterestAmount());
//                allocationAmount.setThisInterestAmount(new BigDecimal(0));
//            }
//        }
        //本金
//        if(remainDerateAmount.compareTo(new BigDecimal(0))>0){
//            if(remainDerateAmount.compareTo(allocationAmount.getThisContractAmount())<=0){
//                allocationAmount.setThisContractAmount(allocationAmount.getThisContractAmount().subtract(remainDerateAmount));
//            }else {
//                remainDerateAmount = remainDerateAmount.subtract(allocationAmount.getThisContractAmount());
//                allocationAmount.setThisContractAmount(new BigDecimal(0));
//            }
//        }




//
//        allocationAmount.setThisContractAmount(bill.getRepayPrinciple().compareTo(repayAmountSum)>=0?thisRepayAmount.subtract(bill.getRepayPrinciple().subtract(repayAmountSum)):new BigDecimal(0));
//        allocationAmount.setThisInterestAmount();
//
//        if(bill.getRepayPrinciple().compareTo(repayAmountSum)>=0){
//            allocationAmount.set
//        }
//        if((repayAmountSum.add(thisRepayAmount)).compareTo(bill.getRepayPrinciple())<=0){
//            allocationAmount.setThisContractAmount(thisRepayAmount);
//        }else if((repayAmountSum.add(thisRepayAmount)).compareTo(bill.getRepayPrinciple().add(bill.getRepayInterest()))<=0){
//            allocationAmount.setThisInterestAmount(repayAmountSum.add(thisRepayAmount).subtract(bill.getRepayPrinciple()));
//            if(bill.getRepayPrinciple().subtract(repayAmountSum).compareTo(new BigDecimal(0))<=0) {
//                allocationAmount.setThisContractAmount(new BigDecimal(0));
//            }else {
//                allocationAmount.setThisContractAmount(bill.getRepayPrinciple().subtract(repayAmountSum));
//            }
//        }else if(overdueBill!=null && (repayAmountSum.add(thisRepayAmount).add(thisDerateAmount).add(thisRepayAmount)).compareTo(bill.getRepayPrinciple().add(bill.getRepayInterest()).add(overdueBill.getOverdueFine()))<=0){
//            allocationAmount.setThisContractAmount(bill.getRepayPrinciple().subtract(repayAmountSum).compareTo(new BigDecimal(0))<=0?new BigDecimal(0):);
//           allocationAmount.setThisInterestAmount();
//           allocationAmount.setThisFeeLate();
//        }else if(overdueBill!=null && (repayAmountSum.add(thisRepayAmount).add(thisDerateAmount).add(thisRepayAmount)).compareTo(bill.getRepayPrinciple().add(bill.getRepayInterest()).add(overdueBill.getOverdueFine().add(overdueBill.getOverdueAmount())))<=0){
//            allocationAmount.setThisContractAmount();
//            allocationAmount.setThisInterestAmount();
//            allocationAmount.setThisFeeLate();
//            allocationAmount.setThisOverdueFaxi();
//        }
//
//        if((repayAmountSum.add(thisRepayAmount)).compareTo(bill.getRepayPrinciple().add(bill.getRepayInterest()))<=0){
//            allocationAmount.setThisInterestAmount();
//        }
//
//
//
//        else{
//            if(repayAmountSum.compareTo(bill.getRepayPrinciple()) <= 0){
//
//            }else{
//
//            }
//        }


    }

    public static void main(String[] args) {
//        Map map = new HashMap();
//        map.put("1","34");
//
//        System.out.println(map.size());
//        map.remove("1");
//        System.out.println(map.size());
        AmountOfHistory amountOfHistory = new AmountOfHistory();
        amountOfHistory.setRepayAmountSum(new BigDecimal(100));
        amountOfHistory.setDerateAmountSum(new BigDecimal(10));

        Bill bill = new Bill();
        bill.setRepayPrinciple(new BigDecimal(500));
        bill.setRepayInterest(new BigDecimal(3.5));
        bill.setReceivableAmount(new BigDecimal(521));

        OverdueBill overdueBill = new OverdueBill();
        overdueBill.setOverdueFine(new BigDecimal(10));
        overdueBill.setOverdueAmount(new BigDecimal(7.5));

        TRepay tRepay = new TRepay();
        tRepay.setRepayAmount(new BigDecimal(400));
        tRepay.setDerateAmount(new BigDecimal(1));


//        System.out.println(Utils.calAllocation(amountOfHistory,tRepay,bill,overdueBill));
    }
}
