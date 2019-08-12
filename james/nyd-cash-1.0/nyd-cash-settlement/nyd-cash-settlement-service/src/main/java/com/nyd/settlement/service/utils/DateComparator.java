package com.nyd.settlement.service.utils;

import com.nyd.settlement.model.po.repay.RepayAmountOfDay;

import java.util.Calendar;
import java.util.Comparator;

/**
 * Cong Yuxiang
 * 2018/1/24
 **/
public class DateComparator implements Comparator{
    @Override
    public int compare(Object o1, Object o2) {
        RepayAmountOfDay r1 = (RepayAmountOfDay)o1;
        RepayAmountOfDay r2 = (RepayAmountOfDay)o2;

        Calendar c1 = Calendar.getInstance();
        c1.setTime(r1.getRepayTime());

        Calendar c2 = Calendar.getInstance();
        c2.setTime(r2.getRepayTime());
        return c1.compareTo(c2);
    }
}
