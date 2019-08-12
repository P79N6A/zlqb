package com.creativearts.nyd.pay.service.baofoo.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.creativearts.nyd.pay.model.RepayMessage;
import com.creativearts.nyd.pay.model.RepayType;
import com.creativearts.nyd.pay.model.WithholdOrderDetailsResDTO;
import com.creativearts.nyd.pay.model.baofoo.*;
import com.creativearts.nyd.pay.service.baofoo.JoinPayWithholdService;
import com.creativearts.nyd.pay.service.baofoo.util.HttpUtils;
import com.nyd.order.api.OrderContract;
import com.nyd.order.entity.WithholdOrder;
import com.nyd.order.model.YmtKzjrBill.enums.BillStatusEnum;
import com.nyd.order.model.dto.CreatePayOrderDto;
import com.nyd.order.model.dto.RequestData;
import com.nyd.order.model.dto.SubmitWithholdDto;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.model.UserInfo;
import com.nyd.zeus.api.BillContract;
import com.nyd.zeus.model.BillInfo;
import com.nyd.zeus.model.RepayInfo;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author liuqiu
 */
@Service
public class JoinPayWithholdServiceImpl implements JoinPayWithholdService {

    private static Logger logger = LoggerFactory.getLogger(JoinPayWithholdServiceImpl.class);

    @Value("${common.pay.ip}")
    private String commonPayIp;
    @Value("${common.pay.port}")
    private String commonPayPort;
    @Value("${common.pay.query.ip}")
    private String commonPayQueryIp;
    @Value("${common.pay.query.port}")
    private String commonPayQueryPort;
    @Value("${withhold.callback.url}")
    private String callbackUrl;
    @Autowired
    private UserIdentityContract identityContract;
    @Autowired
    private OrderContract orderContract;
    @Autowired
    private BillContract billContract;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;

