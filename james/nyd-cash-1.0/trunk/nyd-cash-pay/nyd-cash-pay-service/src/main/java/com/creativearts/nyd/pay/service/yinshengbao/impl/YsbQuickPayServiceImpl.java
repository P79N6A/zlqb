package com.creativearts.nyd.pay.service.yinshengbao.impl;


import com.alibaba.fastjson.JSON;
import com.creativearts.nyd.pay.model.MemberCarryResp;
import com.creativearts.nyd.pay.model.PayCarryVo;
import com.creativearts.nyd.pay.model.RepayMessage;
import com.creativearts.nyd.pay.model.RepayType;
import com.creativearts.nyd.pay.model.enums.PayStatus;
import com.creativearts.nyd.pay.model.yinshengbao.*;
import com.creativearts.nyd.pay.service.Constants;
import com.creativearts.nyd.pay.service.RedisProcessService;
import com.creativearts.nyd.pay.service.helibao.util.DingdingUtil;
import com.creativearts.nyd.pay.service.log.LoggerUtils;
import com.creativearts.nyd.pay.service.yinshengbao.properties.QuickPayYsbProperties;
import com.creativearts.nyd.pay.service.yinshengbao.util.Md5Encrypt;
import com.creativearts.nyd.pay.service.yinshengbao.util.YsbQuickPayService;
import com.nyd.member.model.msg.MemberFeeLogMessage;
import com.nyd.msg.model.SmsRequest;
import com.nyd.msg.service.ISendSmsService;
import com.nyd.user.api.UserAccountContract;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.api.UserSourceContract;
import com.nyd.user.entity.LoginLog;
import com.nyd.user.model.mq.RechargeFeeInfo;
import com.nyd.zeus.api.BillContract;
import com.nyd.zeus.model.BillInfo;
import com.nyd.zeus.model.RepayInfo;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author cm
 */

@Service
public class YsbQuickPayServiceImpl implements YsbQuickPayService {
    Logger logger = LoggerFactory.getLogger(YsbQuickPayServiceImpl.class);

    @Autowired
    private QuickPayYsbProperties quickPayYsbProperties;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserAccountContract userAccountContract;

    @Autowired
    private UserIdentityContract userIdentityContract;

    @Autowired
    private ISendSmsService sendSmsService;

    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;

    @Autowired(required = false)
    private BillContract billContract;

    @Autowired
    private RedisProcessService redisProcessService;

    @Autowired
    private UserSourceContract userSourceContract;

