package com.nyd.settlement.service.utils;

import com.nyd.product.model.ProductOverdueFeeItemInfo;
import com.nyd.settlement.model.po.repay.RepayAmountOfDay;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * Cong Yuxiang
 * 2018/1/19
 **/
public class AmountUtils {
    public static BigDecimal calcuteAmountAfter(String repayDateStr, List<RepayAmountOfDay> amountOfDayList) throws ParseException {
        if(amountOfDayList==null||amountOfDayList.size()==0){
            return new BigDecimal("0");
        }

        Date repayDate = DateUtils.parseDate(repayDateStr,"yyyy-MM-dd HH:mm:ss");

        Calendar repayDateCalendar = Calendar.getInstance();
        repayDateCalendar.setTime(repayDate);

        BigDecimal result = new BigDecimal("0");
        for(RepayAmountOfDay repayAmountOfDay:amountOfDayList){

            Calendar amountOfDayCalendar = Calendar.getInstance();
            amountOfDayCalendar.setTime(repayAmountOfDay.getRepayTime());

            if(amountOfDayCalendar.compareTo(repayDateCalendar)>=0){
                result = result.add(repayAmountOfDay.getRepayAmountOfDay());
            }

        }
        return result;
    }
    public static BigDecimal calcuteAmountBefore(String repayDateStr, List<RepayAmountOfDay> amountOfDayList) throws ParseException {
        if(amountOfDayList==null||amountOfDayList.size()==0){
            return new BigDecimal("0");
        }
        if(repayDateStr.trim().length()!=19){
            repayDateStr = repayDateStr+" 00:00:00";
        }
        Date repayDate = DateUtils.parseDate(repayDateStr,"yyyy-MM-dd HH:mm:ss");
        Calendar repayDateCalendar = Calendar.getInstance();
        repayDateCalendar.setTime(repayDate);
        BigDecimal result = new BigDecimal("0");

        for(RepayAmountOfDay repayAmountOfDay:amountOfDayList){



            Calendar amountOfDayCalendar = Calendar.getInstance();
            amountOfDayCalendar.setTime(repayAmountOfDay.getRepayTime());

            if(amountOfDayCalendar.compareTo(repayDateCalendar)<0){
                result = result.add(repayAmountOfDay.getRepayAmountOfDay());
            }

        }
        return result;
    }

    public static BigDecimal calcuteDerateBefore(String repayDateStr, List<RepayAmountOfDay> amountOfDayList) throws ParseException {
        if(amountOfDayList==null||amountOfDayList.size()==0){
            return new BigDecimal("0");
        }
        if(repayDateStr.trim().length()!=19){
            repayDateStr = repayDateStr+" 00:00:00";
        }
        Date repayDate = DateUtils.parseDate(repayDateStr,"yyyy-MM-dd HH:mm:ss");
        Calendar repayDateCalendar = Calendar.getInstance();
        repayDateCalendar.setTime(repayDate);
        BigDecimal result = new BigDecimal("0");

        for(RepayAmountOfDay repayAmountOfDay:amountOfDayList){



            Calendar amountOfDayCalendar = Calendar.getInstance();
            amountOfDayCalendar.setTime(repayAmountOfDay.getRepayTime());

            if(amountOfDayCalendar.compareTo(repayDateCalendar)<0){
                result = result.add(repayAmountOfDay.getDerateAmountOfDay());
            }

        }
        return result;
    }

    /**
     *
     * @param repayDateStr  yyyy-MM-dd HH:mm:ss
     * @param amountOfDayList
     * @param promiseDate  yyyy-MM-dd
     * @return
     * @throws ParseException
     */
    public static TreeMap<Integer,BigDecimal> calAmountAndOverdueBefore(String repayDateStr, List<RepayAmountOfDay> amountOfDayList,String promiseDate) throws ParseException {

        TreeMap<Integer,BigDecimal> result = new TreeMap();
        String repayDate = repayDateStr.split(" ")[0];
        if(amountOfDayList==null){
            int days = DateUtil.calcuteDateBetween(repayDate,promiseDate);
            if(result.get(days)==null){
                result.put(days,new BigDecimal(0));
            }
            return result;
        }
//        Date repayDate = DateUtils.parseDate(repayDateStr,"yyyy-MM-dd HH:mm:ss");
//        Date repayDate = DateUtils.parseDate(repayDateStr.split(" ")[0],"yyyy-MM-dd");
//        Calendar repayDateCalendar = Calendar.getInstance();
//        repayDateCalendar.setTime(repayDate);


        for(RepayAmountOfDay repayAmountOfDay:amountOfDayList){

//            Calendar amountOfDayCalendar = Calendar.getInstance();
//            amountOfDayCalendar.setTime(repayAmountOfDay.getRepayTime());
            String recordDate = DateFormatUtils.format(repayAmountOfDay.getRepayTime(),"yyyy-MM-dd");
            if(DateUtil.calcuteDateBetween(recordDate,repayDate)==0){

                int days = DateUtil.calcuteDateBetween(recordDate,promiseDate);
                if(days==0){
                    if(result.get(0)==null){
                        result.put(0,repayAmountOfDay.getRepayAmountOfDay());
                    }else {
                       BigDecimal tmp = result.get(0);
                       result.put(0,repayAmountOfDay.getRepayAmountOfDay().add(tmp));
                    }
                }else {
                    if(result.get(days)==null){
                        result.put(days,repayAmountOfDay.getRepayAmountOfDay());
                    }else {
                        BigDecimal tmp = result.get(days);
                        result.put(days,repayAmountOfDay.getRepayAmountOfDay().add(tmp));
                    }
                }
//                result.add(repayAmountOfDay.getRepayAmountOfDay());
            }

        }
        //将最后这一天的截止日 也加上。
        int days = DateUtil.calcuteDateBetween(repayDate,promiseDate);
        if(result.get(days)==null){
            result.put(days,new BigDecimal(0));
        }

        return result;
    }

