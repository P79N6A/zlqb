package com.nyd.order.dao.YmtKzjrBill;

import com.nyd.order.entity.YmtKzjrBill.OverdueBillYmt;
import com.nyd.order.model.YmtKzjrBill.OverdueBillYmtInfo;

import java.util.List;

public interface OverdueBillYmtDao {

    /**
     *
     * 根据子订单号找到相关逾期信息
     * @param orderSno
     * @return
     */
    List<OverdueBillYmtInfo> selectByOrderSno(String orderSno) throws Exception;

    /**
     * 根据账单号找到相关逾期信息
     * @param billNo
     * @return
     * @throws Exception
     */
    OverdueBillYmtInfo selectByBillNo(String billNo) throws Exception;
    /**
     * 跟新逾期账单表
     * @param overdueBillInfo
     */
    void updateByOrderSno(OverdueBillYmtInfo overdueBillInfo) throws Exception;

    /**
     * 根据billNo更新逾期账单信息
     * @param overdueBillInfo
     * @throws Exception
     */
    void updateByBillNo(OverdueBillYmtInfo overdueBillInfo) throws Exception;

    /**
     *
     * @param billStatus
     * @return 更具账单状态查找逾期账单
     * @throws Exception
     */
    List<OverdueBillYmtInfo> selectByBillStatus(String billStatus) throws Exception;

    /**
     * 保存逾期账单
     * @param overdueBillInfo
     * @throws Exception
     */
    void save(OverdueBillYmtInfo overdueBillInfo) throws Exception;
}
