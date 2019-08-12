package com.nyd.capital.service.pocket.business.impl;

import com.alibaba.fastjson.JSONObject;
import com.nyd.capital.entity.UserPocket;
import com.nyd.capital.model.enums.FundSourceEnum;
import com.nyd.capital.model.enums.PocketAccountEnum;
import com.nyd.capital.model.pocket.*;
import com.nyd.capital.service.OrderHandler;
import com.nyd.capital.service.UserPocketService;
import com.nyd.capital.service.jx.config.OpenPageConstant;
import com.nyd.capital.service.mq.PocketCrawlerProduct;
import com.nyd.capital.service.pocket.run.MsgCodeRunnable;
import com.nyd.capital.service.pocket.service.Pocket2Service;
import com.nyd.capital.service.pocket.util.PocketConfig;
import com.nyd.capital.service.pocket.util.UnicodeUtil;
import com.nyd.capital.service.utils.Constants;
import com.nyd.order.api.CapitalOrderRelationContract;
import com.nyd.order.api.OrderContract;
import com.nyd.order.model.OrderInfo;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.model.UserInfo;
import com.tasfe.framework.support.model.ResponseData;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author liuqiu
 */
@Service
public class PocketHtmlService {

    private static final Logger logger = LoggerFactory.getLogger(PocketHtmlService.class);

    @Autowired
    private Pocket2Service pocket2Service;
    @Autowired
    private OrderContract orderContract;
    @Autowired
    private UserIdentityContract userIdentityContract;
    @Autowired
    private PocketConfig pocketConfig;

    @Autowired
    private OrderHandler orderHandler;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private PocketCrawlerProduct product;


    public ResponseData confirmOrderHtml(PocketHtmlMessage message) {
        ResponseData responseData = ResponseData.success();
        logger.info("begin confirm order html,and message is:" + message.toString());
        ResponseData<OrderInfo> orderByOrderNo = orderContract.getOrderByOrderNo(message.getOrderNo());
        if (!OpenPageConstant.STATUS_ZERO.equals(orderByOrderNo.getStatus())) {
            return ResponseData.error("select order by orderNo error and orderNo is" + message.getOrderNo());
        }
        OrderInfo orderInfo = orderByOrderNo.getData();
        ResponseData<UserInfo> userInfoResponseData = userIdentityContract.getUserInfo(message.getUserId());
        if (!OpenPageConstant.STATUS_ZERO.equals(userInfoResponseData.getStatus())) {
            return ResponseData.error("select user by userId error and userId is" + message.getUserId());
        }
        UserInfo userInfo = userInfoResponseData.getData();
        WebDriver driver = null;
        try {
            //合规页面
            PocketComplianceBorrowPageDto pageDto = new PocketComplianceBorrowPageDto();
            pageDto.setBorrowCost(orderInfo.getAnnualizedRate().setScale(0, BigDecimal.ROUND_DOWN).toPlainString());
            pageDto.setCardNo(orderInfo.getBankAccount());
            pageDto.setCounterFee("0");
            pageDto.setIdNumber(userInfo.getIdNumber());
//            pageDto.setLoanInterests((orderInfo.getInterest().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN)).toPlainString());
            pageDto.setLoanInterests((new BigDecimal(0.05).multiply(new BigDecimal(orderInfo.getBorrowTime()).setScale(1, BigDecimal.ROUND_DOWN)).multiply(orderInfo.getLoanAmount()).setScale(0,BigDecimal.ROUND_DOWN)).toPlainString());
            pageDto.setLoanMethod("0");
            pageDto.setLoanMoney((orderInfo.getLoanAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN)).toPlainString());
            pageDto.setLoanPurpose("5");
            pageDto.setLoanTerm(orderInfo.getBorrowTime().toString());
            pageDto.setName(UnicodeUtil.gbEncoding(userInfo.getRealName()));
            pageDto.setReturnUrl(pocketConfig.getPocketComplianceBorrowPageRetUrl() +"?orderNo="+message.getOrderNo());
            pageDto.setOrderId(orderInfo.getOrderNo());
            pageDto.setPhone(userInfo.getAccountNumber());
            pageDto.setPlanRepaymentMoney((orderInfo.getRepayTotalAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN)).toPlainString());
            pageDto.setPlanRepaymentMoney(String.valueOf(Integer.valueOf(pageDto.getLoanMoney()) + Integer.valueOf(pageDto.getLoanInterests())));
            pageDto.setRepayerType("1");
            ResponseData<PocketParentResult> borrowPage = pocket2Service.complianceBorrowPage(pageDto);
            if (!OpenPageConstant.STATUS_ZERO.equals(borrowPage.getStatus())) {
                responseData.setMsg("pocket compliance borrow page has exception,and param is:" + pageDto.toString());
                responseData.setStatus(OpenPageConstant.STATUS_ONE);
                return responseData;
            }
            PocketParentResult pageData = borrowPage.getData();
            if (!OpenPageConstant.STATUS_ZERO.equals(pageData.getRetCode())) {
                responseData.setMsg("pocket compliance borrow page error,and param is:" + pageDto.toString());
                responseData.setStatus(OpenPageConstant.STATUS_ONE);
                return responseData;
            }
            String url = MsgCodeRunnable.getPocketResultUrl(borrowPage);
            CrawlerMessage crawlerMessage = new CrawlerMessage();
            crawlerMessage.setUrl(url);
            crawlerMessage.setOrderNo(orderInfo.getOrderNo());
            product.sendMsg(crawlerMessage);

        } catch (Throwable e) {
            logger.error("begin confirm order html,and message is:" + message.toString());
            responseData.setMsg("confirm order html has exception");
            responseData.setStatus(OpenPageConstant.STATUS_ONE);
            orderHandler.orderFailHankdler(orderInfo, Constants.KDLC_CALLBACK_PREFIX);
            if (driver != null) {
                driver.close();
            }
            return responseData;
        }
        return responseData;
    }

}
