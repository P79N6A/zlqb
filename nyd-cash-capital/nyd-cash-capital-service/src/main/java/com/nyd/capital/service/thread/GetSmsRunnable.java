package com.nyd.capital.service.thread;

import com.alibaba.fastjson.JSON;
import com.nyd.capital.model.kzjr.OpenPageInfo;
import com.nyd.capital.service.Contants;
import com.nyd.capital.service.kzjr.KzjrOpenPageService;
import com.nyd.order.api.OrderContract;
import com.nyd.order.model.KzjrPageInfo;
import com.nyd.order.model.enums.BorrowConfirmChannel;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Cong Yuxiang
 * 2018/5/12
 **/
public class GetSmsRunnable implements Runnable {

    Logger logger = LoggerFactory.getLogger(GetSmsRunnable.class);
    private KzjrOpenPageService kzjrOpenPageService;
    private OpenPageInfo openPageInfo;
    private OrderContract orderContract;
    private RedisTemplate redisTemplate;

    public GetSmsRunnable(KzjrOpenPageService kzjrOpenPageService, OpenPageInfo openPageInfo, OrderContract orderContract, RedisTemplate redisTemplate) {
        this.kzjrOpenPageService = kzjrOpenPageService;
        this.openPageInfo = openPageInfo;
        this.orderContract = orderContract;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void run() {

        try {
            ResponseData responseData = kzjrOpenPageService.getSmsCode(openPageInfo);

            logger.info(openPageInfo.getUserId() + "testtest" + JSON.toJSONString(responseData));

            if ("0".equals(responseData.getStatus())) {
                if ("4".equals(responseData.getCode())) {
                    //失败 转wt
                    ResponseData<KzjrPageInfo> kzjrPageInfoResponseData = null;

                    try {
                        kzjrPageInfoResponseData = orderContract.kzjrPageErrorGenerateOrder(openPageInfo.getUserId());
                        logger.info("kzjr失败转单" + JSON.toJSONString(kzjrPageInfoResponseData));
                    } catch (Exception e) {
                        //e.printStackTrace();
                        logger.error("调用kzjrPageErrorGenerateOrder异常" + openPageInfo.getUserId(), e);
//                    PrintWriter out = response.getWriter();tail
//                    out.println(page);
//                    e.printStackTrace();
//                    return;
                    }
                    if (kzjrPageInfoResponseData == null) {
                        logger.info("begin to setRedis for KZJR_SMS_STATUS" + openPageInfo.getUserId());
                        redisTemplate.opsForValue().set(Contants.KZJR_SMS_STATUS + openPageInfo.getUserId(), "7", 20, TimeUnit.SECONDS);

//                    jsonObject.put("type","7");
//                    result.setMsg("转稳通失败");

                    } else {
                        if ("0".equals(kzjrPageInfoResponseData.getStatus())) {
                            KzjrPageInfo kzjrPageInfo = kzjrPageInfoResponseData.getData();
                        /*jsonObject.put("type","1");

                        result.setMsg("放款渠道转换");*/
                            logger.info("begin to setRedis for KZJR_SMS_STATUS" + kzjrPageInfo.getChannel());
                            if (BorrowConfirmChannel.NYD.getChannel().equals(kzjrPageInfo.getChannel())) {

                                redisTemplate.opsForValue().set(Contants.KZJR_SMS_STATUS + openPageInfo.getUserId(), "1_" + kzjrPageInfo.getOrderNo(), 20, TimeUnit.SECONDS);

//                            jsonObject.put("orderNo",kzjrPageInfo.getOrderNo());
//                            logger.info("nyd转wt"+kzjrConfig.getPageErrorUrlNyd() + "?orderNo=" + kzjrPageInfo.getOrderNo());
//                            response.sendRedirect(kzjrConfig.getPageErrorUrlNyd() + "?orderNo=" + kzjrPageInfo.getOrderNo());
                            } else if (BorrowConfirmChannel.YMT.getChannel().equals(kzjrPageInfo.getChannel())) {
                                redisTemplate.opsForValue().set(Contants.KZJR_SMS_STATUS + openPageInfo.getUserId(), "1", 20, TimeUnit.SECONDS);

//                            jsonObject.put("borrowTime",kzjrPageInfo.getBorrowTime());
//                            jsonObject.put("loanAmount",kzjrPageInfo.getLoanAmount());
//                            logger.info("ymt转wt"+kzjrConfig.getPageErrorUrlYmt() + "?borrowTime=" + kzjrPageInfo.getBorrowTime() + "&loanAmount=" + kzjrPageInfo.getLoanAmount());
//                            response.sendRedirect(kzjrConfig.getPageErrorUrlYmt() + "?borrowTime=" + kzjrPageInfo.getBorrowTime() + "&loanAmount=" + kzjrPageInfo.getLoanAmount());
                            }
                        } else {
                            redisTemplate.opsForValue().set(Contants.KZJR_SMS_STATUS + openPageInfo.getUserId(), "8", 20, TimeUnit.SECONDS);

//                        logger.error("调用kzjrPageErrorGenerateOrder返回状态为1失败");
//                        jsonObject.put("type","8");
//                        result.setMsg("调用kzjrPageErrorGenerateOrder返回状态为1失败");
//                        PrintWriter out = response.getWriter();
//                        out.println("服务器开小差q，请重新尝试借款");
                        }
                    }

                } else {
                    //成功发送短信验证码
//                jsonObject.put("type","2");
//                result.setMsg("发送成功");
                    redisTemplate.opsForValue().set(Contants.KZJR_SMS_STATUS + openPageInfo.getUserId(), "2", 20, TimeUnit.SECONDS);

                }
            } else {
//            logger.info(openPageInfo.getUrl()+"出现异常 重新获取验证码");
//            jsonObject.put("type","6");
//            result.setMsg("发送短信异常,请重新获取");
                redisTemplate.opsForValue().set(Contants.KZJR_SMS_STATUS + openPageInfo.getUserId(), "6", 20, TimeUnit.SECONDS);

            }
//        result.setData(jsonObject);
//        logger.info(openPageInfo.getUserId()+"获取验证码结果"+ JSON.toJSONString(result));
//        return result;
        } catch (Exception e) {
            logger.error("GetSmsRunnable is error", e);
        }
    }
}
