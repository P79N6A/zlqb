package com.nyd.order.service.YmtKzjr.impl;


import com.alibaba.fastjson.JSON;
import com.nyd.order.api.BillYmtContract;
import com.nyd.order.dao.YmtKzjrBill.BillYmtDao;
import com.nyd.order.dao.YmtKzjrBill.OverdueBillYmtDao;
import com.nyd.order.entity.YmtKzjrBill.BillYmt;
import com.nyd.order.model.YmtKzjrBill.BillYmtInfo;
import com.nyd.order.model.YmtKzjrBill.OverdueBillYmtInfo;
import com.nyd.order.model.YmtKzjrBill.dto.BillYmtDto;
import com.nyd.order.model.YmtKzjrBill.dto.ProductOverdueFeeItemYmtDto;
import com.nyd.order.model.YmtKzjrBill.enums.BillStatusEnum;
import com.nyd.order.service.util.DateUtil;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service(value = "billYmtContract")
public class BillYmtContractImpl implements BillYmtContract {
    private static Logger logger = LoggerFactory.getLogger(BillYmtContractImpl.class);

    @Autowired
    private BillYmtDao billYmtDao;
    @Autowired
    private OverdueBillYmtDao overdueBillYmtDao;

    @Override
    public ResponseData<BillYmtInfo> findByAssetCode(String assetCode) {
        logger.info("空中金融产品还款资产编号："+assetCode);
        ResponseData responseData = ResponseData.success();
        if (!StringUtils.isNotBlank(assetCode)){
            logger.error("资产编号为空");
            responseData = responseData.error("资产编号为空");
            return responseData;
        }

        try {
            List<BillYmtInfo> list = billYmtDao.selectByAssetCode(assetCode);
            if (list != null && list.size() > 0 ){
                BillYmtInfo billYmtInfo = list.get(0);
                responseData.setData(billYmtInfo);
            }else {
                logger.info("资产编号:"+assetCode+"不存在");
                responseData = responseData.error("该订单不存在");
                return responseData;
            }
        }catch (Exception e){
            logger.error("find repay detail by assetCode has error ",e);
            responseData = responseData.error("服务器开小差");
        }

        return responseData;
    }

    @Override
    public ResponseData<BillYmtInfo> findByOrderSno(String orderSno) {
        logger.info("空中金融产品还款子订单号："+orderSno);
        ResponseData responseData = ResponseData.success();
        if (!StringUtils.isNotBlank(orderSno)){
            logger.error("子订单号为空");
            responseData = responseData.error("子订单号为空");
            return responseData;
        }

        try {
            List<BillYmtInfo> list = billYmtDao.selectByOrderSno(orderSno);
            if (list != null && list.size() > 0 ){
                BillYmtInfo billInfo = list.get(0);
                responseData.setData(billInfo);
            }else {
                logger.error("子订单:"+orderSno+"不存在");
                responseData = responseData.error("该订单不存在");
                return responseData;
            }
        }catch (Exception e){
            logger.error("find repay detail by orderSno has error ",e);
            responseData = responseData.error("服务器开小差");
        }
        return responseData;
    }

    @Override
    public ResponseData saveBillYmt(BillYmtInfo billYmtInfo) {
        logger.info("nyd 保存银码头账单 start param is "+ JSON.toJSONString(billYmtInfo));
        ResponseData responseData = ResponseData.success();
        BillYmt billYmt = new BillYmt();
        try {
            BeanUtils.copyProperties(billYmtInfo,billYmt);
            billYmtDao.save(billYmt);
        } catch (Exception e) {
            logger.error("nyd 保存银码头账单 start param is "+ JSON.toJSONString(billYmtInfo),e);
            responseData = responseData.error("服务器开小差");
        }
        return responseData;
    }

    @Override
    public ResponseData<List<BillYmtInfo>> getByStatus(BillYmtDto billYmtDto) {
        logger.info("获取约定还款日在前一天订单列表 billDto:"+ JSON.toJSONString(billYmtDto));
        ResponseData responseData = ResponseData.success();
        try {
            List<BillYmtInfo> billInfos = billYmtDao.getByTimeAndStatus(billYmtDto);
            responseData.setData(billInfos);
            logger.info("获取的到期订单数量 size:"+billInfos.size());
        } catch (Exception e) {
            logger.error("获取约定还款日在前一天订单列表异常",e);
            responseData = ResponseData.error("服务器开小差");
        }
        return responseData;
    }

