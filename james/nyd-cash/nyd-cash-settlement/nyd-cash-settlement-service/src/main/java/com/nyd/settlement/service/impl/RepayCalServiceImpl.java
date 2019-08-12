package com.nyd.settlement.service.impl;

import com.alibaba.fastjson.JSON;
import com.creativearts.nyd.pay.model.RepayMessage;
import com.nyd.order.api.OrderDetailContract;
import com.nyd.order.model.OrderDetailInfo;
import com.nyd.product.api.ProductContract;
import com.nyd.product.model.ProductOverdueFeeItemInfo;
import com.nyd.settlement.dao.mapper.RepayMapper;
import com.nyd.settlement.dao.mapper.RepayTmpMapper;
import com.nyd.settlement.entity.OverdueBill;
import com.nyd.settlement.entity.repay.RepayAdjustLog;
import com.nyd.settlement.entity.repay.RepaySettleTmp;
import com.nyd.settlement.model.po.repay.RepayAmountOfDay;
import com.nyd.settlement.service.RepayCalService;
import com.nyd.settlement.service.struct.RepayStruct;
import com.nyd.settlement.service.utils.AmountUtils;
import com.nyd.settlement.service.utils.DateUtil;
import com.nyd.zeus.api.BillContract;
import com.nyd.zeus.model.BillInfo;
import com.nyd.zeus.model.OverdueBillInfo;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Cong Yuxiang
 * 2018/1/23
 **/
@Service
public class RepayCalServiceImpl implements RepayCalService {
    Logger logger = LoggerFactory.getLogger(RepayCalServiceImpl.class);

    @Autowired
    BillContract billContract;
    @Autowired
    private ProductContract productContract;
    @Autowired
    private OrderDetailContract orderDetailContract;

    @Autowired
    RepayMapper repayMapper;
    @Autowired
    RepayTmpMapper repayTmpMapper;

    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;

