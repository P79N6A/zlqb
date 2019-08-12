package com.nyd.batch.service.impls;

import com.nyd.batch.dao.ds.DataSourceContextHolder;
import com.nyd.batch.dao.mapper.BillMapper;
import com.nyd.batch.entity.MaturityBill;
import com.nyd.batch.service.BuckleService;
import com.nyd.batch.service.aspect.RoutingDataSource;
import com.nyd.batch.service.thread.BuckleRunable;
import com.nyd.batch.service.thread.CountableThreadPool;
import com.nyd.order.api.OrderContract;
import com.nyd.pay.api.service.PayService;
import com.nyd.user.api.UserIdentityContract;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cong Yuxiang
 * 2017/12/19
 **/
@Service
public class BuckleServiceImpl implements BuckleService{

    Logger logger = LoggerFactory.getLogger(BuckleServiceImpl.class);

    @Autowired
    private BillMapper billMapper;
    @Autowired
    private PayService payService;
    @Autowired
    private OrderContract orderContract;
    @Autowired
    private UserIdentityContract userIdentityContract;
    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;

    @Autowired
    private CountableThreadPool countableThreadPool;


    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public void processQK(int i) {
        Map map = new HashMap();
        map.put("repayDate",DateFormatUtils.format(new Date(),"yyyy-MM-dd"));
        List<MaturityBill> maturityBills = billMapper.getMaturityBills(map);



        for(MaturityBill maturityBill:maturityBills){
            while (countableThreadPool.getThreadAlive()>=countableThreadPool.getThreadNum()){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            BuckleRunable buckleRunable = new BuckleRunable(i,maturityBill,payService,orderContract,userIdentityContract,billMapper);
            countableThreadPool.execute(buckleRunable);
           /* OrderInfo orderInfo = orderContract.getOrderByOrderNo(maturityBill.getOrderNo()).getData();
            int loopCount = 10;
            while (orderInfo == null&&loopCount>0){
                orderInfo = orderContract.getOrderByOrderNo(maturityBill.getOrderNo()).getData();
                loopCount--;
            }
            if(orderInfo==null){
                continue;
            }
            UserInfo userInfo = userIdentityContract.getUserInfo(maturityBill.getUserId()).getData();
//            OrderDetailInfo orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(maturityBill.getOrderNo()).getData();
            loopCount = 10;
            while (userInfo == null&&loopCount>0){
                userInfo = userIdentityContract.getUserInfo(maturityBill.getUserId()).getData();
                loopCount--;
            }
            if(userInfo == null){
                continue;
           }



            for(int i=0;i<5;i++) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                CreateOrderVo vo = new CreateOrderVo();


                vo.setP3_orderId(maturityBill.getBillNo()+"-"+System.currentTimeMillis()+"-q");
                vo.setP4_timestamp(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
                vo.setP8_cardNo(orderInfo.getBankAccount());
                vo.setP7_idCardNo(userInfo.getIdNumber());
                vo.setP5_payerName(userInfo.getRealName());

                if(i==0) {
                    vo.setP11_orderAmount(maturityBill.getWaitRepayAmount());
                }else {
                    vo.setP11_orderAmount(maturityBill.getWaitRepayAmount().divide(new BigDecimal(2*i),2, RoundingMode.HALF_UP));
                }
                ResponseData responseData = payService.withHold(vo, WithHoldType.WITH_HOLD);

                if ("0".equals(responseData.getStatus())) {
                    RepayMessage repayMessage = new RepayMessage();
                    repayMessage.setBillNo(maturityBill.getBillNo());
                    repayMessage.setRepayAmount(maturityBill.getWaitRepayAmount());
                    repayMessage.setRepayStatus("0");
                    logger.info(maturityBill.getBillNo()+"第"+(i+1)+"次强扣还款回写mq"+JSON.toJSONString(responseData));
//                    rabbitmqProducerProxy.convertAndSend("repay.nyd", repayMessage);
                    if(i==0){
                        break;
                    }


                } else if (JSON.toJSONString(responseData).contains("余额不足")) {
                    logger.info(maturityBill.getBillNo()+"第"+(i+1)+"次余额不足"+JSON.toJSONString(responseData));
                    continue;
                } else {
                    logger.info(maturityBill.getBillNo()+"第"+(i+1)+"次错误："+JSON.toJSONString(responseData));
                    continue;
                }

            }*/
//            if()


        }

    }
}
