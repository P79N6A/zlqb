package com.nyd.order.dao.YmtKzjrBill;


import com.nyd.order.entity.YmtKzjrBill.BillYmt;
import com.nyd.order.model.YmtKzjrBill.BillYmtInfo;
import com.nyd.order.model.YmtKzjrBill.dto.BillYmtDto;

import java.util.List;

public interface BillYmtDao {


    /**
     * 根据子订单号查找账单信息
     * @param orderSno
     * @return
     * @throws Exception
     */
    List<BillYmtInfo> selectByOrderSno(String orderSno) throws Exception;


    /**
     * 根据资产编号，找到账单信息
     *
     * @param assetCode
     * @return
     */
    List<BillYmtInfo> selectByAssetCode(String assetCode) throws Exception;

    /**
     * 更新账单状态
     * @param billYmtInfo
     * @throws Exception
     */
    void updateByOrderSno(BillYmtInfo billYmtInfo) throws Exception;

    /**
     * 保存空中金融账单
     * @param billYmt
     * @throws Exception
     */
    void save(BillYmt billYmt) throws Exception;


    /**
     * 查找约定还款时间在前一天初始状态的账单
     * @param billYmtDto
     * @return
     * @throws Exception
     */
    List<BillYmtInfo> getByTimeAndStatus(BillYmtDto billYmtDto)throws Exception;

    /**
     * 查询未结清记录
     */
    List<BillYmtInfo> getUnRepayBillByUserId(String ymtUserId) throws Exception;

    /**
     * 更具帐单号更新账单
     * @param billYmtInfo
     * @throws Exception
     */
    void updateByBillNo(BillYmtInfo billYmtInfo) throws Exception;

    /**
     * 根据帐单号查找账单信息
     * @param billNo
     * @return
     * @throws Exception
     */
    List<BillYmtInfo> getBillInfoByBillNo(String billNo) throws Exception;

    /**
     * 查找需要还款时的账单
     * @param assetCode
     * @param currentPeriod
     * @param billStatus
     * @return
     * @throws Exception
     */
    BillYmtInfo selectByAssetCodeAndPeriod(String assetCode,int currentPeriod,String  billStatus) throws Exception;
}