    @Override
    public ResponseData<SelectBankCardResult> selectBankCard(SelectBankCardDto dto) {
        ResponseData<SelectBankCardResult> result = new ResponseData<>();
        if (dto == null || StringUtils.isBlank(dto.getUserId())) {
            result.setStatus("1");
            result.setMsg("参数错误");
            return result;
        }
        if (StringUtils.isBlank(dto.getAppName())) {
            dto.setAppName("nyd");
        }
        logger.info("begin select bank card,and param is:" + dto.toString());
        ResponseData<UserInfo> userInfo = identityContract.getUserInfo(dto.getUserId());
        if (!"0".equals(userInfo.getStatus())) {
            result.setStatus("1");
            result.setMsg("查询用户信息失败");
            return result;
        }
        UserInfo info = userInfo.getData();
        //调用第三方接口查询
        String url = "http://" + commonPayIp + ":" + commonPayPort + "/common/pay/queryBindCard";
        QueryBindCardReqDTO cardReqDTO = new QueryBindCardReqDTO();
        cardReqDTO.setIdcardNo(info.getIdNumber());
        String[] channelCode = new String[]{"joinpay"};
        cardReqDTO.setPayChannelCodes(channelCode);
        cardReqDTO.setSource("nyd-repay");
        cardReqDTO.setState(3);
        String json = JSON.toJSONString(cardReqDTO);
        try {
            String sendPost = HttpUtils.sendPost(url, json);
            if (StringUtils.isBlank(sendPost)) {
                result.setStatus("1");
                result.setMsg("查询失败");
                return result;
            }
            logger.info("调用公共服务获取的结果是:" + sendPost);
            JSONObject jsonObject = JSONObject.parseObject(sendPost);
            String status = jsonObject.getString("status");
            if (!"0".equals(status)) {
                result.setStatus("1");
                result.setMsg("查询失败");
                return result;
            }
            String data = jsonObject.getString("data");
            List<QueryBuindCard> list = JSONObject.parseArray(data, QueryBuindCard.class);
            SelectBankCardResult cardResult = new SelectBankCardResult();
            if (list.size() == 0) {
                cardResult.setIfBindCard(0);
            } else {
                cardResult.setIfBindCard(1);
                cardResult.setCardList(list);
                QueryBuindCard card = list.get(0);
                cardResult.setIdNumber(card.getIdcardNo());
            }
            result.setStatus("0");
            result.setMsg("success");
            result.setData(cardResult);
            logger.info("end select bank card list is :" + JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            logger.error("begin select bank card has exception,and param is", e);
            result.setStatus("1");
            result.setMsg("查询失败");
            return result;
        }
    }

    @Override
    public ResponseData withHold(BaoFooWithHoldDto dto) {
        logger.info("begin joinpay repay withhold,and param is:" + dto.toString());
        try {
            ResponseData<UserInfo> userInfo = identityContract.getUserInfo(dto.getUserId());
            if (!"0".equals(userInfo.getStatus())) {
                return ResponseData.error("查询身份证号失败");
            }
            if (redisTemplate.hasKey("withHold:joinpay" + dto.getBillNo())) {
                return ResponseData.error("请勿重复提交");
            }
            ResponseData<BillInfo> responseData = billContract.getBillInfo(dto.getBillNo());
            if (!"0".equals(responseData.getStatus())) {
                return responseData;
            }
            BillInfo billInfo = responseData.getData();
            String billStatus = billInfo.getBillStatus();
            if (BillStatusEnum.REPAY_SUCCESS.getCode().equals(billStatus)) {
                return ResponseData.error("该账单已还清,请勿重复还款");
            }
            BigDecimal amount = billInfo.getWaitRepayAmount();
            if (amount.compareTo(new BigDecimal(dto.getAmount())) != 0) {
                return ResponseData.error("还款金额不符");
            }
            UserInfo info = userInfo.getData();
            String idNumber = info.getIdNumber();
            //调用代扣
            //调用代扣订单生成接口
            String url = "http://" + commonPayIp + ":" + commonPayPort + "/common/pay/createOrder";
            CreatePayOrderDto createPayOrderDto = new CreatePayOrderDto();
            createPayOrderDto.setBankcardNo(dto.getCardNo());
            createPayOrderDto.setBusinessOrderNo(dto.getBillNo());
            createPayOrderDto.setBusinessOrderType("侬要贷还款");
            createPayOrderDto.setIdNumber(idNumber);
            createPayOrderDto.setMobile(dto.getPhone());
            createPayOrderDto.setPayAmount(Double.valueOf(dto.getAmount()));
            createPayOrderDto.setPayChannelCodeBx("joinpay");
            //还款商户平台统一用nyd(产品确认)
            createPayOrderDto.setMerchantCode("nyd-repay");
            createPayOrderDto.setPayType(1);
            createPayOrderDto.setCallbackUrl(callbackUrl);
            createPayOrderDto.setSplitCode("no_retry");
            RequestData requestData = new RequestData();
            requestData.setData(createPayOrderDto);
            requestData.setRequestAppId("common.order");
            requestData.setRequestId(getUUID32());
            requestData.setRequestTime(JSON.toJSONString(new Date()));
            String json = JSON.toJSONString(requestData);
            String payOrderNo = null;
            WithholdOrder withholdOrder = new WithholdOrder();
            withholdOrder.setMemberId(dto.getBillNo());
            withholdOrder.setPayAmount(new BigDecimal(dto.getAmount()));
            withholdOrder.setUserId(dto.getUserId());
            withholdOrder.setAppName(dto.getAppName());
            withholdOrder.setPayType("2");
            try {
                String sendPost = HttpUtils.sendPost(url, json);
                if (StringUtils.isBlank(sendPost)) {
                    return ResponseData.error("支付失败");
                }
                logger.info("调用公共服务创建代扣订单获取的结果是:" + sendPost);
                JSONObject jsonObject = JSONObject.parseObject(sendPost);
                String status = jsonObject.getString("status");
                if (!"0".equals(status)) {
                    return ResponseData.error("支付失败");
                }
                JSONObject data = jsonObject.getJSONObject("data");
                payOrderNo = data.getString("payOrderNo");
                redisTemplate.opsForValue().set("withHold:joinpay" + dto.getBillNo(), "1",24*60,TimeUnit.MINUTES);
            } catch (Exception e) {
                logger.error("调用生成代扣订单接口发生异常");
                return ResponseData.error("支付失败");
            }
            withholdOrder.setPayOrderNo(payOrderNo);
            //调用代扣接口
            SubmitWithholdDto submitWithholdDto = new SubmitWithholdDto();
            String url1 = "http://" + commonPayIp + ":" + commonPayPort + "/common/pay/submitWithhold";
            submitWithholdDto.setPayOrderNo(payOrderNo);
            submitWithholdDto.setWithholdAmount(Double.valueOf(dto.getAmount()));
            requestData.setData(submitWithholdDto);
            requestData.setRequestAppId("nyd-repay");
            requestData.setRequestId(getUUID32());
            requestData.setRequestTime(JSON.toJSONString(new Date()));
            String json1 = JSON.toJSONString(requestData);
            String withholdOrderNo = null;
            try {
                String sendPost = HttpUtils.sendPost(url1, json1);
                if (StringUtils.isBlank(sendPost)) {
                    return ResponseData.error("支付失败");
                }
                logger.info("调用公共服务发起代扣获取的结果是:" + sendPost);
                JSONObject jsonObject = JSONObject.parseObject(sendPost);
                String status = jsonObject.getString("status");
                if (!"0".equals(status)) {
                    return ResponseData.error("支付失败");
                }
                JSONObject data = jsonObject.getJSONObject("data");
                withholdOrderNo = data.getString("withholdOrderNo");
                withholdOrder.setWithholdOrderNo(withholdOrderNo);
            } catch (Exception e) {
                logger.error("调用生成代扣订单接口发生异常");
                return ResponseData.error("支付失败");
            }
            withholdOrder.setOrderStatus(1);
            orderContract.saveWithholdOrder(withholdOrder);
            Map<String, String> map = new HashMap<>();
            map.put("payOrderNo", payOrderNo);
            return ResponseData.success(map);
        } catch (Exception e) {
            logger.error("joinpay repay withhold has exception,and param is:" + dto.toString());
            return ResponseData.error("支付失败");
        }
    }

    @Override
    public ResponseData payCallback(PayCallbackDTO dto) {
        //接受到成功的回调后,进行后续操作
        String payOrderNo = dto.getPayOrderNo();
        if(StringUtils.isBlank(payOrderNo)) {
        	return ResponseData.error("参数异常");
        }
        ResponseData<WithholdOrder> data = orderContract.selectWithholdOrder(payOrderNo);
        if (data == null || (!"0".equals(data.getStatus()))) {
            return ResponseData.error("查询失败");
        }
        WithholdOrder withholdOrder = data.getData();
        RepayInfo repayInfo = new RepayInfo();
        if (dto.getResult().equals(1)) {
            withholdOrder.setOrderStatus(2);
            repayInfo.setRepayStatus("0");
            String billNo = withholdOrder.getMemberId();
            RepayMessage repayMessage = new RepayMessage();
            repayMessage.setBillNo(billNo);
            repayMessage.setRepayAmount(withholdOrder.getPayAmount());
            repayMessage.setRepayStatus("0");
            rabbitmqProducerProxy.convertAndSend("repay.nyd", repayMessage);
            if (dto.getResult().equals(1)) {
                //银码头
                try {
                    ResponseData<BillInfo> responseData = billContract.getBillInfo(billNo);
                    logger.info("付款成功后获取billInfo" + JSON.toJSONString(responseData));
                    if ("0".equals(responseData.getStatus())) {
                        BillInfo billInfo = responseData.getData();
                        if (billInfo != null && StringUtils.isNotBlank(billInfo.getIbankOrderNo())) {
                            repayMessage.setBillNo(billInfo.getIbankOrderNo());
                            repayMessage.setRepayTime(new Date());
                            logger.info("还款成功发送银码头" + JSON.toJSONString(repayMessage));
                            rabbitmqProducerProxy.convertAndSend("payIbank.ibank", repayMessage);
                        }
                    } else {
                        logger.info(billNo + "获取billInfo为status为1");
                    }

                } catch (Exception e) {
                    logger.info(billNo + "error", e);
                }
            }
            repayInfo.setBillNo(billNo);
            repayInfo.setRepayAmount(new BigDecimal(dto.getAmount()));
            repayInfo.setRepayChannel("joinpay");
            repayInfo.setRepayTime(new Date());
            repayInfo.setRepayType(RepayType.JOINPAY_WITHHOLD.getCode());
            repayInfo.setUserId(withholdOrder.getUserId());
            repayInfo.setRepayNo(dto.getWithholdOrderNo());
            try {
                rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
            } catch (Exception e) {
                return ResponseData.error();
            }
        } else {
    	   repayInfo.setBillNo(withholdOrder.getMemberId());
           repayInfo.setRepayAmount(new BigDecimal(dto.getAmount()));
           repayInfo.setRepayChannel("joinpay");
           repayInfo.setRepayTime(new Date());
           repayInfo.setRepayType(RepayType.JOINPAY_WITHHOLD.getCode());
           repayInfo.setUserId(withholdOrder.getUserId());
           repayInfo.setRepayNo(dto.getWithholdOrderNo());
            withholdOrder.setOrderStatus(3);
            repayInfo.setRepayStatus("1");
            try {
                rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
            } catch (Exception e) {
                return ResponseData.error();
            }
        }
        redisTemplate.delete("withHold:joinpay" + withholdOrder.getMemberId());
        orderContract.updateWithHoldOrder(withholdOrder);
        logger.info(repayInfo.toString());
        return ResponseData.success();
    }

    @Override
    public ResponseData withHoldResult(BaoFooWithHoldResultDto dto) {
        logger.info("查询汇聚还款代扣结果,参数:" + dto.toString());
        try {
            //根据账单号去查询代扣结果
            Map<String, String> map = new HashMap<>();
            ResponseData<WithholdOrder> responseData = orderContract.selectWithholdOrder(dto.getPayOrderNo());
            if (responseData == null || (!"0".equals(responseData.getStatus()))) {
                map.put("result", "0");
            } else {
                WithholdOrder data = responseData.getData();
                if (data.getOrderStatus().equals(1)) {
                    map.put("result", "0");
                } else if (data.getOrderStatus().equals(2)) {
                    map.put("result", "1");
                } else if (data.getOrderStatus().equals(3)) {
                    map.put("result", "2");
                }
                //map.put("result","1");
            }
            return ResponseData.success(map);
        } catch (Exception e) {
            logger.error("查询汇聚还款代扣结果发生异常", e);
            return ResponseData.error("查询失败");
        }
    }

    @Override
    public ResponseData selectStatusOne() {
        ResponseData responseData = orderContract.selectWithholdIng();
        return responseData;
    }

    @Override
    public ResponseData withHoldOrder(String payOrderNo) {
        ResponseData result = ResponseData.success();
        //调用第三方接口查询
        String url = "http://" + commonPayQueryIp + ":" + commonPayQueryPort + "/common/pay/query/queryAllWithholdOrderByPayOrderNo";
        Map<String, Object> map = new HashMap<>(100);
        map.put("payOrderNo", payOrderNo);
        int[] state = {3,4};
        map.put("state",state);
        String json = JSON.toJSONString(map);
        try {
            String sendPost = HttpUtils.sendPost(url, json);
            if (StringUtils.isBlank(sendPost)) {
                result.setStatus("1");
                result.setMsg("查询失败");
                return result;
            }
            logger.info("调用公共服务获取的结果是:" + sendPost);
            JSONObject jsonObject = JSONObject.parseObject(sendPost);
            String status = jsonObject.getString("status");
            if (!"0".equals(status)) {
                result.setStatus("1");
                result.setMsg("查询失败");
                return result;
            }
            String data = jsonObject.getString("data");
            List<WithholdOrderDetailsResDTO> list = JSONObject.parseArray(data, WithholdOrderDetailsResDTO.class);
            if (list.size() != 0){
                WithholdOrderDetailsResDTO withholdOrderDetailsResDTO = list.get(0);
                Short state1 = withholdOrderDetailsResDTO.getState();
                String s = String.valueOf(state1);
                return result.setData(s);
            }else {
                return result.setData("2");
            }

        } catch (Exception e) {
            logger.error("查询公共汇聚还款代扣结果发生异常", e);
            return ResponseData.error("查询失败");
        }
    }

    private String getUUID32() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

}
