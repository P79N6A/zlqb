package com.nyd.user.service.impl;


import com.alibaba.fastjson.JSON;
import com.nyd.member.model.msg.MemberFeeLogMessage;
import com.nyd.user.api.UserAccountContract;
import com.nyd.user.dao.AccountDao;
import com.nyd.user.model.PayByCashCouponInfo;
import com.nyd.user.service.CashCouponPayService;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import com.tasfe.framework.support.model.ResponseData;
import com.tasfe.framework.uid.service.IdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class CashCouponPayServiceImpl implements CashCouponPayService{
    private static Logger logger = LoggerFactory.getLogger(CashCouponPayServiceImpl.class);

    @Autowired
    private UserAccountContract userAccountContract;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;



    /**
     * 用现金券支付评估费处理逻辑
     * @param payByCashCouponInfo
     * @return
     */
    @Override
    public ResponseData handleByCashCoupon(PayByCashCouponInfo payByCashCouponInfo) {
        ResponseData responseData = ResponseData.success();

        if (payByCashCouponInfo == null){
            logger.error("参数为空");
            responseData = ResponseData.error("参数为空");
            return responseData;
        }

        if (StringUtils.isNotBlank(payByCashCouponInfo.getAccountNumber()) && StringUtils.isNotBlank(payByCashCouponInfo.getUserId()) && payByCashCouponInfo.getCouponUseFee() != null){
            MemberFeeLogMessage memberModel = new MemberFeeLogMessage();
            memberModel.setUserId(payByCashCouponInfo.getUserId());            //用户userid
            memberModel.setMobile(payByCashCouponInfo.getAccountNumber());            //手机号
//            memberModel.setMemberFee(payByCashCouponInfo.getRecommendFee());   //原本要支付的评估费

//            String memberId;
//            String orderNo;
           /* try {
                memberId = idGenerator.generatorId(BizCode.MEMBER).toString();
                logger.info("手机号："+payByCashCouponInfo+"的会员id："+memberId);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("memberId生成失败"+payByCashCouponInfo.getUserId());
                Random random = new Random();
                memberId= random.nextInt()+"";
            }
            memberModel.setMemberId(memberId);*/
//            orderNo = payByCashCouponInfo.getUserId() + "tj" + (System.currentTimeMillis() + "").substring(2, 11);
//            logger.info("手机号："+payByCashCouponInfo+"的orderNo："+orderNo);
//            memberModel.setOrderNo(orderNo);                //订单编号
            memberModel.setDebitFlag("1");                                    //成功
            memberModel.setDebitChannel("xjq");                               //扣款渠道
            memberModel.setCouponId(payByCashCouponInfo.getCouponId());       //优惠券id
            memberModel.setCouponUseFee(payByCashCouponInfo.getCouponUseFee());//使用现金券金额
            memberModel.setMemberType("1");   //会员类型
            memberModel.setAppName(payByCashCouponInfo.getAppName());
            logger.info("只利用现金券支付，发送的mq对象："+ JSON.toJSON(memberModel));

            try {
                rabbitmqProducerProxy.convertAndSend("mfee.nyd", memberModel);
            }catch (Exception e){
                e.printStackTrace();
            }

            logger.info("回调成功");
            return responseData;
        }else {
            logger.error("手机号和使用优惠券金额参数为空");
            responseData = ResponseData.error("手机号和使用优惠券金额参数为空");
            return responseData;

        }



    }
}
