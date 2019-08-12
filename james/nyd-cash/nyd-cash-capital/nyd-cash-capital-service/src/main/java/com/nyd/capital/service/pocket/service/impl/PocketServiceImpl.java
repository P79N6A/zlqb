package com.nyd.capital.service.pocket.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esotericsoftware.minlog.Log;
import com.nyd.capital.model.RemitMessage;
import com.nyd.capital.model.enums.KdlcResponseEnum;
import com.nyd.capital.model.pocket.*;
import com.nyd.capital.service.jx.config.OpenPageConstant;
import com.nyd.capital.service.jx.util.DingdingUtil;
import com.nyd.capital.service.pocket.service.PocketService;
import com.nyd.capital.service.pocket.util.*;
import com.nyd.capital.service.utils.Constants;
import com.nyd.order.api.OrderContract;
import com.nyd.order.api.OrderExceptionContract;
import com.nyd.order.entity.PockerOrderEntity;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.enums.BorrowConfirmChannel;
import com.nyd.user.api.UserBankContract;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.model.BankInfo;
import com.nyd.user.model.UserInfo;
import com.nyd.zeus.model.RemitInfo;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author liuqiu
 */
@Service
public class PocketServiceImpl implements PocketService {

    private static Logger logger = LoggerFactory.getLogger(PocketServiceImpl.class);

