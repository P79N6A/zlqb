package com.nyd.capital.service.pocket.job;

import com.alibaba.fastjson.JSONObject;
import com.nyd.capital.entity.UserPocket;
import com.nyd.capital.model.enums.FundSourceEnum;
import com.nyd.capital.model.enums.PocketAccountEnum;
import com.nyd.capital.model.pocket.PocketParentResult;
import com.nyd.capital.model.pocket.PocketQueryOrderWithdrawStatusData;
import com.nyd.capital.model.pocket.PocketQueryOrderWithdrawStatusDto;
import com.nyd.capital.service.UserPocketService;
import com.nyd.capital.service.jx.config.OpenPageConstant;
import com.nyd.capital.service.pocket.business.impl.PocketCallbackService;
import com.nyd.capital.service.pocket.service.Pocket2Service;
import com.nyd.capital.service.utils.RedisUtil;
import com.nyd.order.api.OrderContract;
import com.nyd.order.model.OrderInfo;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author liuqiu
 */
@Component
public class PocketTask {

    private static final Logger logger = LoggerFactory.getLogger(PocketTask.class);

    @Autowired
    private OrderContract orderContract;
    @Autowired
    private Pocket2Service pocket2Service;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserPocketService userPocketService;
    @Autowired
    private PocketCallbackService pocketCallbackService;

