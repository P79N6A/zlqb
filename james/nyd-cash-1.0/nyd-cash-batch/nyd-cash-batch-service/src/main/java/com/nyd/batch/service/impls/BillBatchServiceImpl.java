package com.nyd.batch.service.impls;

import com.google.gson.Gson;
import com.nyd.batch.dao.ds.DataSourceContextHolder;
import com.nyd.batch.dao.mapper.BillMapper;
import com.nyd.batch.entity.Bill;
import com.nyd.batch.service.BillBatchService;
import com.nyd.batch.service.aspect.RoutingDataSource;
import com.nyd.batch.service.enums.BillStatusEnum;
import com.nyd.batch.service.quartz.BillTask;
import com.nyd.batch.service.util.DateUtil;
import com.nyd.batch.service.util.OverdueAmountUtil;
import com.nyd.order.api.OrderContract;
import com.nyd.order.api.OrderDetailContract;
import com.nyd.order.model.OrderDetailInfo;
import com.nyd.order.model.OrderInfo;
import com.nyd.product.api.ProductContract;
import com.nyd.product.model.ProductOverdueFeeItemInfo;
import com.nyd.zeus.api.BillContract;
import com.nyd.zeus.model.BillInfo;
import com.nyd.zeus.model.OverdueBillInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by zhujx on 2017/11/20.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BillBatchServiceImpl implements BillBatchService {

    private static Logger LOGGER = LoggerFactory.getLogger(BillTask.class);

    @Autowired
    BillContract billContract;

    @Autowired
    OrderContract orderContract;

    @Autowired
    OrderDetailContract orderDetailContract;

    @Autowired
    ProductContract productContract;

    @Autowired
    private BillMapper billMapper;

    /**
     * 更新账单状态为逾期，插入一条逾期账单
     * @param bill
     */
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public void updateBillInfoAndOverdueBillInfo(Bill bill) throws Exception {
        try {
            BillInfo billInfo = new BillInfo();
            BeanUtils.copyProperties(bill,billInfo);
            LOGGER.info("update billInfo data is :" + new Gson().toJson(billInfo));
            /**账单信息**/
            //查询订单详细信息
            OrderDetailInfo orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(billInfo.getOrderNo()).getData();
            //查询逾期计算费率信息
            ProductOverdueFeeItemInfo productOverdueFeeItemInfo = productContract.getProductOverdueFeeItemInfo(orderDetailInfo.getProductCode()).getData();

            billInfo.setBillStatus(BillStatusEnum.REPAY_OVEDUE.getCode());

            //待还款金额+滞纳金
            BigDecimal waitRepayAmount = billInfo.getWaitRepayAmount().add(productOverdueFeeItemInfo.getOverdueFine());
            //应实收金额加上滞纳金
            BigDecimal receivableAmount   = billInfo.getReceivableAmount().add(productOverdueFeeItemInfo.getOverdueFine());
            billInfo.setWaitRepayAmount(waitRepayAmount);
            billInfo.setReceivableAmount(receivableAmount);

            //更新账单信息
            billContract.updateBillInfoByBillNo(billInfo);

            /**逾期账单信息**/
            OverdueBillInfo overdueBillInfo = new OverdueBillInfo();
            overdueBillInfo.setUserId(billInfo.getUserId());
            overdueBillInfo.setBillNo(billInfo.getBillNo());
            overdueBillInfo.setOrderNo(billInfo.getOrderNo());
            overdueBillInfo.setBillStatus(BillStatusEnum.REPAY_OVEDUE.getCode());
            overdueBillInfo.setOverdueFine(productOverdueFeeItemInfo.getOverdueFine());

            //生成一条逾期账单
            billContract.saveOverdueBillInfo(overdueBillInfo);
        }catch(Exception e){
            LOGGER.info("updateBillInfoAndOverdueBillInfo:" + e.getMessage());
            throw new Exception("runtime exception");
        }
    }


    /**
     * 更新逾期账单
     * @param overdueBillInfo
     */
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public void updateOverdueBillInfo(OverdueBillInfo overdueBillInfo) {
        try {
            //查询主账单信息
            BillInfo billInfo = billContract.getBillInfo(overdueBillInfo.getBillNo()).getData();
            //查询订单详细信息
            OrderDetailInfo orderDetailInfo =  orderDetailContract.getOrderDetailByOrderNo(billInfo.getOrderNo()).getData();
            //查询订单信息
            OrderInfo orderInfo = orderContract.getOrderByOrderNo(billInfo.getOrderNo()).getData();
            //查询逾期计算费率信息
            ProductOverdueFeeItemInfo productOverdueFeeItemInfo = productContract.getProductOverdueFeeItemInfo(orderDetailInfo.getProductCode()).getData();
            BigDecimal totalAmount = billInfo.getRepayInterest().add(overdueBillInfo.getOverdueFine()).add(overdueBillInfo.getOverdueAmount());
            //逾期综合利率不超过最大综合利率&&剩余应还本金大于0的情况下计算罚息
            //LOGGER.info("billInfo:" + billInfo.toString());
            if(totalAmount.divide(billInfo.getRepayPrinciple(),2,BigDecimal.ROUND_HALF_UP).compareTo(productOverdueFeeItemInfo.getMaxOverdueFeeRate().divide(new BigDecimal(100))) == -1
                    && billInfo.getWaitRepayPrinciple().compareTo(new BigDecimal(0)) == 1){
                //计算逾期天数
                int overdueDays = DateUtil.getDay(billInfo.getPromiseRepaymentDate(),new Date());
                //LOGGER.info("overdueDays:" + overdueDays);
                if(overdueDays != overdueBillInfo.getOverdueDays()){
                    BigDecimal dayOverdueAmount = OverdueAmountUtil.dayOverdueAmount(
                            overdueDays,productOverdueFeeItemInfo,billInfo);
                    //当前罚息+新第一天的罚息
                    BigDecimal overdueAmount = overdueBillInfo.getOverdueAmount().add(dayOverdueAmount);
                    overdueBillInfo.setOverdueDays(overdueDays);
                    overdueBillInfo.setOverdueAmount(overdueAmount);

                    //根据账单号更新逾期账单
                    billContract.updateOverdueBillInfoByBillNo(overdueBillInfo);

                    //待还款金额加上新一天的罚息
                    BigDecimal waitRepayAmount = billInfo.getWaitRepayAmount().add(dayOverdueAmount);
                    //应实收金额加上滞纳金
                    BigDecimal receivableAmount   = billInfo.getReceivableAmount().add(dayOverdueAmount);
                    billInfo.setWaitRepayAmount(waitRepayAmount);
                    billInfo.setReceivableAmount(receivableAmount);

                    //更新账单信息
                    billContract.updateBillInfoByBillNo(billInfo);
                }
            }else {
                //计算逾期天数
                int overdueDays = DateUtil.getDay(billInfo.getPromiseRepaymentDate(),new Date());
                //LOGGER.info("overdueDays:" + overdueDays);
                if(overdueDays != overdueBillInfo.getOverdueDays()){
                    overdueBillInfo.setOverdueDays(overdueDays);
                    //根据账单号更新逾期账单
                    billContract.updateOverdueBillInfoByBillNo(overdueBillInfo);
                }
            }
        }catch(Exception e){
            LOGGER.info("updateOverdueBillInfo:"+e.getMessage());
        }
    }
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public List<Bill> getSmsBills() {
        return billMapper.getSmsBills();
    }

}
