package com.creativearts.nyd.pay.service.helibao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.creativearts.nyd.pay.model.MemberCarryResp;
import com.creativearts.nyd.pay.model.PayCarryVo;
import com.creativearts.nyd.pay.model.RepayMessage;
import com.creativearts.nyd.pay.model.RepayType;
import com.creativearts.nyd.pay.model.enums.PayStatus;
import com.creativearts.nyd.pay.model.helibao.*;
import com.creativearts.nyd.pay.service.Constants;
import com.creativearts.nyd.pay.service.RedisProcessService;
import com.creativearts.nyd.pay.service.helibao.HelibaoPayService;
import com.creativearts.nyd.pay.service.helibao.config.HelibaoConfig;
import com.creativearts.nyd.pay.service.helibao.config.NewHelibaoConfig;
import com.creativearts.nyd.pay.service.helibao.util.Des3Encryption;
import com.creativearts.nyd.pay.service.helibao.util.DingdingUtil;
import com.creativearts.nyd.pay.service.helibao.util.Disguiser;
import com.creativearts.nyd.pay.service.helibao.util.HttpClientService;
import com.creativearts.nyd.pay.service.log.LoggerUtils;
import com.creativearts.nyd.pay.service.utils.CallBackUtils;
import com.creativearts.nyd.pay.service.validator.ValidateUtil;
import com.nyd.member.model.msg.MemberFeeLogMessage;
import com.nyd.msg.model.SmsRequest;
import com.nyd.msg.service.ISendSmsService;
import com.nyd.order.api.BillYmtContract;
import com.nyd.order.api.OrderContract;
import com.nyd.order.model.YmtKzjrBill.BillYmtInfo;
import com.nyd.pay.api.enums.WithHoldType;
import com.nyd.user.api.UserAccountContract;
import com.nyd.user.api.UserSourceContract;
import com.nyd.user.entity.LoginLog;
import com.nyd.user.model.dto.AccountDto;
import com.nyd.user.model.mq.RechargeFeeInfo;
import com.nyd.zeus.api.BillContract;
import com.nyd.zeus.model.BillInfo;
import com.nyd.zeus.model.RepayInfo;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Cong Yuxiang
 * 2017/12/15
 **/
@Service
public class HelibaoPayServiceImpl implements HelibaoPayService {

    Logger log = LoggerFactory.getLogger(HelibaoPayServiceImpl.class);

    @Autowired
    private ValidateUtil validateUtil;
    @Autowired
    private HelibaoConfig helibaoConfig;

    @Autowired
    private NewHelibaoConfig newHelibaoConfig;

    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;
//    @Autowired
//    private RepayContract repayContract;

    @Autowired
    private ISendSmsService sendSmsService;
    @Autowired
    private OrderContract orderContract;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired(required = false)
    private BillContract billContract;
    @Autowired
    private RedisProcessService redisProcessService;
    @Autowired
    private UserAccountContract userAccountContract;
    @Autowired
    private UserSourceContract userSourceContract;

    @Autowired
    private BillYmtContract billYmtContract;

    @Override
    public ResponseData withHold(CreateOrderVo orderVo, WithHoldType type) {
        orderVo.setP2_customerNumber(helibaoConfig.getMerchantNo());
        orderVo.setP18_serverCallbackUrl(helibaoConfig.getCallBackUrl());
        validateUtil.validate(orderVo);

        try {
            //需要DES加密的参数
            //证件号码
            orderVo.setP7_idCardNo(Des3Encryption.encode(helibaoConfig.getDeskey(), orderVo.getP7_idCardNo()));
            //银行卡号
            orderVo.setP8_cardNo(Des3Encryption.encode(helibaoConfig.getDeskey(), orderVo.getP8_cardNo()));
            //手机号码
            if(orderVo.getP9_phone()!=null) {
                orderVo.setP9_phone(Des3Encryption.encode(helibaoConfig.getDeskey(), orderVo.getP9_phone()));
            }

            Map<String,String> map = helibaoConfig.convertBean(orderVo, new LinkedHashMap());
            String oriMessage = helibaoConfig.getSigned(map, null);
            log.info("签名原文串：" + oriMessage);
            String sign = Disguiser.disguiseMD5(oriMessage.trim());
            log.info("签名串：" + sign);
            map.put("sign", sign);
            log.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientService.getHttpResp(map,
                    helibaoConfig.getRequestUrl());
            log.info("响应结果：" + resultMap);
            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                CreateOrderResponseVo orderResponseVo = JSONObject.parseObject(
                        resultMsg, CreateOrderResponseVo.class);
                String[] excludes = {};
                String assemblyRespOriSign = helibaoConfig.getSigned(orderResponseVo, excludes);
                log.info("组装返回结果签名串：" + assemblyRespOriSign);
                String responseSign = orderResponseVo.getSign();
                log.info("响应签名：" + responseSign);
                String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign
                        .trim());
                log.info("验证签名：" + checkSign);
                if (checkSign.equals(responseSign)) {
                    if ("0000".equals(orderResponseVo.getRt2_retCode())) {
                        log.info(orderVo.getP3_orderId()+"代扣成功");
                        return ResponseData.success();
                    }else if("0001".equals(orderResponseVo.getRt2_retCode())) {
                        log.info(orderVo.getP3_orderId()+"处理中");
                        return ResponseData.success();
                    }else{
                        log.error(orderVo.getP3_orderId()+"代扣失败:"+orderResponseVo.getRt3_retMsg());
                        return ResponseData.error(orderResponseVo.getRt3_retMsg());
                    }
                } else {
                    log.info(orderVo.getP3_orderId()+"代扣验签失败:");
                   return ResponseData.error("代扣验签失败");
                }
            } else {
                log.info(orderVo.getP3_orderId()+"代扣请求失败:");
                return ResponseData.error("代扣请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info(orderVo.getP3_orderId()+"代扣异常");
            return ResponseData.error(e.getMessage());
        }

    }

    @Override
    public ResponseData withHoldQuery(QueryOrderVo vo) {
        vo.setP1_bizType("WithholdQuery");
        vo.setP2_customerNumber(helibaoConfig.getMerchantNo());
        vo.setP4_timestamp(DateFormatUtils.format(new Date(),"yyyyMMddHHmmss"));
        log.info("--------进入订单查询接口----------");
        try {
            Map<String, String> map = helibaoConfig.convertBean(vo, new LinkedHashMap());
            String oriMessage = helibaoConfig.getSigned(map, null);
            log.info("签名原文串：" + oriMessage);
            String sign = Disguiser.disguiseMD5(oriMessage.trim());
            log.info("签名串：" + sign);
            map.put("sign", sign);
            log.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientService.getHttpResp(map,
                    helibaoConfig.getRequestUrl());
            log.info("响应结果：" + resultMap);
            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                QueryOrderResponseVo queryOrderResponseVo = JSONObject
                        .parseObject(resultMsg, QueryOrderResponseVo.class);
                String[] excludes = {"rt14_reason"};
                String assemblyRespOriSign = helibaoConfig.getSigned(
                        queryOrderResponseVo, excludes);
                log.info("组装返回结果签名串：" + assemblyRespOriSign);
                String responseSign = queryOrderResponseVo.getSign();
                log.info("响应签名：" + responseSign);
                String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign
                        .trim());
                if (checkSign.equals(responseSign)) {
                    if ("0000".equals(queryOrderResponseVo.getRt2_retCode())) {
                        log.info(vo.getP3_orderId()+"代扣查询成功");
                        return ResponseData.success();
                    } else {
                        log.error(vo.getP3_orderId()+"代扣查询失败:"+queryOrderResponseVo.getRt3_retMsg());
                        return ResponseData.error(queryOrderResponseVo.getRt3_retMsg());
                    }
                } else {
                    log.info(vo.getP3_orderId()+"代扣查询验签失败:");
                    return ResponseData.error("代扣查询验签失败");
                }
            } else {
                log.info(vo.getP3_orderId()+"代扣查询请求失败:");
                return ResponseData.error("代扣查询请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info(vo.getP3_orderId()+"代扣查询异常");
            return ResponseData.error(e.getMessage());
        }

    }

    @Override
    public ResponseData withHoldBatch(WithholdBatchVo vo) {
        log.info("--------进入批量代扣接口----------");
        vo.setP2_customerNumber(helibaoConfig.getMerchantNo());
        vo.setP10_serverCallbackUrl(helibaoConfig.getCallBackUrl());
//		JSONArray jsonArray = new JSONArray();
//		jsonArray.add(getBatchDetail("P1_"));
//		jsonArray.add(getBatchDetail("P2_"));
        JSONArray jsonArray = JSONArray.parseArray(vo.getP8_records());
        vo.setP8_records(Des3Encryption.encode(helibaoConfig.getDeskey(), jsonArray.toJSONString()));

        try {
            Map<String, String> map = helibaoConfig.convertBean(vo, new LinkedHashMap());
            String oriMessage = helibaoConfig.getSigned(map, null);
            log.info("签名原文串：" + oriMessage);
            String sign = Disguiser.disguiseMD5(oriMessage.trim());
            log.info("签名串：" + sign);
            map.put("sign", sign);
            log.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, helibaoConfig.getRequestUrl());
            log.info("响应结果：" + resultMap);
            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                WithholdBatchResVo withholdBatchResVo = JSONObject.parseObject(resultMsg, WithholdBatchResVo.class);
                String assemblyRespOriSign = helibaoConfig.getSigned(withholdBatchResVo, null);
                log.info("组装返回结果签名串：" + assemblyRespOriSign);
                String responseSign = withholdBatchResVo.getSign();
                log.info("响应签名：" + responseSign);
                String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
                if (checkSign.equals(responseSign)) {
                    if ("0000".equals(withholdBatchResVo.getRt2_retCode())) {
                        log.info("批量代扣成功");
                        return ResponseData.success();
                    } else {
                        log.error("批量代扣失败");
                        return ResponseData.error("批量代扣失败");
                    }
                } else {
                    log.info("批量代扣验签失败");
                    return ResponseData.error("批量代扣验签失败");
                }
            } else {
                log.info("批量代扣请求失败");
                return ResponseData.error("批量代扣请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("批量代扣异常");
            return ResponseData.error(e.getMessage());
        }
    }

    @Override
    public ResponseData withHoldBatchQuery(QueryBatchOrderVo vo) {
        log.info("--------进入批量订单查询接口----------");
        try {
            Map<String, String> map = helibaoConfig.convertBean(vo, new LinkedHashMap());
            String oriMessage = helibaoConfig.getSigned(map, null);
            log.info("签名原文串：" + oriMessage);
            String sign = Disguiser.disguiseMD5(oriMessage.trim());
            log.info("签名串：" + sign);
            map.put("sign", sign);
            log.info("发送参数：" + map);
            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, helibaoConfig.getRequestUrl());
            log.info("响应结果：" + resultMap);
            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                QueryBatchOrderResponseVo queryBatchOrderResponseVo = JSONObject.parseObject(resultMsg, QueryBatchOrderResponseVo.class);
                String[] excludes = {};
                String assemblyRespOriSign = helibaoConfig.getSigned(queryBatchOrderResponseVo, excludes);
                log.info("组装返回结果签名串：" + assemblyRespOriSign);
                String responseSign = queryBatchOrderResponseVo.getSign();
                log.info("响应签名：" + responseSign);
                String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
                if (checkSign.equals(responseSign)) {
                    if ("0000".equals(queryBatchOrderResponseVo.getRt2_retCode())) {
                        log.info("批量查询成功");
                        return ResponseData.success();
                    } else {
                        log.error("批量查询失败");
                        return ResponseData.error("批量查询失败");
                    }
                } else {
                    log.info("批量查询验签失败");
                    return ResponseData.error("批量查询验签失败");
                }
            } else {
                log.info("批量查询请求失败");
                return ResponseData.error("批量查询请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("批量查询异常");
            return ResponseData.error(e.getMessage());
        }

    }

