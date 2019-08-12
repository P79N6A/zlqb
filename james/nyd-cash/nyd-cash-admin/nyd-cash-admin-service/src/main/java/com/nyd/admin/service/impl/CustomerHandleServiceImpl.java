package com.nyd.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.nyd.admin.dao.mapper.CustomerHandleQueryMapper;
import com.nyd.admin.model.Info.RechargePaymentRecordInfo;
import com.nyd.admin.model.Info.RepayInfo;
import com.nyd.admin.model.WithholdDetails;
import com.nyd.admin.model.dto.IdNumberDto;
import com.nyd.admin.model.dto.RechargePaymentRecordDto;
import com.nyd.admin.service.CustomerHandleService;
import com.nyd.admin.service.utils.AdminProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: wucx
 * @Date: 2018/10/18 16:50
 */
@Service
public class CustomerHandleServiceImpl implements CustomerHandleService {
    private static Logger logger = LoggerFactory.getLogger(CustomerHandleServiceImpl.class);

    @Autowired
    private CustomerHandleQueryMapper customerHandleQueryMapper;

    @Autowired
    private AdminProperties adminProperties;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<RechargePaymentRecordInfo> findRechargePaymentDetails(RechargePaymentRecordDto rechargePaymentRecordDto) {
        List<RechargePaymentRecordInfo> totalList = new ArrayList<>();
        logger.info("查询充值付费总记录 start params is:" + JSON.toJSONString(rechargePaymentRecordDto));
        try {
            //1.查询支付评估费记录
            List<RechargePaymentRecordInfo> payAssessmentFeeList = findPayAssessmentFee(rechargePaymentRecordDto);
            if (payAssessmentFeeList != null && payAssessmentFeeList.size() > 0) {
                for (RechargePaymentRecordInfo rechargePaymentRecordInfo:payAssessmentFeeList) {
                    totalList.add(rechargePaymentRecordInfo);
                }
            }
            //2.查询充值和现金付费记录
            List<RechargePaymentRecordInfo> rechargeAndCashPay = findRechargeAndCashPay(rechargePaymentRecordDto);
            if (rechargeAndCashPay != null && rechargeAndCashPay.size() > 0) {
                for (RechargePaymentRecordInfo rechargePaymentRecordInfo:rechargeAndCashPay) {
                    totalList.add(rechargePaymentRecordInfo);
                }
            }
            //3.查询系统代扣记录
            List<RechargePaymentRecordInfo> withHoldDetailList = findWithHoldDetail(rechargePaymentRecordDto);
            if (withHoldDetailList != null && withHoldDetailList.size() > 0) {
                for (RechargePaymentRecordInfo rechargePaymentRecordInfo:withHoldDetailList) {
                    totalList.add(rechargePaymentRecordInfo);
                }
            }
            //4.赠送记录
            List<RechargePaymentRecordInfo> returnTicketLog = findReturnTicketLog(rechargePaymentRecordDto);
            if (returnTicketLog != null && returnTicketLog.size() > 0) {
                for (RechargePaymentRecordInfo rechargePaymentRecordInfo:returnTicketLog) {
                    totalList.add(rechargePaymentRecordInfo);
                }
            }
            logger.info("查询到用户userId为：" + rechargePaymentRecordDto.getUserId() + "的充值付费记录是：" + totalList);
        } catch (Exception e) {
            logger.info("查询充值付费总记录出错！：", e);
        }
        return totalList;
    }

    @Override
    public Integer findPayAssessCount(RechargePaymentRecordDto rechargePaymentRecordDto) {
        logger.info("查询支付评估费记录个数，入参userId为：" + JSON.toJSONString(rechargePaymentRecordDto));
        Integer total = 0;
        try {
            //1.查询支付评估费个数
            Integer payAssessCount = customerHandleQueryMapper.findPayAssessCount(rechargePaymentRecordDto);
            logger.info("查询支付评估费记录个数结果为payAssessCount=：" + payAssessCount);
            if (payAssessCount != null && payAssessCount != 0) {
                total = total + payAssessCount;
            }
            Integer withHoldOrderCount = customerHandleQueryMapper.findWithHoldOrderCount(rechargePaymentRecordDto);
            logger.info("查询代扣记录个数结果为withHoldOrderCount=：" + withHoldOrderCount);
            if (withHoldOrderCount != null && withHoldOrderCount != 0) {
                total = total + withHoldOrderCount;
            }
            Integer rechargeAndCashPayCount = customerHandleQueryMapper.findRechargeAndCashPayCount(rechargePaymentRecordDto);
            logger.info("查询充值和现金记录总个数结果为rechargeAndCashPayCount=：" + rechargeAndCashPayCount);
            if (rechargeAndCashPayCount != null && rechargeAndCashPayCount != 0) {
                total = total + rechargeAndCashPayCount;
            }
            Integer returnTicketLogCount = customerHandleQueryMapper.findReturnTicketLogCount(rechargePaymentRecordDto);
            logger.info("查询赠送记录个数结果为returnTicketLogCount=：" + returnTicketLogCount);
            if (returnTicketLogCount != null && returnTicketLogCount != 0) {
                total = total + returnTicketLogCount;
            }
        } catch (Exception e) {
            logger.info("查询充值付费记录总个数出错！：", e);
        }
        return total;
    }

