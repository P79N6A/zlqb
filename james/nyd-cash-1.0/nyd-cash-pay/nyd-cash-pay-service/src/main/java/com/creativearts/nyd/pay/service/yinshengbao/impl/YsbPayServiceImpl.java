package com.creativearts.nyd.pay.service.yinshengbao.impl;

import com.alibaba.fastjson.JSON;
import com.creativearts.nyd.pay.model.*;
import com.creativearts.nyd.pay.model.enums.PayStatus;
import com.creativearts.nyd.pay.model.enums.SourceType;
import com.creativearts.nyd.pay.model.yinshengbao.*;
import com.creativearts.nyd.pay.service.Constants;
import com.creativearts.nyd.pay.service.RedisProcessService;
import com.creativearts.nyd.pay.service.log.LoggerUtils;
import com.creativearts.nyd.pay.service.yinshengbao.YsbPayService;
import com.creativearts.nyd.pay.service.yinshengbao.YsbUserService;
import com.creativearts.nyd.pay.service.yinshengbao.properties.YsbProperties;
import com.creativearts.nyd.pay.service.yinshengbao.util.Md5Encrypt;
import com.nyd.member.model.msg.MemberFeeLogMessage;
import com.nyd.msg.model.SmsRequest;
import com.nyd.msg.service.ISendSmsService;
import com.nyd.order.api.OrderContract;
import com.nyd.order.api.OrderDetailContract;
import com.nyd.pay.entity.YsbUser;
import com.nyd.user.api.UserAccountContract;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.api.UserSourceContract;
import com.nyd.user.entity.LoginLog;
import com.nyd.user.model.UserDetailInfo;
import com.nyd.user.model.UserInfo;
import com.nyd.user.model.dto.AccountDto;
import com.nyd.user.model.mq.RechargeFeeInfo;
import com.nyd.zeus.api.BillContract;
import com.nyd.zeus.model.BillInfo;
import com.nyd.zeus.model.RepayInfo;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class YsbPayServiceImpl implements YsbPayService{

    Logger logger = LoggerFactory.getLogger(YsbPayServiceImpl.class);

    @Autowired
    private YsbProperties ysbProperties;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserIdentityContract userIdentityContract;

    @Autowired
    private UserAccountContract userAccountContract;

    @Autowired
    private YsbUserService ysbUserService;

    @Autowired
    private OrderDetailContract orderDetailContract;

    @Autowired
    private ISendSmsService sendSmsService;

    @Autowired
    private OrderContract orderContract;

    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired(required = false)
    private BillContract billContract;

    @Autowired
    private RedisProcessService redisProcessService;

    @Autowired
    private UserSourceContract userSourceContract;




    @Override
    public String signSimpleSubContract(NydYsbVo nydYsbVo) {
        AgreementRequest agreementRequest = new AgreementRequest();
        String subContractId = null;
        try {

            //找人代付 需要传入身份证和姓名和手机号
            if(StringUtils.isNotBlank(nydYsbVo.getIdCard())){
                try{
                   redisTemplate.opsForValue().set(Constants.QUICK_PAY+nydYsbVo.getBillNo(),nydYsbVo.getName()+"-"+nydYsbVo.getIdCard()+"-"+nydYsbVo.getBankNo(),1440, TimeUnit.MINUTES);
                    logger.info("将代付款人的信息存入redis里面——————————————————————————————");
                    logger.info("代付款人信息："+redisTemplate.opsForValue().get(Constants.QUICK_PAY + nydYsbVo.getBillNo()));
                }catch (Exception e){
                    e.printStackTrace();
                }
                agreementRequest.setName(nydYsbVo.getName());
                agreementRequest.setPhoneNo(nydYsbVo.getMobile());
                agreementRequest.setIdCardNo(nydYsbVo.getIdCard());
            }else {
                UserInfo userInfo = userIdentityContract.getUserInfo(nydYsbVo.getUserId()).getData();
                AccountDto accountDto = userAccountContract.getAccount(nydYsbVo.getUserId()).getData();
                agreementRequest.setName(userInfo.getRealName());
                agreementRequest.setPhoneNo(accountDto.getAccountNumber());
                agreementRequest.setIdCardNo(userInfo.getIdNumber());
            }

//            agreementRequest.setUserid(nydYsbVo.getUserId());
            agreementRequest.setCardNo(nydYsbVo.getBankNo());
            agreementRequest.setStartDate(DateFormatUtils.format(new Date(), "yyyyMMdd"));
            agreementRequest.setEndDate("22000101");
            //商户相关参数
            String accountId = ysbProperties.getAccountId();
            String contractId = ysbProperties.getContractId();
            String key = ysbProperties.getKey();
            //mac(数字签名)
            String mac="";
            StringBuffer sf = new StringBuffer();
            sf.append("accountId=").append(accountId);
            sf.append("&contractId=").append(contractId);
            sf.append("&name=").append(agreementRequest.getName());
            sf.append("&phoneNo=").append(agreementRequest.getPhoneNo());
            sf.append("&cardNo=").append(agreementRequest.getCardNo());
            sf.append("&idCardNo=").append(agreementRequest.getIdCardNo());
            sf.append("&startDate=").append(agreementRequest.getStartDate());
            sf.append("&endDate=").append(agreementRequest.getEndDate());
            sf.append("&key=").append(key);
            logger.info("加密前+++++++++"+sf.toString());
            mac = Md5Encrypt.md5(sf.toString()).toUpperCase();

            //将请求参数全部封装到agreementRequest对象里面
            agreementRequest.setAccountId(accountId);
            agreementRequest.setContractId(contractId);
            agreementRequest.setMac(mac);
            logger.info("子协议请求参数："+JSON.toJSONString(agreementRequest));

            //发起请求
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            HttpEntity entity = new HttpEntity(JSON.toJSONString(agreementRequest), headers);

            try {
                ResponseEntity<ResponseMode> result = restTemplate.exchange(ysbProperties.getAgreementURL(), HttpMethod.POST, entity, ResponseMode.class);
                logger.info("子协议请求返回："+ result.getBody());
                if ("0000".equals(result.getBody().getResult_code())){
                    subContractId = result.getBody().getSubContractId();
                    logger.info("子协议请求成功");
                    logger.info("子协议号:"+subContractId);
                    YsbUser user = new YsbUser();
                    user.setSubContractId(subContractId);
                    user.setCardNo(nydYsbVo.getBankNo());
                    user.setUserId(nydYsbVo.getUserId());
                    user.setName(agreementRequest.getName());
                    user.setIdNumber(agreementRequest.getIdCardNo());
                    user.setUpdateBy("sys");
                    try {
                        ysbUserService.save(user);
                    } catch (Exception e) {
                        logger.error("ysb user 保存数据库异常",e);
                        e.printStackTrace();
                    }
                }else {
                    logger.info("子协议请求失败返回状态码："+result.getBody().getResult_code());
                    logger.info("子协议请求失败原因："+result.getBody().getResult_msg());
                }

            }catch (Exception e){
                logger.info("生成子协议出错："+e);
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        return subContractId;

    }

    /**
     * 代扣
     * @param daiKouRequest
     */
    @Override
    public ResponseData collect(DaiKouRequest daiKouRequest) {
        try {
//            daiKouRequest.setPurpose("还款");
            //响应地址
            String responseUrl = ysbProperties.getDaiKouResultNotifyURL();
            daiKouRequest.setResponseUrl(responseUrl);
            //订单号
//            daiKouRequest.setOrderId(nydYsbVo.getBillNo());
            //商户相关参数
            String accountId = ysbProperties.getAccountId();
            daiKouRequest.setAccountId(accountId);
            String key = ysbProperties.getKey();
            String mac="";
            StringBuffer sf = new StringBuffer();
            sf.append("accountId=").append(accountId);
            sf.append("&subContractId=").append(daiKouRequest.getSubContractId());
            sf.append("&orderId=").append(daiKouRequest.getOrderId());
            sf.append("&purpose=").append(daiKouRequest.getPurpose());
            sf.append("&amount=").append(daiKouRequest.getAmount());
            if (StringUtils.isNotBlank(daiKouRequest.getPhoneNo())) {
                sf.append("&phoneNo=").append(daiKouRequest.getPhoneNo());
            }
            sf.append("&responseUrl=").append(responseUrl);
            sf.append("&key=").append(key);
            logger.info("加密前+++++++++"+sf.toString());
            mac= Md5Encrypt.md5(sf.toString()).toUpperCase();
            //将请求参数全部封装到daiKouRequest对象里面
            daiKouRequest.setMac(mac);

            logger.info("代扣请求参数："+JSON.toJSONString(daiKouRequest));
            //发起请求
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            HttpEntity entity = new HttpEntity(JSON.toJSONString(daiKouRequest), headers);
//            ResponseEntity<String> result = restTemplate.exchange(ysbProperties.getDaiKouURL(), HttpMethod.POST, entity, String.class);
//            System.out.println("******************************");
//            System.out.println(result);

            ResponseEntity<ResponseMode> result = restTemplate.exchange(ysbProperties.getDaiKouURL(), HttpMethod.POST, entity, ResponseMode.class);
            logger.info("代扣请求返回："+ result.getBody());

            //解析返回结果
            if ("0000".equals(result.getBody().getResult_code())){
                logger.info("代扣返回信息："+result.getBody().getResult_msg());
                logger.info("代扣交易结果描述信息:"+result.getBody().getDesc());
                return ResponseData.success(result.getBody().getResult_msg());

            }else {
                logger.info("代扣请求失败返回信息："+result.getBody().getResult_msg());
                return ResponseData.error(result.getBody().getResult_msg());
            }


        }catch (Exception e){
            e.printStackTrace();
            return ResponseData.error(e.getMessage());
        }
//        return null;
    }

    @Override
    public ResponseData signAndDaikou(NydYsbVo nydYsbVo, String type) {
        //判断是否是本人的账单
        boolean flag= false;
        try {
            if(!SourceType.MEMBER_FEE.getType().equals(nydYsbVo.getSourceType()) && !SourceType.CASH_COUPON.getType().equals(nydYsbVo.getSourceType())) {
                ResponseData<BillInfo> responseData = billContract.getBillInfo(nydYsbVo.getBillNo());
                if (responseData != null && "0".equals(responseData.getStatus())) {
                    BillInfo billInfo = responseData.getData();
                    logger.info("只有还款时才会进这里面校验，billInfo:"+JSON.toJSON(billInfo));
                    if (billInfo != null && StringUtils.isNotBlank(billInfo.getUserId())) {
                        //根据此userId查询是否是本人
                        ResponseData<UserDetailInfo> responseData1 = userIdentityContract.getUserDetailInfo(billInfo.getUserId());
                        if (responseData1 != null && "0".equals(responseData1.getStatus())) {
                            UserDetailInfo userDetailInfo = responseData1.getData();
                            if (userDetailInfo != null && nydYsbVo != null) {
                                if (nydYsbVo.getName().equals(userDetailInfo.getRealName()) && nydYsbVo.getIdCard().equals(userDetailInfo.getIdNumber())) {
                                    flag = true;
                                }
                            }
                        }
                    }
                }
            } else {
                flag=true;
            }
        } catch (Exception e) {
            logger.error("signAndDaikou has exception!",e);
        }
        if (!flag){
            return ResponseData.error("请核对身份信息！");
        }

        String userid = nydYsbVo.getUserId();
        String cardno = nydYsbVo.getBankNo();
        String idNumber = nydYsbVo.getIdCard();
        //1.根据用户id和银行卡号去查找subContractId，如果没有就生成，如果有就不需要重新生成
        Map<String,String> params = new HashMap<>();
//        params.put("userid",userid);
        params.put("cardno",cardno);
        params.put("idNumber",idNumber);
        List<YsbUser> list = ysbUserService.findByUserId(params);
        String subContractId = "";
        if (list != null && list.size() > 0 ){
            YsbUser ysbUser = list.get(0);
            if (StringUtils.isNotBlank(ysbUser.getSubContractId())){    //表明之前已经生成了子协议号，subContractId
                subContractId = ysbUser.getSubContractId();
            }else {
                logger.error(idNumber+"子协议为null");

                return ResponseData.error("子协议为null");
            }

        }else{      //表明没有子协议号，subContractId，需要生成,然后保存到表t_user_ysb 或者 是有子协议号但换了卡，也需要重新生成
            subContractId = signSimpleSubContract(nydYsbVo);
            if(subContractId==null){
                logger.error(idNumber+"签订子协议 子协议为null");

                return ResponseData.error("接口子协议为null");
            }

        }
        logger.info("子协议编号："+subContractId);

        //2.子协议号生成后进行代扣
        DaiKouRequest daiKouRequest = new DaiKouRequest();
        daiKouRequest.setSubContractId(subContractId);
        daiKouRequest.setAmount(nydYsbVo.getAmount().toString());
//        daiKouRequest.setOrderId(nydYsbVo.getBillNo()+"_"+(System.currentTimeMillis()+"").substring(2,11)+"_z");
        if (Constant.MEMBER_TYPE.equals(type)){//会员订单
            daiKouRequest.setOrderId(nydYsbVo.getUserId()+"M"+(System.currentTimeMillis()+"").substring(2,11));
        }else if (Constant.KPAY_TYPE.equals(type)){
            daiKouRequest.setOrderId(nydYsbVo.getBillNo()+"P"+(System.currentTimeMillis()+"").substring(2,11)+"k");
        }else if (Constant.ZPAY_TYPE.equals(type)){
            daiKouRequest.setOrderId(nydYsbVo.getBillNo()+"P"+(System.currentTimeMillis()+"").substring(2,11)+"z");
        }else if (Constant.QPAY_TYPE.equals(type)){
            daiKouRequest.setOrderId(nydYsbVo.getBillNo()+"P"+(System.currentTimeMillis()+"").substring(2,11)+"q");
        }else if (Constant.COUPON_TYPE.equals(type)){    //充值购买现金券订单
            daiKouRequest.setOrderId(nydYsbVo.getUserId()+"xj"+(System.currentTimeMillis()+"").substring(2,11));
        }

        daiKouRequest.setPurpose("侬要贷还款");
        //发起代扣请求
        return  collect(daiKouRequest);
    }

    /**
     * 订单状态查询
     * @param nydYsbVo
     * @return
     */
    @Override
    public ResponseData queryOrderStatus(NydYsbVo nydYsbVo) {
        OrderStatusQuery orderStatusQuery = new OrderStatusQuery();
        orderStatusQuery.setOrderId(nydYsbVo.getBillNo());
        try {
            //商户参数
            String accountId = ysbProperties.getAccountId();
            String key = ysbProperties.getKey();
            //mac(数字签名)
            String mac="";
            StringBuffer sf = new StringBuffer();
            sf.append("accountId=").append(accountId);
            sf.append("&orderId=").append(orderStatusQuery.getOrderId());
            sf.append("&key=").append(key);
            logger.info("加密前+++++++++"+sf.toString());
            mac = Md5Encrypt.md5(sf.toString()).toUpperCase();
            orderStatusQuery.setAccountId(accountId);
            orderStatusQuery.setMac(mac);
            logger.info("订单状态请求参数："+JSON.toJSONString(orderStatusQuery));

            //发起请求
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            HttpEntity entity = new HttpEntity(JSON.toJSONString(orderStatusQuery), headers);
            ResponseEntity<ResponseMode> result = restTemplate.exchange(ysbProperties.getOrderQueryURL(), HttpMethod.POST, entity, ResponseMode.class);
            logger.info("订单状态请求返回："+ result.getBody());
            if ("0000".equals(result.getBody().getResult_code())){
                logger.info("返回信息："+result.getBody().getResult_msg());
                logger.info("交易结果："+result.getBody().getDesc());
                return ResponseData.success();
            }else{
                logger.info("返回信息："+result.getBody().getResult_msg());
                return  ResponseData.error(result.getBody().getResult_msg());
            }

        }catch (Exception e){
            e.printStackTrace();
            return ResponseData.error(e.getMessage());
        }

    }

    /**
     * 子协议号查询
     * @param nydYsbVo
     * @return
     */
    @Override
    public ResponseData querySubContractId(NydYsbVo nydYsbVo) {
        SubContractIdQuery subContractIdQuery = new SubContractIdQuery();
        try {
            String bankNo = nydYsbVo.getBankNo();
            UserInfo userInfo = userIdentityContract.getUserInfo(nydYsbVo.getUserId()).getData();
            String idCardNo = userInfo.getIdNumber();
            String name = userInfo.getRealName();
            //商户相关参数
            String accountId = ysbProperties.getAccountId();
            String key = ysbProperties.getKey();

            //mac(数字签名)
            String mac="";
            StringBuffer sf = new StringBuffer();
            sf.append("accountId=").append(accountId);
            sf.append("&name=").append(name);
            sf.append("&cardNo=").append(bankNo);
            sf.append("&idCardNo=").append(idCardNo);
            sf.append("&key=").append(key);
            logger.info("加密前+++++++++"+sf.toString());
            mac = Md5Encrypt.md5(sf.toString()).toUpperCase();

            subContractIdQuery.setAccountId(accountId);
            subContractIdQuery.setName(name);
            subContractIdQuery.setIdCardNo(idCardNo);
            subContractIdQuery.setMac(mac);
            subContractIdQuery.setCardNo(bankNo);
            logger.info("查询子协议号请求参数："+JSON.toJSONString(subContractIdQuery));
            //发起请求
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            HttpEntity entity = new HttpEntity(JSON.toJSONString(subContractIdQuery), headers);
            ResponseEntity<ResponseMode> result = restTemplate.exchange(ysbProperties.getSubContractIdURL(), HttpMethod.POST, entity, ResponseMode.class);
            logger.info("查询子协议号请求返回："+ result.getBody());
            if ("0000".equals(result.getBody().getResult_code())){
                logger.info("查询到的子协议号为："+result.getBody().getSubContractId());
                return ResponseData.success();
            }else{
                logger.info("没有查询到子协议号原因："+result.getBody().getResult_msg());
                return  ResponseData.error(result.getBody().getResult_msg());
            }

           
        }catch (Exception e){
            e.printStackTrace();
            return ResponseData.error(e.getMessage());
        }

    }

    /**
     * 子协议延期
     * @param subContractIdDelay
     * @return
     */
    @Override
    public ResponseData subContractIdExtension(SubContractIdDelay subContractIdDelay) {
        //发起请求
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        HttpEntity entity = new HttpEntity(JSON.toJSONString(subContractIdDelay), headers);
        ResponseEntity<ResponseMode> result = restTemplate.exchange(ysbProperties.getSubContractIdDelayURL(), HttpMethod.POST, entity, ResponseMode.class);
        logger.info("子协议延期请求返回："+ result.getBody());
        if ("0000".equals(result.getBody().getResult_code())){
            logger.info("子协议延期成功");
            return ResponseData.success();
        }else{
            logger.info("子协议延期失败原因："+result.getBody().getResult_msg());
            return  ResponseData.error(result.getBody().getResult_msg());
        }
    }

    @Override
    public String callBackProcess(YsbNotifyResponseVo notifyResponseVo) {
        logger.info("ysb callback:"+JSON.toJSONString(notifyResponseVo));
        // 避免重复的通知
        try {
            if (redisTemplate.hasKey(Constants.YSB_REDIS_PREFIX + notifyResponseVo.getOrderId())) {
                logger.error("有重复通知" + JSON.toJSONString(notifyResponseVo));
                return "success";
            } else {
                redisTemplate.opsForValue().set(Constants.YSB_REDIS_PREFIX + notifyResponseVo.getOrderId(), "1", 2880, TimeUnit.MINUTES);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("写redis出错"+e.getMessage());
        }


        logger.info("返回的orderid为"+notifyResponseVo.getOrderId());
        if("00".equals(notifyResponseVo.getResult_code())) {            //银生宝通知扣款成功， 00表示扣款成功
            logger.info("扣款成功,接下来分析属于什么类型的扣款+**********************************");
            //M是会员费
            if (notifyResponseVo.getOrderId().contains("M")) {              //扣会员费产生的订单
                String[] ss = notifyResponseVo.getOrderId().split("M");
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

                memberModel.setMobile(payCarryVo.getMobile());
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
                memberModel.setPayCash(new BigDecimal(notifyResponseVo.getAmount()));
                logger.info("银生宝支付会员费，给member发送mq消息的对象："+JSON.toJSON(memberModel));

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
                        logger.info("会员费发送短信结果" + JSON.toJSONString(responseData));
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("会员费发送短信失败" + e.getMessage());
                    }
                }else {
                    logger.info("手机号码为空"+userId);
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
                repayInfo.setBillNo(userId+"_"+System.currentTimeMillis());
                repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getAmount()));
                repayInfo.setRepayChannel("ysb");
                repayInfo.setRepayTime(new Date());
                repayInfo.setRepayNo(notifyResponseVo.getOrderId());
                repayInfo.setRepayType(RepayType.MFEE.getCode());
                repayInfo.setUserId(userId);
                //优惠券id
                if (StringUtils.isNotBlank(payCarryVo.getCouponId())){
                    repayInfo.setCouponId(payCarryVo.getCouponId());
                }

                //现金券使用金额
                repayInfo.setCouponUseFee(payCarryVo.getCouponUseFee());
                logger.info("银生宝支付会员费，给repay发送mq消息的对象："+JSON.toJSON(repayInfo));

                try {
                    rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                }catch (Exception e){
                    e.printStackTrace();
                }
                LoggerUtils.write(repayInfo);


            } else if (notifyResponseVo.getOrderId().contains("xj")){                //充值现金券产生的订单
                String[] ss = notifyResponseVo.getOrderId().split("xj");
                String userId = ss[0];
                String carryStr = (String)redisTemplate.opsForValue().get(Constants.YSB_NYD_CARRAY+userId);
                PayCarryVo payCarryVo;
                if(carryStr == null){
                    payCarryVo = new PayCarryVo();
                }else {
                    payCarryVo = JSON.parseObject(carryStr,PayCarryVo.class);
                }
                logger.info("使用银生宝充值现金券,Redis携带回得信息："+JSON.toJSONString(payCarryVo));

                RechargeFeeInfo rechargeFeeInfo = new RechargeFeeInfo();
                rechargeFeeInfo.setUserId(userId);
                rechargeFeeInfo.setAccountNumber(payCarryVo.getMobile());
                rechargeFeeInfo.setAmount(new BigDecimal(notifyResponseVo.getAmount()));
                rechargeFeeInfo.setCashId(payCarryVo.getCashId());
                rechargeFeeInfo.setOperStatus(1);  // 0：失败  1：成功
                rechargeFeeInfo.setRechargeFlowNo(notifyResponseVo.getOrderId());   //将银生宝返回的订单号orderId,作为充值流水号
                rechargeFeeInfo.setStatusMsg(notifyResponseVo.getResult_msg());
                rechargeFeeInfo.setUserType(1);   //用户充值现金券收入
                rechargeFeeInfo.setCashDescription("用户充值收入");
                logger.info("ysb支付完成，发送mq现金券对象："+JSON.toJSON(rechargeFeeInfo));

                try {
                    rabbitmqProducerProxy.convertAndSend("rechargeCoupon.nyd", rechargeFeeInfo);
                    logger.info("ysb支付完成，发送mq现金券对象完毕");
                }catch (Exception e){
                    logger.info("ysb支付完成，发送mq现金券对象异常",e);
                    e.printStackTrace();
                }

                /**
                 *给用户发信息，告诉用户现金券购买成功
                 */
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
                        logger.info("充值现金券成功，发送短信success：" + JSON.toJSONString(responseData));
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("充值现金券成功，发送短信fail" + e.getMessage());
                    }
                }else {
                    logger.info("手机号码为空,无法进行发短信："+userId);
                }

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
                repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getAmount()));
                repayInfo.setRepayChannel("ysb");
                repayInfo.setRepayTime(new Date());
                repayInfo.setRepayNo(notifyResponseVo.getOrderId());
                repayInfo.setRepayType(RepayType.COUPON_FEE.getCode());
                repayInfo.setUserId(userId);
                logger.info("通过银生宝购买现金券成功，支付流水对象："+JSON.toJSON(repayInfo));

                try {
                    rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                }catch (Exception e){
                    e.printStackTrace();
                }
                LoggerUtils.write(repayInfo);


            } else {                                                            //扣款产生的订单
//                    String[] ss = notifyResponseVo.getRt5_orderId().split("~");
                String sourceOrderId = notifyResponseVo.getOrderId();
//                String type = null;
                String orderId="";
                if (sourceOrderId.contains("P")) {
                    String[] strs = sourceOrderId.split("P");
                    orderId = strs[0];


                }


                RepayMessage repayMessage = new RepayMessage();
                repayMessage.setBillNo(orderId);
                repayMessage.setRepayAmount(new BigDecimal(notifyResponseVo.getAmount()));
                repayMessage.setRepayStatus("0");
                logger.info("还款成功回写mq");

                rabbitmqProducerProxy.convertAndSend("repay.nyd", repayMessage);

                try {
                    redisTemplate.opsForValue().set(Constants.PAY_PREFIX + orderId, PayStatus.PAY_SUCESS.getCode(), 100, TimeUnit.MINUTES);
                }catch (Exception e){
                    logger.info("异常",e);
                    logger.info(orderId+"set key异常ysb");
                }
                //银码头
                try {
                    ResponseData<BillInfo> responseData = billContract.getBillInfo(orderId);
                    logger.info("YSB付款成功后获取billInfo"+JSON.toJSONString(responseData));
                    if ("0".equals( responseData.getStatus())){
                        BillInfo billInfo = responseData.getData();
                        if(billInfo!=null && StringUtils.isNotBlank(billInfo.getIbankOrderNo())){
                            repayMessage.setBillNo(billInfo.getIbankOrderNo());
                            repayMessage.setRepayTime(new Date());
                            logger.info("YSB成功发送银码头rabbit"+JSON.toJSONString(repayMessage));
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
                repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getAmount()));
                repayInfo.setRepayChannel("ysb");
                repayInfo.setRepayTime(new Date());
                repayInfo.setRepayNo(notifyResponseVo.getOrderId());

                if(sourceOrderId.contains("k")){
                    String[] infoarray = null;
                    try {
                        String infos = (String) redisTemplate.opsForValue().get(Constants.QUICK_PAY + orderId);
                        infoarray = infos.split("-");
                    }catch (Exception e){
//                        e.printStackTrace();
                    }


                    repayInfo.setRepayType(RepayType.KJ.getCode());
                    if(infoarray!=null&&infoarray.length==3){
                        repayInfo.setRepayName(infoarray[0]);
                        repayInfo.setRepayIdNumber(infoarray[1]);
                        repayInfo.setRepayAccount(infoarray[2]);
                    }
                    logger.info("快捷还款，还款流水表对象："+JSON.toJSONString(repayInfo));
                    try {
                        rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    LoggerUtils.write(repayInfo);
                }else if(sourceOrderId.contains("z")){

                    repayInfo.setRepayType(RepayType.ZD.getCode());
                    logger.info("主动还款，还款流水表对象："+JSON.toJSONString(repayInfo));
                    try {
                        rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    LoggerUtils.write(repayInfo);
                }else if(sourceOrderId.contains("q")){

                    repayInfo.setRepayType(RepayType.QK.getCode());
                    logger.info("强扣还款，还款流水表对象："+JSON.toJSONString(repayInfo));
                    try {
                        rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    LoggerUtils.write(repayInfo);
                }else {
                    repayInfo.setRepayType(RepayType.UNKNOW.getCode());
                    try {
                        rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    LoggerUtils.write(repayInfo);
                }


            }
        }else {                                                             //银生宝通知扣款失败
            if (notifyResponseVo.getOrderId().contains("M")) {              //扣会员费产生的订单
                String[] ss = notifyResponseVo.getOrderId().split("M");
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

                memberModel.setDebitFlag(notifyResponseVo.getResult_code());
                memberModel.setMemberType(payCarryVo.getMemberType());
                memberModel.setUserId(payCarryVo.getUserId());
                memberModel.setMobile(payCarryVo.getMobile());
                //优惠券id
                if (StringUtils.isNotBlank(payCarryVo.getCouponId())){
                    memberModel.setCouponId(payCarryVo.getCouponId());
                }

                //现金券使用金额
                memberModel.setCouponUseFee(payCarryVo.getCouponUseFee());

                //此笔支付，需要支付的金额
                memberModel.setPayCash(new BigDecimal(notifyResponseVo.getAmount()));
                logger.info("银生宝支付会员费（fail），给member发送mq消息的对象："+JSON.toJSON(memberModel));

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
                repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getAmount()));
                repayInfo.setRepayChannel("ysb");
                repayInfo.setRepayTime(new Date());
                repayInfo.setRepayNo(notifyResponseVo.getOrderId());
                repayInfo.setRepayType(RepayType.MFEE_FAIL.getCode());
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
            }else if (notifyResponseVo.getOrderId().contains("xj")){       //充值现金券失败产生的订单
                String[] ss = notifyResponseVo.getOrderId().split("xj");
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
                rechargeFeeInfo.setAmount(new BigDecimal(notifyResponseVo.getAmount()));
                rechargeFeeInfo.setCashId(payCarryVo.getCashId());
                rechargeFeeInfo.setOperStatus(0);   //0：失败  1：成功
                rechargeFeeInfo.setRechargeFlowNo(notifyResponseVo.getOrderId());       //将银生宝返回的订单号orderId,作为充值流水号
                rechargeFeeInfo.setStatusMsg(notifyResponseVo.getResult_msg());
                rechargeFeeInfo.setUserType(1);                     //用户充值现金券收入
                rechargeFeeInfo.setCashDescription("用户充值收入");
                logger.info("ysb支付fail，发送mq现金券对象："+JSON.toJSON(rechargeFeeInfo));

                try {
                    rabbitmqProducerProxy.convertAndSend("rechargeCoupon.nyd", rechargeFeeInfo);
                    logger.info("ysb支付完成，发送mq现金券对象完毕");
                }catch (Exception e){
                    logger.info("ysb支付完成，发送mq现金券对象异常",e);
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
                repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getAmount()));
                repayInfo.setRepayChannel("ysb");
                repayInfo.setRepayTime(new Date());
                repayInfo.setRepayNo(notifyResponseVo.getOrderId());
                repayInfo.setRepayType(RepayType.COUPON_FEE.getCode());
                repayInfo.setUserId(userId);
                logger.info("通过银生宝购买现金券fail，支付流水对象："+JSON.toJSON(repayInfo));

                try {
                    rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                }catch (Exception e){
                    e.printStackTrace();
                }
                LoggerUtils.write(repayInfo);

            } else{                                                          //扣款产生的订单号
                String sourceOrderId = notifyResponseVo.getOrderId();
                String orderId = null;
                if (sourceOrderId.contains("P")) {
                    String[] strs = sourceOrderId.split("P");
                    orderId = strs[0];

                }

                try {
                    redisTemplate.delete(Constants.PAY_PREFIX + orderId);
                }catch (Exception e){
                    logger.info("异常",e);
                    logger.info(orderId+"删除key异常ysb");
                }

                RepayInfo repayInfo = new RepayInfo();
                repayInfo.setRepayStatus("1");
                repayInfo.setBillNo(orderId);
                repayInfo.setRepayAmount(new BigDecimal(notifyResponseVo.getAmount()));
                repayInfo.setRepayChannel("ysb");
                repayInfo.setRepayTime(new Date());
                repayInfo.setRepayNo(notifyResponseVo.getOrderId());

                if(sourceOrderId.contains("k")){
                    String[] infoarray = null;
                    try {
                        String infos = (String) redisTemplate.opsForValue().get(Constants.QUICK_PAY + orderId);
                        infoarray = infos.split("-");
                    }catch (Exception e){
//                        e.printStackTrace();
                    }

                    repayInfo.setRepayType(RepayType.KJ_FAIL.getCode());
                    if(infoarray!=null&&infoarray.length==3){
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
                }else if(sourceOrderId.contains("z")){

                    repayInfo.setRepayType(RepayType.ZD_FAIL.getCode());
                    logger.info("主动还款，还款流水表对象："+JSON.toJSONString(repayInfo));
                    try {
                        rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    LoggerUtils.write(repayInfo);
                }else if(sourceOrderId.contains("q")){

                    repayInfo.setRepayType(RepayType.QK_FAIL.getCode());
                    logger.info("强扣还款，还款流水表对象："+JSON.toJSONString(repayInfo));
                    LoggerUtils.write(repayInfo);
                }else {

                    repayInfo.setRepayType(RepayType.UNKNOW_FAIL.getCode());
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
        logger.info("回调成功");
        return "success";// 反馈处理结果
    }


}