    @Autowired
    private UserIdentityContract userIdentityContract;
    @Autowired
    private PocketConfig pocketConfig;
    @Autowired
    private PocketOrderUtil pocketOrderUtil;
    @Autowired
    private OrderContract orderContract;
    @Autowired
    private UserBankContract userBankContract;
    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private PocketService pocketService;
    @Autowired(required = false)
    private OrderExceptionContract orderExceptionContract;
    @Override
    public ResponseData withdraw(OrderInfo orderInfo) {
    	ResponseData response = ResponseData.success();
    	if(null==orderInfo) {
    		logger.error("放款参数为空");
    		response = ResponseData.error("放款参数为空");
    		return response;
    	}
        logger.info("口袋理财放款入参数据：" + orderInfo.toString());
        ResponseData<PockerOrderEntity> isExistOrderNo = null;
        try {
        	isExistOrderNo=orderContract.getByUserId(orderInfo.getOrderNo());
        	if(null!=isExistOrderNo.getData()) {
        		response = ResponseData.error("该订单号已经存在,状态为:"+getStatusToString(isExistOrderNo.getData().getOrderStatus()));
            	logger.error("该订单已经存在: 状态为: "+getStatusToString(isExistOrderNo.getData().getOrderStatus()));
                return response;
        	}
		} catch (Exception e) {
		}
        
        try {
            //获取用户信息
            ResponseData<UserInfo> userInfoResponseData = userIdentityContract.getUserInfo(orderInfo.getUserId());
            if (OpenPageConstant.STATUS_ONE.equals(userInfoResponseData.getStatus())) {
                return ResponseData.error("查询用户信息错误!");
            }
            UserInfo userInfo = userInfoResponseData.getData();
            //订单信息
            try {
                orderInfo = orderContract.getOrderByOrderNo(orderInfo.getOrderNo()).getData();
                logger.info("orderInfo:" + orderInfo.toString());
            } catch (Exception e) {
                logger.error("查询订单表出错 orderInfo is", orderInfo.toString(),e);
                return ResponseData.error("查询订单表出错");
            }
            String realName = userInfo.getRealName();
            String bankName = orderInfo.getBankName();
            String bankAccount = orderInfo.getBankAccount();
            Integer amount = orderInfo.getLoanAmount().multiply(new BigDecimal(100)).intValue();
            if("建设银行".equals(bankName)) {
            	bankName="中国建设银行";
            }
            if("农业银行".equals(bankName)) {
            	bankName="中国农业银行";
            }
            if("工商银行".equals(bankName)) {
            	bankName="中国工商银行";
            }
            if("光大银行".equals(bankName)) {
            	bankName="中国光大银行";
            }
            if("邮政银行".equals(bankName)) {
            	bankName="中国邮政储蓄银行";
            }
            int bankCode = BankUtil.getBankCode(bankName);
            if (bankCode == 0) {
                logger.error("不支持的银行卡,orderInfo is " + orderInfo.toString());
                return ResponseData.error("不支持的银行卡");
            }
            Map<String, String> map = new HashMap<>();
            // 生成pocketUserId
            String pocketUserId = String.valueOf(System.currentTimeMillis()).substring(3, 12);
            // 生成yur_ref
            String yur_ref = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 30);
            //设置30位订单号
            map.put("yur_ref", yur_ref);
            map.put("user_id", pocketUserId);
            map.put("project_name", pocketConfig.getLoanOrderName());
            map.put("pwd", pocketConfig.getLoanOrderPwd());
            map.put("real_name", realName);
            map.put("bank_id", String.valueOf(bankCode));
            map.put("card_no", bankAccount);
            map.put("money", amount.toString());
            map.put("fee", "0");
            map.put("pay_summary", "申请放款");
            String sign = PocketPayUtil.createSign(map, pocketConfig.getLoanOrderKey());
            map.put("sign", sign);

            String param = PocketPayUtil.reqStrUtil(map);
            logger.info("口袋理财放款请求参数：" + param);
            
            /**入库*/
            PockerOrderEntity pockerOrderEntity = new PockerOrderEntity();
            pockerOrderEntity.setPocketUserId(Integer.parseInt(pocketUserId));//为口袋理财生成的user_id
            pockerOrderEntity.setUserId(userInfo.getUserId());
            pockerOrderEntity.setOrderNo(orderInfo.getOrderNo());
            pockerOrderEntity.setPocketNo(yur_ref);
            pockerOrderEntity.setOrderStatus(0);//交易状态：0、初始化；1、生成订单处理中;2、生成订单成功；3、生成订单失败；4、放款处理中; 5、放款成功; 6、放款失败
            pockerOrderEntity.setDeleteFlag(0);
            pockerOrderEntity.setCreateTime(new Date());
            pockerOrderEntity.setUpdateBy("sys");
            pockerOrderEntity.setUpdateTime(new Date());
            try {
            	orderContract.savePockerOrderEntity(pockerOrderEntity);
			} catch (Exception e) {
				logger.error("放款时往口袋表里添加此次订单失败，参数为: {}",JSON.toJSONString(pockerOrderEntity),e);
			}
            String result = PocketPayUtil.sendPostRequestBody(pocketConfig.getPocketWithdrawUrl() + pocketConfig.getPocketWithdraw(), param);
            if(StringUtils.isBlank(result)) {
            	//口袋未返回信息
            	PockerOrderEntity pockerOrderEntityUpStatus = new PockerOrderEntity();
            	pockerOrderEntityUpStatus.setOrderStatus(4);
            	pockerOrderEntityUpStatus.setOrderNo(orderInfo.getOrderNo());
            	orderContract.updatePockerOrderEntity(pockerOrderEntityUpStatus);
            	response = ResponseData.error("放款申请未收到口袋理财回复信息");
            	logger.error("放款申请未收到口袋理财回复信息,订单号： {}",orderInfo.getOrderNo());
                return response;
            }
            Map<String, Object> parse = (Map<String, Object>) JSONObject.parse(result);

            logger.info("口袋理财放款返回结果：" + parse);
            Integer code = (Integer) parse.get("code");
            if (code != 0) {
                String msg = String.valueOf(parse.get("msg"));
                response = ResponseData.error(code + msg);
                //放款失败,修改状态
            	PockerOrderEntity pockerOrderEntityUpStatus = new PockerOrderEntity();
            	pockerOrderEntityUpStatus.setOrderStatus(6);
            	pockerOrderEntityUpStatus.setOrderNo(orderInfo.getOrderNo());
            	orderContract.updatePockerOrderEntity(pockerOrderEntityUpStatus);
                return response;
            }
            //放款成功,修改状态
        	PockerOrderEntity pockerOrderEntityUpStatus = new PockerOrderEntity();
        	pockerOrderEntityUpStatus.setOrderStatus(4);
        	pockerOrderEntityUpStatus.setPayOrderId(String.valueOf(parse.get("pay_order_id")));
        	pockerOrderEntityUpStatus.setThirdPlatform(String.valueOf(parse.get("third_platform")));//第三方通道编号
        	pockerOrderEntityUpStatus.setOrderNo(orderInfo.getOrderNo());
        	orderContract.updatePockerOrderEntity(pockerOrderEntityUpStatus);
            response.setData(parse);
        } catch (Exception e) {
            logger.error("口袋理财放款接口发生异常", e);
            response = ResponseData.error(e.getMessage());
        }
        return response;
    }

    @Override
    public ResponseData withdrawQuery(String orderNo) {
    	logger.info("口袋理财放款查询入参数据：{}" , orderNo);
    	ResponseData response = ResponseData.success();
    	ResponseData<PockerOrderEntity> pockerOrderEntityData =null;
    	try {
    		pockerOrderEntityData = orderContract.getByUserId(orderNo);
    		if(null==pockerOrderEntityData.getData()) {
    			logger.error("未查询到该订单, orderNo: {}",orderNo);
    			response = ResponseData.error("未查询到该订单");
    			return response;
    		}
		} catch (Exception e) {
			logger.error("查询到该订单时出错, orderNo: {}",orderNo,e);
			response = ResponseData.error("服务器开小差");
			return response;
		}
        PockerOrderEntity pockerOrderEntity = pockerOrderEntityData.getData();
        try {
            Map<String, String> map = new HashMap<>();
            map.put("yur_ref", pockerOrderEntity.getPocketNo());
            map.put("project_name", pocketConfig.getLoanOrderName());
            String sign = PocketPayUtil.createSign(map, pocketConfig.getLoanOrderKey());
            map.put("sign", sign);
            String param = PocketPayUtil.reqStrUtil(map);
            logger.info("口袋理财放款查询请求参数：" + param);
            String result = PocketPayUtil.sendPostRequestBody(pocketConfig.getPocketWithdrawUrl() + pocketConfig.getPocketWithdrawQuery(), param);
            Map<String, String> parse = (Map<String, String>) JSONObject.parse(result);
            logger.info("口袋理财放款查询返回结果：" + parse);
            Integer code = Integer.parseInt(parse.get("code"));
            if (code != 0) {
            	PockerOrderEntity pockerOrderEntityUpStatus = new PockerOrderEntity();
            	pockerOrderEntityUpStatus.setOrderStatus(6);
            	pockerOrderEntityUpStatus.setOrderNo(orderNo);
            	orderContract.updatePockerOrderEntity(pockerOrderEntityUpStatus);
                response.setData(parse);
                return response;
            }
        	PockerOrderEntity pockerOrderEntityUpStatus = new PockerOrderEntity();
        	pockerOrderEntityUpStatus.setOrderStatus(5);
        	pockerOrderEntityUpStatus.setOrderNo(orderNo);
        	orderContract.updatePockerOrderEntity(pockerOrderEntityUpStatus);
            response.setData(parse);
        } catch (Exception e) {
            logger.error("口袋理财放款查询发生异常", e);
            response = ResponseData.error("服务器开小差");
        }
        return response;
    }

    /**
     * 创建订单
     */
    @Override
    public ResponseData createOrder(OrderInfo request) {
        logger.info("口袋理财创建订单接口参数:" + request.toString());
        ResponseData responseData = ResponseData.success();
        //获取用户信息
        ResponseData<UserInfo> userInfoResponseData = userIdentityContract.getUserInfo(request.getUserId());
        if (OpenPageConstant.STATUS_ONE.equals(userInfoResponseData.getStatus())) {
            logger.error("查询用户信息错误", request.toString());
            return ResponseData.error("查询用户信息错误!");
        }
        UserInfo userInfo = userInfoResponseData.getData();
        if (null == userInfo){
         	logger.error("查询用户信息错误", request.toString());
            return ResponseData.error("查询用户银行信息错误!");
        }
        //订单信息
        ResponseData<OrderInfo> orderByOrderNo = orderContract.getOrderByOrderNo(request.getOrderNo());
        if (OpenPageConstant.STATUS_ONE.equals(orderByOrderNo.getStatus())) {
            logger.error("查询订单信息错误", request.toString());
            return ResponseData.error("查询订单信息错误!");
        }
        if (null == orderByOrderNo.getData()){
            logger.error("查询订单信息错误", request.toString());
            return ResponseData.error("查询订单信息错误!");
        }
        request=orderByOrderNo.getData();;
        
        //获取用户银行信息
        ResponseData<List<BankInfo>> bankInfosByBankAccout = userBankContract.getBankInfosByBankAccout(request.getBankAccount());
        if (OpenPageConstant.STATUS_ONE.equals(bankInfosByBankAccout.getStatus())) {
        	logger.error("查询用户银行信息错误", request.toString());
            return ResponseData.error("查询用户银行信息错误!");
        }
        List<BankInfo> bankAccoutData = bankInfosByBankAccout.getData();
        if (bankAccoutData == null || bankAccoutData.size() == 0){
        	logger.error("查询用户银行信息错误", request.toString());
            return ResponseData.error("查询用户银行信息错误!");
        }
        
        Map<String, Object> map = new HashMap<>();
        try {
        	BankInfo bankInfo = bankAccoutData.get(0);
            //时间戳
            int timestamp = pocketOrderUtil.getSecondTimestamp(new Date());
            //账户
            String account = pocketConfig.getPocketOrderAccount();
            //身份证号
            String idNumber = userInfo.getIdNumber();
            //用户基本信息
            PocketUserBase pocketUserBase = new PocketUserBase();
            pocketUserBase.setBirthday(0);
            pocketUserBase.setContact_phone(0);
            pocketUserBase.setEducation_level(0);
            //身份证
            pocketUserBase.setId_number(idNumber);
            //用户姓名
            pocketUserBase.setName(userInfo.getRealName());
            //用户性别
            pocketUserBase.setProperty(userInfo.getGender());
            //借款人类型 1:企业，2:个人
            pocketUserBase.setType("2");
            map.put("user_base", pocketUserBase);
            map.put("id_number",idNumber);
            
            //打款银行卡信息
            PocketReceiveCard pocketReceiveCard = new PocketReceiveCard();
            String bankName = request.getBankName();
            if("建设银行".equals(bankName)) {
            	bankName="中国建设银行";
            }
            if("农业银行".equals(bankName)) {
            	bankName="中国农业银行";
            }
            if("工商银行".equals(bankName)) {
            	bankName="中国工商银行";
            }
            if("光大银行".equals(bankName)) {
            	bankName="中国光大银行";
            }
            int bankCode = BankUtil.getBankCode(bankName);
            if (bankCode == 0) {
                logger.error("不支持的银行卡,orderInfo is " + request.toString());
                return ResponseData.error("不支持的银行卡");
            }

            pocketReceiveCard.setBank_id(bankCode);
            pocketReceiveCard.setCard_no(request.getBankAccount());
            pocketReceiveCard.setName(userInfo.getRealName());
            pocketReceiveCard.setPhone(bankInfo.getReservedPhone());
            map.put("receive_card", pocketReceiveCard);
            
            //签名
            String createSign = pocketOrderUtil.createSign(userInfo.getIdNumber());
            if (StringUtils.isBlank(createSign)) {
                responseData.setCode(KdlcResponseEnum.SERVER_ERROR.getCode());
                responseData.setCode(KdlcResponseEnum.SERVER_ERROR.getMsg());
                return responseData;
            }
            map.put("sign", createSign);
            map.put("account",account);
            map.put("timestamp",String.valueOf(timestamp));
            
            //订单基本信息
            PocketOrderBase pocketOrderBase = new PocketOrderBase();
            pocketOrderBase.setApr(request.getAnnualizedRate().doubleValue());
            pocketOrderBase.setCounter_fee(0);
            pocketOrderBase.setIs_deposit(0);
            pocketOrderBase.setLend_pay_type(0);
            Float interest = request.getInterest().floatValue() * 100;
            pocketOrderBase.setLoan_interests(interest.intValue());
            pocketOrderBase.setLoan_method(1);
            pocketOrderBase.setLoan_term(request.getBorrowTime());
            Float loanAmount = request.getLoanAmount().floatValue() * 100;
            pocketOrderBase.setMoney_amount(loanAmount.intValue());
            pocketOrderBase.setOrder_time(Long.valueOf(timestamp));
            pocketOrderBase.setOut_trade_no(request.getOrderNo());
            map.put("order_base", pocketOrderBase);
		} catch (Exception e) {
			logger.error("拼装数据错误",e);
		}
        //调用口袋理财资产  创建订单接口
        String sendPost = HttpClientHelper.sendPost(pocketConfig.getPocketOrderCreate(), JSON.toJSONString(map), "utf-8");
        if (StringUtils.isBlank(sendPost)) {
        	PockerOrderEntity pockerOrderEntityUpStatus = new PockerOrderEntity();
        	pockerOrderEntityUpStatus.setOrderStatus(1);//生成订单处理中
        	pockerOrderEntityUpStatus.setOrderNo(request.getOrderNo());
        	orderContract.updatePockerOrderEntity(pockerOrderEntityUpStatus);
            logger.error("创建订单接口未接收到口袋理财返回参数! orderNo:{}",request.getOrderNo());
            responseData.setCode(KdlcResponseEnum.SERVER_ERROR.getCode());
            responseData.setMsg(KdlcResponseEnum.SERVER_ERROR.getMsg());
            return responseData;
        }
        logger.info("创建订单返回参数:{}"+JSON.toJSONString(sendPost));
        Map<String, Object> resultMap = JSON.parseObject(sendPost);
        String code = String.valueOf(resultMap.get("code"));
        if (StringUtils.isBlank(code) || !"0".equals(code)) {
        	PockerOrderEntity pockerOrderEntityUpStatus = new PockerOrderEntity();
        	pockerOrderEntityUpStatus.setOrderStatus(3);
        	pockerOrderEntityUpStatus.setOrderNo(request.getOrderNo());
        	orderContract.updatePockerOrderEntity(pockerOrderEntityUpStatus);
            logger.error("创建订单错误: {}", null != resultMap.get("message") ? resultMap.get("message") : null);
            responseData.setCode(String.valueOf(code));
            responseData.setMsg(String.valueOf(resultMap.get("message")));
            return responseData;
        }
    	PockerOrderEntity pockerOrderEntityUpStatus = new PockerOrderEntity();
    	pockerOrderEntityUpStatus.setOrderStatus(1);
    	pockerOrderEntityUpStatus.setPocketCreateOrderId(String.valueOf(resultMap.get("order_id")));
    	pockerOrderEntityUpStatus.setOrderNo(request.getOrderNo());
    	orderContract.updatePockerOrderEntity(pockerOrderEntityUpStatus);
    	responseData.setData(resultMap);
//        //入库
        return responseData;
    }

    @Override
    public ResponseData queryOrder(OrderInfo request) {
        logger.info("口袋理财订单查询入参数据：" + request.toString());
        ResponseData response = ResponseData.success();
        try {
            //获取用户信息
            ResponseData<UserInfo> userInfoResponseData = userIdentityContract.getUserInfo(request.getUserId());
            if (OpenPageConstant.STATUS_ONE.equals(userInfoResponseData.getStatus())) {
                return ResponseData.error("查询用户信息错误!");
            }
            UserInfo userInfo = userInfoResponseData.getData();
           
            Map<String, String> map = new HashMap<>();
            map.put("account", pocketConfig.getPocketOrderAccount());
            map.put("timestamp", String.valueOf(pocketOrderUtil.getSecondTimestamp(new Date())));
            map.put("id_number", userInfo.getIdNumber());
            map.put("out_trade_no", request.getOrderNo());
            String sign = pocketOrderUtil.createSign(userInfo.getIdNumber());
            map.put("sign", sign);
            String result = PocketPayUtil.sendPostRequestBody(pocketConfig.getPocketOrderQuery(), JSON.toJSONString(map));
            if (StringUtils.isBlank(result)) {
            	PockerOrderEntity pockerOrderEntityUpStatus = new PockerOrderEntity();
            	pockerOrderEntityUpStatus.setOrderStatus(1);
            	pockerOrderEntityUpStatus.setOrderNo(request.getOrderNo());
            	orderContract.updatePockerOrderEntity(pockerOrderEntityUpStatus);
                response = ResponseData.error("查询订单未收到口袋回复信息");
                logger.info("查询订单未收到口袋回复信息, orderNo:{}",request.getOrderNo());
                return response;
            }
            Map<String, String> parse = (Map<String, String>) JSONObject.parse(result);
            logger.info("口袋理财订单查询返回结果：" + parse);
            String code = String.valueOf(parse.get("code"));
            if (!"0".equals(code)) {
            	PockerOrderEntity pockerOrderEntityUpStatus = new PockerOrderEntity();
            	pockerOrderEntityUpStatus.setOrderStatus(1);
            	pockerOrderEntityUpStatus.setOrderNo(request.getOrderNo());
            	orderContract.updatePockerOrderEntity(pockerOrderEntityUpStatus);
                String msg = parse.get("message");
                response = ResponseData.error("查询订单错误 、"+msg);
                response.setStatus(parse.get("status"));
                return response;
            }
            String status = String.valueOf(parse.get("status"));
            if("21".equals(status)) {
            	PockerOrderEntity pockerOrderEntityUpStatus = new PockerOrderEntity();
            	//生成订单成功
            	pockerOrderEntityUpStatus.setOrderStatus(2);
            	pockerOrderEntityUpStatus.setOrderNo(request.getOrderNo());
            	orderContract.updatePockerOrderEntity(pockerOrderEntityUpStatus);
                response.setStatus(String.valueOf(parse.get("status")));
                response.setData(parse);
                return response;
            }
            if("-30".equals(status) || "-31".equals(status) || "-32".equals(status) || "-20".equals(status) || "-11".equals(status)) {
            	PockerOrderEntity pockerOrderEntityUpStatus = new PockerOrderEntity();
            	//生成订单处理中
            	pockerOrderEntityUpStatus.setOrderStatus(3);
            	pockerOrderEntityUpStatus.setOrderNo(request.getOrderNo());
            	orderContract.updatePockerOrderEntity(pockerOrderEntityUpStatus);
                response.setStatus(String.valueOf(parse.get("status")));
                response.setData(parse);
                return response;
            }
            PockerOrderEntity pockerOrderEntityUpStatus = new PockerOrderEntity();
            pockerOrderEntityUpStatus.setOrderStatus(1);
        	pockerOrderEntityUpStatus.setOrderNo(request.getOrderNo());
        	orderContract.updatePockerOrderEntity(pockerOrderEntityUpStatus);
            response.setStatus(String.valueOf(parse.get("status")));
            response.setData(parse);
        } catch (Exception e) {
            logger.error("口袋理财订单查询发生异常", e);
            response = ResponseData.error();
        }
        return response;
    }
    
    /**
     * 口袋理财回调
     */
	@Override
	public PocketCallbackResponseData pocketCallback(PocketCallbackDto pocketCallbackDto) {
		logger.info("口袋理财放款回调接口入口,参数值:{}",JSON.toJSONString(pocketCallbackDto));
		PocketCallbackResponseData pocketCallbackResponseData=new PocketCallbackResponseData<>();
		pocketCallbackResponseData.success();
		
		Map<String, Object> resultMap=new HashMap<String, Object>();
		
		if(null==pocketCallbackDto || StringUtils.isBlank(pocketCallbackDto.getOrder_id()) || StringUtils.isBlank(pocketCallbackDto.getSign())) {
			resultMap.put("code", "1001");
			resultMap.put("err_msg", "参数异常");
			pocketCallbackResponseData.sendParam(resultMap);
			return pocketCallbackResponseData;
		}
		
		ResponseData<PockerOrderEntity> responseData = orderContract.getPockerOrderEntityByPocketNo(pocketCallbackDto.getOrder_id());
		if(!"0".equals(responseData.getStatus()) || null==responseData.getData()){
			resultMap.put("code", "1002");
			resultMap.put("err_msg", "未查到该订单");
			pocketCallbackResponseData.sendParam(resultMap);
			return pocketCallbackResponseData;
		}
		
		PockerOrderEntity pockerOrderEntity = responseData.getData();
		
		Map<String, String> map=new HashMap<String, String>();
		map.put("code", String.valueOf(pocketCallbackDto.getCode()));
		map.put("order_id", pocketCallbackDto.getOrder_id());
		map.put("opr_dat", pocketCallbackDto.getOpr_dat());
		map.put("result", pocketCallbackDto.getResult());
		String sign = PocketPayUtil.createSign(map, pocketConfig.getPockCallbackKey());
		//验签
		if(!sign.equals(pocketCallbackDto.getSign())) {
			resultMap.put("code", "1003");
			resultMap.put("err_msg", "签名认证错误");
			pocketCallbackResponseData.sendParam(resultMap);
			return pocketCallbackResponseData;
		}
		
		PockerOrderEntity pockerOrderEntityUpStatus = new PockerOrderEntity();
		if("0".equals(String.valueOf(pocketCallbackDto.getCode()))) {
			logger.info("口袋理财放款回调该订单放款成功: 口袋理财订单号为:{}",pocketCallbackDto.getOrder_id());
			//口袋放款成功
        	pockerOrderEntityUpStatus.setOrderStatus(5);
        	pockerOrderEntityUpStatus.setOrderNo(pockerOrderEntity.getOrderNo());
		}else {
			//口袋放款失败
			logger.info("口袋理财放款回调该订单放款失败: 口袋理财订单号为:{}",pocketCallbackDto.getOrder_id());
        	pockerOrderEntityUpStatus.setOrderStatus(6);
        	pockerOrderEntityUpStatus.setOrderNo(pockerOrderEntity.getOrderNo());
		}
		orderContract.updatePockerOrderEntity(pockerOrderEntityUpStatus);
		ResponseData<OrderInfo> orderInfoData = orderContract.getOrderByOrderNo(responseData.getData().getOrderNo());		
		if("0".equals(orderInfoData.getStatus()) && orderInfoData.getData() != null) {			
			this.handleResult(pocketCallbackDto, orderInfoData.getData());
		}
    	resultMap.put("code", "0");
		resultMap.put("err_msg", "success");
		pocketCallbackResponseData.sendParam(resultMap);
		logger.info("口袋理财回调接口结束");
		return pocketCallbackResponseData;
	}
	
	 private void handleResult(PocketCallbackDto pocketCallbackDto,OrderInfo orderInfo) {
		  
		 if(pocketCallbackDto == null) {	 
			logger.info("口袋理财放款异步通知pocketCallbackDto参数为空"); 
			return;
		 }
		 //放款通知
		 if("0".equals(String.valueOf(pocketCallbackDto.getCode()))) {
			 if (redisTemplate.hasKey(Constants.KDLC_CALLBACK_PREFIX_NOTICE + orderInfo.getOrderNo())) {
					logger.error("重复处理kdlc查询借款订单接口");
					return;
			 } else {
					redisTemplate.opsForValue().set(Constants.KDLC_CALLBACK_PREFIX_NOTICE + orderInfo.getOrderNo(), "1",24*60,TimeUnit.MINUTES);
			 }
		   pocketService.createOrder(orderInfo);	 
		   RemitMessage remitMessage = new RemitMessage();
	       remitMessage.setRemitStatus("0");
	       remitMessage.setRemitAmount(orderInfo.getLoanAmount());
	       remitMessage.setOrderNo(orderInfo.getOrderNo());
	       rabbitmqProducerProxy.convertAndSend("remit.nyd", remitMessage);
	       //如果是null 默认为nyd的订单来源
	       if (orderInfo.getChannel() == null) {
	           orderInfo.setChannel(BorrowConfirmChannel.NYD.getChannel());
	       }

	       //发送 到 ibank
	       if (orderInfo.getChannel().equals(BorrowConfirmChannel.YMT.getChannel())) {
	           remitMessage.setOrderNo(orderInfo.getIbankOrderNo());
	           logger.info("放款成功发送ibank." + JSON.toJSONString(remitMessage));
	           rabbitmqProducerProxy.convertAndSend("remitIbankOrder.ymt", remitMessage);
	       }

	       RemitInfo remitInfo = new RemitInfo();
	       remitInfo.setRemitTime(new Date());
	       remitInfo.setOrderNo(orderInfo.getOrderNo());
	       remitInfo.setRemitStatus("0");
	       remitInfo.setFundCode("kdlc");
	       remitInfo.setChannel(orderInfo.getChannel());
	       remitInfo.setRemitAmount(orderInfo.getLoanAmount());
	       logger.info("口袋理财放款流水:" + JSON.toJSON(remitInfo));
	       try {
	           rabbitmqProducerProxy.convertAndSend("remitLog.nyd", remitInfo);
	           logger.info("口袋理财放款流水发送mq成功");
	       } catch (Exception e) {
	           logger.error("发送mq消息发生异常");
	           DingdingUtil.getErrMsg("口袋理财放款成功,发送mq消息生成放款记录时发生异常,需要补放款记录,入参为:" + JSON.toJSONString(remitInfo));
	       }
	                  			
		}	 
		if("1003".equals(String.valueOf(pocketCallbackDto.getCode()))) {
			//借款失败
	      DingdingUtil.getErrMsg("口袋理财发起借款申请失败,订单号为:" + orderInfo.getOrderNo()+"错误码：" + pocketCallbackDto.getCode());
	      orderInfo.setOrderStatus(40);
	    //生成异常订单记录
	      try {
	    	  orderExceptionContract.saveByOrderInfo(orderInfo);
          }catch(Exception e) {
          	logger.error("生成异常订单信息异常：" + e.getMessage());
          }
	      orderContract.updateOrderInfo(orderInfo);
	      redisTemplate.delete(Constants.KDLC_CALLBACK_PREFIX + orderInfo.getOrderNo());	  
		 }
	 }
	 
	 /**
	  * 获取订单当前状态
	  * @param status
	  * @return
	  */
	 public String getStatusToString(Integer status) {
		 String string = "";
		 Map<Integer,String> map=new HashMap<Integer,String>();
		 map.put(0, "订单初始化");
		 map.put(1, "生成订单处理中");
		 map.put(2, "生成订单成功");
		 map.put(3, "生成订单失败");
		 map.put(4, "放款处理中");
		 map.put(5, "放款成功");
		 map.put(6, "放款失败");
		 return map.get(status);
	 }
	 
}