    /**
     * 查询支付评估费记录
     * @param rechargePaymentRecordDto
     * @return
     */
    private List<RechargePaymentRecordInfo> findPayAssessmentFee(RechargePaymentRecordDto rechargePaymentRecordDto) {
        logger.info("查询支付评估费记录，入参userId为：" + rechargePaymentRecordDto.getUserId());
        List<RechargePaymentRecordInfo> list = new ArrayList<>();
        try {
            List<RechargePaymentRecordInfo> result = customerHandleQueryMapper.findRechargePaymentRecords(rechargePaymentRecordDto);
            logger.info("查询支付评估费记录，result*** " + result);
            if(result != null && result.size() > 0){
                for (RechargePaymentRecordInfo recharge:result) {
                    recharge.setRepayType("支付评估费");
                    //得出交易金额
                    BigDecimal transactionMoney = new BigDecimal(0.00);
                    if(recharge.getPayCash() != null){ //已付现金
                        transactionMoney = transactionMoney.add(recharge.getPayCash());
                    } else {
                        recharge.setPayCash(new BigDecimal(0.00));
                    }
                    if(recharge.getBalanceUse() != null){ //账户余额（小银券）
                        transactionMoney = transactionMoney.add(recharge.getBalanceUse());
                    } else {
                        recharge.setBalanceUse(new BigDecimal(0.00));
                    }
                    if(recharge.getReturnBalanceFee() != null){ //使用现金券
                        transactionMoney = transactionMoney.add(recharge.getReturnBalanceFee());
                    } else {
                        recharge.setReturnBalanceFee(new BigDecimal(0.00));
                    }
                    //查询优惠卷金额
                    logger.info("查询优惠券 入参memberId:" + recharge.getMemberId());
                    String couponId = customerHandleQueryMapper.findCouponIdByMemberId(recharge.getMemberId());
                    logger.info("查询优惠券ID couponId:" + couponId);
                    BigDecimal couponFee = new BigDecimal(0);
                    if (couponId != null) {
                        couponFee = customerHandleQueryMapper.findCouponFeeByCouponId(couponId);
                        logger.info("查询到用户userId为：" + rechargePaymentRecordDto.getUserId() + "，使用了：" + couponFee + "元的优惠券！");
                    }
                    if (couponFee.compareTo(new BigDecimal(0)) == 1) {
                        transactionMoney = transactionMoney.add(couponFee);
                    }
                    recharge.setTransactionMoney(transactionMoney);
                    recharge.setCouponFee(couponFee);//优惠券
                    List<RepayInfo> repayInfoResult = null;
                    if (recharge.getPayCash().compareTo(new BigDecimal(0)) == 1){ //如果有消费现金
                        logger.info("查询支付渠道和交易订单号入参为 userId:" + recharge.getUserId() + ",创建时间为:" + recharge.getCreateTime());
                        repayInfoResult = customerHandleQueryMapper.findRepayChannelByUserIdAndCreateTime(recharge.getUserId(), recharge.getCreateTime());
                        logger.info("查询支付渠道和交易订单号 结果为:" + repayInfoResult);
                    }
                    if(repayInfoResult != null && repayInfoResult.size() > 0){
                        RepayInfo repayInfo = repayInfoResult.get(0);
                        if (repayInfo.getRepayChannel() != null) {
                            recharge.setRepayChannel(repayInfo.getRepayChannel());
                        }
                        if (repayInfo.getRepayNo() != null) {
                            recharge.setRepayNo(repayInfo.getRepayNo());
                        }
                    } else {
                        recharge.setRepayChannel("");
                        recharge.setRepayNo("");
                    }
                    list.add(recharge);
                }
            }
            logger.info("查询支付评估费记录,result is " + list);
        }catch (Exception e){
            logger.error("查询支付评估费出错~~~~", e);
        }
        return list;
    }