    private String genYsbValidateKey(String phoneNo){
        return "YsbValidate"+"-"+phoneNo;
    }
    /**
     * 发送短信
     * @param vo
     * @return
     */
    @Override
    public ResponseData ysbSendMessage(YsbSendMessageVo vo) {
        logger.info("send message entrance:"+JSON.toJSON(vo));

        try {
            /**
             * 从Redis里面获取token(如果能获取到，则为重新获取验证码; 如果获取不到，则为第一次获取验证码)
             */
            String token = "";
            String ysbValidateKey = genYsbValidateKey(vo.getPhoneNo());
            if (redisTemplate.hasKey(ysbValidateKey)){
                token = (String)redisTemplate.opsForValue().get(ysbValidateKey);
                logger.info("订单号："+vo.getOrderNo()+"获取上一次返回的token成功");
            }else{
                token = "";
            }
            logger.info("token:"+token);

            String accountId = quickPayYsbProperties.getAccountId();
            vo.setAccountId(accountId);            //商户编号
            String notifyURL = quickPayYsbProperties.getDaiKouResultNotifyURL();
            vo.setResponseUrl(notifyURL);
            String key = quickPayYsbProperties.getKey();   //key

//            if (StringUtils.isNotBlank(token)){            //token
//                vo.setToken(token);
//            }
            vo.setToken(token);

            logger.info("ysb快捷支付，before发送短信请求参数："+ JSON.toJSON(vo));

            //mac(数字签名)
            String mac="";
            StringBuffer sf = new StringBuffer();
            sf.append("accountId=").append(accountId);
            sf.append("&customerId=").append(vo.getCustomerId());
            sf.append("&orderNo=").append(vo.getOrderNo());
            sf.append("&purpose=").append(vo.getPurpose());
            sf.append("&amount=").append(vo.getAmount());
            sf.append("&commodityName=").append(vo.getCommodityName());
            sf.append("&businessType=").append(vo.getBusinessType());
            sf.append("&responseUrl=").append(notifyURL);
            sf.append("&token=").append(token);
            if (!StringUtils.isNotBlank(token)){
                sf.append("&name=").append(vo.getName());
                sf.append("&idCardNo=").append(vo.getIdCardNo());
                sf.append("&cardNo=").append(vo.getCardNo());
                sf.append("&phoneNo=").append(vo.getPhoneNo());
            }else if (StringUtils.isNotBlank(token)){
                sf.append("&name=").append("");
                sf.append("&idCardNo=").append("");
                sf.append("&cardNo=").append("");
                sf.append("&phoneNo=").append("");
            }
            sf.append("&key=").append(key);
            logger.info("发送短信加密前+++++++++"+sf.toString());
            mac = Md5Encrypt.md5(sf.toString()).toUpperCase();
            logger.info("mac:"+mac);
            vo.setMac(mac);

            logger.info("send message request param:"+JSON.toJSON(vo));

            //发起发送短信请求
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            HttpEntity entity = new HttpEntity(JSON.toJSONString(vo), headers);


            ResponseEntity<MessageResponse> result = restTemplate.exchange(quickPayYsbProperties.getSendMessageURL(), HttpMethod.POST, entity, MessageResponse.class);
            logger.info("请求发送验证码返回："+ result.getBody());
            if ("0000".equals(result.getBody().getResult_code())){
                ResponseData responseData = ResponseData.success();
                logger.info("验证码请求结果描述："+result.getBody().getResult_msg());
                logger.info("请求用户授权码："+result.getBody().getToken());

                MessageReturn messageReturn = new MessageReturn();
                messageReturn.setToken(result.getBody().getToken());
                messageReturn.setCustomerId(vo.getCustomerId());
                messageReturn.setOrderNo(vo.getOrderNo());
                logger.info("发送验证码返回给前台参数："+JSON.toJSON(messageReturn));
                responseData.setData(messageReturn);

                /**
                 * 将返回的token存入Redis(重新获取验证码需要上一次发送验证码返回的token,所以存入Redis里面)
                 */
                try {
                    redisTemplate.opsForValue().set(ysbValidateKey,result.getBody().getToken(),2, TimeUnit.MINUTES);
                    logger.info("第一次获取验证码返回的token存入Redis成功");
                }catch (Exception e){
                    e.printStackTrace();
                }

                return responseData;

            }else{
                logger.info("返回信息："+result.getBody().getResult_msg());
                return  ResponseData.error(result.getBody().getResult_msg());
            }

        }catch (Throwable e){
            logger.error("send message error:",e);
            return ResponseData.error(e.getMessage());
        }


    }