    @Transactional
    @Override
    public void doRepay(RepaySettleTmp tmp) throws Exception {
        //添加还款流水
//        RepayInfo repayInfo = new RepayInfo();
//        repayInfo.setRepayStatus("0");
//        repayInfo.setBillNo(tmp.getBillNo());
//        repayInfo.setRepayAmount(tmp.getRepayAmount());
//        repayInfo.setRepayChannel(tmp.getRepayChannel());
//        repayInfo.setRepayTime(tmp.getRepayTime());
//        repayInfo.setRepayNo(tmp.getRepayNo());
//        repayInfo.setRepayType("10");
//        try {
//            rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
//        }catch (Exception e){
//            e.printStackTrace();
//            logger.error("settlement发送rabit失败",e);
//        }
        //核销计算
        try {
            logger.info("审核的单子详情" + JSON.toJSONString(tmp));

            RepayAdjustLog log = new RepayAdjustLog();
            BillInfo billInfo = billContract.getBillInfo(tmp.getBillNo()).getData();
            logger.info(tmp.getBillNo()+"billinfo"+JSON.toJSONString(billInfo));
            OverdueBill overdueBill = repayMapper.queryOverdueBill(tmp.getBillNo());

            BigDecimal derateAmount = tmp.getDerateAmount();
//            BigDecimal dtmp = tmp.getDerateAmount();
//            if(dtmp==null){
//                dtmp = new BigDecimal(0);
//            }
            if(derateAmount==null){
                derateAmount = new BigDecimal(0);
            }
            if(billInfo.getCouponDerateAmount()!=null){
                derateAmount = derateAmount.add(billInfo.getCouponDerateAmount());
            }
            log.setBillNo(tmp.getBillNo());
            log.setAlreadyRepayAmountBe(billInfo.getAlreadyRepayAmount());
            log.setBillStatusBe(billInfo.getBillStatus());
            log.setReceivableAmountBe(billInfo.getReceivableAmount());
            log.setWaitRepayAmountBe(billInfo.getWaitRepayAmount());
            log.setWaitRepayPrincipleBe(billInfo.getWaitRepayPrinciple());
            log.setSettleTimeBe(billInfo.getActualSettleDate());

            if (overdueBill != null) {
                log.setOverdueDayBe(overdueBill.getOverdueDays());
                log.setOverdueAmountBe(overdueBill.getOverdueAmount());
                log.setOverdueFineBe(overdueBill.getOverdueFine());
            }


            String repayDateStr = DateFormatUtils.format(tmp.getRepayTime(), "yyyy-MM-dd");
            String promiseDateStr = DateFormatUtils.format(billInfo.getPromiseRepaymentDate(), "yyyy-MM-dd");

            //计算罚息
            List<RepayAmountOfDay> repayAmountOfDayList = repayMapper.queryRepayAmountOfDayDistinct(tmp.getBillNo());
            TreeMap<Integer, BigDecimal> map = AmountUtils.calAmountAndOverdueAll(repayDateStr, repayAmountOfDayList, promiseDateStr);
            logger.info("历史还款金额"+JSON.toJSONString(map));
            int middleDays = DateUtil.calcuteDateBetween(repayDateStr, promiseDateStr);
            if (map.get(middleDays) == null) {
                map.put(middleDays, tmp.getRepayAmount());
            } else {
                map.put(middleDays, map.get(middleDays).add(tmp.getRepayAmount()));
            }
            OrderDetailInfo orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(billInfo.getOrderNo()).getData();

            ProductOverdueFeeItemInfo productOverdueFeeItemInfo = productContract.getProductOverdueFeeItemInfo(orderDetailInfo.getProductCode()).getData();

            BigDecimal remainAmount = new BigDecimal("0"); //剩余金额
            BigDecimal receivableAmount = billInfo.getRepayPrinciple().add(billInfo.getRepayInterest());
            BigDecimal remainPrinciple = billInfo.getRepayPrinciple();
            BigDecimal remainFaxi = new BigDecimal("0");
            BigDecimal remainInterest = billInfo.getRepayInterest();
            BigDecimal remainFine = productOverdueFeeItemInfo.getOverdueFine();
            BigDecimal alreadyAmount = new BigDecimal(0);//已还金额

            int tmpDays = 1;  //根据新的本金 计算罚息的起始天  用于费率档次
            boolean flag = false;
            for (Map.Entry<Integer, BigDecimal> entry : map.entrySet()) {
                alreadyAmount = alreadyAmount.add(entry.getValue());
                //在约定期内的 已有还款
                if (entry.getKey() == 0) {
                    //本金 未冲平  计算剩余本金 及 建议金额。
                    if (entry.getValue().compareTo(billInfo.getRepayPrinciple()) < 0) {
                        remainPrinciple = remainPrinciple.subtract(entry.getValue());
                        remainAmount = remainPrinciple.add(remainInterest);
                    } else if (entry.getValue().compareTo(billInfo.getRepayPrinciple()) >= 0) {  //本金已冲平 剩余本金为0  计算建议金额
                        remainPrinciple = new BigDecimal("0");
                        remainAmount = remainInterest.subtract(entry.getValue().subtract(billInfo.getRepayPrinciple()));
                    }
                    if (remainAmount.compareTo(new BigDecimal(0)) <= 0) {
                        flag = true;
                    }

                } else {
                    //如果剩余本金 已冲平 则无须计算罚息

                    if (remainPrinciple.compareTo(new BigDecimal("0")) == 0) {
                        remainAmount = remainAmount.subtract(entry.getValue());
                    } else {
                        //有剩余本金 需要计算罚息
                        if (remainInterest.add(remainFine).add(remainFaxi).divide(billInfo.getRepayPrinciple(),2, RoundingMode.HALF_DOWN).compareTo(productOverdueFeeItemInfo.getMaxOverdueFeeRate().divide(new BigDecimal(100))) == -1) {
                            remainFaxi = remainFaxi.add(AmountUtils.calFaxi(remainPrinciple, productOverdueFeeItemInfo, tmpDays, entry.getKey()));
                        }

                        if (entry.getValue().compareTo(remainPrinciple) < 0) {
                            remainPrinciple = remainPrinciple.subtract(entry.getValue());
                            remainAmount = remainPrinciple.add(remainInterest).add(remainFine).add(remainFaxi);
                        } else if (entry.getValue().compareTo(remainPrinciple) >= 0) {
                            BigDecimal tm = entry.getValue().subtract(remainPrinciple);
                            remainPrinciple = new BigDecimal("0");
                            remainAmount = remainInterest.add(remainFine).add(remainFaxi).subtract(tm);
                        }

                    }


                    tmpDays = entry.getKey() + 1;
                }

            }


            if (alreadyAmount.subtract(tmp.getRepayAmount()).compareTo(billInfo.getAlreadyRepayAmount()) != 0) {
                logger.error(JSON.toJSONString(tmp) + "还款流水跟bill已还金额不符");
                throw new Exception("还款流水跟bill已还金额不符");
            }

            int curDays = DateUtil.calcuteDateBetween(DateFormatUtils.format(new Date(), "yyyy-MM-dd"), promiseDateStr);
            //更新审核状态
            List<Long> ids = new ArrayList<>();
            ids.add(tmp.getId());
            repayTmpMapper.updateTmp(ids);

            repayMapper.insert(RepayStruct.INSTANCE.tmp2Po(tmp));
            if (flag) {
                receivableAmount = billInfo.getRepayPrinciple().add(billInfo.getRepayInterest());
                billInfo.setAlreadyRepayAmount(billInfo.getAlreadyRepayAmount().add(tmp.getRepayAmount()));

                billInfo.setBillStatus("B003");

                billInfo.setWaitRepayAmount(new BigDecimal("0"));

                billInfo.setWaitRepayPrinciple(new BigDecimal(0));
                billInfo.setCouponDerateAmount(derateAmount);

                billInfo.setReceivableAmount(receivableAmount);
                billInfo.setActualSettleDate(AmountUtils.calSettleDate(tmp.getRepayTime(), repayAmountOfDayList, tmp.getRepayAmount(), receivableAmount));
                log.setAlreadyRepayAmountAf(billInfo.getAlreadyRepayAmount().add(tmp.getRepayAmount()));
                log.setSettleTimeAf(AmountUtils.calSettleDate(tmp.getRepayTime(), repayAmountOfDayList, tmp.getRepayAmount(), receivableAmount));
                log.setBillStatusAf("B003");
                log.setWaitRepayPrincipleAf(new BigDecimal(0));
                log.setWaitRepayAmountAf(new BigDecimal(0));
                log.setReceivableAmountAf(receivableAmount);
                billContract.updateBillInfoByBillNo(billInfo);
                if (overdueBill != null) {
                    OverdueBillInfo overdueBillInfo = new OverdueBillInfo();
                    overdueBillInfo.setBillNo(tmp.getBillNo());
                    overdueBillInfo.setDeleteFlag(1);
                    overdueBillInfo.setBillStatus("B003");
                    billContract.updateOverdueBillInfoByBillNo(overdueBillInfo);
                }
                repayMapper.insertAdjustLog(log);
                sendMsgToYmtOrder(billInfo);
                return;
            }

            if (existGTzero(map)) {

                if (remainAmount.subtract(derateAmount).compareTo(new BigDecimal(0)) <= 0) {
                    receivableAmount = billInfo.getRepayPrinciple().add(billInfo.getRepayInterest()).add(productOverdueFeeItemInfo.getOverdueFine()).add(remainFaxi);

                    billInfo.setAlreadyRepayAmount(alreadyAmount);

                    billInfo.setBillStatus("B003");

                    billInfo.setWaitRepayAmount(new BigDecimal("0"));

                    billInfo.setWaitRepayPrinciple(new BigDecimal(0));
                    billInfo.setCouponDerateAmount(derateAmount);

                    billInfo.setReceivableAmount(receivableAmount);
                    billInfo.setActualSettleDate(AmountUtils.calSettleDate(tmp.getRepayTime(), repayAmountOfDayList, tmp.getRepayAmount(), receivableAmount.subtract(derateAmount)));
                    log.setSettleTimeAf(AmountUtils.calSettleDate(tmp.getRepayTime(), repayAmountOfDayList, tmp.getRepayAmount(), receivableAmount.subtract(derateAmount)));
                    log.setAlreadyRepayAmountAf(alreadyAmount);
                    log.setBillStatusAf("B003");
                    log.setWaitRepayPrincipleAf(new BigDecimal(0));
                    log.setWaitRepayAmountAf(new BigDecimal(0));
                    log.setReceivableAmountAf(receivableAmount);
                    billContract.updateBillInfoByBillNo(billInfo);

                    OverdueBillInfo overdueBillInfo = new OverdueBillInfo();
                    overdueBillInfo.setBillNo(tmp.getBillNo());
                    overdueBillInfo.setBillStatus("B003");
                    overdueBillInfo.setOverdueFine(remainFine);
                    overdueBillInfo.setOverdueDays(map.lastKey());
                    overdueBillInfo.setOverdueAmount(remainFaxi);
//                    overdueBillInfo.setCollectionDerateAmount(derateAmount);
                    log.setOverdueDayAf(map.lastKey());
                    log.setOverdueAmountAf(remainFaxi);
                    if (overdueBill != null) {
                        billContract.updateOverdueBillInfoByBillNo(overdueBillInfo);
                    } else {
                        billContract.saveOverdueBillInfo(overdueBillInfo);
                    }
                    repayMapper.insertAdjustLog(log);
                    sendMsgToYmtOrder(billInfo);
                    return;
                } else {

                    int tmpfla = 0;
                    if (curDays <= map.lastKey()) {
                        tmpfla = map.lastKey();
                    } else {
                        remainFaxi = remainFaxi.add(AmountUtils.calFaxi(remainPrinciple, productOverdueFeeItemInfo, tmpDays, curDays));

                        tmpfla = curDays;
                    }
                    receivableAmount = billInfo.getRepayPrinciple().add(billInfo.getRepayInterest()).add(productOverdueFeeItemInfo.getOverdueFine()).add(remainFaxi);
                    billInfo.setAlreadyRepayAmount(alreadyAmount);

                    billInfo.setBillStatus("B002");

                    billInfo.setWaitRepayAmount(receivableAmount.subtract(alreadyAmount).subtract(derateAmount));

                    billInfo.setWaitRepayPrinciple(remainPrinciple);
                    billInfo.setCouponDerateAmount(derateAmount);
                    billInfo.setReceivableAmount(receivableAmount);
                    log.setAlreadyRepayAmountAf(alreadyAmount);
                    log.setBillStatusAf("B002");
                    log.setWaitRepayPrincipleAf(remainPrinciple);
                    log.setWaitRepayAmountAf(receivableAmount.subtract(alreadyAmount).subtract(derateAmount));
                    log.setReceivableAmountAf(receivableAmount);
                    billContract.updateBillInfoByBillNo(billInfo);

                    OverdueBillInfo overdueBillInfo = new OverdueBillInfo();
                    overdueBillInfo.setBillNo(tmp.getBillNo());
                    overdueBillInfo.setBillStatus("B002");
                    overdueBillInfo.setOverdueFine(remainFine);
                    overdueBillInfo.setOverdueDays(tmpfla);
                    overdueBillInfo.setOverdueAmount(remainFaxi);
//                    overdueBillInfo.setCollectionDerateAmount(derateAmount);
                    log.setOverdueDayAf(tmpfla);
                    log.setOverdueAmountAf(remainFaxi);
                    if (overdueBill != null) {
                        billContract.updateOverdueBillInfoByBillNo(overdueBillInfo);
                    } else {
                        billContract.saveOverdueBillInfo(overdueBillInfo);
                    }
                    repayMapper.insertAdjustLog(log);
                    return;
                }
            } else {

                if (curDays == 0) {
                    receivableAmount = billInfo.getRepayPrinciple().add(billInfo.getRepayInterest());
                    billInfo.setAlreadyRepayAmount(alreadyAmount);

                    billInfo.setBillStatus("B001");

                    billInfo.setWaitRepayAmount(receivableAmount.subtract(alreadyAmount));
                    billInfo.setCouponDerateAmount(derateAmount);

                    billInfo.setWaitRepayPrinciple(remainPrinciple);
                    log.setWaitRepayPrincipleAf(remainPrinciple);

                    billInfo.setReceivableAmount(receivableAmount);
                    log.setAlreadyRepayAmountAf(alreadyAmount);
                    log.setBillStatusAf("B001");

                    log.setWaitRepayAmountAf(receivableAmount.subtract(alreadyAmount));
                    log.setReceivableAmountAf(receivableAmount);
                    billContract.updateBillInfoByBillNo(billInfo);
                    repayMapper.insertAdjustLog(log);
                    return;
                } else {
                    OverdueBillInfo overdueBillInfo = new OverdueBillInfo();
                    remainFaxi = remainFaxi.add(AmountUtils.calFaxi(remainPrinciple, productOverdueFeeItemInfo, tmpDays, curDays));
                    receivableAmount = billInfo.getRepayPrinciple().add(billInfo.getRepayInterest()).add(productOverdueFeeItemInfo.getOverdueFine()).add(remainFaxi);
                    billInfo.setAlreadyRepayAmount(alreadyAmount);
                    if(receivableAmount.subtract(alreadyAmount).subtract(derateAmount).compareTo(new BigDecimal(0))<=0){
                        billInfo.setBillStatus("B003");
                        log.setBillStatusAf("B003");
                        overdueBillInfo.setBillStatus("B003");
                        billInfo.setActualSettleDate(AmountUtils.calSettleDate(tmp.getRepayTime(), repayAmountOfDayList, tmp.getRepayAmount(), receivableAmount.subtract(derateAmount)));
                        log.setSettleTimeAf(AmountUtils.calSettleDate(tmp.getRepayTime(), repayAmountOfDayList, tmp.getRepayAmount(), receivableAmount.subtract(derateAmount)));
                    }else {
                        billInfo.setBillStatus("B002");
                        log.setBillStatusAf("B002");
                        overdueBillInfo.setBillStatus("B002");
                    }


                    billInfo.setWaitRepayAmount(receivableAmount.subtract(alreadyAmount).subtract(derateAmount));

                    billInfo.setWaitRepayPrinciple(remainPrinciple);
                    billInfo.setCouponDerateAmount(derateAmount);
                    billInfo.setReceivableAmount(receivableAmount);
                    log.setAlreadyRepayAmountAf(alreadyAmount);

                    log.setWaitRepayPrincipleAf(remainPrinciple);
                    log.setWaitRepayAmountAf(receivableAmount.subtract(alreadyAmount).subtract(derateAmount));
                    log.setReceivableAmountAf(receivableAmount);
                    billContract.updateBillInfoByBillNo(billInfo);


                    overdueBillInfo.setBillNo(tmp.getBillNo());
//                    overdueBillInfo.setCollectionDerateAmount(derateAmount);
                    overdueBillInfo.setOverdueFine(remainFine);
                    overdueBillInfo.setOverdueDays(curDays);
                    overdueBillInfo.setOverdueAmount(remainFaxi);
                    log.setOverdueDayAf(curDays);
                    log.setOverdueAmountAf(remainFaxi);
                    if (overdueBill != null) {
                        billContract.updateOverdueBillInfoByBillNo(overdueBillInfo);
                    } else {
                        billContract.saveOverdueBillInfo(overdueBillInfo);
                    }
                    repayMapper.insertAdjustLog(log);
                    sendMsgToYmtOrder(billInfo);
                    return;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }

//            if(map.lastKey()!=curDays){
//                if()
//            }

//        billInfo.setAlreadyRepayAmount(billInfo.getAlreadyRepayAmount().add(tmp.getRepayAmount()));
//        log.setAlreadyRepayAmountAf(billInfo.getAlreadyRepayAmount().add(tmp.getRepayAmount()));
//
//        billInfo.setReceivableAmount(receivableAmount);
//        log.setReceivableAmountAf(receivableAmount);
//        //有逾期
//        if (existGTzero(map)) {
//            if (overdueBill == null) {
//                if (billInfo.getAlreadyRepayAmount().compareTo(billInfo.getReceivableAmount()) >= 0) {
//                    billInfo.setWaitRepayAmount(new BigDecimal("0"));
//                    log.setWaitRepayAmountAf(new BigDecimal(0));
//                    billInfo.setBillStatus("B003");
//                    log.setBillStatusAf("B003");
//                    billInfo.setWaitRepayPrinciple(new BigDecimal(0));
//                    log.setWaitRepayPrincipleAf(new BigDecimal(0));
//                    billContract.updateBillInfoByBillNo(billInfo);
//
//                } else {
//                    billInfo.setWaitRepayPrinciple(remainPrinciple);
//                    log.setWaitRepayPrincipleAf(remainPrinciple);
//                    billInfo.setWaitRepayAmount(billInfo.getReceivableAmount().subtract(billInfo.getAlreadyRepayAmount()));
//                    log.setWaitRepayAmountAf(billInfo.getReceivableAmount().subtract(billInfo.getAlreadyRepayAmount()));
//                    billInfo.setBillStatus("B001");
//                    log.setBillStatusAf("B001");
//                    billContract.updateBillInfoByBillNo(billInfo);
//                }
//            } else {
//                if (billInfo.getAlreadyRepayAmount().compareTo(billInfo.getReceivableAmount()) >= 0) {
//                    billInfo.setWaitRepayAmount(new BigDecimal("0"));
//                    log.setWaitRepayAmountAf(new BigDecimal(0));
//                    billInfo.setBillStatus("B003");
//                    log.setBillStatusAf("B003");
//                    billInfo.setWaitRepayPrinciple(new BigDecimal(0));
//                    log.setWaitRepayPrincipleAf(new BigDecimal(0));
//                    billContract.updateBillInfoByBillNo(billInfo);
//                    OverdueBillInfo overdueBillInfo = new OverdueBillInfo();
//                    overdueBillInfo.setBillNo(tmp.getBillNo());
//                    overdueBillInfo.setOverdueAmount(remainFaxi);
//                    log.setOverdueAmountAf(remainFaxi);
//                    overdueBillInfo.setOverdueFine(productOverdueFeeItemInfo.getOverdueFine());
//                    log.setOverdueFineAf(productOverdueFeeItemInfo.getOverdueFine());
//                    overdueBillInfo.setBillStatus("B003");
//                    billContract.updateOverdueBillInfoByBillNo(overdueBillInfo);
//                } else {
//                    billInfo.setWaitRepayAmount(billInfo.getReceivableAmount().subtract(billInfo.getAlreadyRepayAmount()));
//                    billInfo.setBillStatus("B002");
//                    log.setBillStatusAf("B002");
//                    billInfo.setWaitRepayPrinciple(remainPrinciple);
//                    log.setWaitRepayPrincipleAf(remainPrinciple);
//                    billContract.updateBillInfoByBillNo(billInfo);
//                    OverdueBillInfo overdueBillInfo = new OverdueBillInfo();
//                    overdueBillInfo.setBillNo(tmp.getBillNo());
//                    overdueBillInfo.setOverdueAmount(remainFaxi);
//                    log.setOverdueAmountAf(remainFaxi);
//                    overdueBillInfo.setOverdueFine(productOverdueFeeItemInfo.getOverdueFine());
//                    log.setOverdueFineAf(productOverdueFeeItemInfo.getOverdueFine());
//                    overdueBillInfo.setBillStatus("B002");
//                    billContract.updateOverdueBillInfoByBillNo(overdueBillInfo);
//                }
//            }
//
//        } else {
//
//        }
//        if (overdueBill == null) {
//            if (billInfo.getAlreadyRepayAmount().compareTo(billInfo.getReceivableAmount()) >= 0) {
//                billInfo.setWaitRepayAmount(new BigDecimal("0"));
//                log.setWaitRepayAmountAf(new BigDecimal(0));
//                billInfo.setBillStatus("B003");
//                log.setBillStatusAf("B003");
//                billInfo.setWaitRepayPrinciple(new BigDecimal(0));
//                log.setWaitRepayPrincipleAf(new BigDecimal(0));
//                billContract.updateBillInfoByBillNo(billInfo);
//
//            } else {
//                billInfo.setWaitRepayPrinciple(remainPrinciple);
//                log.setWaitRepayPrincipleAf(remainPrinciple);
//                billInfo.setWaitRepayAmount(billInfo.getReceivableAmount().subtract(billInfo.getAlreadyRepayAmount()));
//                log.setWaitRepayAmountAf(billInfo.getReceivableAmount().subtract(billInfo.getAlreadyRepayAmount()));
//                billInfo.setBillStatus("B001");
//                log.setBillStatusAf("B001");
//                billContract.updateBillInfoByBillNo(billInfo);
//            }
//        } else {
//            if (billInfo.getAlreadyRepayAmount().compareTo(billInfo.getReceivableAmount()) >= 0) {
//                billInfo.setWaitRepayAmount(new BigDecimal("0"));
//                log.setWaitRepayAmountAf(new BigDecimal(0));
//                billInfo.setBillStatus("B003");
//                log.setBillStatusAf("B003");
//                billInfo.setWaitRepayPrinciple(new BigDecimal(0));
//                log.setWaitRepayPrincipleAf(new BigDecimal(0));
//                billContract.updateBillInfoByBillNo(billInfo);
//                OverdueBillInfo overdueBillInfo = new OverdueBillInfo();
//                overdueBillInfo.setBillNo(tmp.getBillNo());
//                overdueBillInfo.setOverdueAmount(remainFaxi);
//                log.setOverdueAmountAf(remainFaxi);
//                overdueBillInfo.setOverdueFine(productOverdueFeeItemInfo.getOverdueFine());
//                log.setOverdueFineAf(productOverdueFeeItemInfo.getOverdueFine());
//                overdueBillInfo.setBillStatus("B003");
//                billContract.updateOverdueBillInfoByBillNo(overdueBillInfo);
//            } else {
//                billInfo.setWaitRepayAmount(billInfo.getReceivableAmount().subtract(billInfo.getAlreadyRepayAmount()));
//                billInfo.setBillStatus("B002");
//                log.setBillStatusAf("B002");
//                billInfo.setWaitRepayPrinciple(remainPrinciple);
//                log.setWaitRepayPrincipleAf(remainPrinciple);
//                billContract.updateBillInfoByBillNo(billInfo);
//                OverdueBillInfo overdueBillInfo = new OverdueBillInfo();
//                overdueBillInfo.setBillNo(tmp.getBillNo());
//                overdueBillInfo.setOverdueAmount(remainFaxi);
//                log.setOverdueAmountAf(remainFaxi);
//                overdueBillInfo.setOverdueFine(productOverdueFeeItemInfo.getOverdueFine());
//                log.setOverdueFineAf(productOverdueFeeItemInfo.getOverdueFine());
//                overdueBillInfo.setBillStatus("B002");
//                billContract.updateOverdueBillInfoByBillNo(overdueBillInfo);
//            }
//        }


//            billInfo.setAlreadyRepayAmount(billInfo.getAlreadyRepayAmount().add(tmp.getRepayAmount()));

//            billInfo.setAlreadyRepayAmount(billInfo.getAlreadyRepayAmount().add(tmp.getRepayAmount()));
//
//            boolean existGtZero = existGTzero(map);
//            if(remainAmount.compareTo(new BigDecimal(0))==0){
//                billInfo.setBillStatus("B003");
//
//            }


//            if ("B003".equals(billInfo.getBillStatus())) {
//                logger.info("this bill is already repay, billNo is " + billInfo.getBillNo());
//                billInfo.setAlreadyRepayAmount(billInfo.getAlreadyRepayAmount().add(tmp.getRepayAmount()));
//                if(overdueBill!=null){
//
//                    int days = DateUtil.calcuteDateBetween(repayDateStr,promiseDateStr);
//                    if(days==0){
//                        OverdueBillInfo overdueBillInfo = new OverdueBillInfo();
//                        overdueBillInfo.setBillNo(tmp.getBillNo());
//                        overdueBillInfo.setDeleteFlag(1);
//                        billContract.updateOverdueBillInfoByBillNo(overdueBillInfo);
//                        billInfo.setReceivableAmount(billInfo.getReceivableAmount().subtract(overdueBill.getOverdueFine()).subtract(overdueBill.getOverdueAmount()));
//
//                    }else{
//
//                        OverdueBillInfo overdueBillInfo = new OverdueBillInfo();
//                        overdueBillInfo.setBillNo(tmp.getBillNo());
//                        overdueBillInfo.setOverdueAmount(remainFaxi);
//                        billContract.updateOverdueBillInfoByBillNo(overdueBillInfo);
//                        BigDecimal reciableAmount = billInfo.getRepayPrinciple().add(billInfo.getRepayInterest()).add(overdueBill.getOverdueFine()).add(remainFaxi);
//                        billInfo.setReceivableAmount(reciableAmount);
//
//                        if(billInfo.getAlreadyRepayAmount().compareTo(reciableAmount)<0){
//                            billInfo.setBillStatus("B002");
//                        }
//
//
//                    }
//                }
//                billContract.updateBillInfoByBillNo(billInfo);
//            } else {
//                BigDecimal alreadyRepayAmount = billInfo.getAlreadyRepayAmount().add(tmp.getRepayAmount());
//                BigDecimal waitRepayAmount = billInfo.getWaitRepayAmount();
//                String billStatus = billInfo.getBillStatus();
//                //本次还款金额>=剩余待还款金额
//                if (tmp.getRepayAmount().compareTo(billInfo.getWaitRepayAmount()) > -1) {
//                    billInfo.setWaitRepayAmount(new BigDecimal(0));
//                    billInfo.setAlreadyRepayAmount(alreadyRepayAmount);
//                    billInfo.setWaitRepayPrinciple(new BigDecimal(0));
//
//                    //实际结清时间
//                    billInfo.setActualSettleDate(tmp.getRepayTime());
//                    billInfo.setBillStatus("B003");
//                    //更新账单信息
//                    billContract.updateBillInfoByBillNo(billInfo);
//                    if ("B002".equals(billStatus)) {
//                        //逾期中还款，更新逾期账单（OverdueBill）状态
//                        OverdueBillInfo overdueBillInfo = new OverdueBillInfo();
//                        overdueBillInfo.setBillNo(tmp.getBillNo());
//                        overdueBillInfo.setBillStatus("B003");
//                        //更新逾期账单（OverdueBill）状态
//                        billContract.updateOverdueBillInfoByBillNo(overdueBillInfo);
//                    }
//                } else {//本次还款金额 < 剩余待还款金额
//                    BigDecimal waitRepayPrinciple = billInfo.getWaitRepayPrinciple();
//                    //剩余待还款本金大于0
//                    if (waitRepayPrinciple.compareTo(new BigDecimal(0)) == 1) {
//                        //剩余待还款本金小于等于本次还款金额
//                        if (waitRepayPrinciple.compareTo(tmp.getRepayAmount()) <= 0) {
//                            billInfo.setWaitRepayPrinciple(new BigDecimal(0));
//                        } else {
//                            billInfo.setWaitRepayPrinciple(waitRepayPrinciple.subtract(tmp.getRepayAmount()));
//                        }
//                        //TODO 逾期重新计算逾期费用
//                        if ("B002".equals(billStatus)) {
//
//                       }
//                    }
//                    billInfo.setAlreadyRepayAmount(alreadyRepayAmount);
//                    waitRepayAmount = waitRepayAmount.subtract(tmp.getRepayAmount());
//                    billInfo.setWaitRepayAmount(waitRepayAmount);
//                    //更新账单信息
//                    billContract.updateBillInfoByBillNo(billInfo);
//                }
//            }



//        }catch (Exception e){
//            logger.error("核销总异常",e);
//            e.printStackTrace();
//
//        }finally {
//
//        }

    }

    private boolean existGTzero(TreeMap<Integer, BigDecimal> map) {
        for (Map.Entry<Integer, BigDecimal> entry : map.entrySet()) {
            if (entry.getKey() > 0) {
                return true;
            }
        }
        return false;
    }

    private void sendMsgToYmtOrder(BillInfo billInfo) {
        try {
            if (billInfo!=null&& StringUtils.isNotBlank(billInfo.getIbankOrderNo())&&"B003".equals(billInfo.getBillStatus())) {
                RepayMessage repayMessage = new RepayMessage();
                repayMessage.setBillNo(billInfo.getIbankOrderNo());
                repayMessage.setRepayTime(billInfo.getActualSettleDate());
                logger.info("还款成功发送银码头"+JSON.toJSONString(repayMessage));
                rabbitmqProducerProxy.convertAndSend("payIbank.ibank",repayMessage);
            }
        } catch (AmqpException e) {
            logger.error("send msg to ymtorder has exception! billInfo is "+JSON.toJSONString(billInfo),e);
        }
    }
}