    public static TreeMap<Integer,BigDecimal> calAmountAndOverdueAll(String repayDateStr,List<RepayAmountOfDay> amountOfDayList,String promiseDate) throws ParseException {
        if(amountOfDayList==null){
            return new TreeMap<Integer,BigDecimal>();
        }
        TreeMap<Integer,BigDecimal> result = new TreeMap();


        for(RepayAmountOfDay repayAmountOfDay:amountOfDayList){


            String recordDate = DateFormatUtils.format(repayAmountOfDay.getRepayTime(),"yyyy-MM-dd");


                int days = DateUtil.calcuteDateBetween(recordDate,promiseDate);
                if(days==0){
                    if(result.get(0)==null){
                        result.put(0,repayAmountOfDay.getRepayAmountOfDay());
                    }else {
                        BigDecimal tmp = result.get(0);
                        result.put(0,repayAmountOfDay.getRepayAmountOfDay().add(tmp));
                    }
                }else {
                    if(result.get(days)==null){
                        result.put(days,repayAmountOfDay.getRepayAmountOfDay());
                    }else {
                        BigDecimal tmp = result.get(days);
                        result.put(days,repayAmountOfDay.getRepayAmountOfDay().add(tmp));
                    }
                }
//                result.add(repayAmountOfDay.getRepayAmountOfDay());
            }

//        int days = DateUtil.calcuteDateBetween(DateFormatUtils.format(new Date(),"yyyy-MM-dd"),promiseDate);
//        if(result.get(days)==null){
//            result.put(days,new BigDecimal(0));
//        }
        return result;
    }

    /**
     *
     * @param repayDateStr 当前还款时间 yyyy-MM-dd HH:mm:ss
     * @param amountOfDayList  已还款金额
     * @param curRepayAmount  当前还款金额
     * @param receivableAmount  在最近还款日时 的应还金额
     * @return
     * @throws ParseException
     */
    public static Date calSettleDate(Date repayDateStr,List<RepayAmountOfDay> amountOfDayList,BigDecimal curRepayAmount,BigDecimal receivableAmount) throws ParseException {
        RepayAmountOfDay cur = new RepayAmountOfDay();
        cur.setRepayAmountOfDay(curRepayAmount);
        cur.setRepayTime(repayDateStr);
        List<RepayAmountOfDay> list = new ArrayList<>();
        list.add(cur);
        if(amountOfDayList!=null){
            for(RepayAmountOfDay repayAmountOfDay:amountOfDayList){
                list.add(repayAmountOfDay);
            }
        }
        Collections.sort(list,new DateComparator());

        BigDecimal alreadyAmount = new BigDecimal(0);
        for(RepayAmountOfDay repayAmountOfDay:list){
            alreadyAmount = alreadyAmount.add(repayAmountOfDay.getRepayAmountOfDay());
            if(alreadyAmount.compareTo(receivableAmount)>=0){
                return repayAmountOfDay.getRepayTime();
            }
        }
        return null;
    }

    public static BigDecimal calFaxi(BigDecimal remainPrinciple, ProductOverdueFeeItemInfo productOverdueFeeItemInfo, int startDay, int endDay){
        BigDecimal faxi = new BigDecimal("0");
        for(int i=startDay;i<=endDay;i++){
            if(i <= productOverdueFeeItemInfo.getGearOverdueFeeDays()){
                //剩余应还本金*逾期第一档费率
                faxi = faxi.add(remainPrinciple.multiply(productOverdueFeeItemInfo.getFirstGearOverdueRate().divide(new BigDecimal(100))));
            }else if(i > productOverdueFeeItemInfo.getGearOverdueFeeDays()){
                //剩余应还本金*逾期第二档费率
                faxi =faxi.add(remainPrinciple.multiply(productOverdueFeeItemInfo.getSecondGearOverdueRate().divide(new BigDecimal(100))));
            }
        }
        return faxi;
    }

    public static void main(String[] args) throws Exception {
//        System.out.println(calcuteDate("2017-12-23","2017-12-25"));

        Date repayDateStr = DateUtils.parseDate("2018-01-24 11:11:11","yyyy-MM-dd HH:mm:ss");
        List<RepayAmountOfDay> list = new ArrayList<>();

        RepayAmountOfDay r1 = new RepayAmountOfDay();
        RepayAmountOfDay r2 = new RepayAmountOfDay();
        r1.setRepayTime(DateUtils.parseDate("2018-01-23 11:11:11","yyyy-MM-dd HH:mm:ss"));
        r1.setRepayAmountOfDay(new BigDecimal("100"));
        r2.setRepayTime(DateUtils.parseDate("2018-01-22 11:11:11","yyyy-MM-dd HH:mm:ss"));
        r2.setRepayAmountOfDay(new BigDecimal("200"));
        list.add(r1);
        list.add(r2);

        System.out.println(AmountUtils.calSettleDate(repayDateStr,list,new BigDecimal("150"),new BigDecimal("450")));

    }
}
