package com.nyd.order.api;

import com.nyd.order.entity.YmtKzjrBill.OverdueBillYmt;
import com.nyd.order.model.YmtKzjrBill.BillYmtInfo;
import com.nyd.order.model.YmtKzjrBill.OverdueBillYmtInfo;
import com.nyd.order.model.YmtKzjrBill.dto.BillYmtDto;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

public interface BillYmtContract {

    /**
     *
     * 根据资产找到账单信息
     * @param assetCode
     * @return
     */
    ResponseData<BillYmtInfo> findByAssetCode(String assetCode);


    /**
     * 根据子订单号找到账单信息
     * @param orderSno
     * @return
     */
    ResponseData<BillYmtInfo> findByOrderSno(String orderSno);

    /**
     * 保存银码头账单
     * @param billYmtInfo
     * @return
     */
    ResponseData saveBillYmt(BillYmtInfo billYmtInfo);

    /**
     * 获取前一天的所有待还款的账单
     * @param billYmtDto
     * @return
     */
    ResponseData<List<BillYmtInfo>> getByStatus(BillYmtDto billYmtDto);

    /**
     * 更新逾期的账单状态和新增逾期记录
     * @param billYmtDto
     * @return
     */
    ResponseData updateBillAndAddOverBill(BillYmtDto billYmtDto);

    /**
     * 更新逾期记录
     * @param billYmtDto
     * @return
     */
    ResponseData updateOverdueBill(BillYmtDto billYmtDto);

    /**
     * 更具状态获取逾期账单信息
     * @param billStatus
     * @return
     */
    ResponseData<List<OverdueBillYmtInfo>> getOverBillByStatus(String billStatus);

    /**
     * 查询还款的银码头账单信息
     * @param billYmtDto
     * @return
     */
    ResponseData<BillYmtInfo> selectByAssetCodeAndPeriod(BillYmtDto billYmtDto);

    /**
     * 更新银码头账单
     * @param billYmtInfo
     * @return
     */
    ResponseData updateByOrderSno(BillYmtInfo billYmtInfo);

    /**
     * 逾期罚息
     * @param billNo
     * @return
     */
    ResponseData<OverdueBillYmtInfo>  getOverdueBillInfoByBillNo(String billNo);

    /**
     * 获取未还清账单
     * @param ymtUserId
     * @return
     */
    ResponseData<List<BillYmtInfo>> getUnRepayBillByUserId(String ymtUserId);

    /**
     * 根据billno获取订单
     * @param billNo
     * @return
     */
    ResponseData<BillYmtInfo> getBillYmtByBillNo(String billNo);

    ResponseData updateOverDuebillYmt(OverdueBillYmtInfo overdueBillYmtInfo);

}