    @Override
    public ResponseData updateBillAndAddOverBill(BillYmtDto billYmtDto) {
        logger.info("更新逾期账单和添加逾期账单表 start params is" +JSON.toJSONString(billYmtDto));
        ResponseData responseData = ResponseData.success();
        try {
            BillYmtInfo billInfo = billYmtDto.getBillYmtInfo();
            ProductOverdueFeeItemYmtDto productOverdueFeeItemInfo = billYmtDto.getProductOverdueFeeItemYmtDto();
           // OrderDetailInfo orderDetailInfo =orderDetailDao.getByOrderSon(orderSon).get(0);
            //logger.debug("获取订单详情orderDetailInfo:"+JSON.toJSONString(orderDetailInfo));
            //逾期费率配置信息
          //  ProductOverdueFeeItemInfo productOverdueFeeItemInfo = appProductContract.getOverdueFeeItemByCode(orderDetailInfo.getAppCode()).getData();
            //待还款金额+滞纳金
            BigDecimal waitRepayAmount =  billInfo.getWaitRepayAmount().add(productOverdueFeeItemInfo.getOverdueFine());
            //应实收金额加上滞纳金
            BigDecimal receivableAmount   = billInfo.getReceivableAmount().add(productOverdueFeeItemInfo.getOverdueFine());
            //更新bill逾期信息
            BillYmtInfo updateBill = new BillYmtInfo();
            updateBill.setBillStatus(BillStatusEnum.REPAY_OVEDUE.getCode());
            updateBill.setWaitRepayAmount(waitRepayAmount);
            updateBill.setReceivableAmount(receivableAmount);
            updateBill.setBillNo(billInfo.getBillNo());
            billYmtDao.updateByBillNo(updateBill);
            logger.info("更新bill信息成功 billNo:"+billInfo.getBillNo());
            //新增与其表信息
            OverdueBillYmtInfo overdueBillInfo = new OverdueBillYmtInfo();
            overdueBillInfo.setUserId(billInfo.getUserId());
            overdueBillInfo.setBillNo(billInfo.getBillNo());
            overdueBillInfo.setOrderNo(billInfo.getOrderNo());
            overdueBillInfo.setOrderSno(billInfo.getOrderSno());
            overdueBillInfo.setBillStatus(BillStatusEnum.REPAY_OVEDUE.getCode());
            overdueBillInfo.setOverdueFine(productOverdueFeeItemInfo.getOverdueFine());
            overdueBillYmtDao.save(overdueBillInfo);
            logger.info("保存overdueBillInfo信息成功 billNo:"+billInfo.getBillNo());
        } catch (Exception e) {
            logger.error("更新逾期账单和添加逾期账单表 start异常",e);
            responseData = ResponseData.error("服务器开小差");
        }
        return responseData;
    }