    /**
     * 查询充值记录和现金支付
     * @param rechargePaymentRecordDto
     * @return
     */
    private List<RechargePaymentRecordInfo> findRechargeAndCashPay(RechargePaymentRecordDto rechargePaymentRecordDto){
        List<RechargePaymentRecordInfo> resultList = new ArrayList<>();
        logger.info("查询用户代扣记录 start params is：" + JSON.toJSONString(rechargePaymentRecordDto));
        try {
            List<RechargePaymentRecordInfo> list = customerHandleQueryMapper.findRechargeAndCashPayDetails(rechargePaymentRecordDto);
            if (list != null && list.size() > 0) {
                for (RechargePaymentRecordInfo rechargePaymentRecordInfo:list) {
                    if ("3".equals(rechargePaymentRecordInfo.getRepayType())) {
                        rechargePaymentRecordInfo.setRepayType("现金支付");
                    } else if ("16".equals(rechargePaymentRecordInfo.getRepayType())) {
                        rechargePaymentRecordInfo.setRepayType("充值");
                    }
                    rechargePaymentRecordInfo.setPayCash(rechargePaymentRecordInfo.getTransactionMoney());//已付现金
                    rechargePaymentRecordInfo.setBalanceUse(new BigDecimal(0));
                    rechargePaymentRecordInfo.setReturnBalanceFee(new BigDecimal(0));
                    rechargePaymentRecordInfo.setCouponFee(new BigDecimal(0));
                    resultList.add(rechargePaymentRecordInfo);
                }
            }
            logger.info("查询充值记录和现金支付 result is " + resultList);
        } catch (Exception e) {
            logger.error("查询充值记录和现金支付出错~~~~", e);
        }
        return resultList;
    }

    /**
     * 查询代扣记录
     * @param rechargePaymentRecordDto
     * @return
     */
    private List<RechargePaymentRecordInfo> findWithHoldDetail(RechargePaymentRecordDto rechargePaymentRecordDto){
        List<RechargePaymentRecordInfo> resultList = new ArrayList<>();
        logger.info("查询用户代扣记录 start params is：" + JSON.toJSONString(rechargePaymentRecordDto));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String idNumber = customerHandleQueryMapper.findIdNumberByUserId(rechargePaymentRecordDto);
            IdNumberDto idNumberDto = new IdNumberDto();
            idNumberDto.setIdNumber(idNumber);
            idNumberDto.setState(3); //表示代扣成功
            logger.info("find userIs is:" + rechargePaymentRecordDto.getUserId() + ",idNumber is:" + idNumber);
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            HttpEntity entity = new HttpEntity(JSON.toJSONString(idNumberDto), headers);
            logger.info("URL is " + adminProperties.getSendUrl());
            ResponseEntity<WithholdDetails> result = restTemplate.exchange(adminProperties.getSendUrl(), HttpMethod.POST, entity, WithholdDetails.class);
            logger.info("调用返回的结果是：" + result);
            if (result.getStatusCodeValue() == 200) {
                List<WithholdDetails.ResponseVo> resultBodyList = result.getBody().getData();
                for (WithholdDetails.ResponseVo responseVo:resultBodyList) {
                    RechargePaymentRecordInfo recordInfo = new RechargePaymentRecordInfo();
                    Date time = responseVo.getUpdateTime();
                    String createTime = sdf.format(time);
                    recordInfo.setCreateTime(createTime); //付费时间
                    recordInfo.setRepayType("系统代扣");
                    recordInfo.setTransactionMoney(responseVo.getAmount()); //付款金额
                    recordInfo.setRepayChannel(responseVo.getChannel()); //交易渠道
                    recordInfo.setRepayNo(responseVo.getOrderNo()); //交易订单号
                    recordInfo.setPayCash(new BigDecimal(0));
                    recordInfo.setReturnBalanceFee(new BigDecimal(0));
                    recordInfo.setBalanceUse(new BigDecimal(0));
                    recordInfo.setCouponFee(new BigDecimal(0));
                    resultList.add(recordInfo);
                }
            }
            logger.info("find idNumber is " + idNumber + ",withhold is " + resultList);
        } catch (Exception e) {
            logger.error("查询系统代扣出错~~~~", e);
        }
        return resultList;
    }

    /**
     * 查询赠送记录
     * @param rechargePaymentRecordDto
     * @return
     */
    private List<RechargePaymentRecordInfo> findReturnTicketLog(RechargePaymentRecordDto rechargePaymentRecordDto){
        List<RechargePaymentRecordInfo> resultList = new ArrayList<>();
        logger.info("查询赠送记录 start params is：" + JSON.toJSONString(rechargePaymentRecordDto));
        try {
            List<RechargePaymentRecordInfo> ticketLogList = customerHandleQueryMapper.findReturnTicketLog(rechargePaymentRecordDto);
            if (ticketLogList != null && ticketLogList.size() > 0) {
                for (RechargePaymentRecordInfo rechargePaymentRecordInfo:ticketLogList) {
                    rechargePaymentRecordInfo.setRepayType("赠送现金券");
                    rechargePaymentRecordInfo.setRepayChannel("");
                    rechargePaymentRecordInfo.setPayCash(new BigDecimal(0));
                    rechargePaymentRecordInfo.setBalanceUse(new BigDecimal(0));
                    rechargePaymentRecordInfo.setReturnBalanceFee(new BigDecimal(0));
                    rechargePaymentRecordInfo.setCouponFee(new BigDecimal(0));
                    resultList.add(rechargePaymentRecordInfo);
                }
            }
            logger.info("查询赠送记录 result is " + resultList);
        } catch (Exception e){
            logger.error("查询赠送记录出错~~~~", e);
        }
        return resultList;
    }
}