    @Override
    public String callBack(String callback) {
        NotifyResponseVo notifyResponseVo = null;
        try {
            notifyResponseVo = CallBackUtils.parse(callback,NotifyResponseVo.class);

            log.info("回调"+ JSON.toJSONString(notifyResponseVo));
            String assemblyRespOriSign = helibaoConfig.getSigned(
                    notifyResponseVo, null);
            log.info("组装返回结果签名串：" + assemblyRespOriSign);
            String responseSign = notifyResponseVo.getSign();
            log.info("响应签名：" + responseSign);
            String checkSign = Disguiser
                    .disguiseMD5(assemblyRespOriSign.trim());
            log.info("验证签名：" + checkSign);

            // 避免重复的通知
            try {
                if (redisTemplate.hasKey(Constants.HLB_REDIS_PREFIX + notifyResponseVo.getRt5_orderId())) {
                    log.error("有重复通知" + JSON.toJSONString(notifyResponseVo));
                    return "success";
                } else {
                    redisTemplate.opsForValue().set(Constants.HLB_REDIS_PREFIX + notifyResponseVo.getRt5_orderId(), "1", 2880, TimeUnit.MINUTES);
                }
            }catch (Exception e){
                e.printStackTrace();
                log.error("写redis出错"+e.getMessage());
            }

            //获取手机号
//            String mobile;
//            if (notifyResponseVo.getRt5_orderId().contains("_")) {
//                String[] strArray = notifyResponseVo.getRt5_orderId().split("_");
//
//                if (strArray.length == 4) {
//                    mobile = strArray[3];
//                }
//            }else {
//                String orderId = notifyResponseVo.getRt5_orderId();
//                if (orderId.contains("-")) {
//                    orderId = orderId.split("-")[0];
//
//                }
//                try {
//                    OrderDetailInfo orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(orderId).getData();
//                    if (orderDetailInfo != null) {
//                        mobile = orderDetailInfo.getMobile();
//                    }
//                }catch (Exception e){
//
//                }
//            }


            if (checkSign.equals(responseSign)) {
                // 验证签名成功()
                // 商户根据根据支付结果做业务处理
                log.info("返回的orderid为"+notifyResponseVo.getRt5_orderId());
                if("0000".equals(notifyResponseVo.getRt2_retCode())) {
                    if (notifyResponseVo.getRt5_orderId().contains("_")) {

                        String[] ss = notifyResponseVo.getRt5_orderId().split("_");
                        MemberFeeLogMessage memberModel = new MemberFeeLogMessage();
                        memberModel.setDebitChannel("hlb");
                        String userId = ss[0];

                        String carryStr = (String)redisTemplate.opsForValue().get(Constants.HLB_NYD_CARRAY+userId);
                        PayCarryVo payCarryVo;
                        if(carryStr==null){
                            payCarryVo=new PayCarryVo();
                        }else {
                            payCarryVo = JSON.parseObject(carryStr,PayCarryVo.class);
                        }
                        log.info("携带回得信息为"+JSON.toJSONString(payCarryVo));

                        memberModel.setMobile(payCarryVo.getMobile());
                        memberModel.setUserId(payCarryVo.getUserId());
                        memberModel.setMemberType(payCarryVo.getMemberType());
                        memberModel.setDebitFlag("1");

                            SmsRequest smsRequest = new SmsRequest();
                            smsRequest.setCellphone(payCarryVo.getMobile());
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
                            smsRequest.setSmsType(8);
                            try {
                                ResponseData responseData = sendSmsService.sendSingleSms(smsRequest);
                                log.info("会员费发送短信结果"+JSON.toJSONString(responseData));
                            }catch (Exception e){
                                e.printStackTrace();
                                log.error("会员费发送短信失败"+e.getMessage());
                            }




                        try {
                            rabbitmqProducerProxy.convertAndSend("mfee.nyd", memberModel);
                        }catch (Exception e){
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
                        repayInfo.setBillNo(notifyResponseVo.getRt5_orderId());
                        repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getRt7_orderAmount()));
                        repayInfo.setRepayChannel("hlb");
                        repayInfo.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt11_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                        repayInfo.setRepayNo(notifyResponseVo.getRt6_serialNumber());
                        repayInfo.setRepayType(RepayType.MFEE.getCode());
                        repayInfo.setUserId(payCarryVo.getUserId());

                        try {
                            rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        LoggerUtils.write(repayInfo);

//                        try {
//                            repayContract.save(repayInfo);
//                        }catch (Exception e){
//                            e.printStackTrace();
//                            logger.error("代扣会员费成功写入数据库异常:"+JSON.toJSONString(repayInfo));
//                        }

//                        SmsRequest request = new SmsRequest();
//                        request.setSmsType();
//                        request.setCellphone();
//                        try {
//                            sendSmsService.sendSingleSms(request);
//                        }catch (Exception e){
//                            e.printStackTrace();
//                            logger.error("代扣会员费成功发送短信异常"+e.getMessage());
//                        }


                    } else {
//                    String[] ss = notifyResponseVo.getRt5_orderId().split("~");
                        String orderId = notifyResponseVo.getRt5_orderId();
                        String type = null;
                        if (orderId.contains("-")) {
                            String[] strs = orderId.split("-");
                            orderId = strs[0];

                            if(strs.length==3){
                                type = strs[2];
                            }
                        }


                        RepayMessage repayMessage = new RepayMessage();
                        repayMessage.setBillNo(orderId);
                        repayMessage.setRepayAmount(new BigDecimal(notifyResponseVo.getRt7_orderAmount()));
                        repayMessage.setRepayStatus("0");
                        log.info("还款成功回写mq");
                        rabbitmqProducerProxy.convertAndSend("repay.nyd", repayMessage);

                        try {
                            redisTemplate.opsForValue().set(Constants.PAY_PREFIX + orderId, PayStatus.PAY_SUCESS.getCode(), 100, TimeUnit.MINUTES);
                        }catch (Exception e){
                            log.info("异常",e);
                            log.info(orderId+"redis set异常hlb");
                        }

                        //银码头
                        try {
                            ResponseData<BillInfo> responseData = billContract.getBillInfo(orderId);
                            log.info("付款成功后获取billInfo"+JSON.toJSONString(responseData));
                            if ("0".equals( responseData.getStatus())){
                                BillInfo billInfo = responseData.getData();
                                if(billInfo!=null && StringUtils.isNotBlank(billInfo.getIbankOrderNo())){
                                    repayMessage.setBillNo(billInfo.getIbankOrderNo());
                                    repayMessage.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt11_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                                    log.info("还款成功发送银码头"+JSON.toJSONString(repayMessage));
                                    rabbitmqProducerProxy.convertAndSend("payIbank.ibank",repayMessage);
                                }
                            }else {
                                log.info(orderId+"获取billInfo为status为1");
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            log.info(orderId+"error",e);
                        }

                        if("k".equals(type)){
                            String[] infoarray = null;
                            try {
                                String infos = (String) redisTemplate.opsForValue().get(Constants.HLB_QUICK_PAY + orderId);
                                infoarray = infos.split("-");
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            RepayInfo repayInfo = new RepayInfo();
                            repayInfo.setRepayStatus("0");
                            repayInfo.setBillNo(orderId);
                            repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getRt7_orderAmount()));
                            repayInfo.setRepayChannel("hlb");
                            repayInfo.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt11_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                            repayInfo.setRepayNo(notifyResponseVo.getRt6_serialNumber());
                            repayInfo.setRepayType(RepayType.KJ.getCode());
                            if(infoarray!=null){
                                repayInfo.setRepayName(infoarray[0]);
                                repayInfo.setRepayIdNumber(infoarray[1]);
                                repayInfo.setRepayAccount(infoarray[2]);
                            }
                            try {
                                rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            LoggerUtils.write(repayInfo);
                        }else if("z".equals(type)){
                            RepayInfo repayInfo = new RepayInfo();
                            repayInfo.setRepayStatus("0");
                            repayInfo.setBillNo(orderId);
                            repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getRt7_orderAmount()));
                            repayInfo.setRepayChannel("hlb");
                            repayInfo.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt11_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                            repayInfo.setRepayNo(notifyResponseVo.getRt6_serialNumber());
                            repayInfo.setRepayType(RepayType.ZD.getCode());
                            try {
                                rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            LoggerUtils.write(repayInfo);
                        }else if("q".equals(type)){
                            RepayInfo repayInfo = new RepayInfo();
                            repayInfo.setRepayStatus("0");
                            repayInfo.setBillNo(orderId);
                            repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getRt7_orderAmount()));
                            repayInfo.setRepayChannel("hlb");
                            repayInfo.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt11_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                            repayInfo.setRepayNo(notifyResponseVo.getRt6_serialNumber());
                            repayInfo.setRepayType(RepayType.QK.getCode());
                            try {
                                rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            LoggerUtils.write(repayInfo);
                        }else {
                            RepayInfo repayInfo = new RepayInfo();
                            repayInfo.setRepayStatus("0");
                            repayInfo.setBillNo(orderId);
                            repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getRt7_orderAmount()));
                            repayInfo.setRepayChannel("hlb");
                            repayInfo.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt11_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                            repayInfo.setRepayNo(notifyResponseVo.getRt6_serialNumber());
                            repayInfo.setRepayType(RepayType.UNKNOW.getCode());
                            try {
                                rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            LoggerUtils.write(repayInfo);
                        }


                    }
                }else {
                    if (notifyResponseVo.getRt5_orderId().contains("_")) {
                        String[] ss = notifyResponseVo.getRt5_orderId().split("_");
                        MemberFeeLogMessage memberModel = new MemberFeeLogMessage();
                        memberModel.setDebitChannel("hlb");
                        String userId = ss[0];

                        String carryStr = (String)redisTemplate.opsForValue().get(Constants.HLB_NYD_CARRAY+userId);
                        PayCarryVo payCarryVo;
                        if(carryStr==null){
                            payCarryVo=new PayCarryVo();
                        }else {
                            payCarryVo = JSON.parseObject(carryStr,PayCarryVo.class);
                        }
                        log.info("携带回得信息为"+JSON.toJSONString(payCarryVo));

                        memberModel.setDebitFlag(notifyResponseVo.getRt2_retCode());
                        memberModel.setMemberType(payCarryVo.getMemberType());
                        memberModel.setUserId(userId);

                        memberModel.setMobile(payCarryVo.getMobile());

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
                        repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getRt7_orderAmount()));
                        repayInfo.setRepayChannel("hlb");
                        repayInfo.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt11_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                        repayInfo.setRepayNo(notifyResponseVo.getRt6_serialNumber());
                        repayInfo.setRepayType(RepayType.MFEE_FAIL.getCode());
                        //DingdingUtil.getErrMsg("合利宝支付回调接口，会费支付失败，失败信息:"+notifyResponseVo.getRt3_retMsg()+"订单号orderId:"+notifyResponseVo.getRt5_orderId());
                        try {
                            rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        LoggerUtils.write(repayInfo);
                    }else{
                        String orderId = notifyResponseVo.getRt5_orderId();
                        String type = null;
                        if (orderId.contains("-")) {

                            String[] strs = orderId.split("-");
                            orderId = strs[0];
                            if(strs.length==3){
                                type = strs[2];
                            }
                        }

                        try {
                            redisTemplate.delete(Constants.PAY_PREFIX + orderId);
                        }catch (Exception e){
                            log.info("异常",e);
                            log.info(orderId+"删除key异常hlb");
                        }
                        if("k".equals(type)){
                            String[] infoarray = null;
                            try {
                                String infos = (String) redisTemplate.opsForValue().get(Constants.HLB_QUICK_PAY + orderId);
                                infoarray = infos.split("-");
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            RepayInfo repayInfo = new RepayInfo();
                            repayInfo.setRepayStatus("1");
                            repayInfo.setBillNo(orderId);
                            repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getRt7_orderAmount()));
                            repayInfo.setRepayChannel("hlb");
                            repayInfo.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt11_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                            repayInfo.setRepayNo(notifyResponseVo.getRt6_serialNumber());
                            repayInfo.setRepayType(RepayType.KJ_FAIL.getCode());
                            if(infoarray!=null){
                                repayInfo.setRepayName(infoarray[0]);
                                repayInfo.setRepayIdNumber(infoarray[1]);
                                repayInfo.setRepayAccount(infoarray[2]);
                            }
                            try {
                                rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            LoggerUtils.write(repayInfo);
                        }else if("z".equals(type)){
                            RepayInfo repayInfo = new RepayInfo();
                            repayInfo.setRepayStatus("1");
                            repayInfo.setBillNo(orderId);
                            repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getRt7_orderAmount()));
                            repayInfo.setRepayChannel("hlb");
                            repayInfo.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt11_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                            repayInfo.setRepayNo(notifyResponseVo.getRt6_serialNumber());
                            repayInfo.setRepayType(RepayType.ZD_FAIL.getCode());
                            try {
                                rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            LoggerUtils.write(repayInfo);
                        }else if("q".equals(type)){
                            RepayInfo repayInfo = new RepayInfo();
                            repayInfo.setRepayStatus("1");
                            repayInfo.setBillNo(orderId);
                            repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getRt7_orderAmount()));
                            repayInfo.setRepayChannel("hlb");
                            repayInfo.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt11_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                            repayInfo.setRepayNo(notifyResponseVo.getRt6_serialNumber());
                            repayInfo.setRepayType(RepayType.QK_FAIL.getCode());
                            LoggerUtils.write(repayInfo);
                        }else {
                            RepayInfo repayInfo = new RepayInfo();
                            repayInfo.setRepayStatus("1");
                            repayInfo.setBillNo(orderId);
                            repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getRt7_orderAmount()));
                            repayInfo.setRepayChannel("hlb");
                            repayInfo.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt11_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                            repayInfo.setRepayNo(notifyResponseVo.getRt6_serialNumber());
                            repayInfo.setRepayType(RepayType.UNKNOW_FAIL.getCode());
                            try {
                                rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            LoggerUtils.write(repayInfo);
                        }
                    }