    @Override
    public ResponseData updateOverdueBill(BillYmtDto billYmtDto) {
        logger.info("updateOverdueBill start param is "+JSON.toJSONString(billYmtDto));
        ResponseData responseData = ResponseData.success();
        try {
            OverdueBillYmtInfo overdueBillYmtInfo = billYmtDto.getOverdueBillYmtInfo();
            ProductOverdueFeeItemYmtDto productOverdueFeeItemInfo = billYmtDto.getProductOverdueFeeItemYmtDto();
            //查询主账单信息
            BillYmtInfo billInfo = billYmtDao.getBillInfoByBillNo(overdueBillYmtInfo.getBillNo()).get(0);
            //查询订单详细信息
//            OrderDetailInfo orderDetailInfo = orderDetailDao.getByOrderSon(overdueBillYmtInfo.getOrderSno()).get(0);
//            logger.debug("获取订单详情信息："+JSON.toJSONString(orderDetailInfo));
            //逾期费率配置信息
           // ProductOverdueFeeItemInfo productOverdueFeeItemInfo = appProductContract.getOverdueFeeItemByCode(orderDetailInfo.getAppCode()).getData();
            //计算逾期天数
            int overdueDays = DateUtil.getDay(billInfo.getPromiseRepaymentDate(),new Date());
            logger.info("biilNo:"+overdueBillYmtInfo.getBillNo()+" 逾期天数:"+overdueDays);
            //计算总的逾期利率
            BigDecimal totalOverdue  = new BigDecimal("0.00");
            if(overdueDays <= productOverdueFeeItemInfo.getGearOverdueFeeDays()){
                totalOverdue = productOverdueFeeItemInfo.getFirstGearOverdueRate().multiply(new BigDecimal(overdueDays)).setScale(2,BigDecimal.ROUND_HALF_UP);
            }else if(overdueDays > productOverdueFeeItemInfo.getGearOverdueFeeDays()){
                int diffDay = overdueDays-productOverdueFeeItemInfo.getGearOverdueFeeDays();
                totalOverdue =  productOverdueFeeItemInfo.getFirstGearOverdueRate().multiply(new BigDecimal(productOverdueFeeItemInfo.getGearOverdueFeeDays()))
                        .add(productOverdueFeeItemInfo.getSecondGearOverdueRate().multiply(new BigDecimal(diffDay))).setScale(2,BigDecimal.ROUND_HALF_UP);
            }
            //如果总的逾期率超过最大值则为最大值
            if(totalOverdue.compareTo(productOverdueFeeItemInfo.getMaxOverdueFeeRate())==-1 && billInfo.getWaitRepayPrinciple().compareTo(new BigDecimal(0)) == 1){
                if(overdueDays != overdueBillYmtInfo.getOverdueDays()){
                    //计算日逾期金额 = 剩余应还本金*逾期率
                    BigDecimal dayOverdueAmount = dayOverdueAmount(overdueDays,productOverdueFeeItemInfo,billInfo);
                    logger.info("当日逾期金融 dayOverdueAmount："+dayOverdueAmount);
                    //当前罚息+新第一天的罚息
                    BigDecimal overdueAmount = overdueBillYmtInfo.getOverdueAmount().add(dayOverdueAmount);
                    OverdueBillYmtInfo updateOverdueBillInfo = new OverdueBillYmtInfo();
                    updateOverdueBillInfo.setOverdueDays(overdueDays);
                    updateOverdueBillInfo.setOverdueAmount(overdueAmount);
                    updateOverdueBillInfo.setBillNo(overdueBillYmtInfo.getBillNo());
                    //根据账单号更新逾期账单
                    logger.info("逾期跑批 更新账单逾期信息："+JSON.toJSONString(updateOverdueBillInfo));
                    overdueBillYmtDao.updateByBillNo(updateOverdueBillInfo);
                    logger.info("更新bill成功 billNo:"+overdueBillYmtInfo.getBillNo());
                    //待还款金额加上新一天的罚息
                    BigDecimal waitRepayAmount = billInfo.getWaitRepayAmount().add(dayOverdueAmount);
                    //应实收金额加上滞纳金
                    BigDecimal receivableAmount   = billInfo.getReceivableAmount().add(dayOverdueAmount);

                    BillYmtInfo updateBillInfo = new BillYmtInfo();
                    updateBillInfo.setWaitRepayAmount(waitRepayAmount);
                    updateBillInfo.setReceivableAmount(receivableAmount);
                    updateBillInfo.setBillNo(billInfo.getBillNo());
                    logger.info("逾期跑批 更新已经逾期状态的账单信息："+JSON.toJSONString(updateBillInfo));
                    billYmtDao.updateByBillNo(updateBillInfo);
                }
            }else{
                if(overdueDays != overdueBillYmtInfo.getOverdueDays()){
                    OverdueBillYmtInfo updateOverdueBillInfo = new OverdueBillYmtInfo();
                    updateOverdueBillInfo.setBillNo(overdueBillYmtInfo.getBillNo());
                    updateOverdueBillInfo.setOverdueDays(overdueDays);
                    //根据账单号更新逾期账单
                    logger.info("逾期跑批 更新已经超过最大逾期金额的逾期信息："+JSON.toJSONString(updateOverdueBillInfo));
                    overdueBillYmtDao.updateByBillNo(updateOverdueBillInfo);
                }
            }
            logger.info("biilNo:"+overdueBillYmtInfo.getBillNo()+"  总逾期率："+totalOverdue);

        } catch (Exception e) {
            logger.error("更新逾期账单异常 请求参数:"+JSON.toJSONString(billYmtDto),e);
            responseData.error("服务器开小差");
        }
        return responseData;
    }