    /**
     * 每30分钟跑一次
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void loanJob() {
        logger.info("begin pocket loan job");
        try {
            boolean lock = redisUtil.lock("capital:loan", 3);
            if (!lock) {
                logger.info("other service is doing");
                return;
            }
            //查询所有待放款的用户
            List<UserPocket> userPockets = userPocketService.selectPocketByStage(Integer.valueOf(PocketAccountEnum.Have_Accepted.getCode()));
            if (userPockets != null && userPockets.size() > 0) {
                for (UserPocket pocket : userPockets) {
                    //查询用户订单
                    ResponseData<List<OrderInfo>> lastOrderByUserId = orderContract.getLastOrderByUserId(pocket.getUserId());
                    if (!OpenPageConstant.STATUS_ZERO.equals(lastOrderByUserId.getStatus())) {
                        continue;
                    }
                    List<OrderInfo> data = lastOrderByUserId.getData();
                    if (data == null || data.size() == 0) {
                        continue;
                    }
                    OrderInfo orderInfo = data.get(0);
                    if (!FundSourceEnum.KDLC.getCode().equals(orderInfo.getFundCode())){
                        continue;
                    }
//                    if (redisTemplate.hasKey(OpenPageConstant.NEW_POCKET_CALLBACK_LOAN + orderInfo.getOrderNo())) {
//                        logger.error("repeat callback");
//                        continue;
//                    } else {
//                        redisTemplate.opsForValue().set(OpenPageConstant.NEW_POCKET_CALLBACK_LOAN  + orderInfo.getOrderNo(), "1");
//                    }
                    PocketQueryOrderWithdrawStatusDto dto = new PocketQueryOrderWithdrawStatusDto();
                    dto.setOutTradeNo(orderInfo.getOrderNo());
                    ResponseData<PocketParentResult> status = pocket2Service.queryOrderWithdrawStatus(dto);
                    if (!OpenPageConstant.STATUS_ZERO.equals(status.getStatus())) {
                        continue;
                    }
                    PocketParentResult result = status.getData();
                    if (!OpenPageConstant.STATUS_ZERO.equals(result.getRetCode())){
                        continue;
                    }
                    String retData = result.getRetData();
                    PocketQueryOrderWithdrawStatusData statusData = JSONObject.parseObject(retData, PocketQueryOrderWithdrawStatusData.class);
                    if (statusData.getStatus() == 2) {
                        try {
                            boolean withdrawal = redisUtil.lock("loan" + pocket.getUserId(), 1);
                            if (!withdrawal) {
                                logger.info("other service is doing");
                                return;
                            }
                            pocket.setStage(Integer.valueOf(PocketAccountEnum.Withdrawal_Ing.getCode()));
                            userPocketService.update(pocket);
                            pocketCallbackService.withdrawal(orderInfo.getOrderNo(), pocket.getPassword());
                            redisUtil.unlock("loan" + pocket.getUserId());
                        } catch (Exception e) {
                            redisUtil.unlock("loan" + pocket.getUserId());
                        }
                    }

                }
                redisUtil.unlock("capital:loan");
            }

        } catch (Exception e) {
            logger.error("redis lock has exception", e);
            redisUtil.unlock("capital:loan");
            return;
        }

    }


    /**
     * 每15分钟跑一次
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void withdrawalJob() {
        logger.info("begin pocket withdrawal job");
        try {
            boolean lock = redisUtil.lock("capital:withdrawal", 3);
            if (!lock) {
                logger.info("other service is doing");
                return;
            }
            //查询所有待放款的用户
            List<UserPocket> userPockets = userPocketService.selectPocketByStage(Integer.valueOf(PocketAccountEnum.Withdrawal_Ing.getCode()));
            if (userPockets != null && userPockets.size() > 0) {
                for (UserPocket pocket : userPockets) {
                    //查询用户订单
                    ResponseData<List<OrderInfo>> lastOrderByUserId = orderContract.getLastOrderByUserId(pocket.getUserId());
                    if (!OpenPageConstant.STATUS_ZERO.equals(lastOrderByUserId.getStatus())) {
                        continue;
                    }
                    List<OrderInfo> data = lastOrderByUserId.getData();
                    if (data == null || data.size() == 0) {
                        continue;
                    }
                    OrderInfo orderInfo = data.get(0);
                    if (!FundSourceEnum.KDLC.getCode().equals(orderInfo.getFundCode())){
                        continue;
                    }
//                    if (redisTemplate.hasKey(OpenPageConstant.NEW_POCKET_CALLBACK_WITHDRAW+ orderInfo.getOrderNo())) {
//                        logger.error("repeat callback");
//                        continue;
//                    } else {
//                        redisTemplate.opsForValue().set(OpenPageConstant.NEW_POCKET_CALLBACK_WITHDRAW  + orderInfo.getOrderNo(), "1");
//                    }
                    PocketQueryOrderWithdrawStatusDto dto = new PocketQueryOrderWithdrawStatusDto();
                    dto.setOutTradeNo(orderInfo.getOrderNo());
                    ResponseData<PocketParentResult> status = pocket2Service.queryOrderWithdrawStatus(dto);
                    if (!OpenPageConstant.STATUS_ZERO.equals(status.getStatus())) {
                        continue;
                    }
                    PocketParentResult result = status.getData();
                    if (!OpenPageConstant.STATUS_ZERO.equals(result.getRetCode())){
                        continue;
                    }
                    String retData = result.getRetData();
                    PocketQueryOrderWithdrawStatusData statusData = JSONObject.parseObject(retData, PocketQueryOrderWithdrawStatusData.class);
                    if (statusData.getStatus() == 6) {
                        try {
                            boolean withdrawal = redisUtil.lock("withdrawal" + pocket.getUserId(), 1);
                            if (!withdrawal) {
                                logger.info("other service is doing");
                                return;
                            }
                            pocketCallbackService.handMq(orderInfo, pocket);
                            redisUtil.unlock("withdrawal" + pocket.getUserId());
                        } catch (Exception e) {
                            redisUtil.unlock("withdrawal" + pocket.getUserId());
                        }
                    }else if (statusData.getStatus() == 2){
                        try {
                            boolean withdrawal = redisUtil.lock("withdrawal" + pocket.getUserId(), 1);
                            if (!withdrawal) {
                                logger.info("other service is doing");
                                return;
                            }
                            pocketCallbackService.withdrawal(orderInfo.getOrderNo(), pocket.getPassword());
                            redisUtil.unlock("withdrawal" + pocket.getUserId());
                        }catch (Exception e){
                            redisUtil.unlock("withdrawal" + pocket.getUserId());
                        }
                    }

                }
            }
            redisUtil.unlock("capital:withdrawal");
        } catch (Exception e) {
            logger.error("redis lock has exception", e);
            redisUtil.unlock("capital:withdrawal");
            return;
        }
    }
}
