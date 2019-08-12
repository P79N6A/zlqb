package com.nyd.batch.service.thread;

import com.alibaba.fastjson.JSON;
import com.creativearts.nyd.pay.model.RepayMessage;
import com.creativearts.nyd.pay.model.helibao.CreateOrderVo;
import com.nyd.batch.dao.mapper.BillMapper;
import com.nyd.batch.entity.Bill;
import com.nyd.batch.entity.MaturityBill;
import com.nyd.order.api.OrderContract;
import com.nyd.order.model.OrderInfo;
import com.nyd.pay.api.enums.WithHoldType;
import com.nyd.pay.api.service.PayService;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.model.UserInfo;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cong Yuxiang
 * 2018/1/15
 **/
public class BuckleRunable implements Runnable {
    Logger logger = LoggerFactory.getLogger(BuckleRunable.class);

    private PayService payService;

    private OrderContract orderContract;

    private UserIdentityContract userIdentityContract;

    private BillMapper billMapper;

    private MaturityBill maturityBill;

    private int index;

    public BuckleRunable(int i, MaturityBill maturityBill, PayService payService, OrderContract orderContract, UserIdentityContract userIdentityContract, BillMapper billMapper) {
        this.index = i;
        this.maturityBill = maturityBill;
        this.payService = payService;
        this.orderContract = orderContract;
        this.userIdentityContract = userIdentityContract;
        this.billMapper = billMapper;
    }

    @Override
    public void run() {
        OrderInfo orderInfo = orderContract.getOrderByOrderNo(maturityBill.getOrderNo()).getData();
        int loopCount = 10;
        while (orderInfo == null && loopCount > 0) {
            orderInfo = orderContract.getOrderByOrderNo(maturityBill.getOrderNo()).getData();
            loopCount--;
        }
        if (orderInfo == null) {
            return;
        }
        UserInfo userInfo = userIdentityContract.getUserInfo(maturityBill.getUserId()).getData();
//            OrderDetailInfo orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(maturityBill.getOrderNo()).getData();
        loopCount = 10;
        while (userInfo == null && loopCount > 0) {
            userInfo = userIdentityContract.getUserInfo(maturityBill.getUserId()).getData();
            loopCount--;
        }
        if (userInfo == null) {
            return;
        }


        CreateOrderVo vo = new CreateOrderVo();


        vo.setP3_orderId(maturityBill.getBillNo() + "-" + System.currentTimeMillis() + "-q");
        vo.setP4_timestamp(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
        vo.setP8_cardNo(orderInfo.getBankAccount());
        vo.setP7_idCardNo(userInfo.getIdNumber());
        vo.setP5_payerName(userInfo.getRealName());

        Map map = new HashMap();
        map.put("billNo", maturityBill.getBillNo());
        List<Bill> billList = billMapper.getBillByBillNo(map);
        if (billList == null || billList.size() == 0) {
            return;
        }
        Bill bill = billList.get(0);
        if ("B003".equals(bill.getBillStatus())) {
            return;
        }

        if("B001".equals(bill.getBillStatus())&&(index==0||index==1)){
            return;
        }
        if("B001".equals(bill.getBillStatus())){
            if(index==2){
                vo.setP11_orderAmount(maturityBill.getWaitRepayAmount());
            }else if(index>2) {
                vo.setP11_orderAmount(maturityBill.getWaitRepayAmount().divide(new BigDecimal(2 * (index-2)), 2, RoundingMode.HALF_UP));
            }

        }else if("B002".equals(bill.getBillStatus())){
            if (index == 0) {
                vo.setP11_orderAmount(maturityBill.getWaitRepayAmount());
            } else {
                vo.setP11_orderAmount(maturityBill.getWaitRepayAmount().divide(new BigDecimal(2 * index), 2, RoundingMode.HALF_UP));
            }
        }




        ResponseData responseData = payService.withHold(vo, WithHoldType.WITH_HOLD);
        if ("0".equals(responseData.getStatus())) {
            RepayMessage repayMessage = new RepayMessage();
            repayMessage.setBillNo(maturityBill.getBillNo());
            repayMessage.setRepayAmount(maturityBill.getWaitRepayAmount());
            repayMessage.setRepayStatus("0");
            logger.info(maturityBill.getBillNo() + "第" + (index + 1) + "次强扣还款回写mq" + JSON.toJSONString(responseData));


        } else if (JSON.toJSONString(responseData).contains("余额不足")) {
            logger.info(maturityBill.getBillNo() + "第" + (index + 1) + "次余额不足" + JSON.toJSONString(responseData));
//                continue;
        } else {
            logger.info(maturityBill.getBillNo() + "第" + (index + 1) + "次错误：" + JSON.toJSONString(responseData));
//                continue;
        }

//        }
    }

}