    /**
     * 银生宝快捷支付确认接口
     * @param vo
     * @return
     */
    @Override
    public ResponseData ysbQuickPayConfirm(YsbQuickPayConfirmVo vo) {
        logger.info("confirm pay entrance :"+JSON.toJSON(vo));
        ResponseData responseData = ResponseData.success();
        try {
            String accountId = quickPayYsbProperties.getAccountId();
            vo.setAccountId(accountId);                     //商户编号
            String key = quickPayYsbProperties.getKey();    //Key
            //mac(数字签名)
            String mac="";
            StringBuffer sf = new StringBuffer();
            sf.append("accountId=").append(accountId);
            sf.append("&customerId=").append(vo.getCustomerId());
            sf.append("&orderNo=").append(vo.getOrderNo());
            sf.append("&vericode=").append(vo.getVericode());
            sf.append("&token=").append(vo.getToken());
            sf.append("&key=").append(key);
            logger.info("确认支付加密前+++++++++"+sf.toString());
            mac = Md5Encrypt.md5(sf.toString()).toUpperCase();
            logger.info("mac:"+mac);
            vo.setMac(mac);
            logger.info("confirm pay request param:"+JSON.toJSON(vo));

            //发起确认支付请求
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            HttpEntity entity = new HttpEntity(JSON.toJSONString(vo), headers);


            ResponseEntity<ConfirmPayResponse> result = restTemplate.exchange(quickPayYsbProperties.getConfirmPayURL(), HttpMethod.POST, entity, ConfirmPayResponse.class);
            logger.info("确认支付请求返回："+ result.getBody());

            if ("0000".equals(result.getBody().getResult_code())){
                logger.info("确认支付,支付描述："+result.getBody().getResult_msg());
                logger.info("确认支付,交易状态："+result.getBody().getStatus());
                if("00".equals(result.getBody().getStatus())){
                    responseData.setMsg("交易成功");
                }else if("10".equals(result.getBody().getStatus())){
                    responseData.setMsg("交易失败");
                    //DingdingUtil.getErrMsg("银生宝快捷支付确认接口，交易失败，失败信息:"+result.getBody().getDesc()+"订单号orderNo"+vo.getOrderNo());
                }else if ("20".equals(result.getBody().getStatus())){
                    responseData.setMsg("处理中");
                }

            }else{
                logger.info("确认支付,返回信息："+result.getBody().getResult_msg());
                return  ResponseData.error(result.getBody().getResult_msg());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return responseData;
    }


    /**
     * 银生宝快捷支付回调通知处理
     * @param ysbQuickPayNotifyResponseVo
     * @return
     */
    @Override
    public String handleCallBack(YsbQuickPayNotifyResponseVo ysbQuickPayNotifyResponseVo) {
        logger.info("ysb quick pay callback:"+JSON.toJSONString(ysbQuickPayNotifyResponseVo));
        // 避免重复的通知
        try {
            if (redisTemplate.hasKey(Constants.YSB_REDIS_PREFIX + ysbQuickPayNotifyResponseVo.getOrderNo())) {
                logger.error("有重复通知" + JSON.toJSONString(ysbQuickPayNotifyResponseVo));
                return "success";
            } else {
                redisTemplate.opsForValue().set(Constants.YSB_REDIS_PREFIX + ysbQuickPayNotifyResponseVo.getOrderNo(), "1", 2880, TimeUnit.MINUTES);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("写redis出错"+e.getMessage());
        }

        logger.info("返回的orderNo为"+ysbQuickPayNotifyResponseVo.getOrderNo());
        //银生宝快捷支付回调通知扣款成功， 0000表示扣款成功
        if("0000".equals(ysbQuickPayNotifyResponseVo.getResult_code())) {
            logger.info("扣款成功,接下来分析属于什么类型的扣款+******************");
            //支付会员费生成的订单,  M代表会员费
            if (ysbQuickPayNotifyResponseVo.getOrderNo().contains("M")) {
                String[] ss = ysbQuickPayNotifyResponseVo.getOrderNo().split("M");
                MemberFeeLogMessage memberModel = new MemberFeeLogMessage();
                memberModel.setDebitChannel("ysb");
                String userId = ss[0];

                //从支付会员费时存入Redis里面去取值
                String carryStr = (String)redisTemplate.opsForValue().get(Constants.YSB_NYD_CARRAY+userId);
                PayCarryVo payCarryVo;
                if(carryStr == null){
                    payCarryVo = new PayCarryVo();
                }else {
                    payCarryVo = JSON.parseObject(carryStr,PayCarryVo.class);
                }
                logger.info("会员费支付成功，回调携带回来的信息:"+JSON.toJSONString(payCarryVo));

                memberModel.setMobile(payCarryVo.getMobile());
                memberModel.setUserId(payCarryVo.getUserId());
                memberModel.setMemberType(payCarryVo.getMemberType());
                memberModel.setDebitFlag("1");
                //优惠券id
                if (StringUtils.isNotBlank(payCarryVo.getCouponId())){
                    memberModel.setCouponId(payCarryVo.getCouponId());
                }
                
                //渠道名称（马甲包来源）
                if(StringUtils.isBlank(payCarryVo.getAppName())) {
                    memberModel.setAppName("nyd");
                }else {
                	memberModel.setAppName(payCarryVo.getAppName());
                }
                
                //现金券使用金额
                memberModel.setCouponUseFee(payCarryVo.getCouponUseFee());

                //此笔支付，需要支付的金额
                memberModel.setPayCash(new BigDecimal(ysbQuickPayNotifyResponseVo.getAmount()));
                logger.info("银生宝快捷支付会员费，给member发送mq消息的对象："+JSON.toJSON(memberModel));

                if(StringUtils.isNotBlank(payCarryVo.getMobile())) {
                    SmsRequest smsRequest = new SmsRequest();
                    smsRequest.setCellphone(payCarryVo.getMobile());
                    smsRequest.setSmsType(8);
                    ResponseData data = userSourceContract.selectUserSourceByMobile(payCarryVo.getMobile());
                    String appName = null;
                    if ("0".equals(data.getStatus())){
                        LoginLog loginLog =(LoginLog)data.getData();
                        if (loginLog != null){
                            appName = loginLog.getAppName();
                        }
                    }
                    if (appName != null) {
                        smsRequest.setAppName(appName);
                    }
                    try {
                        ResponseData responseData = sendSmsService.sendSingleSms(smsRequest);
                        logger.info("银生宝快捷支付会员费成功,发送短信结果" + JSON.toJSONString(responseData));
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("银生宝快捷支付会员费失败,发送短信结果" + e.getMessage());
                    }
                }else {
                    logger.info("手机号码为空"+userId);
                }

                try {
                    rabbitmqProducerProxy.convertAndSend("mfee.nyd", memberModel);
                    logger.info("银生宝快捷支付会员费成功,发送member_MQ成功");
                }catch (Exception e){
                    logger.info("ysb快捷支付完成，给member发送mq消息的对象异常",e);
                    e.printStackTrace();
                }

                MemberCarryResp resp = new MemberCarryResp();
                resp.setMobile(payCarryVo.getMobile());
                resp.setProductCode(payCarryVo.getProductCode());
                resp.setStatus("0");
                resp.setMemberType(payCarryVo.getMemberType());
                redisProcessService.putPayStatusMember(userId,JSON.toJSONString(resp));

                RepayInfo repayInfo = new RepayInfo();
                repayInfo.setRepayStatus("0");
                repayInfo.setBillNo(userId+"_"+System.currentTimeMillis());
                repayInfo.setRepayAmount(new BigDecimal(ysbQuickPayNotifyResponseVo.getAmount()));
                repayInfo.setRepayChannel("ysb");
                repayInfo.setRepayTime(new Date());
                repayInfo.setRepayNo(ysbQuickPayNotifyResponseVo.getOrderNo());
                repayInfo.setRepayType(RepayType.MFEE.getCode());
                repayInfo.setUserId(userId);
                //渠道名称（马甲包来源）
                if(StringUtils.isBlank(payCarryVo.getAppName())) {
                	repayInfo.setAppName("nyd");
                }else {
                	repayInfo.setAppName(payCarryVo.getAppName());
                }
                
                //优惠券id
                if (StringUtils.isNotBlank(payCarryVo.getCouponId())){
                    repayInfo.setCouponId(payCarryVo.getCouponId());
                }
                //现金券使用金额
                repayInfo.setCouponUseFee(payCarryVo.getCouponUseFee());
                logger.info("银生宝快捷支付会员费，给repay发送mq消息的对象："+JSON.toJSON(repayInfo));

                try {
                    rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                    logger.info("银生宝快捷支付会员费成功,发送repay_MQ成功");
                }catch (Exception e){
                    logger.info("ysb快捷支付完成，给repay发送mq消息的对象异常",e);
                    e.printStackTrace();
                }
                LoggerUtils.write(repayInfo);

                //充值现金券产生的订单
            } else if (ysbQuickPayNotifyResponseVo.getOrderNo().contains("xj")){
                String[] ss = ysbQuickPayNotifyResponseVo.getOrderNo().split("xj");
                String userId = ss[0];
                //从充值现金券时存入Redis里面去取值
                String carryStr = (String)redisTemplate.opsForValue().get(Constants.YSB_NYD_CARRAY+userId);
                PayCarryVo payCarryVo;
                if(carryStr == null){
                    payCarryVo = new PayCarryVo();
                }else {
                    payCarryVo = JSON.parseObject(carryStr,PayCarryVo.class);
                }
                logger.info("使用银生宝快捷支付充值现金券,Redis携带回来的信息："+JSON.toJSONString(payCarryVo));

                RechargeFeeInfo rechargeFeeInfo = new RechargeFeeInfo();
                rechargeFeeInfo.setUserId(userId);
                rechargeFeeInfo.setAccountNumber(payCarryVo.getMobile());
                rechargeFeeInfo.setAmount(new BigDecimal(ysbQuickPayNotifyResponseVo.getAmount()));
                rechargeFeeInfo.setCashId(payCarryVo.getCashId());
                rechargeFeeInfo.setOperStatus(1);  // 0：失败  1：成功
                rechargeFeeInfo.setRechargeFlowNo(ysbQuickPayNotifyResponseVo.getOrderNo());   //将银生宝返回的订单号orderId,作为充值流水号
                rechargeFeeInfo.setStatusMsg(ysbQuickPayNotifyResponseVo.getResult_msg());
                rechargeFeeInfo.setUserType(1);   //用户充值现金券收入
                rechargeFeeInfo.setCashDescription("用户充值收入");
                //渠道名称（马甲包来源）
                if(StringUtils.isBlank(payCarryVo.getAppName())) {
                	rechargeFeeInfo.setAppName("nyd");
                }else {
                	rechargeFeeInfo.setAppName(payCarryVo.getAppName());
                }
                logger.info("ysb快捷支付完成，发送mq现金券对象："+JSON.toJSON(rechargeFeeInfo));

                try {
                    rabbitmqProducerProxy.convertAndSend("rechargeCoupon.nyd", rechargeFeeInfo);
                    logger.info("ysb快捷支付完成，发送mq现金券对象over");
                }catch (Exception e){
                    logger.info("ysb快捷支付完成，发送mq现金券对象异常",e);
                    e.printStackTrace();
                }

                /**
                 *给用户发信息，告诉用户现金券购买成功
                 */
                /*if(StringUtils.isNotBlank(payCarryVo.getMobile())) {
                    SmsRequest smsRequest = new SmsRequest();
                    smsRequest.setCellphone(payCarryVo.getMobile());
                    smsRequest.setSmsType(8);
                    ResponseData data = userSourceContract.selectUserSourceByMobile(payCarryVo.getMobile());
                    String appName = null;
                    if ("0".equals(data.getStatus())){
                        LoginLog loginLog =(LoginLog)data.getData();
                        if (loginLog != null){
                            appName = loginLog.getAppName();
                        }
                    }
                    if (appName != null) {
                        smsRequest.setAppName(appName);
                    }
                    try {
                        ResponseData responseData = sendSmsService.sendSingleSms(smsRequest);
                        logger.info("ysb快捷支付充值现金券成功，发送短信success：" + JSON.toJSONString(responseData));
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("ysb快捷支付充值现金券失败，发送短信fail" + e.getMessage());
                    }
                }else {
                    logger.info("手机号码为空,无法进行发短信："+userId);
                }*/

                MemberCarryResp resp = new MemberCarryResp();
                resp.setMobile(payCarryVo.getMobile());
                resp.setProductCode(payCarryVo.getProductCode());
                resp.setStatus("0");
                resp.setMemberType(payCarryVo.getMemberType());
                redisProcessService.putPayStatusMember(userId,JSON.toJSONString(resp));

                /**
                 * 购买现金券的流水写入表t_repay
                 */
                RepayInfo repayInfo = new RepayInfo();
                repayInfo.setRepayStatus("0");          //0：成功；1：失败
                repayInfo.setBillNo(userId+"_"+System.currentTimeMillis());
                repayInfo.setRepayAmount(new BigDecimal(ysbQuickPayNotifyResponseVo.getAmount()));
                repayInfo.setRepayChannel("ysb");
                repayInfo.setRepayTime(new Date());
                repayInfo.setRepayNo(ysbQuickPayNotifyResponseVo.getOrderNo());
                repayInfo.setRepayType(RepayType.COUPON_FEE.getCode());
                repayInfo.setUserId(userId);
                //渠道名称（马甲包来源）
                if(StringUtils.isBlank(payCarryVo.getAppName())) {
                	repayInfo.setAppName("nyd");
                }else {
                	repayInfo.setAppName(payCarryVo.getAppName());
                }
                logger.info("通过ysb快捷支付购买现金券成功，支付流水对象："+JSON.toJSON(repayInfo));

                try {
                    rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                    logger.info("银生宝快捷支付购买现金券成功,发送repay_MQ成功");
                }catch (Exception e){
                    logger.info("ysb快捷支付购买现金券完成，给repay发送mq消息的对象异常",e);
                    e.printStackTrace();
                }
                LoggerUtils.write(repayInfo);

            } else {              //还款产生的订单
                String sourceOrderId = ysbQuickPayNotifyResponseVo.getOrderNo();
                String orderId="";
                if (sourceOrderId.contains("P")) {
                    String[] strs = sourceOrderId.split("P");
                    orderId = strs[0];
                }
                
                //从还款时存入Redis里面去取值
                String carryStr = (String)redisTemplate.opsForValue().get(Constants.YSB_NYD_CARRAY+"ZC"+orderId);
                PayCarryVo payCarryVo;
                if(carryStr == null){
                    payCarryVo = new PayCarryVo();
                }else {
                    payCarryVo = JSON.parseObject(carryStr,PayCarryVo.class);
                }
                logger.info("使用银生宝快捷支付还款,Redis携带回来的信息："+JSON.toJSONString(payCarryVo));

                RepayMessage repayMessage = new RepayMessage();
                repayMessage.setBillNo(orderId);
                repayMessage.setRepayAmount(new BigDecimal(ysbQuickPayNotifyResponseVo.getAmount()));
                repayMessage.setRepayStatus("0");
                logger.info("通过ysb快捷支付还款成功，repayMessage："+JSON.toJSON(repayMessage));

                rabbitmqProducerProxy.convertAndSend("repay.nyd", repayMessage);
                logger.info("还款成功回写mq");

                try {
                    redisTemplate.opsForValue().set(Constants.PAY_PREFIX + orderId, PayStatus.PAY_SUCESS.getCode(), 100, TimeUnit.MINUTES);
                }catch (Exception e){
                    logger.info("异常",e);
                    logger.info(orderId+"set key异常ysb");
                }
                //银码头
                try {
                    ResponseData<BillInfo> responseData = billContract.getBillInfo(orderId);
                    logger.info("YSB快捷支付付款成功后获取billInfo"+JSON.toJSONString(responseData));
                    if ("0".equals( responseData.getStatus())){
                        BillInfo billInfo = responseData.getData();
                        if(billInfo!=null && StringUtils.isNotBlank(billInfo.getIbankOrderNo())){
                            repayMessage.setBillNo(billInfo.getIbankOrderNo());
                            repayMessage.setRepayTime(new Date());
                            logger.info("YSB快捷支付成功发送银码头rabbit"+JSON.toJSONString(repayMessage));
                            rabbitmqProducerProxy.convertAndSend("payIbank.ibank",repayMessage);
                        }
                    }else {
                        logger.info(orderId+"获取billInfo为status为1");
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    logger.info(orderId+"error",e);
                }
                //还款流水对象
                RepayInfo repayInfo = new RepayInfo();
                repayInfo.setRepayStatus("0");
                repayInfo.setBillNo(orderId);
                repayInfo.setRepayAmount(new BigDecimal(ysbQuickPayNotifyResponseVo.getAmount()));
                repayInfo.setRepayChannel("ysb");
                repayInfo.setRepayTime(new Date());
                repayInfo.setRepayNo(ysbQuickPayNotifyResponseVo.getOrderNo());
                //渠道名称（马甲包来源）
                if(StringUtils.isBlank(payCarryVo.getAppName())) {
                	repayInfo.setAppName("nyd");
                }else {
                	repayInfo.setAppName(payCarryVo.getAppName());
                }

                if(sourceOrderId.contains("k")){    //快捷支付的主动还款

                /**
                 * 之前可以找人代付,现在只能主动还款,现在订单号末尾带K,是为了跟之前保持一致，
                 * 所以订单的生成跟之前保持一致：String orderNo = nydYsbVo.getBillNo()+"P"+(System.currentTimeMillis()+"").substring(2,11)+"k";
                 *
                 * 银生宝快捷支付将不再保存换人的姓名、身份证号、卡号
                 */
                    repayInfo.setRepayType(RepayType.KJ.getCode());

                    logger.info("ysb快捷还款，还款流水表对象："+JSON.toJSONString(repayInfo));
                    try {
                        rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    LoggerUtils.write(repayInfo);

                } else {
                    repayInfo.setRepayType(RepayType.UNKNOW.getCode());
                    logger.info("ysb快捷还款，还款流水表对象(unknow repay type)："+JSON.toJSONString(repayInfo));
                    try {
                        rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    LoggerUtils.write(repayInfo);
                }
            }
        }else {                                   //银生宝通知支付失败
            if (ysbQuickPayNotifyResponseVo.getOrderNo().contains("M")) {              //支付会员费产生的订单
                String[] ss = ysbQuickPayNotifyResponseVo.getOrderNo().split("M");
                MemberFeeLogMessage memberModel = new MemberFeeLogMessage();
                memberModel.setDebitChannel("ysb");
                String userId = ss[0];

                String carryStr = (String)redisTemplate.opsForValue().get(Constants.YSB_NYD_CARRAY+userId);
                PayCarryVo payCarryVo;
                if(carryStr==null){
                    payCarryVo=new PayCarryVo();
                }else {
                    payCarryVo = JSON.parseObject(carryStr,PayCarryVo.class);
                }
                logger.info("携带回得信息为"+JSON.toJSONString(payCarryVo));
                
                memberModel.setDebitFlag(ysbQuickPayNotifyResponseVo.getResult_code());
                memberModel.setMemberType(payCarryVo.getMemberType());
                memberModel.setUserId(payCarryVo.getUserId());
                memberModel.setMobile(payCarryVo.getMobile());

                //优惠券id
                /*if (StringUtils.isNotBlank(payCarryVo.getCouponId())){
                    memberModel.setCouponId(payCarryVo.getCouponId());
                }*/

                //现金券使用金额
                memberModel.setCouponUseFee(payCarryVo.getCouponUseFee());
                //渠道名称（马甲包来源）
                if(StringUtils.isBlank(payCarryVo.getAppName())) {
                	memberModel.setAppName("nyd");
                }else {
                	memberModel.setAppName(payCarryVo.getAppName());
                }

                //此笔支付，需要支付的金额
                memberModel.setPayCash(new BigDecimal(ysbQuickPayNotifyResponseVo.getAmount()));
                logger.info("银生宝支付会员费（fail），给member发送mq消息的对象："+JSON.toJSON(memberModel));
                //DingdingUtil.getErrMsg("银生宝支付会员费失败，失败信息:"+ysbQuickPayNotifyResponseVo.getResult_msg()+"订单号orderNo"+ysbQuickPayNotifyResponseVo.getOrderNo());
                try {
                    rabbitmqProducerProxy.convertAndSend("mfee.nyd", memberModel);
                }catch (Exception e){
                    e.printStackTrace();
                }

                MemberCarryResp resp = new MemberCarryResp();
                resp.setMobile(payCarryVo.getMobile());
                resp.setProductCode(payCarryVo.getProductCode());
                resp.setStatus("1");
                resp.setMemberType(payCarryVo.getMemberType());
                redisProcessService.putPayStatusMember(userId,JSON.toJSONString(resp));

                RepayInfo repayInfo = new RepayInfo();
                repayInfo.setRepayStatus("1");
                repayInfo.setBillNo(userId+"_"+System.currentTimeMillis());
                repayInfo.setRepayAmount(new BigDecimal(ysbQuickPayNotifyResponseVo.getAmount()));
                repayInfo.setRepayChannel("ysb");
                repayInfo.setRepayTime(new Date());
                repayInfo.setRepayNo(ysbQuickPayNotifyResponseVo.getOrderNo());
                repayInfo.setRepayType(RepayType.MFEE_FAIL.getCode());
                //渠道名称（马甲包来源）
                if(StringUtils.isBlank(payCarryVo.getAppName())) {
                	repayInfo.setAppName("nyd");
                }else {
                	repayInfo.setAppName(payCarryVo.getAppName());
                }
                //优惠券id
                if (StringUtils.isNotBlank(payCarryVo.getCouponId())){
                    repayInfo.setCouponId(payCarryVo.getCouponId());
                }

                //现金券使用金额
                repayInfo.setCouponUseFee(payCarryVo.getCouponUseFee());
                logger.info("银生宝支付会员费（fail），给repay发送mq消息的对象："+JSON.toJSON(repayInfo));

                try {
                    rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                }catch (Exception e){
                    e.printStackTrace();
                }
                LoggerUtils.write(repayInfo);
            }else if (ysbQuickPayNotifyResponseVo.getOrderNo().contains("xj")){       //充值现金券失败产生的订单
                String[] ss = ysbQuickPayNotifyResponseVo.getOrderNo().split("xj");
                String userId = ss[0];
                String carryStr = (String)redisTemplate.opsForValue().get(Constants.YSB_NYD_CARRAY+userId);
                PayCarryVo payCarryVo;
                if(carryStr == null){
                    payCarryVo = new PayCarryVo();
                }else {
                    payCarryVo = JSON.parseObject(carryStr,PayCarryVo.class);
                }
                logger.info("充值现金券失败,Redis携带回得信息："+JSON.toJSONString(payCarryVo));

                RechargeFeeInfo rechargeFeeInfo = new RechargeFeeInfo();
                rechargeFeeInfo.setUserId(userId);
                rechargeFeeInfo.setAccountNumber(payCarryVo.getMobile());
                rechargeFeeInfo.setAmount(new BigDecimal(ysbQuickPayNotifyResponseVo.getAmount()));
                rechargeFeeInfo.setCashId(payCarryVo.getCashId());
                rechargeFeeInfo.setOperStatus(0);   //0：失败  1：成功
                rechargeFeeInfo.setRechargeFlowNo(ysbQuickPayNotifyResponseVo.getOrderNo());       //将银生宝返回的订单号orderId,作为充值流水号
                rechargeFeeInfo.setStatusMsg(ysbQuickPayNotifyResponseVo.getResult_msg());
                rechargeFeeInfo.setUserType(1);                     //用户充值现金券收入
                rechargeFeeInfo.setCashDescription("用户充值收入");
                //渠道名称（马甲包来源）
                if(StringUtils.isBlank(payCarryVo.getAppName())) {
                	rechargeFeeInfo.setAppName("nyd");
                }else {
                	rechargeFeeInfo.setAppName(payCarryVo.getAppName());
                }
                logger.info("ysb支付fail，发送mq现金券对象："+JSON.toJSON(rechargeFeeInfo));

                try {
                    rabbitmqProducerProxy.convertAndSend("rechargeCoupon.nyd", rechargeFeeInfo);
                    logger.info("ysb快捷支付完成(失败订单)，发送mq现金券对象完毕");
                }catch (Exception e){
                    logger.info("ysb快捷支付完成(失败订单)，发送mq现金券对象异常",e);
                    e.printStackTrace();
                }

                /**
                 *购买失败，不需要给用户发短信
                 */

                MemberCarryResp resp = new MemberCarryResp();
                resp.setMobile(payCarryVo.getMobile());
                resp.setProductCode(payCarryVo.getProductCode());
                resp.setStatus("1");
                resp.setMemberType(payCarryVo.getMemberType());
                redisProcessService.putPayStatusMember(userId,JSON.toJSONString(resp));

                /**
                 * 购买现金券失败的流水写入表t_repay
                 */
                RepayInfo repayInfo = new RepayInfo();
                repayInfo.setRepayStatus("1");    //0:成功；1：失败
                repayInfo.setBillNo(userId+"_"+System.currentTimeMillis());
                repayInfo.setRepayAmount(new BigDecimal(ysbQuickPayNotifyResponseVo.getAmount()));
                repayInfo.setRepayChannel("ysb");
                repayInfo.setRepayTime(new Date());
                repayInfo.setRepayNo(ysbQuickPayNotifyResponseVo.getOrderNo());
                repayInfo.setRepayType(RepayType.COUPON_FEE.getCode());
                repayInfo.setUserId(userId);
                //渠道名称（马甲包来源）
                if(StringUtils.isBlank(payCarryVo.getAppName())) {
                	repayInfo.setAppName("nyd");
                }else {
                	repayInfo.setAppName(payCarryVo.getAppName());
                }
                
                logger.info("通过银生宝购买现金券fail，支付流水对象："+JSON.toJSON(repayInfo));

                try {
                    rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                }catch (Exception e){
                    e.printStackTrace();
                }
                LoggerUtils.write(repayInfo);

            } else{                                          //扣款产生的订单号
                String sourceOrderId = ysbQuickPayNotifyResponseVo.getOrderNo();
                String orderId = null;
                if (sourceOrderId.contains("P")) {
                    String[] strs = sourceOrderId.split("P");
                    orderId = strs[0];
                }
                
                //从还款时存入Redis里面去取值
                String carryStr = (String)redisTemplate.opsForValue().get(Constants.YSB_NYD_CARRAY+"ZC"+orderId);
                PayCarryVo payCarryVo;
                if(carryStr == null){
                    payCarryVo = new PayCarryVo();
                }else {
                    payCarryVo = JSON.parseObject(carryStr,PayCarryVo.class);
                }
                logger.info("使用银生宝快捷支付还款,Redis携带回来的信息："+JSON.toJSONString(payCarryVo));

                try {
                    redisTemplate.delete(Constants.PAY_PREFIX + orderId);
                }catch (Exception e){
                    logger.info("异常",e);
                    logger.info(orderId+"删除key异常ysb");
                }

                RepayInfo repayInfo = new RepayInfo();
                repayInfo.setRepayStatus("1");
                repayInfo.setBillNo(orderId);
                repayInfo.setRepayAmount(new BigDecimal(ysbQuickPayNotifyResponseVo.getAmount()));
                repayInfo.setRepayChannel("ysb");
                repayInfo.setRepayTime(new Date());
                repayInfo.setRepayNo(ysbQuickPayNotifyResponseVo.getOrderNo());
                //渠道名称（马甲包来源）
                if(StringUtils.isBlank(payCarryVo.getAppName())) {
                	repayInfo.setAppName("nyd");
                }else {
                	repayInfo.setAppName(payCarryVo.getAppName());
                }

                if(sourceOrderId.contains("k")){
                    /*String[] infoarray = null;
                    try {
                        String infos = (String) redisTemplate.opsForValue().get(Constants.QUICK_PAY + orderId);
                        infoarray = infos.split("-");
                    }catch (Exception e){
                        e.printStackTrace();
                    }*/

                    repayInfo.setRepayType(RepayType.KJ_FAIL.getCode());
                    /*if(infoarray!=null&&infoarray.length==3){
                        repayInfo.setRepayName(infoarray[0]);
                        repayInfo.setRepayIdNumber(infoarray[1]);
                        repayInfo.setRepayAccount(infoarray[2]);
                    }*/
                    try {
                        rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    LoggerUtils.write(repayInfo);
                }else {
                    repayInfo.setRepayType(RepayType.UNKNOW_FAIL.getCode());
                    logger.info("ysb快捷还款(fail)，还款流水表对象(unknow repay type)："+JSON.toJSONString(repayInfo));
                    try {
                        rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    LoggerUtils.write(repayInfo);
                }
            }

        }

        logger.info("回调成功");
        return "success";
    }


}
