package com.nyd.batch.service.quartz;

import com.ibank.order.model.OrderDetailInfo;
import com.nyd.order.api.OrderContract;
import com.nyd.order.model.OrderInfo;
import com.nyd.zeus.api.RemitContract;
import com.nyd.zeus.model.RemitInfo;
import com.nyd.zeus.model.RemitModel;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author: zhangp
 * @Description: 批量跑remit表中放款失败的 更新为失败订单
 * @Date: 15:56 2018/9/3
 */
@Component("orderFailTask")
public class OrderFailTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderFailTask.class);

    @Autowired
    private OrderContract orderContract;
//    @Autowired
//    private com.ibank.order.api.OrderContract orderContractYmt;
    @Autowired
    private RemitContract remitContract;

    public void run(){
//        try {
//            LOGGER.info("执行获取放款失败的订单，并且修改订单为失败订单开始。。。");
//            long methodStart = System.currentTimeMillis();
//            Date current = new Date();
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(current);
//            calendar.add(Calendar.DATE,-1);
//            Date startTime = calendar.getTime();
//            Date endTime = current;
//            int state = 1;
//            ResponseData<List<RemitModel>> remitResult= remitContract.getByCreateTime(state,startTime,endTime);
//            if(null!=remitResult && "0".equals(remitResult.getStatus())){
//                List<RemitModel> remitInfos = remitResult.getData();
//                if(null==remitInfos||remitInfos.size()<1){
//                    LOGGER.warn("没有查到前一天放款失败的订单 date:"+new Date());
//                    return;
//                }
//                LOGGER.info("获取remitInfos size:"+remitInfos.size());
//                for (RemitModel ri:remitInfos){
//                    String orderNo = ri.getOrderNo();
//                    ResponseData<OrderInfo> orderNydResult = orderContract.getOrderByOrderNo(orderNo);
//                    if(null!=orderNydResult && "0".equals(orderNydResult.getStatus())){
//                        OrderInfo orderInfoNyd = orderNydResult.getData();
//                        if(null!=orderInfoNyd){
//                            int channel = 0;
//                            if(null!=orderInfoNyd.getChannel()){
//                                channel = orderInfoNyd.getChannel();
//                            }
//                            //更新nyd的订单状态为失败
//                            if(orderInfoNyd.getOrderStatus()<40){
//                                OrderInfo orderInfo = new OrderInfo();
//                                orderInfo.setOrderNo(orderNo);
//                                orderInfo.setOrderStatus(40);
//                                orderContract.updateOrderInfo(orderInfo);
//                            }
//                            //如果是ymt来的订单，则查询出银马头的orderSno
//                            if(channel==1){
//                                LOGGER.info("更新侬要贷订单成功 orderNo："+orderNo);
//                                String ibankOrderSno=orderInfoNyd.getIbankOrderNo();
//                                ResponseData<List<OrderDetailInfo>> orderDetailResult =orderContractYmt.getOrderDetaiByOrderSon(ibankOrderSno);
//                                if(null!=orderDetailResult && "0".equals(orderDetailResult.getStatus())){
//                                    List<OrderDetailInfo> orderDetailInfos = orderDetailResult.getData();
//                                    if(null!=orderDetailInfos && orderDetailInfos.size()>0) {
//                                        OrderDetailInfo orderDetailInfo =orderDetailInfos.get(0);
//                                        if(null!=orderDetailInfo && orderDetailInfo.getOrderStatus()<40){
//                                            //更新的银马头订单
//                                            OrderDetailInfo updateOrderDetailInfo = new OrderDetailInfo();
//                                            updateOrderDetailInfo.setOrderStatus(40);
//                                            updateOrderDetailInfo.setOrderStatusTime(new Date());
//                                            updateOrderDetailInfo.setOrderSno(ibankOrderSno);
//                                            orderContractYmt.updateOrderDetail(updateOrderDetailInfo);
//                                            LOGGER.info("更新银马头单成功 ibankOrderSno："+ibankOrderSno);
//                                        }
//                                    }
//                                }
//                            }
//                        }else {
//                            LOGGER.info("侬要贷不存在该笔订单 orderNoNyd："+orderNo);
//                        }
//                    }
//                }
//            }else {
//                LOGGER.error("orderFailTask run 获取remit dubbo异常 date:"+new Date());
//                return;
//            }
//            long methodEnd = System.currentTimeMillis();
//            LOGGER.info("执行获取放款失败的订单，并且修改订单任务结束 耗时（ms）"+(methodEnd-methodStart));
//        }catch (Exception e){
//            LOGGER.error("orderFailTask run exception",e);
//        }
    }
}
