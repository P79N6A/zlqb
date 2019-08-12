package com.nyd.capital.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.capital.model.RemitMessage;
import com.nyd.capital.model.WsmQuery;
import com.nyd.capital.model.enums.RemitStatus;
import com.nyd.capital.model.kzjr.AssetSubmitRequest;
import com.nyd.capital.model.kzjr.FailOrderKzjrInfo;
import com.nyd.capital.model.kzjr.QueryAssetRequest;
import com.nyd.capital.model.kzjr.response.KzjrCallbackResponse;
import com.nyd.capital.model.kzjr.response.KzjrInvestorResponse;
import com.nyd.capital.service.*;
import com.nyd.capital.service.kzjr.KzjrService;
import com.nyd.capital.service.kzjr.config.KzjrConfig;
import com.nyd.capital.service.utils.Constants;
import com.nyd.capital.service.utils.LoggerUtils;
import com.nyd.capital.service.validate.ValidateException;
import com.nyd.order.api.OrderContract;
import com.nyd.order.api.OrderDetailContract;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.enums.BorrowConfirmChannel;
import com.nyd.pay.api.service.PayService;
import com.nyd.zeus.model.RemitInfo;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import com.tasfe.framework.redis.RedisService;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Cong Yuxiang
 * 2017/12/13
 **/
@Service
public class KzjrFundService implements FundService{

    Logger logger = LoggerFactory.getLogger(KzjrFundService.class);

    @Autowired
    private KzjrService kzjrService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private FundSourceService fundSourceService;

    @Autowired
    private KzjrProductConfigService kzjrProductConfigService;

    @Autowired
    private FailOrderService failOrderService;

    @Autowired(required = false)
    private OrderDetailContract orderDetailContract;


//    @Autowired(required = false)
//    private RemitContract remitContract;

    @Autowired(required = false)
    private OrderContract orderContract;

    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;

    @Autowired
    private KzjrConfig kzjrConfig;

    @Autowired
    private PayService payService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisService redisService;

    /**
     * 推送资产到空中金融
     * @param orderList 订单列表
     * @return RemitStatus
     */
    @Override
    public ResponseData sendOrder(List orderList) {
        Iterator<AssetSubmitRequest> ite=orderList.iterator();

        String date = DateFormatUtils.format(new Date(),"yyyy-MM-dd");
        while (ite.hasNext()){
            AssetSubmitRequest request = ite.next();
            int duration = request.getDuration();



//            KzjrProductConfig configInfo = kzjrProductConfigService.queryByPriority(duration,request.getAmount());
//
//            if(configInfo == null){
//                logger.info("选择的productcode为null");
//                FailOrderKzjrInfo info = new FailOrderKzjrInfo();
//                info.setReason(2);
//                info.setDescription("没有适合期限和时间的产品");
//                info.setAccountId(request.getAccountId());
//                info.setAmount(request.getAmount());
//                info.setDuration(request.getDuration());
//                info.setOrderNo(request.getOrderId());
//                try {
//                    failOrderService.saveKzjrInfo(info);
//                }catch (Exception e){
//                    e.printStackTrace();
//                    logger.error(JSON.toJSONString(request)+"saveKzjrInfo保存数据库失败"+e.getMessage());
//                }
//                return RemitStatus.KZJR_NO_PRODUCT;
//            }
//            logger.info("选择的productcode为:"+JSON.toJSONString(configInfo));

//                request.setProductCode(configInfo.getProductCode());
                logger.info("资产为:"+ JSON.toJSONString(request));
            JSONObject resultObj = kzjrService.assetSubmit(request); // 提交资产

            Integer channel = Integer.valueOf(request.getOrderId().charAt(request.getOrderId().length()-1)+"");
            request.setOrderId(request.getOrderId().split(Constants.KZJR_SPLIT)[0]);
                logger.info("资产提交结果:"+resultObj.toJSONString());
                if(resultObj.getInteger("status")==0){
                    return ResponseData.success();
                }else if(resultObj.getInteger("status")==5012){
                    logger.error("渠道订单号已存在"+request.getOrderId());
//                    limitProcess(configInfo.getId(),configInfo.getProductCode(),request.getAmount());
                    return ResponseData.error();
                }
                else{
                    logger.error("sendorder error***orderId:"+request.getOrderId()+"原因:"+resultObj.toJSONString());
                    FailOrderKzjrInfo info = new FailOrderKzjrInfo();
                    info.setReason(0);
                    info.setDescription(resultObj.getString("msg"));
                    info.setAccountId(request.getAccountId());
                    info.setAmount(request.getAmount());
                    info.setDuration(request.getDuration());
                    info.setOrderNo(request.getOrderId());
                    info.setChannel(channel);
                    try {

                        failOrderService.saveKzjrInfo(info);

                    }catch (Exception e){
                        e.printStackTrace();
                        logger.error("保存数据库失败"+e.getMessage());
                    }
//                    limitProcess(configInfo.getId(),configInfo.getProductCode(),request.getAmount());
                    return ResponseData.error();
//                    return RemitStatus.ERROR;
//                    return false;
                }
//            }



        }
        logger.error("无订单发送");
        return ResponseData.error();

    }
    private void limitProcess(Long id,String productCode,BigDecimal amount){
        try {
            String flag = redisService.acquireLock(Constants.KZJR_LOCK_PREFIX + productCode);
            //获取不到循环
            int loopCount = 120;
            while (flag == null&&loopCount>0) {
                loopCount--;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                flag = redisService.acquireLock(Constants.KZJR_LOCK_PREFIX + productCode);
            }
            if(flag==null){
                logger.error("提交资产返回失败 获取锁 失败");
                return;
            }
            redisTemplate.opsForValue().increment(Constants.KZJR_PREFIX + productCode, amount.doubleValue());
            double remainAmount = (double) redisTemplate.opsForValue().get(Constants.KZJR_PREFIX + productCode);
            kzjrProductConfigService.update(id, 0, 0, new BigDecimal(remainAmount));
            redisService.releaseLock(Constants.KZJR_LOCK_PREFIX + productCode);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("提交资产返回失败 重新更新余额 异常");
            return;
        }finally {
            try {
                redisService.releaseLock(Constants.KZJR_LOCK_PREFIX + productCode);
            }catch (Exception e){
                e.printStackTrace();
                logger.error("释放锁失败");
            }
        }
    }

