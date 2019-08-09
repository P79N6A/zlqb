package com.nyd.capital.service.thread;

import com.nyd.capital.model.enums.FundSourceEnum;
import com.nyd.capital.service.FundFactory;
import com.nyd.capital.service.FundService;
import com.nyd.capital.service.jx.config.OpenPageConstant;
import com.nyd.capital.service.validate.ValidateException;
import com.nyd.order.api.OrderContract;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.msg.OrderMessage;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liuqiu
 */
public class SendCapitalRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SendCapitalRunnable.class);

    private OrderMessage message;
    private FundFactory fundFactory;
    private OrderContract orderContract;
    private com.ibank.order.api.OrderContract orderContractYmt;

    // ReentrantLock lock = new ReentrantLock(true);
    public SendCapitalRunnable(OrderMessage message, FundFactory fundFactory, OrderContract orderContract, com.ibank.order.api.OrderContract orderContractYmt) {
        this.message = message;
        this.fundFactory = fundFactory;
        this.orderContract = orderContract;
        this.orderContractYmt = orderContractYmt;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run() {
        logger.info("进行侬要贷推单处理,message is" + message.toString());
        String userId = message.getUserId();
        String orderNo = message.getOrderNo();
        String ibankOrderNo = orderNo;
        String fundCode = message.getFundCode();
        FundSourceEnum fundSourceEnum = FundSourceEnum.toEnum(fundCode);
        if (fundSourceEnum == null) {
            logger.error("根据资金源编码 获取不到资金源" + orderNo);
            return;
        }
        // 根据资金的工厂类，进行构建资金类
        FundService fundService = fundFactory.buildChannel(fundSourceEnum);
        List orders = null;

        if (message.getIfTask() != 1 && message.getChannel() == 1) {
            OrderInfo orderInfo = orderContract.getOrderByIbankOrderNo(orderNo);
            logger.info("银码头的查出的订单为:" + orderInfo);
            orderNo = orderInfo.getOrderNo();
            userId = orderInfo.getUserId();
        }

        try {
            // 拼装所有请求空中金融的订单列表
            orders = fundService.generateOrders(userId, orderNo, message.getChannel());
        } catch (Exception e) {
            logger.info(userId + "-" + orderNo + "生成订单错误" + e.getMessage());
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderNo(orderNo);
            orderInfo.setOrderStatus(40);
            orderContract.updateOrderInfo(orderInfo);
            if (message.getIfTask() != 1 && message.getChannel() == 1) {
                orderContractYmt.updateOrderDetailStatus(ibankOrderNo, 40);
            }
            try {
                throw e;
            } catch (ValidateException e1) {
            }
        }
        if (orders == null || orders.size() == 0) {
            logger.error("组装资产订单数据失败" + orderNo);
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderNo(orderNo);
            orderInfo.setOrderStatus(40);
            orderContract.updateOrderInfo(orderInfo);
            if (message.getIfTask() != 1 && message.getChannel() == 1) {
                orderContractYmt.updateOrderDetailStatus(ibankOrderNo, 40);
            }
            try {
                throw new Exception();
            } catch (Exception e) {
            }
        }


        try {
            // 推送资产到资金方
            ResponseData result = fundService.sendOrder(orders);
            if (OpenPageConstant.STATUS_ZERO.equals(result.getStatus())) {
                logger.info("等待" + fundCode + "的进行处理");

            } else if ("2".equals(result.getStatus())){
                if (message.getIfTask() != 1 && message.getChannel() == 1) {
                    orderContractYmt.updateOrderDetailStatus(ibankOrderNo, 40);
                }
                try {
                    throw new Exception();
                } catch (Exception e) {
                }
            } else {
                logger.info("等待" + fundCode + "提交资产订单返回失败");
                if (StringUtils.isBlank(result.getMsg())) {
                    return;
                }
                return;
            }
        } catch (Exception e) {
            logger.error("sendOrder to fund has exception,order is " + orderNo, e);
            throw e;
        }
    }

}
