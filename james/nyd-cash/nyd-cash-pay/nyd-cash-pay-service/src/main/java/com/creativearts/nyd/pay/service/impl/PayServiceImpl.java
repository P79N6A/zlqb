package com.creativearts.nyd.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.creativearts.nyd.pay.model.RepayMessage;
import com.creativearts.nyd.pay.model.RepayType;
import com.creativearts.nyd.pay.model.helibao.CreateOrderVo;
import com.creativearts.nyd.pay.service.baofoo.JoinPayWithholdService;
import com.creativearts.nyd.pay.service.helibao.HelibaoPayService;
import com.nyd.order.api.OrderContract;
import com.nyd.order.entity.WithholdOrder;
import com.nyd.pay.api.enums.WithHoldType;
import com.nyd.pay.api.service.PayService;
import com.nyd.zeus.api.BillContract;
import com.nyd.zeus.model.BillInfo;
import com.nyd.zeus.model.RepayInfo;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Cong Yuxiang
 * 2017/12/12
 **/
@Service("payService")
public class PayServiceImpl implements PayService {
    Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

    @Autowired
    private HelibaoPayService helibaoPayService;
    @Autowired
    private JoinPayWithholdService joinPayWithholdService;
    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;
    @Autowired
    private BillContract billContract;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private OrderContract orderContract;

    @Override
    public ResponseData withHold(CreateOrderVo vo, WithHoldType type) {
//        CreateOrderVo vo = new CreateOrderVo();
        return helibaoPayService.withHold(vo, type);
    }

    @Override
    public ResponseData daiFu() {
        return null;
    }


    @Override
    public ResponseData selectWithholdResult() {
        ResponseData responseData = joinPayWithholdService.selectStatusOne();
        if ("0".equals(responseData.getStatus())) {
            List<WithholdOrder> data = (List<WithholdOrder>) responseData.getData();
            if (data.size() == 0) {
                return ResponseData.success();
            }
            logger.info("跑批查询支付中的还款代扣集合为:" + JSON.toJSONString(data));
            for (WithholdOrder order : data) {
                ResponseData withHoldOrder = joinPayWithholdService.withHoldOrder(order.getPayOrderNo());
                if ("0".equals(withHoldOrder.getStatus())) {
                    String payState = (String) withHoldOrder.getData();
                    RepayInfo repayInfo = new RepayInfo();
                    if ("3".equals(payState)) {
                        order.setOrderStatus(2);
                        repayInfo.setRepayStatus("0");
                        String billNo = order.getMemberId();
                        RepayMessage repayMessage = new RepayMessage();
                        repayMessage.setBillNo(billNo);
                        repayMessage.setRepayAmount(order.getPayAmount());
                        repayMessage.setRepayStatus("0");
                        rabbitmqProducerProxy.convertAndSend("repay.nyd", repayMessage);
                        //银码头
                        try {
                            ResponseData<BillInfo> responseData1 = billContract.getBillInfo(billNo);
                            logger.info("付款成功后获取billInfo" + JSON.toJSONString(responseData));
                            if ("0".equals(responseData.getStatus())) {
                                BillInfo billInfo = responseData1.getData();
                                if (billInfo != null && StringUtils.isNotBlank(billInfo.getIbankOrderNo())) {
                                    repayMessage.setBillNo(billInfo.getIbankOrderNo());
                                    repayMessage.setRepayTime(new Date());
                                    logger.info("还款成功发送银码头" + JSON.toJSONString(repayMessage));
                                    rabbitmqProducerProxy.convertAndSend("payIbank.ibank", repayMessage);
                                }
                            } else {
                                logger.info(billNo + "获取billInfo为status为1");
                            }

                        } catch (Exception e) {
                            logger.info(billNo + "error", e);
                        }
                        repayInfo.setBillNo(billNo);
                        repayInfo.setRepayAmount(order.getPayAmount());
                        repayInfo.setRepayChannel("joinpay");
                        repayInfo.setRepayTime(new Date());
                        repayInfo.setRepayType(RepayType.JOINPAY_WITHHOLD.getCode());
                        repayInfo.setUserId(order.getUserId());
                        repayInfo.setRepayNo(order.getWithholdOrderNo());
                        try {
                            rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                        } catch (Exception e) {
                            continue;
//                            return ResponseData.error();
                        }
                    } else if ("4".equals(payState)) {
                        repayInfo.setBillNo(order.getMemberId());
                        repayInfo.setRepayAmount(order.getPayAmount());
                        repayInfo.setRepayChannel("joinpay");
                        repayInfo.setRepayTime(new Date());
                        repayInfo.setRepayType(RepayType.JOINPAY_WITHHOLD.getCode());
                        repayInfo.setUserId(order.getUserId());
                        repayInfo.setRepayNo(order.getWithholdOrderNo());
                        order.setOrderStatus(3);
                        repayInfo.setRepayStatus("1");
                        try {
                            rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                        } catch (Exception e) {
                            continue;
//                            return ResponseData.error();
                        }
                    }
                    redisTemplate.delete("withHold:joinpay" + order.getMemberId());
                    orderContract.updateWithHoldOrder(order);
                    logger.info(repayInfo.toString());
                }
            }
        }
        return ResponseData.success();
    }


}