    /**
     * 保存借款结果
     * @param result callback返回的JSON
     * @return 是否成功
     */
    @Override
    public boolean saveLoanResult(String result) {

        Date remitDate = new Date();//放款时间

        KzjrCallbackResponse response  = JSONObject.parseObject(result, KzjrCallbackResponse.class);
        logger.info("json转对象"+JSON.toJSONString(response));

        Integer channel = Integer.valueOf(response.getOrderId().charAt(response.getOrderId().length()-1)+"");
        logger.info("channel"+channel);
        String remitNo = response.getOrderId();
        String orderTmp = response.getOrderId().split(Constants.KZJR_SPLIT)[0];
        response.setOrderId(orderTmp);


        // 避免重复的通知
        try {
            if (redisTemplate.hasKey(Constants.KZJR_CALLBACK_PREFIX + response.getOrderId())) {
                logger.error("有重复通知" + JSON.toJSONString(response));
                return true;
            } else {
                redisTemplate.opsForValue().set(Constants.KZJR_CALLBACK_PREFIX + response.getOrderId(), "1", 2880, TimeUnit.MINUTES);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("写redis出错"+e.getMessage());
        }

        List<KzjrInvestorResponse> investorList = response.getInvestorList();

        BigDecimal remitAmountTotal=new BigDecimal(0);
        for (KzjrInvestorResponse investorResponse:investorList){
            remitAmountTotal = remitAmountTotal.add(investorResponse.getAmount());
        }

        logger.info(response.getOrderId()+"放款金额为"+remitAmountTotal);

        RemitMessage remitMessage = new RemitMessage();
        remitMessage.setRemitStatus("0");

        remitMessage.setRemitAmount(remitAmountTotal);
        //查询空中金融资产状态
        QueryAssetRequest request = new QueryAssetRequest();
        request.setChannelCode(kzjrConfig.getChannelCode());
        request.setOrderId(remitNo);
        String status = "";
        try {
            JSONObject jsonObject = kzjrService.queryAsset(request);
            logger.info(JSON.toJSONString(request) + "查询资产code" + JSONObject.toJSONString(jsonObject));
            if (jsonObject!=null&&jsonObject.getJSONObject("data")!=null) {
                JSONObject data = jsonObject.getJSONObject("data");
                if (data!=null&&data.getString("status")!=null) {
                    status = data.getString("status");
                }
            }
        } catch (Exception e) {
            logger.error("queryAsset has exception！request is "+JSON.toJSONString(request),e);
        }
        if (!status.equals("50")) { //50提现失败 40成功
            remitMessage.setOrderNo(response.getOrderId());
            rabbitmqProducerProxy.convertAndSend("remit.nyd",remitMessage);
        }


        OrderInfo orderInfo = orderContract.getOrderByOrderNo(response.getOrderId()).getData();
        int loopCount=10;
        while (orderInfo==null&&loopCount>0){
            orderInfo = orderContract.getOrderByOrderNo(response.getOrderId()).getData();
            loopCount--;
        }
        if(orderInfo == null){
            logger.error(response.getOrderId()+"orderinfo为空");
            return false;
        }

        logger.info("orderInfo:"+JSON.toJSONString(orderInfo));

        //如果是null 默认为nyd的订单来源
        if(orderInfo.getChannel()==null){
            orderInfo.setChannel(BorrowConfirmChannel.NYD.getChannel());
        }

        //发送 到 ibank
        if(orderInfo.getChannel()==BorrowConfirmChannel.YMT.getChannel()){
            if (!status.equals("50")) {
                remitMessage.setOrderNo(orderInfo.getIbankOrderNo());
                logger.info("放款成功发送ibank."+JSON.toJSONString(remitMessage));
                rabbitmqProducerProxy.convertAndSend("remitIbankOrder.ymt",remitMessage);
            }
        }



     /*   OrderDetailInfo orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(orderInfo.getOrderNo()).getData();
        loopCount=10;
        while (orderDetailInfo==null&&loopCount>0){
            orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(orderInfo.getOrderNo()).getData();
            loopCount--;
        }
        if(orderDetailInfo==null){
            logger.error("orderDetailInfo为空");
            return false;
        }
        logger.info("orderDetailInfo:"+JSON.toJSONString(orderDetailInfo));*/

        // Update by Jiawei Cheng 2018-3-29 20:48 如果是银码头的来源，不进行扣会员费
        //会员费前置 不主动扣会员费
//        if(orderInfo.getMemberFee().compareTo(new BigDecimal("0.0"))==1 && BorrowConfirmChannel.NYD.getChannel().equals(orderInfo.getChannel())){
//            CreateOrderVo vo = new CreateOrderVo();
//            vo.setP3_orderId(orderInfo.getOrderNo()+"_"+(System.currentTimeMillis()+"").substring(2,11));
//            vo.setP4_timestamp(DateFormatUtils.format(new Date(),"yyyyMMddHHmmss"));
//            vo.setP5_payerName(orderDetailInfo.getRealName());
//            vo.setP7_idCardNo(orderDetailInfo.getIdNumber());
//            vo.setP8_cardNo(orderInfo.getBankAccount());
//            vo.setP11_orderAmount(orderInfo.getMemberFee());
//            logger.info("代扣会员费请求参数:"+JSON.toJSONString(vo));
//            ResponseData r = payService.withHold(vo, WithHoldType.MEMBER_FEE);
//            logger.info("会员费 代扣结果"+JSON.toJSONString(r));
//        }


        for(KzjrInvestorResponse investor:investorList) {
            RemitInfo remitInfo = new RemitInfo();
//        try {
//            JSONObject assetResult = kzjrService.queryAsset(request);
//            logger.info(JSON.toJSONString(request) + "查询资产code" + JSONObject.toJSONString(assetResult));
//            if (assetResult.getInteger("status") == 0) {
//                remitInfo.setContractLink(assetResult.getJSONObject("data").getString("productCode"));
//            } else {
//                remitInfo.setContractLink("111111");
//            }
//        }catch (Exception e){
//            logger.error("调用查询kzjr资产code失败",e);
//            e.printStackTrace();
//            remitInfo.setContractLink("111111");
//        }

            remitInfo.setRemitTime(remitDate);
            remitInfo.setOrderNo(response.getOrderId());
            if (status.equals("50")) {
                remitInfo.setRemitStatus("1");
            } else {
                remitInfo.setRemitStatus("0");
            }
            remitInfo.setFundCode("kzjr");
            remitInfo.setRemitNo(remitNo);
            remitInfo.setChannel(orderInfo.getChannel());
            remitInfo.setRemitAmount(investor.getAmount());
            remitInfo.setInvestorId(investor.getInvestorId());
            remitInfo.setInvestorName(investor.getInvestorName());
            try {
                rabbitmqProducerProxy.convertAndSend("remitLog.nyd", remitInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            LoggerUtils.write(remitInfo);
        }
//        try {
//            remitContract.save(remitInfo);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        remitInfo.set
//        payService.withHold()
        return true;
    }

    @Override
    public String queryOrderInfo(WsmQuery query) {
        return null;
    }

    @Override
    public List generateOrdersTest() {
        return null;
    }

    @Override
    public List generateOrders(String userId, String orderNo,Integer channel) throws ValidateException {
        List list = new ArrayList();
        OrderInfo orderInfo = orderContract.getOrderByOrderNo(orderNo).getData();
        int loopCount=10;
        while (orderInfo == null && loopCount>0){ // 防止dubbo出现错误
            orderInfo = orderContract.getOrderByOrderNo(orderNo).getData();
            loopCount--;
        }
        if(orderInfo == null){
            logger.error("userId:"+userId+"orderNo:"+orderNo+"orderInfo为null");
        }


        AssetSubmitRequest request = new AssetSubmitRequest();
//        request.setChannelCode(kzjrConfig.getChannelCode());


        request.setOrderId(orderNo+Constants.KZJR_SPLIT+(System.currentTimeMillis()+"").substring(2,11)+channel); //请求空中金融放款的订单号
        request.setAccountId(userAccountService.queryAccountIdByUserId(userId));//空中金融开户id
        request.setAmount(orderInfo.getLoanAmount());//订单放款金额
        request.setType(2); //1、自然资产 2、通路资产 3、过账资产，默认为资产资产
        request.setDuration(orderInfo.getBorrowTime());//借款时长


        list.add(request);
        return list;
    }


    public static void main(String[] args) {
        BigDecimal a = new BigDecimal("0.01");
        BigDecimal b = new BigDecimal("0.0");
        System.out.println(a.compareTo(b));
    }
}
