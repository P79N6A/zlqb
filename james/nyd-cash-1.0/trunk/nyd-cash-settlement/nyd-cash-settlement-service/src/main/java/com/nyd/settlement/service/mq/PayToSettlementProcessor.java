package com.nyd.settlement.service.mq;


import com.alibaba.fastjson.JSON;
import com.ibank.pay.model.mq.WXNotifyInfo;
import com.nyd.settlement.entity.YmtOrder;
import com.nyd.settlement.entity.repay.YmtPayFlow;
import com.nyd.settlement.model.dto.RecommendRefundDto;
import com.nyd.settlement.service.YmtOrderService;
import com.nyd.settlement.service.YmtPayFlowService;
import com.nyd.settlement.service.YmtRefundService;
import com.nyd.settlement.service.utils.DateUtil;
import com.tasfe.framework.rabbitmq.RabbitmqMessageProcesser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

public class PayToSettlementProcessor implements RabbitmqMessageProcesser<WXNotifyInfo> {

    private static Logger LOGGER = LoggerFactory.getLogger(PayToSettlementProcessor.class);

    @Autowired
    private YmtPayFlowService ymtPayFlowService;

    @Autowired
    private YmtOrderService ymtOrderService;

    @Autowired
    private YmtRefundService ymtRefundService;



    @Override
    public void processMessage(WXNotifyInfo message) {
        LOGGER.info("解密参数结果："+ JSON.toJSONString(message));
        if (message.getRefund_status().equals("SUCCESS")){      //表示退款成功，写进表里面，将refund_status设置为1
            //将退款记录保存到表t_refund里面
            RecommendRefundDto dto = new RecommendRefundDto();

            //根据商户订单号找到商户相关信息
            YmtPayFlow ymtPayFlow = ymtPayFlowService.findByTradeNo(message.getOut_trade_no());

            String repayNo = ymtPayFlow.getRepayNo();//交易流水号
            LOGGER.info("微信交易流水号为："+repayNo);

            List<YmtOrder> list = ymtOrderService.findByRepayNo(repayNo);
            String orderNo = "";
            String name = "";
            if (list.size() > 0 && list != null){
                YmtOrder ymtOrder = list.get(0);
                orderNo = ymtOrder.getOrderNo();
                name = ymtOrder.getRealName();
            }
            dto.setOrderNo(orderNo);//订单号
            dto.setRefundReason("微信推荐费退款");//退款原因
            dto.setMobile(ymtPayFlow.getMobile());//退款人手机号码
            dto.setRefundChannel("wx");//退款方式
            dto.setRefundAccount(message.getRefund_recv_accout());//退款账户
            dto.setRefundType(2);//退款类型
            try {
                dto.setRefundTime(DateUtil.transferFormat(message.getSuccess_time()));//退款时间
            } catch (ParseException e) {
                e.printStackTrace();
                LOGGER.info(e.getMessage());
            }
            //退款手续费
            dto.setRefundFee(new BigDecimal( 0.01));
            dto.setRefundAmount(new BigDecimal(message.getRefund_fee()));//退款金额
            dto.setRefundFlowNo(repayNo);//退款流水号
            dto.setName(name);//退款人姓名
            dto.setRefundStatus(1);
            ymtRefundService.addRecommendRefund(dto);
            LOGGER.info("退款成功，商户订单号：" + message.getOut_trade_no() + "," + "商户退款单号:" + message.getOut_refund_no() + "," + "微信退款单号:" + message.getRefund_id()+"," + "退款金额：" + message.getRefund_fee());

        }else if (message.getRefund_status().equals("CHANGE")){                                                     //退款失败，也写进表里面，只是将refund_status设置为0
            //将退款记录保存到表t_refund里面
            RecommendRefundDto dto = new RecommendRefundDto();
            //根据商户订单号找到商户相关信息
            YmtPayFlow ymtPayFlow = ymtPayFlowService.findByTradeNo(message.getOut_trade_no());
            String repayNo = ymtPayFlow.getRepayNo();//交易流水号
            List<YmtOrder> list = ymtOrderService.findByRepayNo(repayNo);
            String orderNo = "";
            String name = "";
            if (list.size() > 0 && list != null){
                YmtOrder ymtOrder = list.get(0);
                orderNo = ymtOrder.getOrderNo();
                name = ymtOrder.getRealName();
            }
            dto.setOrderNo(orderNo);//订单号
            dto.setRefundReason("微信推荐费退款");//退款原因
            dto.setMobile(ymtPayFlow.getMobile());//退款人手机号码
            dto.setRefundChannel("wx");//退款方式
            dto.setRefundType(2);//退款类型
            try {
                dto.setRefundTime(DateUtil.transferFormat(message.getSuccess_time()));//退款时间
            } catch (ParseException e) {
                e.printStackTrace();
                LOGGER.info(e.getMessage());
            }
            //退款手续费
            dto.setRefundFee(new BigDecimal( 0.01));
            dto.setRefundAccount(message.getRefund_recv_accout());//退款账户
            dto.setRefundAmount(new BigDecimal(message.getRefund_fee()));//退款金额
            dto.setRefundFlowNo(repayNo);//退款流水号
            dto.setName(name);//退款人姓名
            dto.setRefundStatus(0);         //未退款
            ymtRefundService.addRecommendRefund(dto);
            LOGGER.info("微信退款失败，商户订单号：" + message.getOut_trade_no() + "," + "商户退款单号:" + message.getOut_refund_no() + "," + "微信退款单号:" + message.getRefund_id()+"," + "退款金额：" + message.getRefund_fee());
        }

    }




}
