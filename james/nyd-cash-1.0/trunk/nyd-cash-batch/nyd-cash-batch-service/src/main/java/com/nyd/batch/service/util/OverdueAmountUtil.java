package com.nyd.batch.service.util;

import com.nyd.product.model.ProductOverdueFeeItemInfo;
import com.nyd.zeus.model.BillInfo;

import java.math.BigDecimal;

/**
 * Created by zhujx on 2017/11/20.
 */
public class OverdueAmountUtil {

    /**
     * 计算每一天的逾期罚息
     * @param overdueDays
     * @param productOverdueFeeItemInfo
     * @param billInfo
     * @return
     */
    public static BigDecimal dayOverdueAmount(int overdueDays,ProductOverdueFeeItemInfo productOverdueFeeItemInfo,BillInfo billInfo){
        BigDecimal dayOverdueAmount = new BigDecimal(0);
        if(overdueDays <= productOverdueFeeItemInfo.getGearOverdueFeeDays()){
            //剩余应还本金*逾期第一档费率
            dayOverdueAmount = billInfo.getWaitRepayPrinciple().multiply(productOverdueFeeItemInfo.getFirstGearOverdueRate().divide(new BigDecimal(100)));
        }else if(overdueDays > productOverdueFeeItemInfo.getGearOverdueFeeDays()){
            //剩余应还本金*逾期第二档费率
            dayOverdueAmount = billInfo.getWaitRepayPrinciple().multiply(productOverdueFeeItemInfo.getSecondGearOverdueRate().divide(new BigDecimal(100)));
        }
        return dayOverdueAmount;
    }
}