//                    String orderId = notifyResponseVo.getRt5_orderId();
//                    if (orderId.contains("-")) {
//                        orderId = orderId.split("-")[0];
//                    }
//                    RepayInfo repayInfo = new RepayInfo();
//                    repayInfo.setRepayStatus(notifyResponseVo.getRt2_retCode());
//                    repayInfo.setBillNo(orderId);
//                    repayInfo.setRepayStatus("1");
//                    repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getRt7_orderAmount()));
//                    repayInfo.setRepayChannel("hlb");
//                    repayInfo.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt11_completeDate(), "yyyy-MM-dd HH:mm:ss"));
//                    repayInfo.setRepayNo(notifyResponseVo.getRt6_serialNumber());
//                   repayInfo.setRepayType(ss[1]);
//                    try {
//                        repayContract.save(repayInfo);
//                    }catch (Exception e){
//                        e.printStackTrace();
//                        logger.error("还款失败写入数据库异常:"+JSON.toJSONString(repayInfo));
//                    }
//                    SmsRequest request = new SmsRequest();
//                    request.setSmsType();
//                    request.setCellphone();
//                    try {
//                        sendSmsService.sendSingleSms(request);
//                    }catch (Exception e){
//                        e.printStackTrace();
//                        logger.error("还款失败发送短信异常"+e.getMessage());
//                    }
                }
                //
                log.info("回调成功");
                return "success";// 反馈处理结果
            } else {
                log.info("回调验证签名失败");
//                SmsRequest request = new SmsRequest();
//                request.setSmsType();
//                request.setCellphone();
//                try {
//                    sendSmsService.sendSingleSms(request);
//                }catch (Exception e){
//                    e.printStackTrace();
//                    logger.error("回调验证签名失败发送短信异常"+e.getMessage());
//                }
                LoggerUtils.write("验签"+JSON.toJSONString(notifyResponseVo));
                return "fail 验证签名失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("回调异常");
            if(notifyResponseVo==null){
                LoggerUtils.write("回调异常");
            }else {
                LoggerUtils.write("回调异常" + JSON.toJSONString(notifyResponseVo));
            }
//            SmsRequest request = new SmsRequest();
//            request.setSmsType();
//            request.setCellphone();
//            try {
//                sendSmsService.sendSingleSms(request);
//            }catch (Exception e1){
//                e1.printStackTrace();
//                logger.error("回调异常过程发送短信异常"+e1.getMessage());
//            }
            return "fail 系统内部错误" + e.getMessage();// 反馈处理结果
        }
    }

    @Override
    public ResponseData firstPayType(FirstPayCreateOrderVo vo, WithHoldType type) {
        log.info("--------进入创建订单接口----------");
        vo.setP2_customerNumber(helibaoConfig.getMerchantNo());                                  //商户编号
        vo.setP23_serverCallbackUrl(helibaoConfig.getCallBackUrl());                             //回调地址
        vo.setP5_timestamp(DateFormatUtils.format(new Date(),"yyyyMMddHHmmss"));        //时间戳
        validateUtil.validate(vo);
        log.info("首次支付下单，请求参数："+JSON.toJSONString(vo));
        try {
            Map<String,String> map = helibaoConfig.convertBean(vo, new LinkedHashMap());
            String oriMessage = newHelibaoConfig.getSigned(map, null);
            log.info("签名原文串：" + oriMessage);

            String sign = Disguiser.disguiseMD5(oriMessage.trim());
            log.info("签名串：" + sign);

            map.put("sign", sign);
            log.info("发送参数：" + map);

            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, helibaoConfig.getQuickPayApiUrl());
            log.info("首次支付响应结果：" + resultMap);

            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                FirstPayCreateOrderResponseVo firstPayCreateOrderResponseVo = JSONObject.parseObject(resultMsg, FirstPayCreateOrderResponseVo.class);
                log.info("下单请求，返回的响应信息:"+firstPayCreateOrderResponseVo);

                String[] excludes = {};
                String assemblyRespOriSign = newHelibaoConfig.getSigned(firstPayCreateOrderResponseVo, excludes);
                log.info("组装返回结果签名串：" + assemblyRespOriSign);

                String responseSign = firstPayCreateOrderResponseVo.getSign();
                log.info("响应签名：" + responseSign);

                String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
                log.info("验证签名：" + checkSign);

                if (checkSign.equals(responseSign)) {
                    if ("0000".equals(firstPayCreateOrderResponseVo.getRt2_retCode())) {
                        ResponseData responseData = ResponseData.success();
                        responseData.setData(firstPayCreateOrderResponseVo);
                        log.info(vo.getP4_orderId()+"memberfee代扣成功");
                        return responseData;
                    } else {
                        log.error(vo.getP4_orderId()+"memberfee代扣失败:"+firstPayCreateOrderResponseVo.getRt3_retMsg());
                        return ResponseData.error(firstPayCreateOrderResponseVo.getRt3_retMsg());
                    }
                } else {
                    log.info(vo.getP4_orderId()+"代扣验签失败");
                    return ResponseData.error("代扣验签失败");
                }
            } else {
                log.info(vo.getP4_orderId()+"代扣请求失败:");
                return ResponseData.error("代扣请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info(vo.getP4_orderId()+"代扣异常");
            return ResponseData.error(e.getMessage());
        }
    }


    @Override
    public ResponseData firstPay(FirstPayCreateOrderVo vo) {
        log.info("--------进入创建订单接口----------");
        vo.setP2_customerNumber(helibaoConfig.getMerchantNo());                                  //商户编号
        vo.setP23_serverCallbackUrl(helibaoConfig.getCallBackUrl());                             //回调地址
        vo.setP5_timestamp(DateFormatUtils.format(new Date(),"yyyyMMddHHmmss"));        //时间戳
        validateUtil.validate(vo);
        try {
            //需要DES加密的参数
            //证件号码
          /* vo.setP8_idCardNo(Des3Encryption.encode(helibaoConfig.getDeskey(), vo.getP8_idCardNo()));
            //银行卡号
            vo.setP9_cardNo(Des3Encryption.encode(helibaoConfig.getDeskey(), vo.getP9_cardNo()));
            //手机号码
           if(vo.getP13_phone()!= null) {
                vo.setP13_phone(Des3Encryption.encode(helibaoConfig.getDeskey(), vo.getP13_phone()));
            }*/

            log.info("首次支付下单请求参数："+JSON.toJSONString(vo));

            Map<String,String> map = helibaoConfig.convertBean(vo, new LinkedHashMap());
            String oriMessage = newHelibaoConfig.getSigned(map, null);
            log.info("签名原文串：" + oriMessage);

            String sign = Disguiser.disguiseMD5(oriMessage.trim());
            log.info("签名串：" + sign);

            map.put("sign", sign);
            log.info("发送参数：" + map);

            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, helibaoConfig.getQuickPayApiUrl());
            log.info("首次支付响应结果：" + resultMap);

            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                FirstPayCreateOrderResponseVo firstPayCreateOrderResponseVo = JSONObject.parseObject(resultMsg, FirstPayCreateOrderResponseVo.class);
                log.info("下单请求返回的响应信息:"+firstPayCreateOrderResponseVo);

                String[] excludes = {};
                String assemblyRespOriSign = newHelibaoConfig.getSigned(firstPayCreateOrderResponseVo, excludes);
                log.info("组装返回结果签名串：" + assemblyRespOriSign);

                String responseSign = firstPayCreateOrderResponseVo.getSign();
                log.info("响应签名：" + responseSign);

                String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
                log.info("验证签名：" + checkSign);

                if (checkSign.equals(responseSign)) {
                    if ("0000".equals(firstPayCreateOrderResponseVo.getRt2_retCode())) {
                        ResponseData responseData = ResponseData.success();
                        responseData.setData(firstPayCreateOrderResponseVo);
                        log.info(vo.getP4_orderId()+"代扣成功");
                        return responseData;
                    } else {
                        log.error(vo.getP4_orderId()+"代扣失败:"+firstPayCreateOrderResponseVo.getRt3_retMsg());
                        return ResponseData.error(firstPayCreateOrderResponseVo.getRt3_retMsg());
                    }
                } else {
                    log.info(vo.getP4_orderId()+"代扣验签失败:");
                    return ResponseData.error("代扣验签失败");
                }
            } else {
                log.info(vo.getP4_orderId()+"代扣请求失败:");
                return ResponseData.error("代扣请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info(vo.getP4_orderId()+"代扣异常");
            return ResponseData.error(e.getMessage());
        }

    }

    @Override
    public ResponseData firstPayMessage(SendValidateCodeVo vo) {
        log.info("--------进入发送短信接口----------");
        vo.setP2_customerNumber(helibaoConfig.getMerchantNo());             //商户编号
        vo.setP4_timestamp(DateFormatUtils.format(new Date(),"yyyyMMddHHmmss"));
        validateUtil.validate(vo);
        try {
            //手机号码(需要DES加密的参数)
            /*if(vo.getP5_phone()!= null) {
                vo.setP5_phone(Des3Encryption.encode(helibaoConfig.getDeskey(), vo.getP5_phone()));
            }*/
            log.info("首次支付短信请求参数："+JSON.toJSONString(vo));

            Map<String,String> map = helibaoConfig.convertBean(vo, new LinkedHashMap());
            String oriMessage = newHelibaoConfig.getSigned(map, null);
            log.info("签名原文串：" + oriMessage);

            String sign = Disguiser.disguiseMD5(oriMessage.trim());
            log.info("签名串：" + sign);

            map.put("sign", sign);
            log.info("发送参数：" + map);

            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, helibaoConfig.getQuickPayApiUrl());
            log.info("首次支付短信请求响应结果：" + resultMap);

            if ((Integer) (resultMap.get("statusCode")) == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                SendValidateCodeResponseVo sendResponseVo = JSONObject.parseObject(resultMsg, SendValidateCodeResponseVo.class);
                log.info("短信请求返回的响应信息:"+sendResponseVo);

                String assemblyRespOriSign = newHelibaoConfig.getSigned(sendResponseVo, null);
                log.info("组装返回结果签名串(message)：" + assemblyRespOriSign);

                String responseSign = sendResponseVo.getSign();
                log.info("响应签名：" + responseSign);
                String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
                if (checkSign.equals(responseSign)) {
                    if ("0000".equals(sendResponseVo.getRt2_retCode())) {
                        ResponseData responseData = ResponseData.success();
//                        responseData.setData(sendResponseVo.getRt5_orderId());
                        SurePayVo surePayVo = new SurePayVo();
                        surePayVo.setOrderNo(sendResponseVo.getRt5_orderId());
                        responseData.setData(surePayVo);
                        log.info(vo.getP3_orderId()+"短信发送成功");
                        return responseData;
                    } else {
                        log.error(vo.getP3_orderId()+"短信发送失败:"+sendResponseVo.getRt3_retMsg());
                        return ResponseData.error(sendResponseVo.getRt3_retMsg());
                    }
                } else {
                    log.info(vo.getP3_orderId()+"短信发送验签失败:");
                    return ResponseData.error("短信发送验签失败");
                }

            }else {
                log.info(vo.getP3_orderId()+"首次支付短信请求失败:");
                return ResponseData.error("首次支付短信请求失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            log.info(vo.getP3_orderId()+"首次支付短信异常");
            return ResponseData.error(e.getMessage());
        }

    }

    @Override
    public ResponseData confirmPay(ConfirmPayVo vo) {
        log.info("--------进入确认支付接口----------");
        vo.setP2_customerNumber(helibaoConfig.getMerchantNo());             //商户编号
        vo.setP4_timestamp(DateFormatUtils.format(new Date(),"yyyyMMddHHmmss"));
        validateUtil.validate(vo);
        log.info("确认支付请求参数："+JSON.toJSONString(vo));
        try {
            Map<String, String> map = helibaoConfig.convertBean(vo, new LinkedHashMap());
            String oriMessage =  newHelibaoConfig.getSigned(map, null);
            log.info("确认支付签名原文串：" + oriMessage);

            String sign = Disguiser.disguiseMD5(oriMessage.trim());
            log.info("确认支付签名串：" + sign);

            map.put("sign", sign);
            log.info("发送参数：" + map);

            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, helibaoConfig.getQuickPayApiUrl());
            log.info("确认支付响应结果：" + resultMap);
            if ((Integer) (resultMap.get("statusCode")) == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                ConfirmPayResponseVo confirmPayResponseVo = JSONObject.parseObject(resultMsg, ConfirmPayResponseVo.class);
                log.info("确认支付返回的响应信息:"+confirmPayResponseVo);

                String assemblyRespOriSign = newHelibaoConfig.getSigned(confirmPayResponseVo, null);
                log.info("确认支付，组装返回结果签名串：" + assemblyRespOriSign);

                String responseSign = confirmPayResponseVo.getSign();
                log.info("确认支付，响应签名：" + responseSign);

                String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
                if (checkSign.equals(responseSign)) {
                    if ("0000".equals(confirmPayResponseVo.getRt2_retCode())) {
                        log.info(vo.getP3_orderId()+"确认支付成功");
                        return ResponseData.success();
                    } else {
                        log.error(vo.getP3_orderId()+"确认支付失败:"+confirmPayResponseVo.getRt3_retMsg());
                        //DingdingUtil.getErrMsg("合利宝确认支付接口，支付失败，失败信息:"+confirmPayResponseVo.getRt3_retMsg()+"商户订单号"+vo.getP3_orderId());
                        return ResponseData.error(confirmPayResponseVo.getRt3_retMsg());
                    }

                }else {
                    log.info(vo.getP3_orderId()+"确认支付验签失败:");
//                    return ResponseData.error("确认支付验签失败");
                    return ResponseData.error(confirmPayResponseVo.getRt3_retMsg());
                }
            }else {
                log.info(vo.getP3_orderId()+"确认支付请求失败:");
                return ResponseData.error("确认支付请求失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            log.info(vo.getP3_orderId()+"确认支付异常");
            return ResponseData.error(e.getMessage());
        }

    }

    @Override
    public String hlbCallback(String callback) {
        log.info("合利宝异步通知："+callback);
        AsynNotifyResponseVo notifyResponseVo = null;
        try {
            notifyResponseVo = CallBackUtils.parse(callback,AsynNotifyResponseVo.class);
            log.info("回调结果："+ JSON.toJSONString(notifyResponseVo));

            String assemblyRespOriSign = newHelibaoConfig.getSigned(notifyResponseVo, null);
            log.info("回调，组装返回结果签名串：" + assemblyRespOriSign);

            String responseSign = notifyResponseVo.getSign();
            log.info("回调，响应签名：" + responseSign);

            String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
            log.info("回调，验证签名：" + checkSign);



            if (checkSign.equals(responseSign)) {                   // 验证签名成功()
                // 商户根据根据支付结果做业务处理
                // 避免重复的通知
                try {
                    if (redisTemplate.hasKey(Constants.HLB_REDIS_PREFIX + notifyResponseVo.getRt5_orderId())) {
                        log.error("有重复通知:" + JSON.toJSONString(notifyResponseVo));
                        return "success";
                    } else {
                        redisTemplate.opsForValue().set(Constants.HLB_REDIS_PREFIX + notifyResponseVo.getRt5_orderId(), "1", 2880, TimeUnit.MINUTES);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    log.error("写redis出错"+e.getMessage());
                }
                log.info("返回的orderid为"+notifyResponseVo.getRt5_orderId());
                if("0000".equals(notifyResponseVo.getRt2_retCode())) {
                    if (notifyResponseVo.getRt5_orderId().contains("_")) {

                        String[] ss = notifyResponseVo.getRt5_orderId().split("_");
                        MemberFeeLogMessage memberModel = new MemberFeeLogMessage();
                        memberModel.setDebitChannel("hlb");
                        String userId = ss[0];
                        //平台账号
                        String mobile = getMobile(userId);

                        String carryStr = (String)redisTemplate.opsForValue().get(Constants.HLB_NYD_CARRAY+userId);
                        PayCarryVo payCarryVo;
                        if(carryStr == null){
                            payCarryVo = new PayCarryVo();
                        }else {
                            payCarryVo = JSON.parseObject(carryStr,PayCarryVo.class);
                        }
                        log.info("携带回得信息为"+JSON.toJSONString(payCarryVo));

//                        memberModel.setMobile(payCarryVo.getMobile());
                        memberModel.setMobile(mobile);
                        memberModel.setUserId(payCarryVo.getUserId());
                        memberModel.setMemberType(payCarryVo.getMemberType());
                        memberModel.setDebitFlag("1");
                        //优惠券id
                        if (StringUtils.isNotBlank(payCarryVo.getCouponId())){
                            memberModel.setCouponId(payCarryVo.getCouponId());
                        }

                        //现金券使用金额
                        memberModel.setCouponUseFee(payCarryVo.getCouponUseFee());

                        //此笔支付，需要支付的金额
                        memberModel.setPayCash(new BigDecimal(notifyResponseVo.getRt8_orderAmount()));
                        log.info("合利宝支付会员费，给member发送mq消息的对象："+JSON.toJSON(memberModel));

                        SmsRequest smsRequest = new SmsRequest();
                        smsRequest.setCellphone(payCarryVo.getMobile());
                        smsRequest.setSmsType(8);
//                        ResponseData data = userSourceContract.selectUserSourceByMobile(payCarryVo.getMobile());
                        ResponseData data = userSourceContract.selectUserSourceByMobile(mobile);
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
                            log.info("会员费发送短信结果："+JSON.toJSONString(responseData));
                        }catch (Exception e){
                            e.printStackTrace();
                            log.error("会员费发送短信失败："+e.getMessage());
                        }

                        memberModel.setAppName(payCarryVo.getAppName());
                        log.info("合利宝支付评估费成功,发送mq对象:"+JSON.toJSON(memberModel));
                        try {
                            rabbitmqProducerProxy.convertAndSend("mfee.nyd", memberModel);
                            log.info("合利宝支付评估费成功,发送mq对象成功");
                        }catch (Exception e){
                            log.error("合利宝支付评估费成功,发送mq对象异常",e);
                        }

                        MemberCarryResp resp = new MemberCarryResp();
//                        resp.setMobile(payCarryVo.getMobile());
                        resp.setMobile(mobile);
                        resp.setProductCode(payCarryVo.getProductCode());
                        resp.setStatus("0");
                        redisProcessService.putPayStatusMember(userId,JSON.toJSONString(resp));

                        RepayInfo repayInfo = new RepayInfo();
                        repayInfo.setRepayStatus("0");
                        repayInfo.setBillNo(notifyResponseVo.getRt5_orderId());
                        repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getRt8_orderAmount()));
                        repayInfo.setRepayChannel("hlb");
                        repayInfo.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt7_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                        repayInfo.setRepayNo(notifyResponseVo.getRt6_serialNumber());
                        repayInfo.setRepayType(RepayType.MFEE.getCode());
                        repayInfo.setUserId(payCarryVo.getUserId());
                        repayInfo.setAppName(payCarryVo.getAppName());
                        //优惠券id
                        if (StringUtils.isNotBlank(payCarryVo.getCouponId())){
                            repayInfo.setCouponId(payCarryVo.getCouponId());
                        }
                        //现金券使用金额
                        repayInfo.setCouponUseFee(payCarryVo.getCouponUseFee());
                        log.info("合利宝支付会员费，给repay发送mq消息的对象："+JSON.toJSON(repayInfo));

                        try {
                            rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                            log.info("合利宝支付评估费成功,发送流水mq成功");
                        }catch (Exception e){
                            log.error("合利宝支付评估费成功,发送流水mq异常",e);
                        }
                        LoggerUtils.write(repayInfo);

                    } else if (notifyResponseVo.getRt5_orderId().contains("xj")){                   //处理现金券购买完成后的逻辑
                        String[] ss = notifyResponseVo.getRt5_orderId().split("xj");
                        String userId = ss[0];
                        //平台账号
                        String mobile = getMobile(userId);

                        String carryStr = (String)redisTemplate.opsForValue().get(Constants.HLB_NYD_CARRAY+userId);
                        PayCarryVo payCarryVo;
                        if(carryStr == null){
                            payCarryVo = new PayCarryVo();
                        }else {
                            payCarryVo = JSON.parseObject(carryStr,PayCarryVo.class);
                        }
                        log.info("redis携带回来的信息为："+JSON.toJSONString(payCarryVo));

                        RechargeFeeInfo rechargeFeeInfo = new RechargeFeeInfo();
                        rechargeFeeInfo.setUserId(userId);
//                        rechargeFeeInfo.setAccountNumber(payCarryVo.getMobile());
                        rechargeFeeInfo.setAccountNumber(mobile);
                        rechargeFeeInfo.setAmount(new BigDecimal(notifyResponseVo.getRt8_orderAmount()));
                        rechargeFeeInfo.setCashId(payCarryVo.getCashId());
                        rechargeFeeInfo.setOperStatus(1);          //0：失败  1：成功
                        rechargeFeeInfo.setRechargeFlowNo(notifyResponseVo.getRt6_serialNumber());
                        rechargeFeeInfo.setStatusMsg(notifyResponseVo.getRt3_retMsg());
                        rechargeFeeInfo.setUserType(1);   //用户充值现金券收入
                        rechargeFeeInfo.setCashDescription("用户充值收入");
                        rechargeFeeInfo.setAppName(payCarryVo.getAppName());
                        log.info("hlb支付完成，发送mq现金券对象："+JSON.toJSON(rechargeFeeInfo));

                        try {
                            rabbitmqProducerProxy.convertAndSend("rechargeCoupon.nyd", rechargeFeeInfo);
                            log.info("hlb支付完成，发送mq现金券对象完毕");
                        }catch (Exception e){
                            log.error("hlb支付完成，发送mq现金券对象异常",e);
                            e.printStackTrace();
                        }

                        /**
                         * 给用户发信息，告诉用户现金券购买成功
                         */
                        //充值现金券不发短信
                        /*SmsRequest smsRequest = new SmsRequest();
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
                            log.info("现金券购买结果发送短信成功："+JSON.toJSONString(responseData));
                        }catch (Exception e){
                            e.printStackTrace();
                            log.error("现金券购买结果发送短信失败："+e.getMessage());
                        }*/

                        MemberCarryResp resp = new MemberCarryResp();
//                        resp.setMobile(payCarryVo.getMobile());
                        resp.setMobile(mobile);
                        resp.setProductCode(payCarryVo.getProductCode());
                        resp.setStatus("0");
                        redisProcessService.putPayStatusMember(userId,JSON.toJSONString(resp));

                        /**
                         * 购买现金券的流水写入表t_repay
                         */
                        RepayInfo repayInfo = new RepayInfo();
                        repayInfo.setRepayStatus("0");      //0：成功；1：失败
                        repayInfo.setBillNo(notifyResponseVo.getRt5_orderId());
                        repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getRt8_orderAmount()));
                        repayInfo.setRepayChannel("hlb");
                        repayInfo.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt7_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                        repayInfo.setRepayNo(notifyResponseVo.getRt6_serialNumber());
                        repayInfo.setRepayType(RepayType.COUPON_FEE.getCode());
                        repayInfo.setUserId(payCarryVo.getUserId());
                        repayInfo.setAppName(payCarryVo.getAppName());
                        log.info("充值现金券成功，支付流水对象："+JSON.toJSON(repayInfo));

                        try {
                            rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                            log.info("充值现金券成功,支付流水mq发送成功");
                        }catch (Exception e){
                            log.error("充值现金券成功,支付流水mq发送异常",e);
                        }
                        LoggerUtils.write(repayInfo);

                    }else if (notifyResponseVo.getRt5_orderId().contains("kzjr")){    //空中金融还款
                        String orderId = notifyResponseVo.getRt5_orderId();
                        log.info("kzjr返回的orderId:"+orderId);
                        //账单编号
                        String billNo = "";
                        String assetCode = "";
                        if (orderId.contains("-")){
                            String[] ss = orderId.split("-");
                            billNo = ss[0];
                        }
                        try {
                            ResponseData<BillYmtInfo> responseData = billYmtContract.getBillYmtByBillNo(billNo);
                            if (responseData!=null&&"0".equals(responseData.getStatus())) {
                                BillYmtInfo billYmtInfo = responseData.getData();
                                assetCode = billYmtInfo.getAssetCode();
                            }
                        } catch (Exception e) {
                            log.error("getBillYmtByBillNo has error, billNo is "+billNo,e);
                        }
                        log.info("账单编号:"+billNo+",资产编号:"+assetCode);

                        //取出还款时存入Redis里面的信息
                        String[] arr = null;
                        try {
                            String infos = (String) redisTemplate.opsForValue().get(Constants.HLB_QUICK_PAY + billNo);
                            arr = infos.split("-");
                        }catch (Exception e){
                            log.error("(还款成功时)取出还款时存入Redis里面的信息出错",e);
                            e.printStackTrace();
                        }

                        /**
                         *  给流水表发送mq
                         */
                        RepayInfo repayInfo = new RepayInfo();
                        repayInfo.setRepayStatus("0");
                        repayInfo.setBillNo(billNo);
                        repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getRt8_orderAmount()));
                        repayInfo.setRepayChannel("hlb");
                        repayInfo.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt7_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                        repayInfo.setRepayNo(notifyResponseVo.getRt6_serialNumber());
                        repayInfo.setRepayType(RepayType.KZJR_REPAY.getCode());
                        if(arr != null){
                            repayInfo.setRepayName(arr[0]);
                            repayInfo.setRepayIdNumber(arr[1]);
                            repayInfo.setRepayAccount(arr[2]);
                            repayInfo.setRepayAccount(arr[3]);
                        }
                        log.info("空中金融还款存入repay表中的对象:"+JSON.toJSON(repayInfo));
                        try {
                            rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                            log.info("空中金融还款发送mq到流水表成功");
                        }catch (Exception e){
                            log.error("空中金融还款发送mq到流水表出错",e);
                            e.printStackTrace();
                        }

                        /**
                         * 通知空中金融
                         */
                        com.ibank.pay.model.RepayMessage repayMessage = new com.ibank.pay.model.RepayMessage();
                        try {
                            ResponseData<BillYmtInfo> data = billYmtContract.findByAssetCode(assetCode);
                            log.info("根据资产编号查找账单信息成功");
                            BillYmtInfo billYmtInfo = data.getData();
                            log.info("根据资产编号查找账单信息:"+JSON.toJSON(billYmtInfo));

                            //子订单号
                            String orderSno = billYmtInfo.getOrderSno();
                            repayMessage.setBillNo(orderSno);

                            //资产编号
                            repayMessage.setAssetNo(assetCode);

                            //还款金额
                            repayMessage.setRepayAmount(new BigDecimal(notifyResponseVo.getRt8_orderAmount()));

                            //还款状态
                            repayMessage.setRepayStatus("0");//还款状态  0：表示成功   1：表示失败

                            //还款时间
                            repayMessage.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt7_completeDate(), "yyyy-MM-dd HH:mm:ss"));

                        }catch (Exception e){
                            log.error("根据资产编号查找账单信息出错",e);
                            e.printStackTrace();
                        }

                        /**
                         * 还款后给订单发mq
                         *
                         */
                        try {
                            rabbitmqProducerProxy.convertAndSend("repayKzjrOrder.order",repayMessage);
                            log.info("通过hlb,kzjr发送mq消息到order成功");
                        }catch (Exception e){
                            log.error("通过hlb,kzjr发送mq消息到order出错",e);
                            e.printStackTrace();
                        }

                    } else {
                        String orderId = notifyResponseVo.getRt5_orderId();
                        String type = null;
                        if (orderId.contains("-")) {
                            String[] strs = orderId.split("-");
                            orderId = strs[0];

                            if(strs.length==3){
                                type = strs[2];
                            }
                        }

                        RepayMessage repayMessage = new RepayMessage();
                        repayMessage.setBillNo(orderId);
                        repayMessage.setRepayAmount(new BigDecimal(notifyResponseVo.getRt8_orderAmount()));
                        repayMessage.setRepayStatus("0");
                        log.info("还款成功回写mq");
                        rabbitmqProducerProxy.convertAndSend("repay.nyd", repayMessage);

                        try {
                            redisTemplate.opsForValue().set(Constants.PAY_PREFIX + orderId, PayStatus.PAY_SUCESS.getCode(), 100, TimeUnit.MINUTES);
                        }catch (Exception e){
                            log.info("异常",e);
                            log.info(orderId+"redis set异常hlb");
                        }

                        //银码头
                        try {
                            ResponseData<BillInfo> responseData = billContract.getBillInfo(orderId);
                            log.info("付款成功后获取billInfo"+JSON.toJSONString(responseData));
                            if ("0".equals( responseData.getStatus())){
                                BillInfo billInfo = responseData.getData();
                                if(billInfo != null && StringUtils.isNotBlank(billInfo.getIbankOrderNo())){
                                    repayMessage.setBillNo(billInfo.getIbankOrderNo());
                                    repayMessage.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt7_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                                    log.info("还款成功发送银码头"+JSON.toJSONString(repayMessage));
                                    rabbitmqProducerProxy.convertAndSend("payIbank.ibank",repayMessage);
                                }
                            }else {
                                log.info(orderId+"获取billInfo为status为1");
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            log.info(orderId+"error",e);
                        }

                        if("k".equals(type)){
                            String[] infoarray = null;
                            try {
                                String infos = (String) redisTemplate.opsForValue().get(Constants.HLB_QUICK_PAY + orderId);
                                infoarray = infos.split("-");
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            RepayInfo repayInfo = new RepayInfo();
                            repayInfo.setRepayStatus("0");
                            repayInfo.setBillNo(orderId);
                            repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getRt8_orderAmount()));
                            repayInfo.setRepayChannel("hlb");
                            repayInfo.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt7_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                            repayInfo.setRepayNo(notifyResponseVo.getRt6_serialNumber());
                            repayInfo.setRepayType(RepayType.KJ.getCode());
                            if(infoarray!=null){
                                repayInfo.setRepayName(infoarray[0]);
                                repayInfo.setRepayIdNumber(infoarray[1]);
                                repayInfo.setRepayAccount(infoarray[2]);
                                repayInfo.setAppName(infoarray[3]);
                            }
                            try {
                                rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            LoggerUtils.write(repayInfo);
                        }else if("z".equals(type)){
                            String[] infoarray = null;
                            try {
                                String infos = (String) redisTemplate.opsForValue().get(Constants.HLB_QUICK_PAY + orderId);
                                infoarray = infos.split("-");
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            RepayInfo repayInfo = new RepayInfo();
                            repayInfo.setRepayStatus("0");
                            repayInfo.setBillNo(orderId);
                            repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getRt8_orderAmount()));
                            repayInfo.setRepayChannel("hlb");
                            repayInfo.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt7_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                            repayInfo.setRepayNo(notifyResponseVo.getRt6_serialNumber());
                            repayInfo.setRepayType(RepayType.ZD.getCode());
                            if(infoarray!=null){
                                repayInfo.setRepayName(infoarray[0]);
                                repayInfo.setRepayIdNumber(infoarray[1]);
                                repayInfo.setRepayAccount(infoarray[2]);
                                repayInfo.setAppName(infoarray[3]);
                            }
                            try {
                                rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            LoggerUtils.write(repayInfo);
                        }else if("q".equals(type)){
                            String[] infoarray = null;
                            try {
                                String infos = (String) redisTemplate.opsForValue().get(Constants.HLB_QUICK_PAY + orderId);
                                infoarray = infos.split("-");
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            RepayInfo repayInfo = new RepayInfo();
                            repayInfo.setRepayStatus("0");
                            repayInfo.setBillNo(orderId);
                            repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getRt8_orderAmount()));
                            repayInfo.setRepayChannel("hlb");
                            repayInfo.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt7_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                            repayInfo.setRepayNo(notifyResponseVo.getRt6_serialNumber());
                            repayInfo.setRepayType(RepayType.QK.getCode());
                            if(infoarray!=null){
                                repayInfo.setRepayName(infoarray[0]);
                                repayInfo.setRepayIdNumber(infoarray[1]);
                                repayInfo.setRepayAccount(infoarray[2]);
                                repayInfo.setAppName(infoarray[3]);
                            }
                            try {
                                rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            LoggerUtils.write(repayInfo);
                        }else {
                            String[] infoarray = null;
                            try {
                                String infos = (String) redisTemplate.opsForValue().get(Constants.HLB_QUICK_PAY + orderId);
                                infoarray = infos.split("-");
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            RepayInfo repayInfo = new RepayInfo();
                            repayInfo.setRepayStatus("0");
                            repayInfo.setBillNo(orderId);
                            repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getRt8_orderAmount()));
                            repayInfo.setRepayChannel("hlb");
                            repayInfo.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt7_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                            repayInfo.setRepayNo(notifyResponseVo.getRt6_serialNumber());
                            repayInfo.setRepayType(RepayType.UNKNOW.getCode());
                            if(infoarray!=null){
                                repayInfo.setRepayName(infoarray[0]);
                                repayInfo.setRepayIdNumber(infoarray[1]);
                                repayInfo.setRepayAccount(infoarray[2]);
                                repayInfo.setAppName(infoarray[3]);
                            }
                            try {
                                rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            LoggerUtils.write(repayInfo);
                        }


                    }
                }else {
                    if (notifyResponseVo.getRt5_orderId().contains("_")) {                 //支付评估费(之前的会员费)失败
                        String[] ss = notifyResponseVo.getRt5_orderId().split("_");
                        MemberFeeLogMessage memberModel = new MemberFeeLogMessage();
                        memberModel.setDebitChannel("hlb");
                        String userId = ss[0];
                        //平台账号
                        String mobile = getMobile(userId);

                        String carryStr = (String)redisTemplate.opsForValue().get(Constants.HLB_NYD_CARRAY+userId);
                        PayCarryVo payCarryVo;
                        if(carryStr==null){
                            payCarryVo=new PayCarryVo();
                        }else {
                            payCarryVo = JSON.parseObject(carryStr,PayCarryVo.class);
                        }
                        log.info("携带回得信息为"+JSON.toJSONString(payCarryVo));

                        memberModel.setDebitFlag(notifyResponseVo.getRt2_retCode());
                        memberModel.setMemberType(payCarryVo.getMemberType());
                        memberModel.setUserId(userId);
//                        memberModel.setMobile(payCarryVo.getMobile());
                        memberModel.setMobile(mobile);
                        //优惠券id
                        if (StringUtils.isNotBlank(payCarryVo.getCouponId())){
                            memberModel.setCouponId(payCarryVo.getCouponId());
                        }

                        //现金券使用金额
                        memberModel.setCouponUseFee(payCarryVo.getCouponUseFee());

                        //此笔支付，需要支付的金额
                        memberModel.setPayCash(new BigDecimal(notifyResponseVo.getRt8_orderAmount()));
                        log.info("合利宝支付会员费(fail)，给member发送mq消息的对象："+JSON.toJSON(memberModel));
                        memberModel.setAppName(payCarryVo.getAppName());
                        try {
                            rabbitmqProducerProxy.convertAndSend("mfee.nyd", memberModel);
                            log.info("合利宝支付评估费(fail),给member发送mq成功");
                        }catch (Exception e){
                            log.error("合利宝支付评估费(fail),给member发送mq异常",e);
                        }

                        MemberCarryResp resp = new MemberCarryResp();
//                        resp.setMobile(payCarryVo.getMobile());
                        resp.setMobile(mobile);
                        resp.setProductCode(payCarryVo.getProductCode());
                        resp.setStatus("1");
                        redisProcessService.putPayStatusMember(userId,JSON.toJSONString(resp));

                        RepayInfo repayInfo = new RepayInfo();
                        repayInfo.setRepayStatus("1");
                        repayInfo.setBillNo(userId+"_"+System.currentTimeMillis());
                        repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getRt8_orderAmount()));
                        repayInfo.setRepayChannel("hlb");
                        repayInfo.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt7_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                        repayInfo.setRepayNo(notifyResponseVo.getRt6_serialNumber());
                        repayInfo.setRepayType(RepayType.MFEE_FAIL.getCode());
                        repayInfo.setAppName(payCarryVo.getAppName());
                        //优惠券id
                        if (StringUtils.isNotBlank(payCarryVo.getCouponId())){
                            repayInfo.setCouponId(payCarryVo.getCouponId());
                        }

                        //现金券使用金额
                        repayInfo.setCouponUseFee(payCarryVo.getCouponUseFee());
                        log.info("合利宝支付会员费(fail)，给repay发送mq消息的对象："+JSON.toJSON(repayInfo));


                        try {
                            rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                            log.info("合利宝支付评估费(fail),给repay发送mq成功");
                        }catch (Exception e){
                            log.error("合利宝支付评估费(fail),给repay发送mq异常",e);
                        }
                        LoggerUtils.write(repayInfo);
                    }else if (notifyResponseVo.getRt5_orderId().contains("xj")){                //表示充值现金券支付失败

                        String[] ss = notifyResponseVo.getRt5_orderId().split("xj");
                        String userId = ss[0];
                        //平台账号
                        String mobile = getMobile(userId);

                        String carryStr = (String)redisTemplate.opsForValue().get(Constants.HLB_NYD_CARRAY+userId);
                        PayCarryVo payCarryVo;

                        if(carryStr == null){
                            payCarryVo = new PayCarryVo();
                        }else {
                            payCarryVo = JSON.parseObject(carryStr,PayCarryVo.class);
                        }
                        log.info("redis携带回来的信息为："+JSON.toJSONString(payCarryVo));

                        MemberCarryResp resp = new MemberCarryResp();
//                        resp.setMobile(payCarryVo.getMobile());
                        resp.setMobile(mobile);
                        resp.setProductCode(payCarryVo.getProductCode());
                        resp.setStatus("1");
                        redisProcessService.putPayStatusMember(userId,JSON.toJSONString(resp));

                        RechargeFeeInfo rechargeFeeInfo = new RechargeFeeInfo();
                        rechargeFeeInfo.setUserId(userId);
//                        rechargeFeeInfo.setAccountNumber(payCarryVo.getMobile());
                        rechargeFeeInfo.setAccountNumber(mobile);
                        rechargeFeeInfo.setAmount(new BigDecimal(notifyResponseVo.getRt8_orderAmount()));
                        rechargeFeeInfo.setCashId(payCarryVo.getCashId());
                        rechargeFeeInfo.setOperStatus(0);   //0：失败  1：成功
                        rechargeFeeInfo.setRechargeFlowNo(notifyResponseVo.getRt6_serialNumber());
                        rechargeFeeInfo.setStatusMsg(notifyResponseVo.getRt3_retMsg());
                        rechargeFeeInfo.setUserType(1);   //用户充值现金券收入
                        rechargeFeeInfo.setCashDescription("用户充值收入");
                        rechargeFeeInfo.setAppName(payCarryVo.getAppName());
                        log.info("hlb支付完成（fail），发送mq现金券对象："+JSON.toJSON(rechargeFeeInfo));

                        try {
                            rabbitmqProducerProxy.convertAndSend("rechargeCoupon.nyd", rechargeFeeInfo);
                            log.info("hlb支付完成（fail），发送mq现金券对象完毕");
                        }catch (Exception e){
                            log.error("hlb支付完成(fail)，发送mq现金券对象异常",e);
                        }

                        /**
                         * 用户现金券购买失败，不给用户发信息
                         */

                        /**
                         * 购买现金券失败，流水写入表t_repay
                         */
                        RepayInfo repayInfo = new RepayInfo();
                        repayInfo.setRepayStatus("1");  //0：成功；1：失败',
                        repayInfo.setBillNo(notifyResponseVo.getRt5_orderId());
                        repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getRt8_orderAmount()));
                        repayInfo.setRepayChannel("hlb");
                        repayInfo.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt7_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                        repayInfo.setRepayNo(notifyResponseVo.getRt6_serialNumber());
                        repayInfo.setRepayType(RepayType.COUPON_FEE.getCode());
                        repayInfo.setUserId(payCarryVo.getUserId());
                        repayInfo.setAppName(payCarryVo.getAppName());
                        log.info("购买现金券失败，支付流水对象："+JSON.toJSON(repayInfo));

                        try {
                            rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                            log.info("合利宝购买现金券失败,发送流水mq成功");
                        }catch (Exception e){
                            log.error("合利宝购买现金券失败,发送流水mq异常",e);
                        }
                        LoggerUtils.write(repayInfo);

                    }else if (notifyResponseVo.getRt5_orderId().contains("kzjr")){    //空中金融还款(失败)
                        String orderId = notifyResponseVo.getRt5_orderId();
                        log.info("kzjr返回的orderId:"+orderId);
                        //账单编号
                        String billNo = "";
                        //资产编号
                        String assetCode = "";
                        if (orderId.contains("-")){
                            String[] ss = orderId.split("-");
                            billNo = ss[0];
                            assetCode = ss[1];
                        }
                        log.info("账单编号:"+billNo+",资产编号:"+assetCode);

                        //取出还款时存入Redis里面的信息
                        String[] arr = null;
                        try {
                            String infos = (String) redisTemplate.opsForValue().get(Constants.HLB_QUICK_PAY + billNo);
                            arr = infos.split("-");
                        }catch (Exception e){
                            log.error("取出还款时存入Redis里面的信息出错",e);
                            e.printStackTrace();
                        }

                        /**
                         *  给流水表发送mq
                         */
                        RepayInfo repayInfo = new RepayInfo();
                        repayInfo.setRepayStatus("1");    //0：成功；1：失败,
                        repayInfo.setBillNo(billNo);
                        repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getRt8_orderAmount()));
                        repayInfo.setRepayChannel("hlb");
                        repayInfo.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt7_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                        repayInfo.setRepayNo(notifyResponseVo.getRt6_serialNumber());
                        repayInfo.setRepayType(RepayType.KZJR_REPAY.getCode());
                        if(arr != null){
                            repayInfo.setRepayName(arr[0]);
                            repayInfo.setRepayIdNumber(arr[1]);
                            repayInfo.setRepayAccount(arr[2]);
                            repayInfo.setAppName(arr[3]);
                        }
                        log.info("空中金融还款存入repay表中的对象:"+JSON.toJSON(repayInfo));
                        try {
                            rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                            log.info("空中金融还款发送mq到流水表成功(fail)");
                        }catch (Exception e){
                            log.info("空中金融还款发送mq到流水表出错(fail)",e);
                            e.printStackTrace();
                        }

                        /**
                         * 还款失败,就不通知空中金融
                         */

                    } else{
                        String orderId = notifyResponseVo.getRt5_orderId();
                        String type = null;
                        if (orderId.contains("-")) {

                            String[] strs = orderId.split("-");
                            orderId = strs[0];
                            if(strs.length==3){
                                type = strs[2];
                            }
                        }

                        try {
                            redisTemplate.delete(Constants.PAY_PREFIX + orderId);
                        }catch (Exception e){
                            log.info("异常",e);
                            log.info(orderId+"删除key异常hlb");
                        }
                        if("k".equals(type)){
                            String[] infoarray = null;
                            try {
                                String infos = (String) redisTemplate.opsForValue().get(Constants.HLB_QUICK_PAY + orderId);
                                infoarray = infos.split("-");
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            RepayInfo repayInfo = new RepayInfo();
                            repayInfo.setRepayStatus("1");
                            repayInfo.setBillNo(orderId);
                            repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getRt8_orderAmount()));
                            repayInfo.setRepayChannel("hlb");
                            repayInfo.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt7_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                            repayInfo.setRepayNo(notifyResponseVo.getRt6_serialNumber());
                            repayInfo.setRepayType(RepayType.KJ_FAIL.getCode());
                            if(infoarray!=null){
                                repayInfo.setRepayName(infoarray[0]);
                                repayInfo.setRepayIdNumber(infoarray[1]);
                                repayInfo.setRepayAccount(infoarray[2]);
                                repayInfo.setAppName(infoarray[3]);
                            }
                            try {
                                rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            LoggerUtils.write(repayInfo);
                        }else if("z".equals(type)){
                            String[] infoarray = null;
                            try {
                                String infos = (String) redisTemplate.opsForValue().get(Constants.HLB_QUICK_PAY + orderId);
                                infoarray = infos.split("-");
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            RepayInfo repayInfo = new RepayInfo();
                            repayInfo.setRepayStatus("1");
                            repayInfo.setBillNo(orderId);
                            repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getRt8_orderAmount()));
                            repayInfo.setRepayChannel("hlb");
                            repayInfo.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt7_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                            repayInfo.setRepayNo(notifyResponseVo.getRt6_serialNumber());
                            repayInfo.setRepayType(RepayType.ZD_FAIL.getCode());
                            if(infoarray!=null){
                                repayInfo.setAppName(infoarray[3]);
                            }
                            try {
                                rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            LoggerUtils.write(repayInfo);
                        }else if("q".equals(type)){
                            RepayInfo repayInfo = new RepayInfo();
                            repayInfo.setRepayStatus("1");
                            repayInfo.setBillNo(orderId);
                            repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getRt8_orderAmount()));
                            repayInfo.setRepayChannel("hlb");
                            repayInfo.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt7_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                            repayInfo.setRepayNo(notifyResponseVo.getRt6_serialNumber());
                            repayInfo.setRepayType(RepayType.QK_FAIL.getCode());
                            LoggerUtils.write(repayInfo);
                        }else {
                            String[] infoarray = null;
                            try {
                                String infos = (String) redisTemplate.opsForValue().get(Constants.HLB_QUICK_PAY + orderId);
                                infoarray = infos.split("-");
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            RepayInfo repayInfo = new RepayInfo();
                            repayInfo.setRepayStatus("1");
                            repayInfo.setBillNo(orderId);
                            repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getRt8_orderAmount()));
                            repayInfo.setRepayChannel("hlb");
                            repayInfo.setRepayTime(DateUtils.parseDate(notifyResponseVo.getRt7_completeDate(), "yyyy-MM-dd HH:mm:ss"));
                            repayInfo.setRepayNo(notifyResponseVo.getRt6_serialNumber());
                            repayInfo.setRepayType(RepayType.UNKNOW_FAIL.getCode());
                            if(infoarray!=null){
                                repayInfo.setAppName(infoarray[3]);
                            }
                            try {
                                rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            LoggerUtils.write(repayInfo);
                        }
                    }

                }
                //
                log.info("回调成功");
                return "success";// 反馈处理结果
            } else {
                log.info("回调验证签名失败");
                LoggerUtils.write("验签"+JSON.toJSONString(notifyResponseVo));
                return "fail 验证签名失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("回调异常");
            if(notifyResponseVo==null){
                LoggerUtils.write("回调异常");
            }else {
                LoggerUtils.write("回调异常" + JSON.toJSONString(notifyResponseVo));
            }
            return "fail 系统内部错误" + e.getMessage();// 反馈处理结果
        }


    }

    @Override
    public ResponseData bindCardMessage(BindCardMessageVo vo) {
        log.info("--------进入鉴权绑卡短信接口----------");
        vo.setP2_customerNumber(helibaoConfig.getMerchantNo());             //商户编号
        vo.setP5_timestamp(DateFormatUtils.format(new Date(),"yyyyMMddHHmmss"));
        validateUtil.validate(vo);
        try {
            //需要DES加密的参数
            //银行卡号
            vo.setP6_cardNo(Des3Encryption.encode(helibaoConfig.getDeskey(), vo.getP6_cardNo()));
            //手机号码
            if(vo.getP7_phone()!= null) {
                vo.setP7_phone(Des3Encryption.encode(helibaoConfig.getDeskey(), vo.getP7_phone()));
            }
            log.info("鉴权绑卡短信请求参数："+JSON.toJSONString(vo));

            Map<String,String> map = helibaoConfig.convertBean(vo, new LinkedHashMap());
            String oriMessage = helibaoConfig.getSigned(map, null);
            log.info("鉴权绑卡短信,签名原文串：" + oriMessage);

            String sign = Disguiser.disguiseMD5(oriMessage.trim());
            log.info("鉴权绑卡短信,签名串：" + sign);

            map.put("sign", sign);
            log.info("发送参数：" + map);

            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, helibaoConfig.getRequestUrl());
            log.info("鉴权绑卡短信响应结果：" + resultMap);

            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                BindCardMessageResponseVo bindCardMessageResponseVo = JSONObject.parseObject(resultMsg, BindCardMessageResponseVo.class);
                log.info("鉴权绑卡短信响应信息:"+bindCardMessageResponseVo);

                String[] excludes = {};
                String assemblyRespOriSign = helibaoConfig.getSigned(bindCardMessageResponseVo, excludes);
                log.info("鉴权绑卡短信,组装返回结果签名串：" + assemblyRespOriSign);

                String responseSign = bindCardMessageResponseVo.getSign();
                log.info("鉴权绑卡短信,响应签名：" + responseSign);

                String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
                log.info("鉴权绑卡短信,验证签名：" + checkSign);

                if (checkSign.equals(responseSign)) {
                    if ("0000".equals(bindCardMessageResponseVo.getRt2_retCode())) {
                        log.info(vo.getP4_orderId()+"鉴权绑卡短信发送成功");
                        return ResponseData.success();
                    } else {
                        log.error(vo.getP4_orderId()+"鉴权绑卡短信发送失败:"+bindCardMessageResponseVo.getRt3_retMsg());
                        return ResponseData.error(bindCardMessageResponseVo.getRt3_retMsg());
                    }
                } else {
                    log.info(vo.getP4_orderId()+"鉴权绑卡短信验签失败:");
                    return ResponseData.error("鉴权绑卡短信验签失败");
                }
            } else {
                log.info(vo.getP4_orderId()+"鉴权绑卡短信请求失败:");
                return ResponseData.error("鉴权绑卡短信请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info(vo.getP4_orderId()+"鉴权绑卡短信异常");
            return ResponseData.error(e.getMessage());
        }
    }

    @Override
    public ResponseData bindCard(BindCardVo vo) {
        log.info("--------进入鉴权绑卡接口----------");
        vo.setP2_customerNumber(helibaoConfig.getMerchantNo());             //商户编号
        vo.setP5_timestamp(DateFormatUtils.format(new Date(),"yyyyMMddHHmmss"));
        validateUtil.validate(vo);
        try {
            //需要DES加密的参数
            //证件号码
            vo.setP8_idCardNo(Des3Encryption.encode(helibaoConfig.getDeskey(), vo.getP8_idCardNo()));
            //银行卡号
            vo.setP9_cardNo(Des3Encryption.encode(helibaoConfig.getDeskey(), vo.getP9_cardNo()));
            //手机号码
            if(vo.getP13_phone()!= null) {
                vo.setP13_phone(Des3Encryption.encode(helibaoConfig.getDeskey(), vo.getP13_phone()));
            }
            log.info("鉴权绑卡请求参数："+JSON.toJSONString(vo));

            Map<String,String> map = helibaoConfig.convertBean(vo, new LinkedHashMap());
            String oriMessage = helibaoConfig.getSigned(map, null);
            log.info("鉴权绑卡,签名原文串：" + oriMessage);

            String sign = Disguiser.disguiseMD5(oriMessage.trim());
            log.info("鉴权绑卡,签名串：" + sign);

            map.put("sign", sign);
            log.info("发送参数：" + map);

            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, helibaoConfig.getRequestUrl());
            log.info("鉴权绑卡响应结果：" + resultMap);

            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                BindCardResponseVo bindCardResponseVo = JSONObject.parseObject(resultMsg, BindCardResponseVo.class);
                log.info("鉴权绑卡请求返回的响应信息:"+bindCardResponseVo);

                String[] excludes = {};
                String assemblyRespOriSign = helibaoConfig.getSigned(bindCardResponseVo, excludes);
                log.info("鉴权绑卡,组装返回结果签名串：" + assemblyRespOriSign);

                String responseSign = bindCardResponseVo.getSign();
                log.info("鉴权绑卡,响应签名：" + responseSign);

                String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
                log.info("鉴权绑卡,验证签名：" + checkSign);

                if (checkSign.equals(responseSign)) {
                    if ("0000".equals(bindCardResponseVo.getRt2_retCode())) {
                        log.info(vo.getP4_orderId()+"鉴权绑卡成功");
                        return ResponseData.success();
                    } else {
                        log.error(vo.getP4_orderId()+"鉴权绑卡失败:"+bindCardResponseVo.getRt3_retMsg());
                        return ResponseData.error(bindCardResponseVo.getRt3_retMsg());
                    }
                } else {
                    log.info(vo.getP4_orderId()+"鉴权绑卡验签失败:");
                    return ResponseData.error("鉴权绑卡验签失败");
                }
            } else {
                log.info(vo.getP4_orderId()+"鉴权绑卡请求失败:");
                return ResponseData.error("鉴权绑卡请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info(vo.getP4_orderId()+"鉴权绑卡异常");
            return ResponseData.error(e.getMessage());
        }
    }

    @Override
    public ResponseData bindCardPayMessage(BindCardPayMessageVo vo) {
        log.info("--------进入绑卡支付短信接口----------");
        vo.setP2_customerNumber(helibaoConfig.getMerchantNo());             //商户编号
        vo.setP6_timestamp(DateFormatUtils.format(new Date(),"yyyyMMddHHmmss"));
        //绑卡ID

        validateUtil.validate(vo);
        try {
            //需要DES加密的参数
            //手机号码
            if(vo.getP9_phone()!= null) {
                vo.setP9_phone(Des3Encryption.encode(helibaoConfig.getDeskey(), vo.getP9_phone()));
            }
            log.info("绑卡支付短信请求参数："+JSON.toJSONString(vo));

            Map<String,String> map = helibaoConfig.convertBean(vo, new LinkedHashMap());
            String oriMessage = helibaoConfig.getSigned(map, null);
            log.info("绑卡支付短信,签名原文串：" + oriMessage);

            String sign = Disguiser.disguiseMD5(oriMessage.trim());
            log.info("绑卡支付短信,签名串：" + sign);

            map.put("sign", sign);
            log.info("发送参数：" + map);

            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, helibaoConfig.getRequestUrl());
            log.info("绑卡支付短信响应结果：" + resultMap);

            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                BindCardPayMessageResponseVo bindCardPayMessageResponseVo = JSONObject.parseObject(resultMsg, BindCardPayMessageResponseVo.class);
                log.info("绑卡支付短信请求返回的响应信息:"+bindCardPayMessageResponseVo);

                String[] excludes = {};
                String assemblyRespOriSign = helibaoConfig.getSigned(bindCardPayMessageResponseVo, excludes);
                log.info("绑卡支付短信,组装返回结果签名串：" + assemblyRespOriSign);

                String responseSign = bindCardPayMessageResponseVo.getSign();
                log.info("绑卡支付短信,响应签名：" + responseSign);

                String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
                log.info("绑卡支付短信,验证签名：" + checkSign);

                if (checkSign.equals(responseSign)) {
                    if ("0000".equals(bindCardPayMessageResponseVo.getRt2_retCode())) {
                        log.info(vo.getP5_orderId()+"绑卡支付短信发送成功");
                        return ResponseData.success();
                    } else {
                        log.error(vo.getP5_orderId()+"绑卡支付短信发送失败:"+bindCardPayMessageResponseVo.getRt3_retMsg());
                        return ResponseData.error(bindCardPayMessageResponseVo.getRt3_retMsg());
                    }
                } else {
                    log.info(vo.getP5_orderId()+"绑卡支付短信验签失败:");
                    return ResponseData.error("绑卡支付短信验签失败");
                }
            } else {
                log.info(vo.getP5_orderId()+"绑卡支付短信请求失败:");
                return ResponseData.error("绑卡支付短信请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info(vo.getP5_orderId()+"绑卡支付短信异常");
            return ResponseData.error(e.getMessage());
        }
    }

    @Override
    public ResponseData bindCardPay(BindCardPayVo vo) {
        log.info("--------进入绑卡支付接口----------");
        vo.setP2_customerNumber(helibaoConfig.getMerchantNo());             //商户编号
        vo.setP6_timestamp(DateFormatUtils.format(new Date(),"yyyyMMddHHmmss"));
        vo.setP16_serverCallbackUrl(helibaoConfig.getCallBackUrl());
        //绑卡ID

        log.info("绑卡支付请求参数："+JSON.toJSONString(vo));
        validateUtil.validate(vo);
        try {
            Map<String,String> map = helibaoConfig.convertBean(vo, new LinkedHashMap());
            String oriMessage = helibaoConfig.getSigned(map, null);
            log.info("绑卡支付,签名原文串：" + oriMessage);

            String sign = Disguiser.disguiseMD5(oriMessage.trim());
            log.info("绑卡支付,签名串：" + sign);

            map.put("sign", sign);
            log.info("发送参数：" + map);

            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, helibaoConfig.getRequestUrl());
            log.info("绑卡支付响应结果：" + resultMap);

            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                BindCardPayResponseVo bindCardPayResponseVo = JSONObject.parseObject(resultMsg, BindCardPayResponseVo.class);
                log.info("绑卡支付请求返回的响应信息:"+bindCardPayResponseVo);

                String[] excludes = {};
                String assemblyRespOriSign = helibaoConfig.getSigned(bindCardPayResponseVo, excludes);
                log.info("绑卡支付,组装返回结果签名串：" + assemblyRespOriSign);

                String responseSign = bindCardPayResponseVo.getSign();
                log.info("绑卡支付,响应签名：" + responseSign);

                String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
                log.info("绑卡支付,验证签名：" + checkSign);

                if (checkSign.equals(responseSign)) {
                    if ("0000".equals(bindCardPayResponseVo.getRt2_retCode())) {
                        log.info(vo.getP5_orderId()+"绑卡支付成功");
                        return ResponseData.success();
                    } else {
                        log.error(vo.getP5_orderId()+"绑卡支付失败:"+bindCardPayResponseVo.getRt3_retMsg());
                        return ResponseData.error(bindCardPayResponseVo.getRt3_retMsg());
                    }
                } else {
                    log.info(vo.getP5_orderId()+"绑卡支付验签失败:");
                    return ResponseData.error("绑卡支付验签失败");
                }
            } else {
                log.info(vo.getP5_orderId()+"绑卡支付请求失败:");
                return ResponseData.error("绑卡支付请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info(vo.getP5_orderId()+"绑卡支付异常");
            return ResponseData.error(e.getMessage());
        }
    }

    @Override
    public ResponseData queryOrder(OrderQueryVo vo) {
        log.info("--------进入订单查询接口----------");
        vo.setP3_customerNumber(helibaoConfig.getMerchantNo());
        log.info("订单查询请求参数："+JSON.toJSONString(vo));
        try {
            Map<String, String> map = helibaoConfig.convertBean(vo, new LinkedHashMap());
            String oriMessage = helibaoConfig.getSigned(map, null);
            log.info("签名原文串：" + oriMessage);

            String sign = Disguiser.disguiseMD5(oriMessage.trim());
            log.info("签名串：" + sign);

            map.put("sign", sign);
            log.info("发送参数：" + map);

            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, helibaoConfig.getRequestUrl());
            log.info("响应结果：" + resultMap);

            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                OrderQueryResponseVo orderQueryResponseVo = JSONObject.parseObject(resultMsg, OrderQueryResponseVo.class);

//                String[] excludes = {"rt14_reason"};
//                String assemblyRespOriSign = helibaoConfig.getSigned(orderQueryResponseVo, excludes);
                String assemblyRespOriSign = helibaoConfig.getSigned(orderQueryResponseVo, null);
                log.info("组装返回结果签名串：" + assemblyRespOriSign);

                String responseSign = orderQueryResponseVo.getSign();
                log.info("响应签名：" + responseSign);
                String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
                if (checkSign.equals(responseSign)) {
                    if ("0000".equals(orderQueryResponseVo.getRt2_retCode())) {
                        log.info(vo.getP2_orderId()+"订单查询成功");
                        return ResponseData.success();
                    } else {
                        log.error(vo.getP2_orderId()+"订单查询失败:"+orderQueryResponseVo.getRt3_retMsg());
                        return ResponseData.error(orderQueryResponseVo.getRt3_retMsg());
                    }
                } else {
                    log.info(vo.getP2_orderId()+"订单查询验签失败:");
                    return ResponseData.error("订单查询验签失败");
                }
            } else {
                log.info(vo.getP2_orderId()+"订单查询请求失败:");
                return ResponseData.error("订单查询请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info(vo.getP2_orderId()+"订单查询异常");
            return ResponseData.error(e.getMessage());
        }
    }

    @Override
    public ResponseData unBindCard(UnBindCardVo vo) {
        log.info("--------进入银行卡解绑接口----------");
        vo.setP2_customerNumber(helibaoConfig.getMerchantNo());
        //绑定id

        log.info("银行卡解绑请求参数："+JSON.toJSONString(vo));
        try {
            Map<String, String> map = helibaoConfig.convertBean(vo, new LinkedHashMap());
            String oriMessage = helibaoConfig.getSigned(map, null);
            log.info("签名原文串：" + oriMessage);

            String sign = Disguiser.disguiseMD5(oriMessage.trim());
            log.info("签名串：" + sign);

            map.put("sign", sign);
            log.info("发送参数：" + map);

            Map<String, Object> resultMap = HttpClientService.getHttpResp(map, helibaoConfig.getRequestUrl());
            log.info("响应结果：" + resultMap);

            if ((Integer) resultMap.get("statusCode") == HttpStatus.SC_OK) {
                String resultMsg = (String) resultMap.get("response");
                UnBindCardResponseVo unBindCardResponseVo = JSONObject.parseObject(resultMsg, UnBindCardResponseVo.class);

                String assemblyRespOriSign = helibaoConfig.getSigned(unBindCardResponseVo, null);
                log.info("银行卡解绑,组装返回结果签名串：" + assemblyRespOriSign);

                String responseSign = unBindCardResponseVo.getSign();
                log.info("银行卡解绑,响应签名：" + responseSign);
                String checkSign = Disguiser.disguiseMD5(assemblyRespOriSign.trim());
                if (checkSign.equals(responseSign)) {
                    if ("0000".equals(unBindCardResponseVo.getRt2_retCode())) {
                        log.info(vo.getP5_orderId()+"银行卡解绑成功");
                        return ResponseData.success();
                    } else {
                        log.error(vo.getP5_orderId()+"银行卡解绑失败:"+unBindCardResponseVo.getRt3_retMsg());
                        return ResponseData.error(unBindCardResponseVo.getRt3_retMsg());
                    }
                } else {
                    log.info(vo.getP5_orderId()+"银行卡解绑验签失败:");
                    return ResponseData.error("银行卡解绑验签失败");
                }
            } else {
                log.info(vo.getP5_orderId()+"银行卡解绑请求失败:");
                return ResponseData.error("银行卡解绑请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info(vo.getP5_orderId()+"银行卡解绑异常");
            return ResponseData.error(e.getMessage());
        }
    }

    /**
     * 通过userId去获取手机号(平台账号)
     * @param userId
     * @return
     */
    private String getMobile(String userId){
        String mobile = "";
        try{
            ResponseData<AccountDto> data = userAccountContract.getAccount(userId);
            if ("0".equals(data.getStatus())){
                AccountDto accountDto = data.getData();
                log.info("find by userId:"+JSON.toJSONString(accountDto));
                mobile = accountDto.getAccountNumber();
                log.info("userId:"+userId+",对应的手机号:"+mobile);
            }
        }catch(Exception e){
            log.error("根据userId查找手机号出错",e);
        }
        return  mobile;

    }


    @Override
    public ResponseData bindCardInformationQuery(BindCardInformationQueryVo vo) {
        return null;
    }
}
