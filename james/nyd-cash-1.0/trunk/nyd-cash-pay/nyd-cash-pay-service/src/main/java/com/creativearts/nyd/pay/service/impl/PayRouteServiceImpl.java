package com.creativearts.nyd.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.creativearts.nyd.pay.model.Constant;
import com.creativearts.nyd.pay.model.PayCarryVo;
import com.creativearts.nyd.pay.model.helibao.*;
import com.creativearts.nyd.pay.model.yinshengbao.*;
import com.creativearts.nyd.pay.service.Constants;
import com.creativearts.nyd.pay.service.PayRouteService;
import com.creativearts.nyd.pay.service.helibao.HelibaoPayService;
import com.creativearts.nyd.pay.service.yinshengbao.YsbPayService;
import com.creativearts.nyd.pay.service.yinshengbao.properties.QuickPayYsbProperties;
import com.creativearts.nyd.pay.service.yinshengbao.util.YsbQuickPayService;
import com.nyd.pay.api.enums.WithHoldType;
import com.nyd.user.api.UserAccountContract;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.model.UserDetailInfo;
import com.nyd.user.model.UserInfo;
import com.nyd.user.model.dto.AccountDto;
import com.nyd.zeus.api.BillContract;
import com.nyd.zeus.model.BillInfo;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author  cm
 * 2018/4/20
 **/
@Service
public class PayRouteServiceImpl implements PayRouteService {

    Logger logger = LoggerFactory.getLogger(PayRouteServiceImpl.class);

    @Autowired
    private HelibaoPayService helibaoPayService;
    @Autowired(required = false)
    private BillContract billContract;
    @Autowired(required = false)
    private UserIdentityContract userIdentityContract;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private YsbPayService ysbPayService;

    @Autowired
    private UserAccountContract userAccountContract;

    @Autowired
    private YsbQuickPayService ysbQuickPayService;

    @Autowired
    private QuickPayYsbProperties quickPayYsbProperties;