    @Override
    public ResponseData<List<OverdueBillYmtInfo>> getOverBillByStatus(String billStatus) {
        ResponseData responseData = ResponseData.success();
        if(null==BillStatusEnum.getByCode(billStatus)){
            logger.info("该账单状态不存在 billStatus："+billStatus);
            responseData = responseData.error("参数异常");
            return responseData;
        }
        try {
            List<OverdueBillYmtInfo> overdueBillInfos = overdueBillYmtDao.selectByBillStatus(billStatus);
            responseData.setData(overdueBillInfos);
            logger.info("获取逾期账单信息 数量:"+overdueBillInfos.size());
        } catch (Exception e) {
            logger.error("获取逾期账单异常 date:"+ DateUtil.dateToString(new Date()),e);
            responseData = responseData.error("服务器开小差");
        }
        return responseData;
    }

    //每日逾期账单 前一天的逾期金额
    private BigDecimal dayOverdueAmount(int overdueDays, ProductOverdueFeeItemYmtDto productOverdueFeeItemInfo, BillYmtInfo billInfo){
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

    @Override
    public ResponseData<BillYmtInfo> selectByAssetCodeAndPeriod(BillYmtDto billYmtDto) {
        ResponseData responseData = ResponseData.success();
        logger.info("查询还款的银码头账单 start billYmtDto:"+JSON.toJSONString(billYmtDto));
        try {
            BillYmtInfo billYmtInfo = billYmtDao.selectByAssetCodeAndPeriod(billYmtDto.getAssetCode(),billYmtDto.getCurrentPeriod(),billYmtDto.getStatus());
            responseData.setData(billYmtInfo);
        }catch (Exception e){
            logger.error("selectByAssetCodeAndPeriod 异常 params："+JSON.toJSONString(billYmtDto),e);
            responseData =  ResponseData.error("服务器开小差");
        }
        return responseData;
    }

    @Override
    public ResponseData updateByOrderSno(BillYmtInfo billYmtInfo) {
        ResponseData responseData = ResponseData.success();
        logger.info("查询还款的银码头账单 start billYmtDto:"+JSON.toJSONString(billYmtInfo));
        try {
            billYmtDao.updateByOrderSno(billYmtInfo);
        }catch (Exception e){
            logger.error("updateByOrderSno 异常 orderSno："+JSON.toJSONString(billYmtInfo.getOrderSno()),e);
            responseData =  ResponseData.error("服务器开小差");
        }
        return responseData;
    }

    @Override
    public ResponseData<OverdueBillYmtInfo> getOverdueBillInfoByBillNo(String billNo) {
        ResponseData responseData = ResponseData.success();
        logger.info("查询还款的银码头账单 start billNo:"+billNo);
        try {
            OverdueBillYmtInfo overdueBillYmtInfo = overdueBillYmtDao.selectByBillNo(billNo);
            responseData.setData(overdueBillYmtInfo);
        }catch (Exception e){
            logger.error("getOverdueBillInfoByBillNo 异常 billNo："+billNo,e);
            responseData =  ResponseData.error("服务器开小差");
        }
        return responseData;
    }

    @Override
    public ResponseData<List<BillYmtInfo>> getUnRepayBillByUserId(String ymtUserId) {
        ResponseData responseData = ResponseData.success();
        logger.info("查询未还清账单 userId is "+ymtUserId);
        try {
            List<BillYmtInfo> list = billYmtDao.getUnRepayBillByUserId(ymtUserId);
            responseData.setData(list);
        } catch (Exception e) {
            logger.error("getUnRepayBillByUserId has error! userId is "+ymtUserId,e);
            responseData =  ResponseData.error("服务器开小差");
        }
        return responseData;
    }

    @Override
    public ResponseData<BillYmtInfo> getBillYmtByBillNo(String billNo) {
        ResponseData responseData = ResponseData.success();
        if (StringUtils.isBlank(billNo)) {
            responseData = ResponseData.error("billNo不能为空");
            return responseData;
        }
        try {
            List<BillYmtInfo> list = billYmtDao.getBillInfoByBillNo(billNo);
            if (list!=null&&list.size()>0) {
                responseData.setData(list.get(0));
            }
        } catch (Exception e) {
            logger.error("getbillymt by billno has exception! billNo is "+billNo,e);
            responseData = ResponseData.error("服务器开小差了");
        }
        return responseData;
    }

    @Override
    public ResponseData updateOverDuebillYmt(OverdueBillYmtInfo overdueBillYmtInfo) {
        ResponseData responseData = ResponseData.success();
        try {
            overdueBillYmtDao.updateByBillNo(overdueBillYmtInfo);
        } catch (Exception e) {
            logger.error("updateOverDuebillYmt has error",e);
        }
        return responseData;
    }
}