    @Override
    public ResponseData HlbMemberFeePay(NydHlbVo vo) {
        try {

            logger.info("memberfee" + JSON.toJSONString(vo));



            CreateOrderVo createOrderVo = new CreateOrderVo();
            createOrderVo.setP3_orderId(vo.getUserId() + "_" + (System.currentTimeMillis() + "").substring(2, 11));
            createOrderVo.setP4_timestamp(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));

            createOrderVo.setP8_cardNo(vo.getBankNo());
            createOrderVo.setP11_orderAmount(vo.getAmount());

            //生成携带信息对象
            PayCarryVo payCarryVo = new PayCarryVo();
            payCarryVo.setMemberType(vo.getMemberType());
            payCarryVo.setUserId(vo.getUserId());
            payCarryVo.setProductCode(vo.getProductCode());

            AccountDto accountDto = userAccountContract.getAccount(vo.getUserId()).getData();
            payCarryVo.setMobile(accountDto.getAccountNumber());
            createOrderVo.setP9_phone(accountDto.getAccountNumber());
            redisTemplate.opsForValue().set(Constants.HLB_NYD_CARRAY+vo.getUserId(),JSON.toJSONString(payCarryVo),144,TimeUnit.MINUTES);
            if (StringUtils.isBlank(vo.getIdCard())) {
                UserInfo userInfo = userIdentityContract.getUserInfo(vo.getUserId()).getData();

                logger.info("HlbMemberFeePay userinfo:" + JSON.toJSONString(userInfo));
                createOrderVo.setP5_payerName(userInfo.getRealName());
                createOrderVo.setP7_idCardNo(userInfo.getIdNumber());
            } else {
                createOrderVo.setP5_payerName(vo.getName());
                createOrderVo.setP7_idCardNo(vo.getIdCard());
            }

            if (StringUtils.isBlank(vo.getPlatformType())) {
                createOrderVo.setP15_terminalType("OTHER");
            } else if ("android".equals(vo.getPlatformType().toLowerCase())) {
                createOrderVo.setP15_terminalType("IMEI");
            } else if ("ios".equals(vo.getPlatformType().toLowerCase())) {
                createOrderVo.setP15_terminalType("UUID");
            } else {
                createOrderVo.setP15_terminalType("OTHER");
            }
            logger.info("会员费请求参数:" + JSON.toJSONString(createOrderVo));
            ResponseData r = helibaoPayService.withHold(createOrderVo, WithHoldType.MEMBER_FEE);
            logger.info("会员费 代扣结果" + JSON.toJSONString(r));
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResponseData.error(e.getMessage());
        }
    }

    @Override
    public ResponseData HlbReturnPay(NydHlbVo vo) {
        try {
            CreateOrderVo createOrderVo = new CreateOrderVo();
            AccountDto accountDto = userAccountContract.getAccount(vo.getUserId()).getData();
            logger.info("HlbReturnPay accountDto:" + JSON.toJSONString(accountDto));
            createOrderVo.setP9_phone(accountDto.getAccountNumber());
            if (StringUtils.isBlank(vo.getIdCard())) {

                UserInfo userInfo = userIdentityContract.getUserInfo(vo.getUserId()).getData();

                logger.info("HlbReturnPay userinfo:" + JSON.toJSONString(userInfo));
                createOrderVo.setP3_orderId(vo.getBillNo() + "-" + System.currentTimeMillis() + "-z");
                createOrderVo.setP4_timestamp(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
                createOrderVo.setP5_payerName(userInfo.getRealName());
                createOrderVo.setP7_idCardNo(userInfo.getIdNumber());
                createOrderVo.setP8_cardNo(vo.getBankNo());
                createOrderVo.setP11_orderAmount(vo.getAmount());
            } else {
                boolean flag = false;
                ResponseData<BillInfo> responseData = billContract.getBillInfo(vo.getBillNo());
                if (responseData != null && "0".equals(responseData.getStatus())) {
                    BillInfo billInfo = responseData.getData();
                    if (billInfo != null && StringUtils.isNotBlank(billInfo.getUserId())) {


                        //根据此userId查询是否是本人
                        ResponseData<UserDetailInfo> responseData1 = userIdentityContract.getUserDetailInfo(billInfo.getUserId());
                        if (responseData1 != null && "0".equals(responseData1.getStatus())) {
                            UserDetailInfo userDetailInfo = responseData1.getData();
                            if (userDetailInfo != null && vo != null) {
                                if (vo.getName().equals(userDetailInfo.getRealName()) && vo.getIdCard().equals(userDetailInfo.getIdNumber())) {
                                    flag = true;
                                }
                            }
                        }
                    }
                }
                if (!flag) {
                    return ResponseData.error("请核对身份信息！");
                }

                createOrderVo.setP3_orderId(vo.getBillNo() + "-" + System.currentTimeMillis() + "-k");
                createOrderVo.setP4_timestamp(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
                createOrderVo.setP5_payerName(vo.getName());
                createOrderVo.setP7_idCardNo(vo.getIdCard());
                createOrderVo.setP8_cardNo(vo.getBankNo());
                createOrderVo.setP11_orderAmount(vo.getAmount());

                try {
                    redisTemplate.opsForValue().set(Constants.HLB_QUICK_PAY + vo.getBillNo(), vo.getName() + "-" + vo.getIdCard() + "-" + vo.getBankNo(), 1440, TimeUnit.MINUTES);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            logger.info("产生的createOrderVo为:" + JSON.toJSONString(createOrderVo));

            if (StringUtils.isBlank(vo.getPlatformType())) {
                createOrderVo.setP15_terminalType("OTHER");
            } else if ("android".equals(vo.getPlatformType().toLowerCase())) {
                createOrderVo.setP15_terminalType("IMEI");
            } else if ("ios".equals(vo.getPlatformType().toLowerCase())) {
                createOrderVo.setP15_terminalType("UUID");
            } else {
                createOrderVo.setP15_terminalType("OTHER");
            }
            ResponseData r = helibaoPayService.withHold(createOrderVo, WithHoldType.OWN_REPAY);
            logger.info("会员费 代扣结果" + JSON.toJSONString(r));
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResponseData.error(e.getMessage());
        }
    }

    @Override
    public ResponseData YsbMemberFeePay(NydYsbVo vo) {
        //生成携带信息对象
        PayCarryVo payCarryVo = new PayCarryVo();
        payCarryVo.setMemberType(vo.getMemberType());
        payCarryVo.setUserId(vo.getUserId());
        payCarryVo.setProductCode(vo.getProductCode());

        AccountDto accountDto = userAccountContract.getAccount(vo.getUserId()).getData();
        payCarryVo.setMobile(accountDto.getAccountNumber());
        //优惠券id(新增优惠券id**********************)
        if (StringUtils.isNotBlank(vo.getCouponId())){
            payCarryVo.setCouponId(vo.getCouponId());
        }


        //现金券使用金额
        if (vo.getCouponUseFee() !=  null){
            payCarryVo.setCouponUseFee(vo.getCouponUseFee());
        }
        logger.info("银生宝支付会员费，携带信息对象："+JSON.toJSON(payCarryVo));

        redisTemplate.opsForValue().set(Constants.YSB_NYD_CARRAY+vo.getUserId(),JSON.toJSONString(payCarryVo),144,TimeUnit.MINUTES);

        return ysbPayService.signAndDaikou(vo, Constant.MEMBER_TYPE);
    }

    @Override
    public ResponseData YsbReturnPay(NydYsbVo vo) {
        return ysbPayService.signAndDaikou(vo,Constant.KPAY_TYPE);
    }


    /**
     * 支付会员费
     * @param vo
     * @return
     */
    @Override
    public ResponseData PayHlbMemberFee(NydHlbVo vo) {
        logger.info("进入合利宝扣会员费+++++++++++++++++++++++++++++");
        try {
            logger.info("memberfee：" + JSON.toJSONString(vo));
            FirstPayCreateOrderVo firstPayCreateOrderVo = new FirstPayCreateOrderVo();
            firstPayCreateOrderVo.setP3_userId(vo.getUserId());
            String orderId = vo.getUserId() + "_" + (System.currentTimeMillis() + "").substring(2, 11);
            logger.info("会员费的orderId："+orderId);
            firstPayCreateOrderVo.setP4_orderId(orderId);
            firstPayCreateOrderVo.setP5_timestamp(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
            firstPayCreateOrderVo.setP9_cardNo(vo.getBankNo());
            firstPayCreateOrderVo.setP15_orderAmount(vo.getAmount());
            firstPayCreateOrderVo.setP13_phone(vo.getPhone());

            //生成携带信息对象
            PayCarryVo payCarryVo = new PayCarryVo();
//            payCarryVo.setMemberType(vo.getMemberType());
//            payCarryVo.setUserId(vo.getUserId());
//            payCarryVo.setProductCode(vo.getProductCode());

//            AccountDto accountDto = userAccountContract.getAccount(vo.getUserId()).getData();
//            payCarryVo.setMobile(accountDto.getAccountNumber());
            payCarryVo.setMobile(vo.getPhone());

            //优惠券id(新增********************************)
//            if (StringUtils.isNotBlank(vo.getCouponId())){
//                payCarryVo.setCouponId(vo.getCouponId());
//            }

            //现金券使用金额
//            if (vo.getCouponUseFee() != null){
//                payCarryVo.setCouponUseFee(vo.getCouponUseFee());
//            }
            BeanUtils.copyProperties(vo,payCarryVo);
            logger.info("支付会员费,所携带信息对象:"+JSON.toJSON(payCarryVo));

            redisTemplate.opsForValue().set(Constants.HLB_NYD_CARRAY+vo.getUserId(),JSON.toJSONString(payCarryVo),144,TimeUnit.MINUTES);
            if (StringUtils.isBlank(vo.getIdCard())) {
                UserInfo userInfo = userIdentityContract.getUserInfo(vo.getUserId()).getData();
                logger.info("合利宝支付会员费 userinfo:" + JSON.toJSONString(userInfo));
                firstPayCreateOrderVo.setP6_payerName(userInfo.getRealName());
                firstPayCreateOrderVo.setP8_idCardNo(userInfo.getIdNumber());
            } else {
                firstPayCreateOrderVo.setP6_payerName(vo.getName());
                firstPayCreateOrderVo.setP8_idCardNo(vo.getIdCard());
            }

            if (StringUtils.isBlank(vo.getPlatformType())) {
                firstPayCreateOrderVo.setP18_terminalType("OTHER");
            } else if ("android".equals(vo.getPlatformType().toLowerCase())) {
                firstPayCreateOrderVo.setP18_terminalType("IMEI");
            } else if ("ios".equals(vo.getPlatformType().toLowerCase())) {
                firstPayCreateOrderVo.setP18_terminalType("UUID");
            } else {
                firstPayCreateOrderVo.setP18_terminalType("OTHER");
            }
            logger.info("会员费请求参数:" + JSON.toJSONString(firstPayCreateOrderVo));

            try {
                ResponseData r = helibaoPayService.firstPayType(firstPayCreateOrderVo, WithHoldType.MEMBER_FEE);
                logger.info("会员费代扣结果下单：" + JSON.toJSONString(r));
                FirstPayCreateOrderResponseVo data = (FirstPayCreateOrderResponseVo) r.getData();
                logger.info("会员费代扣结果Data:"+JSON.toJSONString(data));
                if (data == null){
                    return  r;
                }

                if ("0000".equals(data.getRt2_retCode())){
                    /**
                     * 给用户发送支付短信
                     */
                    SendValidateCodeVo sendValidateCodeVo = new SendValidateCodeVo();
                    sendValidateCodeVo.setP1_bizType("QuickPaySendValidateCode");
                    sendValidateCodeVo.setP5_phone(vo.getPhone());
                    sendValidateCodeVo.setP3_orderId(orderId);

                    try {
                        ResponseData responseData = helibaoPayService.firstPayMessage(sendValidateCodeVo);
                        logger.info("支付会员费，给用户发送短信成功");
                        logger.info("支付会员费，短信请求返回的响应信息："+JSON.toJSONString(r.getData()));
                        return responseData;
                    }catch (Exception e){
                        e.printStackTrace();
                        logger.error(e.getMessage());
                        return ResponseData.error(e.getMessage());
                    }
                }else {
                    return r;
                }
            }catch (Exception e){
                e.printStackTrace();
                logger.error(e.getMessage());
                return ResponseData.error(e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResponseData.error(e.getMessage());
        }
    }

    /**
     * 通过合利宝还款
     * @param vo
     * @return
     */
    @Override
    public ResponseData PayHlbReturn(NydHlbVo vo) {
        logger.info("进入合利宝还款++++++++++++++++++++++++");
        String orderId = "";
        try {
            FirstPayCreateOrderVo firstPayCreateOrderVo = new FirstPayCreateOrderVo();
            if (StringUtils.isNotBlank(vo.getIdCard())) {
                UserInfo userInfo = userIdentityContract.getUserInfo(vo.getUserId()).getData();
                logger.info("合利宝还款 userinfo:" + JSON.toJSONString(userInfo));

                orderId = vo.getBillNo() + "-" + System.currentTimeMillis() + "-z";
                logger.info("还款的orderId:"+orderId);

                firstPayCreateOrderVo.setP3_userId(vo.getUserId());
                firstPayCreateOrderVo.setP4_orderId(orderId);
                firstPayCreateOrderVo.setP5_timestamp(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
//                firstPayCreateOrderVo.setP6_payerName(userInfo.getRealName());
//                firstPayCreateOrderVo.setP8_idCardNo(userInfo.getIdNumber());
                firstPayCreateOrderVo.setP6_payerName(vo.getName());
                firstPayCreateOrderVo.setP8_idCardNo(vo.getIdCard());
                firstPayCreateOrderVo.setP9_cardNo(vo.getBankNo());
                firstPayCreateOrderVo.setP15_orderAmount(vo.getAmount());
                firstPayCreateOrderVo.setP13_phone(vo.getPhone());

                try {
                    redisTemplate.opsForValue().set(Constants.HLB_QUICK_PAY + vo.getBillNo(), vo.getName() + "-" + vo.getIdCard() + "-" + vo.getBankNo() + "-" + vo.getAppName(), 1440, TimeUnit.MINUTES);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
//            else {
//                boolean flag = false;
//                ResponseData<BillInfo> responseData = billContract.getBillInfo(vo.getBillNo());
//                if (responseData != null && "0".equals(responseData.getStatus())) {
//                    BillInfo billInfo = responseData.getData();
//                    logger.info("账单信息："+JSON.toJSONString(billInfo));
//
//                    if (billInfo != null && StringUtils.isNotBlank(billInfo.getUserId())) {
//                        //根据此userId查询是否是本人
//                        ResponseData<UserDetailInfo> responseData1 = userIdentityContract.getUserDetailInfo(billInfo.getUserId());
//                        if (responseData1 != null && "0".equals(responseData1.getStatus())) {
//                            UserDetailInfo userDetailInfo = responseData1.getData();
//                            if (userDetailInfo != null && vo != null) {
//                                if (vo.getName().equals(userDetailInfo.getRealName()) && vo.getIdCard().equals(userDetailInfo.getIdNumber())) {
//                                    flag = true;
//                                }
//                            }
//                        }
//                    }
//
//                }
//                if (!flag) {
//                    return ResponseData.error("请核对身份信息！");
//                }
////                vo.getBillNo() + "-" + System.currentTimeMillis() + "-k"
//                firstPayCreateOrderVo.setP4_orderId(orderId);
//                firstPayCreateOrderVo.setP6_payerName(vo.getName());
//                firstPayCreateOrderVo.setP8_idCardNo(vo.getIdCard());
//                firstPayCreateOrderVo.setP9_cardNo(vo.getBankNo());
//                firstPayCreateOrderVo.setP15_orderAmount(vo.getAmount());
//
//                try {
//                    redisTemplate.opsForValue().set(Constants.HLB_QUICK_PAY + vo.getBillNo(), vo.getName() + "-" + vo.getIdCard() + "-" + vo.getBankNo(), 1440, TimeUnit.MINUTES);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }

            if (StringUtils.isBlank(vo.getPlatformType())) {
                firstPayCreateOrderVo.setP18_terminalType("OTHER");
            } else if ("android".equals(vo.getPlatformType().toLowerCase())) {
                firstPayCreateOrderVo.setP18_terminalType("IMEI");
            } else if ("ios".equals(vo.getPlatformType().toLowerCase())) {
                firstPayCreateOrderVo.setP18_terminalType("UUID");
            } else {
                firstPayCreateOrderVo.setP18_terminalType("OTHER");
            }

            logger.info("还款请求参数为:" + JSON.toJSONString(firstPayCreateOrderVo));

            try {
                ResponseData r = helibaoPayService.firstPayType(firstPayCreateOrderVo, WithHoldType.OWN_REPAY);
                logger.info("还款代扣结果：" + JSON.toJSONString(r));
                FirstPayCreateOrderResponseVo data = (FirstPayCreateOrderResponseVo) r.getData();
                logger.info("还款代扣结果Data:"+JSON.toJSONString(data));
                if (data == null){
                    return  r;
                }

                if ("0000".equals(data.getRt2_retCode())){
                    /**
                     * 给用户发送支付短信
                     */
                    SendValidateCodeVo sendValidateCodeVo = new SendValidateCodeVo();
                    sendValidateCodeVo.setP1_bizType("QuickPaySendValidateCode");
                    sendValidateCodeVo.setP5_phone(vo.getPhone());
                    sendValidateCodeVo.setP3_orderId(orderId);
                    try {
                        ResponseData responseData = helibaoPayService.firstPayMessage(sendValidateCodeVo);
                        logger.info("还款时，给用户发送短信成功");
                        logger.info("还款时，短信请求返回的响应信息："+JSON.toJSONString(r.getData()));
                        return responseData;
                    }catch (Exception e){
                        e.printStackTrace();
                        logger.error(e.getMessage());
                        return ResponseData.error(e.getMessage());
                    }
                }else {
                    return r;
                }

            }catch (Exception e){
                e.printStackTrace();
                logger.error(e.getMessage());
                return ResponseData.error(e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResponseData.error(e.getMessage());
        }
    }

    /**
     * 通过合利宝充值现金券
     * @param vo
     * @return
     */
    @Override
    public ResponseData PayHlbCashCoupon(NydHlbVo vo) {
        logger.info("通过合利宝充值现金券+++++++++++++++++++++++++++++");
        logger.info("充值现金券请求参数：" + JSON.toJSONString(vo));
        try {
            FirstPayCreateOrderVo firstPayCreateOrderVo = new FirstPayCreateOrderVo();
            firstPayCreateOrderVo.setP3_userId(vo.getUserId());
            String orderId = vo.getUserId() + "xj" + (System.currentTimeMillis() + "").substring(2, 11);
            logger.info("现金券的orderId："+orderId);
            firstPayCreateOrderVo.setP4_orderId(orderId);
            firstPayCreateOrderVo.setP5_timestamp(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
            firstPayCreateOrderVo.setP9_cardNo(vo.getBankNo());
            firstPayCreateOrderVo.setP15_orderAmount(vo.getAmount());
            firstPayCreateOrderVo.setP13_phone(vo.getPhone());

            //存入redis里面的对象
            PayCarryVo payCarryVo = new PayCarryVo();
//            payCarryVo.setUserId(vo.getUserId());
//            payCarryVo.setCashId(vo.getCashId());    //现金券id（前台页面传过来）
//            payCarryVo.setProductCode(vo.getProductCode());
            BeanUtils.copyProperties(vo,payCarryVo);
            payCarryVo.setMobile(vo.getPhone());
            redisTemplate.opsForValue().set(Constants.HLB_NYD_CARRAY+vo.getUserId(),JSON.toJSONString(payCarryVo),144,TimeUnit.MINUTES);

            if (StringUtils.isBlank(vo.getIdCard())) {
                UserInfo userInfo = userIdentityContract.getUserInfo(vo.getUserId()).getData();
                logger.info("合利宝支付现金券 userinfo:" + JSON.toJSONString(userInfo));
                firstPayCreateOrderVo.setP6_payerName(userInfo.getRealName());
                firstPayCreateOrderVo.setP8_idCardNo(userInfo.getIdNumber());
            } else {
                firstPayCreateOrderVo.setP6_payerName(vo.getName());
                firstPayCreateOrderVo.setP8_idCardNo(vo.getIdCard());
            }

            if (StringUtils.isBlank(vo.getPlatformType())) {
                firstPayCreateOrderVo.setP18_terminalType("OTHER");
            } else if ("android".equals(vo.getPlatformType().toLowerCase())) {
                firstPayCreateOrderVo.setP18_terminalType("IMEI");
            } else if ("ios".equals(vo.getPlatformType().toLowerCase())) {
                firstPayCreateOrderVo.setP18_terminalType("UUID");
            } else {
                firstPayCreateOrderVo.setP18_terminalType("OTHER");
            }
            logger.info("利用合利宝支付现金券，请求参数:" + JSON.toJSONString(firstPayCreateOrderVo));

            try {
                ResponseData r = helibaoPayService.firstPayType(firstPayCreateOrderVo, WithHoldType.CASH_COUPON);
                logger.info("支付现金券代扣结果下单：" + JSON.toJSONString(r));
                FirstPayCreateOrderResponseVo data = (FirstPayCreateOrderResponseVo) r.getData();
                logger.info("支付现金券返回结果Data:"+JSON.toJSONString(data));
                if (data == null){
                    return  r;
                }

                if ("0000".equals(data.getRt2_retCode())){
                    /**
                     * 给用户发送支付短信
                     */
                    SendValidateCodeVo sendValidateCodeVo = new SendValidateCodeVo();
                    sendValidateCodeVo.setP1_bizType("QuickPaySendValidateCode");
                    sendValidateCodeVo.setP5_phone(vo.getPhone());
                    sendValidateCodeVo.setP3_orderId(orderId);

                    try {
                        ResponseData responseData = helibaoPayService.firstPayMessage(sendValidateCodeVo);
                        logger.info("支付现金券，给用户发送短信成功");
                        logger.info("支付现金券，短信请求返回的响应信息："+JSON.toJSONString(r.getData()));
                        return responseData;
                    }catch (Exception e){
                        e.printStackTrace();
                        logger.error(e.getMessage());
                        return ResponseData.error(e.getMessage());
                    }
                }else {
                    return r;
                }
            }catch (Exception e){
                e.printStackTrace();
                logger.error(e.getMessage());
                return ResponseData.error(e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResponseData.error(e.getMessage());
        }
    }


    /**
     * 通过银生宝充值现金券
     * @param nydYsbVo
     * @return
     */
    @Override
    public ResponseData YsbCashCouponPay(NydYsbVo nydYsbVo) {
        //生成携带信息对象(存入redis里面的对象)
        PayCarryVo payCarryVo = new PayCarryVo();
        payCarryVo.setMemberType(nydYsbVo.getMemberType());
        payCarryVo.setCashId(nydYsbVo.getCashId());
        payCarryVo.setUserId(nydYsbVo.getUserId());
        //payCarryVo.setProductCode(nydYsbVo.getProductCode());

        AccountDto accountDto = userAccountContract.getAccount(nydYsbVo.getUserId()).getData();
        payCarryVo.setMobile(accountDto.getAccountNumber());
        redisTemplate.opsForValue().set(Constants.YSB_NYD_CARRAY+nydYsbVo.getUserId(),JSON.toJSONString(payCarryVo),144,TimeUnit.MINUTES);

        return ysbPayService.signAndDaikou(nydYsbVo, Constant.COUPON_TYPE);
    }

    /**
     * 银生宝快捷支付支付评估费
     * @param nydYsbVo
     * @return
     */
    @Override
    public ResponseData YsbQuickPayMemberFee(NydYsbVo nydYsbVo) {

        logger.info("进入银生宝快捷支付支付评估费");
        logger.info("ysb qucik pay member fee request param:"+JSON.toJSON(nydYsbVo));
        //交易订单号
        String orderNo = nydYsbVo.getUserId() + "M" + (System.currentTimeMillis() + "").substring(2, 11);
        logger.info("支付评估费orderNo："+orderNo);
        //交易用户 ID
        String customerId = nydYsbVo.getUserId();
        logger.info("支付评估费customerId："+customerId);
        try {
            //发送短信对象：
            YsbSendMessageVo ysbSendMessageVo = new YsbSendMessageVo();
            //用户ID
            ysbSendMessageVo.setCustomerId(customerId);
            //订单号
            ysbSendMessageVo.setOrderNo(orderNo);
            //目的
            ysbSendMessageVo.setPurpose("支付信用评估费");
            //金额
            ysbSendMessageVo.setAmount(nydYsbVo.getAmount().toString());
            //商品简称
            ysbSendMessageVo.setCommodityName("商家消费");
            //业务种类
            ysbSendMessageVo.setBusinessType("100006");
            //姓名
            ysbSendMessageVo.setName(nydYsbVo.getName());
            //身份证号
            ysbSendMessageVo.setIdCardNo(nydYsbVo.getIdCard());
            //银行卡号(用户自已输入)
            ysbSendMessageVo.setCardNo(nydYsbVo.getBankNo());
            //手机号(用户自已输入)
            ysbSendMessageVo.setPhoneNo(nydYsbVo.getMobile());

            /**
             * 生成携带信息对象
             */
            PayCarryVo payCarryVo = new PayCarryVo();
            payCarryVo.setMemberType(nydYsbVo.getMemberType());
            payCarryVo.setUserId(nydYsbVo.getUserId());
            payCarryVo.setProductCode(nydYsbVo.getProductCode());
//            payCarryVo.setMobile(nydYsbVo.getMobile());
            AccountDto accountDto = userAccountContract.getAccount(nydYsbVo.getUserId()).getData();
            payCarryVo.setMobile(accountDto.getAccountNumber());
            payCarryVo.setAppName(nydYsbVo.getAppName());

            //优惠券id
            if (StringUtils.isNotBlank(nydYsbVo.getCouponId())){
                payCarryVo.setCouponId(nydYsbVo.getCouponId());
            }

            //现金券使用金额
            if (nydYsbVo.getCouponUseFee() !=  null){
                payCarryVo.setCouponUseFee(nydYsbVo.getCouponUseFee());
            }
            logger.info("银生宝快捷支付评估费，携带信息对象："+JSON.toJSON(payCarryVo));

            redisTemplate.opsForValue().set(Constants.YSB_NYD_CARRAY+nydYsbVo.getUserId(),JSON.toJSONString(payCarryVo),144,TimeUnit.MINUTES);

            /**
             * 获取支付验证码
             */
            logger.info("ysb获取支付评估费验证码请求参数："+JSON.toJSON(ysbSendMessageVo));
            String token = "";
            try {
                ResponseData messageData = ysbQuickPayService.ysbSendMessage(ysbSendMessageVo);
                logger.info("评估费messageData:"+JSON.toJSON(messageData));
                if ("0".equals(messageData.getStatus())){
                    ResponseData responseData = ResponseData.success();
                    logger.info("获取支付评估费验证码成功");
                    MessageReturn messageReturn = (MessageReturn) messageData.getData();
                    logger.info("支付评估费messageReturn:"+JSON.toJSON(messageReturn));
                    responseData.setData(messageReturn);
                    return responseData;
                }else {
                    logger.info("获取支付评估费验证码失败");
                    MessageReturn messageReturn = (MessageReturn) messageData.getData();
                    logger.info("支付评估费messageReturn:"+JSON.toJSON(messageReturn));
                    return ResponseData.error(messageData.getMsg());
                }
            }catch (Exception e){
                e.printStackTrace();
                logger.error(e.getMessage());
                return ResponseData.error("请求超时,请重新获取验证码");
            }

        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResponseData.error("服务器开小差，请稍后再试");
        }

    }

    /**
     *银生宝快捷支付还款
     * @param nydYsbVo
     * @return
     */
    @Override
    public ResponseData YsbQuickPayReturnPay(NydYsbVo nydYsbVo) {

        logger.info("进入银生宝快捷支付还款");
        logger.info("ysb qucik pay repay request param:"+JSON.toJSON(nydYsbVo));
        String orderNo = nydYsbVo.getBillNo()+"P"+(System.currentTimeMillis()+"").substring(2,11)+"k";
        logger.info("ysb快捷还款orderNo："+orderNo);
        //交易用户 ID
        String customerId = nydYsbVo.getUserId();
        logger.info("ysb快捷还款customerId："+customerId);
        try {
            //发送短信对象：
            YsbSendMessageVo ysbSendMessageVo = new YsbSendMessageVo();
            //用户ID
            ysbSendMessageVo.setCustomerId(customerId);
            //订单号
            ysbSendMessageVo.setOrderNo(orderNo);
            //目的
            ysbSendMessageVo.setPurpose("还款");
            //金额
            ysbSendMessageVo.setAmount(nydYsbVo.getAmount().toString());
            //商品简称
            ysbSendMessageVo.setCommodityName("商家消费");
            //业务种类
            ysbSendMessageVo.setBusinessType("100006");
            //姓名
            ysbSendMessageVo.setName(nydYsbVo.getName());
            //身份证号
            ysbSendMessageVo.setIdCardNo(nydYsbVo.getIdCard());
            //银行卡号(用户自已输入)
            ysbSendMessageVo.setCardNo(nydYsbVo.getBankNo());
            //手机号(用户自已输入)
            ysbSendMessageVo.setPhoneNo(nydYsbVo.getMobile());
            
            
            //生成携带信息对象(存入redis里面的对象)
            PayCarryVo payCarryVo = new PayCarryVo();
            payCarryVo.setMemberType(nydYsbVo.getMemberType());
            payCarryVo.setCashId(nydYsbVo.getCashId());
            payCarryVo.setUserId(nydYsbVo.getUserId());
            payCarryVo.setAppName(nydYsbVo.getAppName());
            //payCarryVo.setProductCode(nydYsbVo.getProductCode());

            AccountDto accountDto = userAccountContract.getAccount(nydYsbVo.getUserId()).getData();
            payCarryVo.setMobile(accountDto.getAccountNumber());
            logger.info("银生宝快捷支付还款，携带信息对象："+JSON.toJSON(payCarryVo));

            redisTemplate.opsForValue().set(Constants.YSB_NYD_CARRAY+"ZC"+nydYsbVo.getBillNo(),JSON.toJSONString(payCarryVo),144,TimeUnit.MINUTES);
            
            /**
             * 获取支付验证码
             */
            logger.info("ysb获取还款验证码请求参数："+JSON.toJSON(ysbSendMessageVo));
            String token = "";
            try {
                ResponseData messageData = ysbQuickPayService.ysbSendMessage(ysbSendMessageVo);
                logger.info("还款messageData："+JSON.toJSON(messageData));
                if ("0".equals(messageData.getStatus())){
                    ResponseData responseData = ResponseData.success();
                    logger.info("获取还款验证码成功");
                    MessageReturn messageReturn = (MessageReturn) messageData.getData();
                    logger.info("还款messageReturn:"+JSON.toJSON(messageReturn));
                    responseData.setData(messageReturn);
                    return responseData;
                }else {
                    logger.info("获取还款验证码失败");
                    MessageReturn messageReturn = (MessageReturn) messageData.getData();
                    logger.info("还款messageReturn:"+JSON.toJSON(messageReturn));
                    return ResponseData.error(messageData.getMsg());
                }
            }catch (Exception e){
                e.printStackTrace();
                logger.error(e.getMessage());
                return ResponseData.error("请求超时,请重新获取验证码");
            }

        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResponseData.error("服务器开小差，请稍后再试");
        }


    }

    /**
     *银生宝快捷充值现金券
     * @param nydYsbVo
     * @return
     */
    @Override
    public ResponseData YsbQuickPayCouponPay(NydYsbVo nydYsbVo) {

        logger.info("进入银生宝快捷支付充值现金券");
        logger.info("ysb qucikpay pay CashCoupon request param:"+JSON.toJSON(nydYsbVo));
        String orderNo = nydYsbVo.getUserId()+"xj"+(System.currentTimeMillis()+"").substring(2,11);
        logger.info("充值现金券orderNo："+orderNo);
        //交易用户 ID
        String customerId = nydYsbVo.getUserId();
        logger.info("充值现金券customerId："+customerId);
        try {
            //发送短信对象：
            YsbSendMessageVo ysbSendMessageVo = new YsbSendMessageVo();
            //用户ID
            ysbSendMessageVo.setCustomerId(customerId);
            //订单号
            ysbSendMessageVo.setOrderNo(orderNo);
            //目的
            ysbSendMessageVo.setPurpose("充值");
            //金额
            ysbSendMessageVo.setAmount(nydYsbVo.getAmount().toString());
            //商品简称
            ysbSendMessageVo.setCommodityName("商家消费");
            //业务种类
            ysbSendMessageVo.setBusinessType("100006");
            //姓名
            ysbSendMessageVo.setName(nydYsbVo.getName());
            //身份证号
            ysbSendMessageVo.setIdCardNo(nydYsbVo.getIdCard());
            //银行卡号(用户自已输入)
            ysbSendMessageVo.setCardNo(nydYsbVo.getBankNo());
            //手机号(用户自已输入)
            ysbSendMessageVo.setPhoneNo(nydYsbVo.getMobile());

            //生成携带信息对象(存入redis里面的对象)
            PayCarryVo payCarryVo = new PayCarryVo();
            payCarryVo.setMemberType(nydYsbVo.getMemberType());
            payCarryVo.setCashId(nydYsbVo.getCashId());
            payCarryVo.setUserId(nydYsbVo.getUserId());
            payCarryVo.setAppName(nydYsbVo.getAppName());
            //payCarryVo.setProductCode(nydYsbVo.getProductCode());

            AccountDto accountDto = userAccountContract.getAccount(nydYsbVo.getUserId()).getData();
            payCarryVo.setMobile(accountDto.getAccountNumber());
            logger.info("银生宝快捷支付充值现金券验，携带信息对象："+JSON.toJSON(payCarryVo));

            redisTemplate.opsForValue().set(Constants.YSB_NYD_CARRAY+nydYsbVo.getUserId(),JSON.toJSONString(payCarryVo),144,TimeUnit.MINUTES);

            /**
             * 获取支付验证码
             */
            logger.info("ysb获取充值现金券验证码请求参数："+JSON.toJSON(ysbSendMessageVo));
            String token = "";
            try {
                ResponseData messageData = ysbQuickPayService.ysbSendMessage(ysbSendMessageVo);
                logger.info("充值messageData："+JSON.toJSON(messageData));
                if ("0".equals(messageData.getStatus())){
                    ResponseData responseData = ResponseData.success();
                    logger.info("获取充值现金券验还款验证码成功");
                    MessageReturn messageReturn = (MessageReturn) messageData.getData();
                    logger.info("充值现金券验messageReturn:"+JSON.toJSON(messageReturn));
                    responseData.setData(messageReturn);
                    return responseData;
                }else {
                    logger.info("获取充值现金券验还款验证码失败");
                    MessageReturn messageReturn = (MessageReturn) messageData.getData();
                    logger.info("充值现金券验messageReturn:"+JSON.toJSON(messageReturn));
                    return ResponseData.error(messageData.getMsg());
                }
            }catch (Exception e){
                e.printStackTrace();
                logger.error(e.getMessage());
                return ResponseData.error("请求超时,请重新获取验证码");
            }

        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResponseData.error("服务器开小差，请稍后再试");
        }



    }

    /**
     * 通过合利宝空中金融还款
     * @param vo
     * @return
     */
    @Override
    public ResponseData PayHlbKzjrRepay(NydHlbVo vo) {
        logger.info("进入合利宝空中金融还款++++++++++++++++++++++++");
        String orderId = "";
        try {
            FirstPayCreateOrderVo firstPayCreateOrderVo = new FirstPayCreateOrderVo();
            if (StringUtils.isNotBlank(vo.getIdCard())) {
                UserInfo userInfo = userIdentityContract.getUserInfo(vo.getUserId()).getData();
                logger.info("合利宝还款人信息:" + JSON.toJSONString(userInfo));

                orderId = vo.getBillNo() + "-" + System.currentTimeMillis() + "-kzjr";
//                orderId = vo.getBillNo() + "-" + vo.getAssetCode() + "-kzjr";
                logger.info("kzjr还款的orderId:"+orderId);

                firstPayCreateOrderVo.setP3_userId(vo.getUserId());
                firstPayCreateOrderVo.setP4_orderId(orderId);
                firstPayCreateOrderVo.setP5_timestamp(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
                firstPayCreateOrderVo.setP6_payerName(vo.getName());
                firstPayCreateOrderVo.setP8_idCardNo(vo.getIdCard());
                firstPayCreateOrderVo.setP9_cardNo(vo.getBankNo());
                firstPayCreateOrderVo.setP15_orderAmount(vo.getAmount());
                firstPayCreateOrderVo.setP13_phone(vo.getPhone());

                try {
                    redisTemplate.opsForValue().set(Constants.HLB_QUICK_PAY + vo.getBillNo(), vo.getName() + "-" + vo.getIdCard() + "-" + vo.getBankNo() + "-" + vo.getAppName(), 1440, TimeUnit.MINUTES);
                } catch (Exception e) {
                    logger.error("空中金融还款存入Redis出错",e);
                    e.printStackTrace();
                }
            }

            if (StringUtils.isBlank(vo.getPlatformType())) {
                firstPayCreateOrderVo.setP18_terminalType("OTHER");
            } else if ("android".equals(vo.getPlatformType().toLowerCase())) {
                firstPayCreateOrderVo.setP18_terminalType("IMEI");
            } else if ("ios".equals(vo.getPlatformType().toLowerCase())) {
                firstPayCreateOrderVo.setP18_terminalType("UUID");
            } else {
                firstPayCreateOrderVo.setP18_terminalType("OTHER");
            }

            logger.info("空中金融还款请求参数为:" + JSON.toJSONString(firstPayCreateOrderVo));

            try {
                ResponseData r = helibaoPayService.firstPayType(firstPayCreateOrderVo, WithHoldType.KZJR_REPAY_YMT);
                logger.info("kzjr还款结果:" + JSON.toJSONString(r));
                FirstPayCreateOrderResponseVo data = (FirstPayCreateOrderResponseVo) r.getData();
                logger.info("kzjr还款结果Data:"+JSON.toJSONString(data));
                if (data == null){
                    return  r;
                }

                if ("0000".equals(data.getRt2_retCode())){
                    /**
                     * 给用户发送支付短信
                     */
                    SendValidateCodeVo sendValidateCodeVo = new SendValidateCodeVo();
                    sendValidateCodeVo.setP1_bizType("QuickPaySendValidateCode");
                    sendValidateCodeVo.setP5_phone(vo.getPhone());
                    sendValidateCodeVo.setP3_orderId(orderId);
                    try {
                        ResponseData responseData = helibaoPayService.firstPayMessage(sendValidateCodeVo);
                        logger.info("空中金融还款,给用户发送短信成功");
                        logger.info("空中金融还款,短信请求返回的响应信息："+JSON.toJSONString(r.getData()));
                        return responseData;
                    }catch (Exception e){
                        e.printStackTrace();
                        logger.error("发送短信出错",e);
                        return ResponseData.error("服务器开小差,请稍后再试");
                    }
                }else {
                    return r;
                }

            }catch (Exception e){
                e.printStackTrace();
                logger.error("创建订单出错",e);
                return ResponseData.error("服务器开小差,请稍后再试");
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("空中金融还款出错",e);
            return ResponseData.error("服务器开小差,请稍后再试");
        }
    }
}
