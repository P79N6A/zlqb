package com.nyd.order.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.nyd.zeus.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.creativearts.das.query.api.QiniuContract;
import com.creativearts.das.query.api.ReportDetailQueryService;
import com.creativearts.das.query.api.model.ReportType;
import com.creativearts.das.query.api.model.ZLQBAssessDetailDto;
import com.creativearts.limit.api.AssessService;
import com.creativearts.limit.query.dto.UserLimitDto;
import com.ibank.pay.model.RepayType;
import com.nyd.activity.api.CashRedBagService;
import com.nyd.activity.model.ActivityFeeInfo;
import com.nyd.application.api.AgreeMentContract;
import com.nyd.capital.api.service.PocketApi;
import com.nyd.capital.entity.UserPocket;
import com.nyd.capital.model.enums.FundSourceEnum;
import com.nyd.capital.model.pocket.PocketAccountDetailDto;
import com.nyd.member.api.MemberConfigContract;
import com.nyd.member.api.MemberContract;
import com.nyd.member.model.MemberModel;
import com.nyd.msg.model.SmsRequest;
import com.nyd.msg.service.ISendSmsService;
import com.nyd.order.api.OrderChannelContract;
import com.nyd.order.api.OrderContract;
import com.nyd.order.api.zzl.OrderSqlService;
import com.nyd.order.dao.OrderDao;
import com.nyd.order.dao.OrderDetailDao;
import com.nyd.order.dao.OrderStatusLogDao;
import com.nyd.order.dao.TestListDao;
import com.nyd.order.dao.mapper.WithholdOrderMapper;
import com.nyd.order.entity.InnerTest;
import com.nyd.order.entity.Order;
import com.nyd.order.entity.OrderDetail;
import com.nyd.order.entity.OrderStatusLog;
import com.nyd.order.entity.WithholdOrder;
import com.nyd.order.model.BaseInfo;
import com.nyd.order.model.BorrowInfo;
import com.nyd.order.model.JudgeInfo;
import com.nyd.order.model.JudgeMemberInfo;
import com.nyd.order.model.OrderDetailInfo;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.OrderStatusLogInfo;
import com.nyd.order.model.OrderUpdatInfo;
import com.nyd.order.model.WithholdCallBackInfo;
import com.nyd.order.model.common.ArithUtil;
import com.nyd.order.model.common.ChkUtil;
import com.nyd.order.model.common.ListTool;
import com.nyd.order.model.dto.BorrowConfirmDto;
import com.nyd.order.model.dto.BorrowDto;
import com.nyd.order.model.dto.BorrowMqConfirmDto;
import com.nyd.order.model.enums.BorrowConfirmChannel;
import com.nyd.order.model.enums.OrderStatus;
import com.nyd.order.model.msg.OrderMessage;
import com.nyd.order.model.vo.BankVo;
import com.nyd.order.model.vo.BorrowDetailVo;
import com.nyd.order.model.vo.BorrowRecordVo;
import com.nyd.order.model.vo.BorrowResultVo;
import com.nyd.order.model.vo.BorrowVo;
import com.nyd.order.model.vo.OrderLogVo;
import com.nyd.order.model.vo.ProductVo;
import com.nyd.order.service.NewOrderInfoService;
import com.nyd.order.service.consts.OrderConsts;
import com.nyd.order.service.rabbit.OrderToAuditProducer;
import com.nyd.order.service.rabbit.WithholdProducer;
import com.nyd.order.service.util.DateUtil;
import com.nyd.order.service.util.HttpUtil;
import com.nyd.order.service.util.OrderProperties;
import com.nyd.order.service.util.RestTemplateApi;
import com.nyd.product.api.ProductContract;
import com.nyd.product.model.ProductInfo;
import com.nyd.user.api.UserAccountContract;
import com.nyd.user.api.UserBankContract;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.api.UserStepContract;
import com.nyd.user.api.zzl.UserBankCardService;
import com.nyd.user.entity.User;
import com.nyd.user.model.BankInfo;
import com.nyd.user.model.PayFeeInfo;
import com.nyd.user.model.StepInfo;
import com.nyd.user.model.UserInfo;
import com.nyd.zeus.api.BillContract;
import com.nyd.zeus.api.payment.PaymentRiskRecordService;
import com.nyd.zeus.api.zzl.ZeusForLXYService;
import com.nyd.zeus.model.common.CommonResponse;
import com.nyd.zeus.model.helibao.util.StatusConstants;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import com.tasfe.framework.support.model.ResponseData;
import com.tasfe.framework.uid.service.BizCode;
import com.tasfe.framework.uid.service.IdGenerator;

/**
 * @author liuqiu
 */
@Service
public class NewOrderInfoServiceImpl implements NewOrderInfoService {

    private static Logger LOGGER = LoggerFactory.getLogger(NewOrderInfoServiceImpl.class);

    private static final String NYD_REDIS_PREFIX = "nydorderp";

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired(required = false)
    private MemberContract memberContract;
    @Autowired(required = false)
    private MemberConfigContract memberConfigContract;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderProperties orderProperties;
    @Autowired(required = false)
    private BillContract billContract;
    @Autowired
    private OrderToAuditProducer orderToAuditProducer;

    @Autowired
    private TestListDao testListDao;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired(required = false)
    private ProductContract productContract;
    @Autowired(required = false)
    private UserIdentityContract userIdentityContract;
    @Autowired(required = false)
    private UserAccountContract userAccountContract;
    @Autowired
    private OrderDetailDao orderDetailDao;
    @Autowired
    private OrderStatusLogDao orderStatusLogDao;
    @Autowired
    private OrderChannelContract orderChannelContract;
    @Autowired(required = false)
    private UserBankContract userBankContract;
    @Autowired
    private WithholdProducer withholdProducer;
    @Autowired
    private AssessService assessService;
    @Autowired
    private WithholdOrderMapper withholdOrderDao;
    @Autowired(required = false)
    private UserStepContract userStepContractNyd;
    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;
    @Autowired(required = false)
    private ISendSmsService sendSmsService;
    @Autowired(required = false)
    private OrderContract orderContract;
    @Autowired
    private PocketApi pocketApi;

    @Autowired(required = false)
    private AgreeMentContract agreeMentContract;

    @Autowired
    private QiniuContract qiniuContract;
    @Autowired
    private ReportDetailQueryService reportDetailQueryService;

    @Autowired
    private com.nyd.application.api.QiniuContract qiniuContractNyd;

    @Autowired
    private CashRedBagService cashRedBagService;

    @Autowired
    RestTemplateApi restTemplateApi;
    
    @Autowired
    PaymentRiskRecordService paymentRiskRecordService;

	@Autowired
    private ZeusForLXYService zeusForLXYService;

	@Autowired
    private OrderSqlService orderSqlService;


    @Autowired

    private UserBankCardService userBankCardService;



    public final  String OrderSubmit = "order_submit";
	
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseData newBorrowInfoConfirm(BorrowConfirmDto borrowConfirmDto) throws Exception {
        LOGGER.info("开始进行借款确认：" + JSON.toJSONString(borrowConfirmDto));
        if(StringUtils.isNotBlank(borrowConfirmDto.getAccountNumber())) {
            borrowConfirmDto.setMobile(borrowConfirmDto.getAccountNumber());
        }
        try {
//            if (redisTemplate.hasKey(NYD_REDIS_PREFIX + borrowConfirmDto.getUserId())) {
//                LOGGER.info("借款确认redis有值" + borrowConfirmDto.getUserId());
//                return ResponseData.error("不能重复提交");
//            } else {
//                redisTemplate.opsForValue().set(NYD_REDIS_PREFIX + borrowConfirmDto.getUserId(), "1", 70, TimeUnit.SECONDS);
//            }
            //判断是否具有借款条件
            JudgeInfo judgeInfo = judgeBorrow(borrowConfirmDto.getUserId(),borrowConfirmDto.getAppName());
            if ("1".equals(judgeInfo.getWhetherLoan())) {
                if ("0".equals(judgeInfo.getUnProcessOrderExist())) {
                    ResponseData responseData = ResponseData.error(judgeInfo.getWhetherLoanMsg());
                    //1000 告诉银码头 在侬要贷 有未处理订单
                    responseData.setCode("1000");
                    LOGGER.error("改银码头用户有未处理的订单");
                    return responseData;
                } else {
                    return ResponseData.error(judgeInfo.getWhetherLoanMsg());
                }
            }
            //删除key
            if (redisTemplate.hasKey("kzjr2qcgz" + borrowConfirmDto.getUserId())) {
                redisTemplate.delete("kzjr2qcgz" + borrowConfirmDto.getUserId());
            }
            String memberId = null;
            String memberType = null;
            BigDecimal memberFee = new BigDecimal(0);
//            //判断会员是否有效, 仅限于侬要贷需要判断 Add by Jiawei Cheng 2018-3027 21:51
//            //由于侬要贷之前并没有channel字段 所以 判断 channel 是否为null 为null 表示 来源 nyd
//            if (borrowConfirmDto.getChannel() == null || BorrowConfirmChannel.NYD.getChannel().equals(borrowConfirmDto.getChannel())) {
//                if (borrowConfirmDto.getType() == null || "".equals(borrowConfirmDto.getType())) {
//                    JudgeMemberInfo judgeMember = judgeMember(borrowConfirmDto.getUserId());
//                    if (!judgeMember.isMemberFlag()) {
//                        LOGGER.error("不是会员");
//                        return ResponseData.error(OrderConsts.MEMBER_ERROR);
//                    } else {
//                        memberId = judgeMember.getMemberId();
//                        memberFee = judgeMember.getMemberFee();
//                        memberType = judgeMember.getMemberType();
//                    }
//                }
//            }
            //调用户系统rpc接口获取用户信息，保存至订单详情
            UserInfo userInfo = userIdentityContract.getUserInfo(borrowConfirmDto.getUserId()).getData();

            Order order = new Order();
            Date date = new Date();
            BeanUtils.copyProperties(borrowConfirmDto, order);
            //判断是否内部测试人员
            if (judgeTestUserFlag(borrowConfirmDto.getMobile())) {
                order.setTestStatus(0);
            } else {
                order.setTestStatus(1);
            }
            //大户的ID服务，生成订单ID
            String orderNo = idGenerator.generatorId(BizCode.ORDER_NYD).toString();
            order.setOrderNo(orderNo);
            order.setOrderStatus(OrderStatus.AUDIT.getCode());
            //申请时间
            order.setLoanTime(date);
            //获取金融产品信息
            ProductInfo productInfo = productContract.getProductInfo(borrowConfirmDto.getProductCode()).getData();
            if (productInfo == null) {
                LOGGER.error("get fianceProduct failed, ProductCode = " + borrowConfirmDto.getProductCode());
                return ResponseData.error(OrderConsts.NO_PRODUCT);
            }
            String business = productInfo.getBusiness();
            order.setBusiness(business);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, +borrowConfirmDto.getBorrowTime());
            order.setMemberId(memberId);
            order.setMemberFee(memberFee);
            order.setMemberType(memberType);
            order.setRealLoanAmount(borrowConfirmDto.getLoanAmount());
            if (borrowConfirmDto.getChannel() == null) {
                order.setChannel(BorrowConfirmChannel.NYD.getChannel());
            } else {
                order.setChannel(borrowConfirmDto.getChannel());
            }
            //新增马甲来源（ps:银马头过来暂定为nyd本身的订单）
            if(StringUtils.isBlank(borrowConfirmDto.getAppName())){
                order.setAppName("xxd");
            }else{
                order.setAppName(borrowConfirmDto.getAppName());
            }
            String orderJxOn = orderProperties.getOrderJxOn();
            //获取借款渠道
            String channel = getChannel(orderJxOn,borrowConfirmDto);
            order.setFundCode(channel);
            ResponseData response = ResponseData.success();
            //保存订单
            try {
                if (order.getChannel().equals(BorrowConfirmChannel.YMT.getChannel())) {
                    order.setIbankOrderNo(borrowConfirmDto.getIbankOrderNo());
                    //银码头 不通过审核 直接 待放款。
                    order.setOrderStatus(OrderStatus.WAIT_LOAN.getCode());
                    order.setAuditStatus("1");
                }
                orderDao.save(order);
            } catch (Exception e) {
                LOGGER.error("save order failed  or dubbo update failed , orderNo = " + orderNo, e);
                throw e;
            }
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setMobile(borrowConfirmDto.getMobile());
            orderDetail.setOrderNo(orderNo);
            BeanUtils.copyProperties(borrowConfirmDto, orderDetail);
            //金融产品相关信息
            orderDetail.setProductCode(productInfo.getProductCode());
            orderDetail.setProductType(productInfo.getProductType());
            orderDetail.setProductPeriods(productInfo.getProductPeriods());
            orderDetail.setInterestRate(productInfo.getInterestRate());
            orderDetail.setIdType(userInfo.getCertificateType());
            orderDetail.setIdNumber(userInfo.getIdNumber());
            orderDetail.setRealName(userInfo.getRealName());
            //调用户系统rpc接口获取用户来源
            String source = userAccountContract.getAccountSource(borrowConfirmDto.getMobile()).getData();
            orderDetail.setSource(source);
            //保存订单详情
            try {
                orderDetailDao.save(orderDetail);
            } catch (Exception e) {
                LOGGER.error("save orderDetail failed, orderNo = " + orderNo, e);
                throw e;
            }
            // 如果是银码头，则更改订单状态为等待放款
            if (borrowConfirmDto.getChannel() != null && borrowConfirmDto.getChannel().equals(BorrowConfirmChannel.YMT.getChannel())) {
                OrderStatusLog orderStatusLog1 = new OrderStatusLog();
                orderStatusLog1.setOrderNo(orderNo);
                orderStatusLog1.setBeforeStatus(OrderStatus.INIT.getCode());
                orderStatusLog1.setAfterStatus(OrderStatus.AUDIT.getCode());
                OrderStatusLog orderStatusLog2 = new OrderStatusLog();
                orderStatusLog2.setOrderNo(orderNo);
                orderStatusLog2.setBeforeStatus(OrderStatus.AUDIT.getCode());
                orderStatusLog2.setAfterStatus(OrderStatus.AUDIT_SUCCESS.getCode());
                OrderStatusLog orderStatusLog3 = new OrderStatusLog();
                orderStatusLog3.setOrderNo(orderNo);
                orderStatusLog3.setBeforeStatus(OrderStatus.AUDIT_SUCCESS.getCode());
                orderStatusLog3.setAfterStatus(OrderStatus.WAIT_LOAN.getCode());
                try {
                    orderStatusLogDao.save(orderStatusLog1);
                    orderStatusLogDao.save(orderStatusLog2);
                    orderStatusLogDao.save(orderStatusLog3);
                } catch (Exception e) {
                    LOGGER.error("save orderStatusLog fail, orderNo = " + orderNo, e);
                    throw e;
                }
            } else { // 如果是侬要贷，则更改订单状态为等待审核
                OrderStatusLog orderStatusLog = new OrderStatusLog();
                orderStatusLog.setOrderNo(orderNo);
                orderStatusLog.setBeforeStatus(OrderStatus.INIT.getCode());
                orderStatusLog.setAfterStatus(OrderStatus.AUDIT.getCode());
                try {
                    orderStatusLogDao.save(orderStatusLog);
                } catch (Exception e) {
                    LOGGER.error("save orderStatusLog fail, orderNo = " + orderNo, e);
                    throw e;
                }
            }
            response.setData(order);
            //往放款发送消息
            OrderMessage msg = new OrderMessage();
            msg.setUserId(borrowConfirmDto.getUserId());
            msg.setOrderNo(orderNo);
            msg.setProductType("nyd");
            msg.setBorrowType(productInfo.getProductType().toString());
            // 如果是银码头，则发送放款消息
            LOGGER.info("传入的渠道为" + borrowConfirmDto.getChannel());
            if (borrowConfirmDto.getChannel() != null && BorrowConfirmChannel.YMT.getChannel().equals(borrowConfirmDto.getChannel())) {
                LOGGER.info("渠道银码头");
            } else {
                msg.setChannel(BorrowConfirmChannel.NYD.getChannel());
                LOGGER.info("渠道侬要贷");
                orderToAuditProducer.sendMsg(msg);
            }
            signOtherAgreement(orderNo);
            LOGGER.info("borrow confirm success, userId is " + borrowConfirmDto.getUserId() + ", orderNo is " + orderNo);
            return response;
        } catch (Exception e) {
            LOGGER.error("借款确认时发生异常，request is :" + JSON.toJSONString(borrowConfirmDto));
            throw e;
        }
    }

    /**
     * 渠道选择
     */
    private String getChannel(String orderJxOn, BorrowConfirmDto borrowConfirmDto) {
        String channel = null;
        if ("1".equals(orderJxOn) && borrowConfirmDto.getChannel() != null && borrowConfirmDto.getChannel() == 1) {
            channel = "dld";
        } else {
            //为测试添加白名单
            List<InnerTest> objectsByMobile = null;
            try {
                objectsByMobile = testListDao.getObjectsByMobile(borrowConfirmDto.getMobile());
            } catch (Exception e) {
                LOGGER.error("查询白名单异常：{}", e.getMessage());
            }
            if (objectsByMobile != null && objectsByMobile.size() > 0 && (objectsByMobile.get(0).getRealName().contains("qcgz") || objectsByMobile.get(0).getRealName().contains("jx") || objectsByMobile.get(0).getRealName().contains("kdlc") || objectsByMobile.get(0).getRealName().contains("dld"))) {
                LOGGER.info("白名单用户:" + objectsByMobile.get(0).getRealName());
                if (objectsByMobile.get(0).getRealName().contains("qcgz")) {
                    channel = "qcgz";
                } else if (objectsByMobile.get(0).getRealName().contains("jx")) {
                    channel = "jx";
                } else if (objectsByMobile.get(0).getRealName().contains("kdlc")) {
                    channel = "kdlc";
                } else if (objectsByMobile.get(0).getRealName().contains("dld")) {
                    channel = "dld";
                }
            } else {
                if (borrowConfirmDto.getChannel() != null && BorrowConfirmChannel.YMT.getChannel().equals(borrowConfirmDto.getChannel())) {
                    ResponseData contractChannel = orderChannelContract.getChannel();
                    if ("0".equals(contractChannel.getStatus())) {
                        channel = (String) contractChannel.getData();
                    } else {
                        channel = "dld";
                    }
                }
            }
        }
        if (channel != null && (borrowConfirmDto.getBorrowTime() == 30 || borrowConfirmDto.getBorrowTime() == 7)) {
            LOGGER.info("30天或7天的产品,需要分配到多来点");
            channel = "dld";
        }
        //判断银行及借款时间为招商银行借款时间为23-24渠道修改
        if (channel != null && "kdlc".equals(channel) && borrowConfirmDto.getBankName() != null && borrowConfirmDto.getBankName().indexOf("招商") >= 0) {
            channel = "dld";
        } else if (channel != null && "kdlc".equals(channel) && DateUtil.isEffectiveDate(DateUtil.dateToHms(new Date()), "23:00:00", "23:59:59")) {
            channel = "dld";
        }
        LOGGER.info("渠道选择借款确认信息：" + JSON.toJSONString(borrowConfirmDto) + "渠道选择为：" + channel);
        return channel;
    }

    @Override
    public ResponseData<BorrowResultVo> getBorrowResult(String userId) {
        LOGGER.info("begin to get borrow result , userId is " + userId);
        ResponseData response = ResponseData.success();
        if (userId == null) {
            return response;
        }
        BorrowResultVo borrowResult = new BorrowResultVo();
        try {
            List<OrderInfo> orderList = orderDao.getObjectsByUserId(userId);
            OrderInfo orderInfo = null;
            if (orderList != null && orderList.size() > 0) {
                orderInfo = orderList.get(0);
                BeanUtils.copyProperties(orderInfo, borrowResult);
            } else {
                return response;
            }
            if (orderInfo.getOrderStatus() >= 20) {
                String stage = null;
                if (StringUtils.isNotBlank(borrowResult.getFundCode()) && FundSourceEnum.KDLC.getCode().equals(borrowResult.getFundCode())) {
                    //查询开户状态
                    PocketAccountDetailDto detailDto = new PocketAccountDetailDto();
                    detailDto.setUserId(userId);
                    ResponseData<Map<String, String>> responseData = pocketApi.selectUserOpenDetail(detailDto, false);
                    if ("0".equals(responseData.getStatus())) {
                        Map<String, String> map = responseData.getData();
                        stage = map.get("stage");
                        borrowResult.setIfOpenPage(stage);
                    } else {
                        return (ResponseData<BorrowResultVo>) ResponseData.error("查询开户情况失败");
                    }
                }
            }
            List<OrderStatusLogInfo> statusList = orderStatusLogDao.getObjectsByOrderNo(borrowResult.getOrderNo());
            if (statusList != null && statusList.size() > 0) {
                List<OrderLogVo> logList = new ArrayList<>();
                for (OrderStatusLogInfo logInfo : statusList) {
                    if (orderInfo.getIfSign() == 0 && OrderStatus.WAIT_LOAN.getCode().equals(orderInfo.getOrderStatus()) && OrderStatus.WAIT_LOAN.getCode().equals(logInfo.getAfterStatus())) {
                        continue;
                    }
                    OrderLogVo orderLog = new OrderLogVo();
                    orderLog.setStatusCode(logInfo.getAfterStatus());
                    orderLog.setStatusTime(DateUtil.dateToString(logInfo.getCreateTime()));
                    logList.add(orderLog);
                }
                borrowResult.setOrderStatusList(logList);
            }
            ResponseData<UserInfo> userInfo = userIdentityContract.getUserInfo(userId);
            if ("1".equals(userInfo.getStatus())) {
                return response;
            }
            UserInfo data = userInfo.getData();
            String name = data.getRealName().substring(0, 1) + "**";
            String number = data.getIdNumber().substring(0, 4) + "**********" + data.getIdNumber().substring(data.getIdNumber().length() - 4);
            borrowResult.setRealName(name);
            borrowResult.setIdNumber(number);
            borrowResult.setMobile(data.getAccountNumber());
            response.setData(borrowResult);
            LOGGER.info("get borrow result success and result is:" + JSON.toJSONString(response));
        } catch (Exception e) {
            LOGGER.error("get orderInfo failed, userId = " + userId, e);
            response = ResponseData.error("查询借款结果发生异常");
        }
        return response;
    }

    @Override
    public ResponseData<BorrowDetailVo> getBorrowDetail(String orderNo) {
        LOGGER.info("begin to get borrow detail , orderNo is " + orderNo);
        ResponseData response = ResponseData.success();
        if (orderNo == null) {
            return response;
        }
        BorrowDetailVo borrowDetail = new BorrowDetailVo();
        try {
            List<OrderInfo> detailList = orderDao.getObjectsByOrderNo(orderNo);
            if (detailList != null && detailList.size() > 0) {
                OrderInfo orderInfo = detailList.get(0);
                BeanUtils.copyProperties(orderInfo, borrowDetail);
                LOGGER.info("get borrowDetail success and result is:" + JSON.toJSONString(borrowDetail));
                LOGGER.info("get OrderInfo success and result is:" + JSON.toJSONString(orderInfo));
                LOGGER.info("getLoanTime is before" + orderInfo.getLoanTime());
                LOGGER.info("getLoanTime is after " + DateUtil.dateToString(orderInfo.getLoanTime()));
                borrowDetail.setLoanTime(DateUtil.dateToString(orderInfo.getLoanTime()));
                borrowDetail.setPayTime(DateUtil.dateToString(orderInfo.getPayTime()));
                borrowDetail.setPromiseRepaymentTime(DateUtil.dateToString(orderInfo.getPromiseRepaymentTime()));
                response.setData(borrowDetail);
            }
            LOGGER.info("get borrow detail success and result is:" + JSON.toJSONString(response));
        } catch (Exception e) {
            LOGGER.error("get orderDetailInfo failed, orderNo = " + orderNo, e);
            response = ResponseData.error(OrderConsts.DB_ERROR_MSG);
        }
        return response;
    }

    @Override
    public ResponseData<BorrowDetailVo> getBorrowDetailByUserId(String userId) {
        LOGGER.info("begin to get borrow detail , userId is " + userId);
        ResponseData response = ResponseData.success();
        if (userId == null) {
            return response;
        }
        BorrowDetailVo borrowDetail = new BorrowDetailVo();
        try {
            List<OrderInfo> detailList = orderDao.getObjectsByUserId(userId);
            if (detailList != null && detailList.size() > 0) {
                OrderInfo orderInfo = detailList.get(0);
                BeanUtils.copyProperties(orderInfo, borrowDetail);
                borrowDetail.setLoanTime(DateUtil.dateToString(orderInfo.getLoanTime()));
                borrowDetail.setPayTime(DateUtil.dateToString(orderInfo.getPayTime()));
                borrowDetail.setPromiseRepaymentTime(DateUtil.dateToString(orderInfo.getPromiseRepaymentTime()));
                if(borrowDetail.getMemberFee().compareTo(BigDecimal.ZERO) == 0) {
                    borrowDetail.setIfNotice(1);
                }
                ResponseData<UserPocket> responseData = pocketApi.selectUserPocket(userId);
                UserPocket userPocket = responseData.getData();
                if (userPocket != null){
                    if (userPocket.getStage() == 0){
                        borrowDetail.setIfOpenPage("0");
                    }else {
                        borrowDetail.setIfOpenPage("1");
                    }
                }
                response.setData(borrowDetail);
            }
            LOGGER.info("get borrow detail success and result is:" + JSON.toJSONString(response));
        } catch (Exception e) {
            LOGGER.error("get orderDetailInfo failed, userId = " + userId, e);
            response = ResponseData.error(OrderConsts.DB_ERROR_MSG);
        }
        return response;
    }

    @Override
    public ResponseData<List<BorrowRecordVo>> getBorrowAll(String userId) {
        LOGGER.info("begin to get borrow record , userId is " + userId);
        ResponseData response = ResponseData.success();
        if (userId == null) {
            return response;
        }
        try {
            List<OrderInfo> orderList = orderDao.getObjectsByUserId(userId);
            if (orderList != null && orderList.size() > 0) {
                List<BorrowRecordVo> borrowList = new ArrayList<>();
                for (OrderInfo orderInfo : orderList) {
                    BorrowRecordVo borrowRecord = new BorrowRecordVo();
                    BeanUtils.copyProperties(orderInfo, borrowRecord);
                    if(StringUtils.isEmpty(orderInfo.getIbankOrderNo())) {
                        borrowRecord.setIfYmt(0);
                    }else {
                        borrowRecord.setIfYmt(1);
                    }
                    borrowRecord.setLoanTime(DateUtil.dateToString(orderInfo.getLoanTime()));
                    borrowRecord.setReportUrl(getReportUrlByOrderNo(orderInfo.getOrderNo(),1));
                    int expireDay = DateUtil.getDayDiffUp(orderInfo.getLoanTime(), new Date());
                    int memberDay = Integer.valueOf(orderProperties.getIntervalDays());
                    if (expireDay > memberDay) {
                        borrowRecord.setOrderStatus(1010);
                    }
                    borrowList.add(borrowRecord);
                }
                response.setData(borrowList);
            }
            LOGGER.info("get borrow record success and result is:" + JSON.toJSONString(response));
        } catch (Exception e) {
            LOGGER.error("get order record error! userId = " + userId, e);
            response = ResponseData.error(OrderConsts.DB_ERROR_MSG);
        }
        return response;
    }
    @Override
    public ResponseData<List<BorrowRecordVo>> getBorrowAllWithAppName(String userId,String appName) {
        LOGGER.info("begin to get borrow record , userId is " + userId);
        ResponseData response = ResponseData.success();
        if(null == userId || "".equals(userId)) {
            return response;
        }
        if(StringUtils.isBlank(appName)) {
            appName = "xxd";
        }
        try {
            List<OrderInfo> temp = orderDao.getObjectsByUserId(userId);
            List<OrderInfo> orderList = new ArrayList<OrderInfo>();
            for(OrderInfo order:temp) {
                if("xxd".equals(appName) && ("xxd".equals(order.getAppName()) || StringUtils.isBlank(order.getAppName()))) {
                    orderList.add(order);
                    continue;
                }
                if(appName.equals(order.getAppName())) {
                    orderList.add(order);
                }
            }
            if (orderList != null && orderList.size() > 0) {
                List<BorrowRecordVo> borrowList = new ArrayList<>();
                for (OrderInfo orderInfo : orderList) {
                    BorrowRecordVo borrowRecord = new BorrowRecordVo();
                    BeanUtils.copyProperties(orderInfo, borrowRecord);
                    if(StringUtils.isEmpty(orderInfo.getIbankOrderNo())) {
                        borrowRecord.setIfYmt(0);
                    }else {
                        borrowRecord.setIfYmt(1);
                    }
                    borrowRecord.setLoanTime(DateUtil.dateToString(orderInfo.getLoanTime()));
                    borrowRecord.setReportUrl(getReportUrlByOrderNo(orderInfo.getOrderNo(),1));
                    borrowRecord.setReportKey(getReportUrlByOrderNo(orderInfo.getOrderNo(),2));
                    borrowRecord.setReportFlag(getReportFlag(orderInfo,borrowRecord.getReportKey()));
                    int expireDay = DateUtil.getDayDiffUp(orderInfo.getLoanTime(), new Date());
                    int memberDay = Integer.valueOf(orderProperties.getIntervalDays());
                    if (expireDay > memberDay) {
                        borrowRecord.setOrderStatus(1010);
                    }
                    borrowList.add(borrowRecord);
                }
                response.setData(borrowList);
            }
            LOGGER.info("get borrow record success and result is:" + JSON.toJSONString(response));
        } catch (Exception e) {
            LOGGER.error("get order record error! userId = " + userId, e);
            response = ResponseData.error(OrderConsts.DB_ERROR_MSG);
        }
        return response;
    }

    private Integer getReportFlag(OrderInfo orderInfo,String reportKey) {
        Date startDate = null;
        try {
            startDate = DateUtils.parseDate("2019-01-05", "yyyy-MM-dd");
        } catch (ParseException e) {
            LOGGER.error("日期转换异常");
        }
        LOGGER.info("查询评估报告信息，orderNo：" + orderInfo.getOrderNo());
        String userId = orderInfo.getUserId();
        String borrowNid =orderInfo.getOrderNo();
        ResponseData<ZLQBAssessDetailDto> res = reportDetailQueryService.getAssessInfo(userId,borrowNid);
        //if(res != null && (res.getData() == null || res.getData().getAssessDetailModel() == null)) {
        //	LOGGER.info("查询评估报告信息为空，orderNo：" + orderInfo.getOrderNo());
        //	return 0;
        //}
        //if(startDate.compareTo(orderInfo.getLoanTime()) <= 0 && orderInfo.getAuditStatus().equals("0") && orderInfo.getOrderStatus().equals(1000)) {
        //	return 1;
        //}
        //return 0;
        if(startDate.compareTo(orderInfo.getLoanTime()) <= 0 && orderInfo.getAuditStatus().equals("0") && orderInfo.getOrderStatus().equals(1000)) {
            if(res != null && (res.getData() != null && res.getData().getAssessDetailModel() != null)) {
                return 1;//新报告
            }else {
                return 2;//显示弹窗报告正在生产中
            }
        }else {
            return 0;//老报告
        }


    }

    @Override
    public ResponseData getNewReport(BaseInfo base) {
        String borrowNid = base.getOrderNo();
        String userId = base.getUserId();
        ResponseData<ZLQBAssessDetailDto> res = reportDetailQueryService.getAssessInfo(userId,borrowNid);
        LOGGER.info(" 获取新报告内容 getNewReport:" + JSON.toJSONString(res));
        return res;
    }

    /**
     * 根据订单号查询评估报告url
     * @param orderNo
     * @return
     * @throws Exception
     */
    private String getReportUrlByOrderNo(String orderNo,int flag) throws Exception{
        List<OrderDetailInfo> de = orderDetailDao.getObjectsByOrderNo(orderNo);
        String key = "";
        if(de != null && de.size() > 0) {
            key = de.get(0).getAssessKey();
        }
        //flag如果是2则是查询评估报告key 是1是查询评估报告url
        if(flag == 2) {
            return key;
        }
        ResponseData response = qiniuContract.getPdfUrlByKey(key);
        if(response != null && "0".equals(response.getStatus())) {
            return (String)response.getData();
        }
        return null;
    }
    @Override
    public ResponseData<BorrowVo> getBorrowInfo(BorrowDto borrowDto) {
        LOGGER.info("begin to get borrowInfo, borrowInfo is " + JSON.toJSONString(borrowDto));
        ResponseData response = ResponseData.success();
        BorrowVo borrowVo = new BorrowVo();
        borrowVo.setMobile(borrowDto.getAccountNumber());

        //获取用户额度
        ResponseData<UserInfo> userInfo = userIdentityContract.getUserInfo(borrowDto.getUserId());
        UserInfo data = userInfo.getData();
        String url = orderProperties.getLimitServiceUrl() + "limitService/getUserLimit";
        Map<String, String> map = new HashMap<>();
        map.put("idCardNo", data.getIdNumber());
        String json = JSON.toJSONString(map);
        int limit;
        try {
            String sendPost = HttpUtil.sendPost(url, json);
            JSONObject jsonObject = JSONObject.parseObject(sendPost);
            String string = jsonObject.getString("data");
            BigDecimal aDouble = new BigDecimal(string);
            limit = aDouble.intValue();
        } catch (Exception e) {
            limit = 1000;
        }
        ResponseData<List<ProductInfo>> productInfos = productContract.getProductInfos();
        List<ProductInfo> infos = productInfos.getData();
        if (infos != null && infos.size() > 0) {
            List<ProductVo> list = new ArrayList<>();
            for (ProductInfo productInfo : infos) {
                if (productInfo.getMaxPrincipal().intValue() <= limit) {
                    ProductVo productVo = new ProductVo();
                    productVo.setBorrowTime(productInfo.getMaxLoanDay());
                    productVo.setInterestRate(productInfo.getInterestRate());
                    productVo.setLoanAmount(productInfo.getMaxPrincipal());
                    productVo.setProductCode(productInfo.getProductCode());
                    productVo.setRecommendFlag(productInfo.getRecommendFlag());
                    list.add(productVo);
                }
            }
            borrowVo.setProductList(list);
        }
        //获取银行卡信息
        ResponseData<List<BankInfo>> bankResponse = userBankContract.getBankInfos(borrowDto.getUserId());
        if ("0".equals(bankResponse.getStatus()) && bankResponse.getData() != null) {
            List<BankInfo> bankInfoList = bankResponse.getData();
            List<BankVo> bankList = new ArrayList<BankVo>();
            if (bankInfoList != null & bankInfoList.size() > 0) {
                for (BankInfo bankInfo : bankInfoList) {
                    BankVo bank = new BankVo();
                    BeanUtils.copyProperties(bankInfo, bank);
                    bankList.add(bank);
                }
                //银行卡信息
                borrowVo.setBankList(bankList);
            }
        }
        //获取会员信息
        ResponseData<MemberModel> memberResponse = memberContract.getMember(borrowDto.getUserId());
        if ("0".equals(memberResponse.getStatus()) && memberResponse.getData() != null) {
            MemberModel model = memberResponse.getData();
            if (new Date().before(model.getExpireTime())) {
                borrowVo.setIfAssess(1);
            } else {
                borrowVo.setIfAssess(2);
            }
            borrowVo.setExpireTime(DateUtil.dateToStringHms(model.getExpireTime()));
            borrowVo.setMemberExpireDay(DateUtil.getDayDiffUp(new Date(), model.getExpireTime()));
        } else {
            borrowVo.setIfAssess(0);
        }
        response.setData(borrowVo);
        LOGGER.info("get borrowInfo success and result is:" + JSON.toJSONString(borrowVo));
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseData borrowInfoConfirm(BorrowConfirmDto borrowConfirmDto) throws Exception {
        LOGGER.info("开始进行借款确认：" + JSON.toJSONString(borrowConfirmDto));
        if(StringUtils.isNotBlank(borrowConfirmDto.getAccountNumber())) {
            borrowConfirmDto.setMobile(borrowConfirmDto.getAccountNumber());
        }

        //侬要贷真身设置默认值
        if (StringUtils.isBlank(borrowConfirmDto.getAppName())){
            borrowConfirmDto.setAppName("xxd");
        }
        try {
            //判断是否具有借款条件
            JudgeInfo judgeInfo = judgeBorrow(borrowConfirmDto.getUserId(),borrowConfirmDto.getAppName());
            if ("1".equals(judgeInfo.getWhetherLoan())) {
                if ("0".equals(judgeInfo.getUnProcessOrderExist())) {
                    ResponseData responseData = ResponseData.error(judgeInfo.getWhetherLoanMsg());
                    //1000 告诉银码头 在侬要贷 有未处理订单
                    responseData.setCode("1000");
                    LOGGER.error("改银码头用户有未处理的订单");
                    return responseData;
                } else {
                    return ResponseData.error(judgeInfo.getWhetherLoanMsg());
                }
            }
            if (redisTemplate.hasKey(NYD_REDIS_PREFIX + borrowConfirmDto.getUserId())) {
                LOGGER.info("借款确认redis有值" + borrowConfirmDto.getUserId());
                return ResponseData.error("不能重复提交");
            } else {
                redisTemplate.opsForValue().set(NYD_REDIS_PREFIX + borrowConfirmDto.getUserId(), "1",60, TimeUnit.SECONDS);
            }
//            //删除key
//            if (redisTemplate.hasKey("kzjr2qcgz" + borrowConfirmDto.getUserId())) {
//                redisTemplate.delete("kzjr2qcgz" + borrowConfirmDto.getUserId());
//            }
            String memberId = null;
            String memberType = null;
            BigDecimal memberFee = new BigDecimal(0);

            //判断会员是否有效, 仅限于侬要贷需要判断 Add by Jiawei Cheng 2018-3027 21:51
            //由于侬要贷之前并没有channel字段 所以 判断 channel 是否为null 为null 表示 来源 nyd
            boolean flag = true;
//            if (borrowConfirmDto.getChannel() == null || BorrowConfirmChannel.NYD.getChannel().equals(borrowConfirmDto.getChannel())) {
//                //获取会员信息
//                ResponseData<MemberModel> memberResponse = memberContract.getMember(borrowConfirmDto.getUserId());
//                MemberModel member = memberResponse.getData();
//                LOGGER.info("find by userId object member:"+JSON.toJSON(member));
////                member.setAppName(borrowConfirmDto.getAppName());//设置appName
////                memberContract.saveMember(member);
//                if (member == null || new Date().after(member.getExpireTime())) {
//                    flag = true;
//                    try {
//                        memberId = idGenerator.generatorId(BizCode.MEMBER).toString();
//                    } catch (Exception e) {
//                        LOGGER.error("memberId生成失败" + borrowConfirmDto.getUserId());
//                        Random random = new Random();
//                        memberId = random.nextInt() + "";
//                    }
//                    ResponseData<List<MemberConfigModel>> memberConfig = memberConfigContract.getMemberConfig("nyd");
//                    List<MemberConfigModel> memberConfigModels = memberConfig.getData();
//                    if (memberConfigModels.size() > 0) {
//                        MemberConfigModel model = memberConfigModels.get(0);
//                        memberFee = model.getDiscountFee();
//                        memberType = model.getType();
//                    } else {
//                        return ResponseData.error("无法获取会员配置信息");
//                    }
//                }else {
//                	JudgeMemberInfo judgeMember = judgeMemberWithMemberId(borrowConfirmDto.getUserId());
//                	LOGGER.info("生成会员判断信息：" + JSON.toJSONString(judgeMember));
//                    if (!judgeMember.isMemberFlag()) {
//                    } else {
//                        memberId = judgeMember.getMemberId();
//                    }
//                }
//            }
            //获取代扣费用；
            ResponseData<List<PayFeeInfo>> memberConfig = userIdentityContract.getPayFeeByBusiness("xxd");
            List<PayFeeInfo> memberConfigModels = memberConfig.getData();
            if (memberConfigModels.size() > 0) {
                PayFeeInfo model = memberConfigModels.get(0);
                memberFee = model.getDiscountFee();
                LOGGER.info("查询获取到的代扣费用：" + memberFee);
            } else {
                return ResponseData.error("无法获取代扣费用！");
            }
            //调用户系统rpc接口获取用户信息，保存至订单详情
            UserInfo userInfo = userIdentityContract.getUserInfo(borrowConfirmDto.getUserId()).getData();

            Order order = new Order();
            Date date = new Date();
            BeanUtils.copyProperties(borrowConfirmDto, order);

            //判断是否内部测试人员
            if (judgeTestUserFlag(borrowConfirmDto.getMobile())) {
                LOGGER.info("是否内部测试人员：是" );
                order.setTestStatus(0);
            } else {
                LOGGER.info("是否内部测试人员：否" );
                order.setTestStatus(1);
            }
            //大户的ID服务，生成订单ID
            String orderNo = idGenerator.generatorId(BizCode.ORDER_NYD).toString();
            order.setOrderNo(orderNo);
            order.setOrderStatus(OrderStatus.AUDIT.getCode());
            //申请时间
            order.setLoanTime(date);
            //获取金融产品信息
            ProductInfo productInfo = productContract.getProductInfo(borrowConfirmDto.getProductCode()).getData();
            if (productInfo == null) {
                LOGGER.error("get fianceProduct failed, ProductCode = " + borrowConfirmDto.getProductCode());
                return ResponseData.error(OrderConsts.NO_PRODUCT);
            }
            //利息 本金 * 日利率 * 天数
            double interest = ArithUtil.getInteg(ArithUtil.mul( ArithUtil.mul(productInfo.getMaxPrincipal().doubleValue(),productInfo.getInterestRate().doubleValue()),productInfo.getMaxLoanDay().doubleValue()));
            //  日利率
            double annualizedRate = productInfo.getInterestRate().doubleValue();
            //应还总金额  本金 + 利息 + 服务费
            double realLoanAmount = ArithUtil.add(ArithUtil.add(productInfo.getMaxPrincipal().doubleValue(),interest),productInfo.getManagerFee().doubleValue());
            //借款金额
            order.setLoanAmount(borrowConfirmDto.getLoanAmount());
            //应还总金额
            order.setRepayTotalAmount(new BigDecimal(realLoanAmount));
            //借款时间（天数）
            order.setBorrowTime(productInfo.getMaxLoanDay());
            //借款期数
            order.setBorrowPeriods(productInfo.getProductPeriods());
            //利息
            order.setInterest(new BigDecimal(interest));
            //年化利率
            order.setAnnualizedRate(new BigDecimal(annualizedRate));
            //管理费
            order.setManagerFee(productInfo.getManagerFee());
            
            
            String business = productInfo.getBusiness();
            order.setBusiness(business);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, +borrowConfirmDto.getBorrowTime());
            order.setMemberId(memberId);
            order.setMemberFee(memberFee);
            order.setMemberType(memberType);
            order.setWhoAudit(0);
            order.setRealLoanAmount(productInfo.getMaxPrincipal());
            if (borrowConfirmDto.getChannel() == null) {
                order.setChannel(BorrowConfirmChannel.NYD.getChannel());
            } else {
                order.setChannel(borrowConfirmDto.getChannel());
            }
            String orderJxOn = orderProperties.getOrderJxOn();
            //获取借款渠道
            String channel = getChannel(orderJxOn, borrowConfirmDto);
            order.setFundCode(channel);
            ResponseData response = ResponseData.success();
            //查询最近一笔订单借款期次
            String orderSql = "select loan_number as loanNumber from t_order where user_id = '"+borrowConfirmDto.getUserId()+"' order by loan_time desc limit 1";
            JSONObject orderJson = orderSqlService.queryOne(orderSql);
            if(ChkUtil.isEmpty(orderJson) || ChkUtil.isEmpty(orderJson.get("loanNumber"))){
            	order.setLoanNumber(0);
            }else{
            	order.setLoanNumber(orderJson.getInteger("loanNumber"));
            }
            //保存订单
            try {
                if (order.getChannel().equals(BorrowConfirmChannel.YMT.getChannel())) {
                    order.setIbankOrderNo(borrowConfirmDto.getIbankOrderNo());
                    //银码头 不通过审核 直接 待放款。
                    order.setOrderStatus(OrderStatus.WAIT_LOAN.getCode());
                    order.setAuditStatus("1");
                }
                /*ResponseData<List<OrderInfo>> orderData =orderContract.getOrdersByUserId(borrowConfirmDto.getUserId());
                //用户首次借款赠送12元现金红包
                if("0".equals(orderData.getStatus()) && (orderData.getData() == null  || orderData.getData().size() == 0)) {
                    LOGGER.info("用户首次借款奖励红包开始"+borrowConfirmDto.getUserId());
                    insertActivityLog(borrowConfirmDto.getAccountNumber(),borrowConfirmDto.getUserId());
                    LOGGER.info("用户首次借款奖励红包结束"+borrowConfirmDto.getUserId());
                }*/
                orderDao.save(order);
                //确认银行卡绑卡记录是否存在
                try {
                    confirmBankInfo(borrowConfirmDto);
                }catch(Exception e) {
                    LOGGER.error("银行卡补偿异常：" + e.getMessage());
                }
            } catch (Exception e) {
                LOGGER.error("save order failed  or dubbo update failed , orderNo = " + orderNo, e);
                throw e;
            }
            OrderDetail orderDetail = new OrderDetail();
            List list = new ArrayList<>();
            list.add(borrowConfirmDto);
            orderDetail = (OrderDetail) new ListTool().parseToObject(list, OrderDetail.class).get(0);
            orderDetail.setMobile(borrowConfirmDto.getMobile());
            orderDetail.setOrderNo(orderNo);
            //金融产品相关信息
            orderDetail.setProductCode(productInfo.getProductCode());
            orderDetail.setProductType(productInfo.getProductType());
            orderDetail.setProductPeriods(productInfo.getProductPeriods());
            orderDetail.setInterestRate(productInfo.getInterestRate());
            orderDetail.setIdType(userInfo.getCertificateType());
            orderDetail.setIdNumber(userInfo.getIdNumber());
            orderDetail.setRealName(userInfo.getRealName());
            //调用户系统rpc接口获取用户来源
            String source = userAccountContract.getAccountSource(borrowConfirmDto.getMobile()).getData();
            orderDetail.setSource(source);
            try {
                //查询评估报告key
                ResponseData reponse = qiniuContract.getLastReportKey(borrowConfirmDto.getUserId(), ReportType.FQZ);
                if(reponse != null && "0".equals(response.getStatus())) {
                    orderDetail.setAssessKey((String)reponse.getData());
                    LOGGER.error("获取评估报告key成功： " + reponse.getData());
                }
            }catch(Exception e) {
                LOGGER.error("获取评估报告key失败： " + e.getMessage());
            }
            //保存订单详情
            try {
                orderDetailDao.save(orderDetail);
            } catch (Exception e) {
                LOGGER.error("save orderDetail failed, orderNo = " + orderNo, e);
                throw e;
            }
            // 如果是银码头，则更改订单状态为等待放款
            if (borrowConfirmDto.getChannel() != null && borrowConfirmDto.getChannel().equals(BorrowConfirmChannel.YMT.getChannel())) {
                OrderStatusLog orderStatusLog1 = new OrderStatusLog();
                orderStatusLog1.setOrderNo(orderNo);
                orderStatusLog1.setBeforeStatus(OrderStatus.INIT.getCode());
                orderStatusLog1.setAfterStatus(OrderStatus.AUDIT.getCode());
                OrderStatusLog orderStatusLog2 = new OrderStatusLog();
                orderStatusLog2.setOrderNo(orderNo);
                orderStatusLog2.setBeforeStatus(OrderStatus.AUDIT.getCode());
                orderStatusLog2.setAfterStatus(OrderStatus.AUDIT_SUCCESS.getCode());
                OrderStatusLog orderStatusLog3 = new OrderStatusLog();
                orderStatusLog3.setOrderNo(orderNo);
                orderStatusLog3.setBeforeStatus(OrderStatus.AUDIT_SUCCESS.getCode());
                orderStatusLog3.setAfterStatus(OrderStatus.WAIT_LOAN.getCode());
                try {
                    orderStatusLogDao.save(orderStatusLog1);
                    orderStatusLogDao.save(orderStatusLog2);
                    orderStatusLogDao.save(orderStatusLog3);
                } catch (Exception e) {
                    LOGGER.error("save orderStatusLog fail, orderNo = " + orderNo, e);
                    throw e;
                }
            } else { // 如果是信信贷，则更改订单状态为等待审核
                OrderStatusLog orderStatusLog = new OrderStatusLog();
                orderStatusLog.setOrderNo(orderNo);
                orderStatusLog.setBeforeStatus(OrderStatus.INIT.getCode());
                orderStatusLog.setAfterStatus(OrderStatus.AUDIT.getCode());
                try {
                    orderStatusLogDao.save(orderStatusLog);
                } catch (Exception e) {
                    LOGGER.error("save orderStatusLog fail, orderNo = " + orderNo, e);
                    throw e;
                }
            }
            response.setData(order);
            //往风控系统发送消息
            OrderMessage msg = new OrderMessage();
            msg.setUserId(borrowConfirmDto.getUserId());
            msg.setOrderNo(orderNo);
            msg.setProductType(business);
            msg.setBorrowType(productInfo.getProductType().toString());

            LOGGER.info("发送消息队列,审批发送Mq对象:"+JSON.toJSON(msg));
            orderToAuditProducer.sendMsg(msg);
            LOGGER.info("发送成功");

            // 如果是银码头，则发送放款消息
            LOGGER.info("传入的渠道为" + borrowConfirmDto.getChannel());
            if (borrowConfirmDto.getChannel() != null && BorrowConfirmChannel.YMT.getChannel().equals(borrowConfirmDto.getChannel())) {
                LOGGER.info("渠道银码头");
            } else {
                msg.setChannel(BorrowConfirmChannel.NYD.getChannel());
                LOGGER.info("渠道信信贷");
            }
                //发送mq处理代扣逻辑
                LOGGER.info("flag:"+flag);
                if (flag) {
                    LOGGER.info("需要进行代扣处理");
                    PaymentRiskRecordExtendVo vo = new PaymentRiskRecordExtendVo();
                    vo.setAppName(borrowConfirmDto.getAppName());
                    vo.setCustName(userInfo.getRealName());
                    vo.setOrderNo(orderNo);
                    vo.setCellPhone(borrowConfirmDto.getMobile());
                    vo.setPhone("95093336");
                    vo.setSmsType("39");
                paymentRiskRecordService.saveRepaymentExtend(vo);
                //查询银行卡信息
                
                List<BankInfo> bankInfos = userBankContract.getBankInfos(borrowConfirmDto.getUserId()).getData();
                BankInfo  bankDetail = new BankInfo();
                for (BankInfo bankInfo : bankInfos) {
                	if(2!=bankInfo.getSoure()){
                		bankDetail = bankInfo;
                	}
				}
                PaymentRiskRequestChangjie request = new PaymentRiskRequestChangjie();

    			request.setBankNo(bankDetail.getBankAccount());
    			request.setIdNo(userInfo.getIdNumber());

                // 畅捷
                String  channelCode =bankDetail.getChannelCode();
                PaymentRiskRequestCommon paymentRiskRequest= new PaymentRiskRequestCommon();
                paymentRiskRequest.setChannelCode(channelCode);
                paymentRiskRequest.setMoney(memberFee);
                paymentRiskRequest.setOrderNo(orderNo);
                paymentRiskRequest.setRiskTime(new Date());

                if(!ChkUtil.isEmpty(channelCode) && "changjie".equals(channelCode)) {
                    PaymentRiskRequestChangjie cj = new PaymentRiskRequestChangjie();
                    cj.setBankNo(bankDetail.getBankAccount());
                    cj.setIdNo(userInfo.getIdNumber());
                    paymentRiskRequest.setChannelJson(JSONObject.toJSONString(cj));
                }
                // xunlian
                if(!ChkUtil.isEmpty(channelCode) && "xunlian".equals(channelCode)) {
                    PaymentRiskRequestXunlian xl = new PaymentRiskRequestXunlian();
                    xl.setAccount(bankDetail.getBankAccount());
                    xl.setName(userInfo.getRealName());
                    xl.setProtocolId(bankDetail.getProtocolId());
                    paymentRiskRequest.setChannelJson(JSONObject.toJSONString(xl));
                }else if(!ChkUtil.isEmpty(channelCode) && "xinsheng".equals(channelCode)) {
                    PaymentRiskRequestXinsheng xs = new PaymentRiskRequestXinsheng();
                    xs.setAmount(String.valueOf(memberFee));
                    xs.setBizProtocolNo(bankDetail.getBizProtocolNo());
                    xs.setMerUserId(borrowConfirmDto.getUserId());
                    xs.setPayProtocolNo(bankDetail.getPayProtocolNo());
                    paymentRiskRequest.setChannelJson(JSONObject.toJSONString(xs));
                }else if(!ChkUtil.isEmpty(channelCode) && "liandong".equals(channelCode)) {
                    PaymentRiskRequestLiandong xs = new PaymentRiskRequestLiandong();
                	xs.setAmount(String.valueOf(memberFee));
                	xs.setUsr_busi_agreement_id(bankDetail.getUsrBusiAgreementId());
                	xs.setOrder_no(orderNo);
                    xs.setMer_date(DateUtil.format(new Date(), "yyyyMMDD"));
                	xs.setUsr_pay_agreement_id(bankDetail.getUsrPayAgreementId());
                	paymentRiskRequest.setChannelJson(JSONObject.toJSONString(xs));
                }

                LOGGER.info("请求扣款参数" + JSON.toJSONString(paymentRiskRequest));
                JSONObject result= JSONObject.parseObject(JSONObject.toJSONString(paymentRiskRecordService.activeRepayment(paymentRiskRequest)));
            }

            LOGGER.info("borrow confirm success, userId is " + borrowConfirmDto.getUserId() + ", orderNo is " + orderNo);
            return response;
        } catch (Exception e) {
        	e.printStackTrace();
            LOGGER.error("借款确认时发生异常，request is :" + JSON.toJSONString(borrowConfirmDto) + e.getMessage());
            LOGGER.error(e.getMessage());
            throw e;
        }

    }

    @Override
    public ResponseData mqRepost(BorrowMqConfirmDto dto) throws Exception {
        ResponseData response = ResponseData.success();
        //获取金融产品信息
        ProductInfo productInfo = productContract.getProductInfo(dto.getProductCode()).getData();
        if (productInfo == null) {
            LOGGER.error("get fianceProduct failed, ProductCode = " + dto.getProductCode());
            return ResponseData.error(OrderConsts.NO_PRODUCT);
        }

        //往放款发送消息
        OrderMessage msg = new OrderMessage();
        msg.setUserId(dto.getUserId());
        msg.setOrderNo(dto.getOrderNo());
        msg.setProductType("xxd");
        msg.setBorrowType(productInfo.getProductType().toString());
        msg.setChannel(BorrowConfirmChannel.NYD.getChannel());
        LOGGER.info("发送消息队列,代扣发送Mq对象:"+JSON.toJSON(msg));
        orderToAuditProducer.sendMsg(msg);
        LOGGER.info("发送成功");

        return response.setMsg(dto.getOrderNo());
    }

    @Override
    public ResponseData getBorrowInfoForPay(BorrowDto borrowDto) {
        LOGGER.info("begin to get borrowInfo, borrowInfo is " + JSON.toJSONString(borrowDto));
        ResponseData response = ResponseData.success();
        BorrowVo borrowVo = new BorrowVo();
        borrowVo.setMobile(borrowDto.getAccountNumber());

        //获取用户额度
        ResponseData<UserInfo> userInfo = userIdentityContract.getUserInfo(borrowDto.getUserId());
        UserInfo data = userInfo.getData();
        String url = orderProperties.getLimitServiceUrl() + "limitService/getUserLimit";
        Map<String, String> map = new HashMap<>();
        map.put("idCardNo", data.getIdNumber());
        String json = JSON.toJSONString(map);
        LOGGER.info("调用风控服务获取用户额度param:"+json);
        int limit;
        try {
            String sendPost = HttpUtil.sendPost(url, json);
            JSONObject jsonObject = JSONObject.parseObject(sendPost);
            String string = jsonObject.getString("data");
            BigDecimal aDouble = new BigDecimal(string);
            limit = aDouble.intValue();
            LOGGER.info("风控返回用户额度结果:"+limit);
            if(limit < 1000) {
                limit = 1000;
            }
        } catch (Exception e) {
            limit = 1000;
        }
        LOGGER.info("用户的最终额度为"+limit);
        //获取金融产品信息
        if(StringUtils.isBlank(borrowDto.getAppName())) {
            borrowDto.setAppName("nyd");
        }
        LOGGER.info("获取金融产品信息appName:"+borrowDto.getAppName());
        ResponseData<List<ProductInfo>> productInfos = null;
        if ("wwj".equals(borrowDto.getAppName())) {
            productInfos = productContract.getProductInfoByBusiness(borrowDto.getAppName());
        } else {
            productInfos = productContract.getProductInfos();
        }
        List<ProductInfo> infos = productInfos.getData();
        if (infos != null && infos.size() > 0) {
            List<ProductVo> list = new ArrayList<>();
            for (ProductInfo productInfo : infos) {
                if (productInfo.getMaxPrincipal().intValue() <= limit) {
                    ProductVo productVo = new ProductVo();
                    productVo.setBorrowTime(productInfo.getMaxLoanDay());
                    productVo.setInterestRate(productInfo.getInterestRate());
                    productVo.setLoanAmount(productInfo.getMaxPrincipal());
                    productVo.setProductCode(productInfo.getProductCode());
                    productVo.setRecommendFlag(productInfo.getRecommendFlag());
                    list.add(productVo);
                }
            }
            borrowVo.setProductList(list);
        }
        //获取银行卡信息
        //String url1 = "http://192.168.10.64:8086/common/pay/queryBindCard";
        String url1 = "http://" + orderProperties.getCommonPayIp() + ":" + orderProperties.getCommonPayPort() + "/common/pay/queryBindCard";
        Map<String, Object> map1 = new HashMap<>();
        map1.put("idcardNo", data.getIdNumber());
        map1.put("state", 3);
        //将渠道来源传给代扣绑卡查询接口
        if (StringUtils.isBlank(borrowDto.getAppName())){
            map1.put("source","xxd");
        }else{
            map1.put("source",borrowDto.getAppName());
        }
        String json1 = JSON.toJSONString(map1);
        List<Map<String, String>> bankInfos;
        try {
            String sendPost = HttpUtil.sendPost(url1, json1);
            JSONObject jsonObject = JSONObject.parseObject(sendPost);
            bankInfos = (List<Map<String, String>>) jsonObject.get("data");
        } catch (Exception e) {
            bankInfos = new ArrayList<>();
        }
        List<BankVo> bankList = new ArrayList<BankVo>();
        if (bankInfos.size() > 0) {
            for (Map<String, String> bankInfo : bankInfos) {
                BankVo bank = new BankVo();
                bank.setBankAccount(bankInfo.get("cardNo"));
                bank.setBankName(bankInfo.get("bankName"));
                bankList.add(bank);
            }
            //银行卡信息
            borrowVo.setBankList(bankList);
        } else {
            borrowVo.setBankList(null);
        }
        response.setData(borrowVo);
        LOGGER.info("get borrowInfo success and result is:" + JSON.toJSONString(borrowVo));
        return response;
    }

    @Override
    public ResponseData borrowBanks(BorrowConfirmDto borrowConfirmDto) {
        LOGGER.info("开始获取银行卡列表,userId is " + borrowConfirmDto.getUserId());
        ResponseData common = new ResponseData();


        if(ChkUtil.isEmptys(borrowConfirmDto.getUserId())){
            LOGGER.info("暂无绑定卡数据,{}", JSON.toJSONString(borrowConfirmDto.getUserId()));
            common.setStatus("1");
            common.setCode(StatusConstants.SUCCESS_CODE);
            common.setMsg("暂无绑定银行卡数据");
            return common;
        }

        List<BankVo> bankVos = new ArrayList<>();

        try {
            ResponseData<List<JSONObject>> userInfo = userBankCardService.queryBankList(borrowConfirmDto.getUserId());
            if (!ChkUtil.isEmpty(userInfo.getData())) {
                List<JSONObject> list = userInfo.getData();
                for (JSONObject jsonObject : list) {
                    BankVo bankVo = new BankVo();
                    bankVo.setBankAccount(jsonObject.getString("bank_account"));
                    bankVo.setBankName(jsonObject.getString("bank_name"));
                    if("2".equals(jsonObject.getString("soure"))){
                    	bankVo.setBankType("2");
                    }else{
                    	bankVo.setBankType("1");
                    }
                    bankVos.add(bankVo);
                }
                common.setData(bankVos);
                common.setStatus("0");
                common.setCode(StatusConstants.SUCCESS_CODE);
                common.setMsg(StatusConstants.SYSTEM_OPERATION_SUCCESS_MSG);
            }else{
                LOGGER.info("暂无绑定卡数据,{}", JSON.toJSONString(borrowConfirmDto.getUserId()));
                common.setStatus("1");
                common.setCode(StatusConstants.SUCCESS_CODE);
                common.setMsg(StatusConstants.SYSTEM_OPERATION_SUCCESS_MSG);
            }
            return  common;
        } catch (Exception e) {
            LOGGER.error("获取银行卡列表发生异常,{}", e);
            return ResponseData.error("获取银行卡列表失败");
        }
    }


    /**
     * 会员判断
     *
     * @param userId
     * @return
     */
    private JudgeMemberInfo judgeMember(String userId) {
        LOGGER.info("begin to judge member, userId is " + userId);
        JudgeMemberInfo memberInfo = new JudgeMemberInfo();
        boolean memberFlag = false;
        try {
            //获取会员信息
            ResponseData<MemberModel> memberResponse = memberContract.getMember(userId);
            if ("0".equals(memberResponse.getStatus())) {
                if (memberResponse.getData() != null) {
                    MemberModel model = memberResponse.getData();
                    int expireDay = DateUtil.getDayDiffUp(new Date(), model.getExpireTime());
                    int memberDay = Integer.valueOf(orderProperties.getMemberDays());
                    if (expireDay > memberDay) {
                        memberFlag = true;
                        memberInfo.setMemberId(model.getMemberId());
                    }
                    memberInfo.setMemberFee(model.getMemberFee());
                    memberInfo.setMemberType(model.getMemberType());
                }
            }
        } catch (Exception e) {
            LOGGER.error("get member info error ! userId = " + userId, e);
        }
        LOGGER.info("judge member result is" + memberFlag);
        memberInfo.setMemberFlag(memberFlag);
        return memberInfo;
    }
    /**
     * 会员判断划扣会员类型
     *
     * @param userId
     * @return
     */
    private JudgeMemberInfo judgeMemberWithMemberId(String userId) {
        LOGGER.info("begin to judge member, userId is " + userId);
        JudgeMemberInfo memberInfo = new JudgeMemberInfo();
        boolean memberFlag = false;
        try {
            //获取会员信息
            ResponseData<MemberModel> memberResponse = memberContract.getMemberWithMemberId(userId);
            if ("0".equals(memberResponse.getStatus())) {
                if (memberResponse.getData() != null) {
                    MemberModel model = memberResponse.getData();
                    int expireDay = DateUtil.getDayDiffUp(new Date(), model.getExpireTime());
                    int memberDay = Integer.valueOf(orderProperties.getMemberDays());
                    if (expireDay > memberDay) {
                        memberFlag = true;
                        memberInfo.setMemberId(model.getMemberId());
                    }
                    memberInfo.setMemberFee(model.getMemberFee());
                    memberInfo.setMemberType(model.getMemberType());
                }
            }
        } catch (Exception e) {
            LOGGER.error("get member info error ! userId = " + userId, e);
        }
        LOGGER.info("judge member result is" + memberFlag);
        memberInfo.setMemberFlag(memberFlag);
        return memberInfo;
    }


    /**
     * 判断是否存在审核中，待放款的订单
     *
     * @param userId
     * @return boolean
     */
    private JudgeInfo judgeBorrow(String userId,String appName) {
        LOGGER.info("begin to Judge borrow, userId is " + userId);
        JudgeInfo judgeInfo = new JudgeInfo();
        if (StringUtils.isBlank(appName)) {
            appName = "xxd";
        }
        try {
            List<OrderInfo> orderList = orderDao.getLastOrderByUserId(userId);
            if (orderList != null && orderList.size() > 0) {
                OrderInfo orderInfo = orderList.get(0);
                int days = DateUtil.getDayDiffDown(orderInfo.getLoanTime(), new Date());
                int intervalDays = Integer.valueOf(orderProperties.getIntervalDays());
                if (appName.equals("wwj")) {
                    intervalDays = Integer.valueOf(orderProperties.getWwjIntervalDays());
                }
              //状态小于40，不可借款
                if (OrderStatus.AUDIT.getCode().toString().equals(orderInfo.getOrderStatus().toString())) {
                    judgeInfo.setWhetherLoan("1");
                    judgeInfo.setWhetherLoanMsg("您有一笔借款在审核中！");
                    judgeInfo.setOrderFlag("1");
                }else if (OrderStatus.AUDIT_SUCCESS.getCode().toString().equals(orderInfo.getOrderStatus().toString())
                		||OrderStatus.WAIT_LOAN.getCode().toString().equals(orderInfo.getOrderStatus().toString())) {
                    judgeInfo.setWhetherLoanMsg("待提现");
                    judgeInfo.setOrderFlag("2");

//                }else if(orderInfo.getOrderStatus() == OrderStatus.WAIT_LOAN.getCode()){
//                    judgeInfo.setWhetherLoan("5");
//                    judgeInfo.setWhetherLoanMsg("您有已存在的订单，请点击首页进度查询查看");
                } else if (OrderStatus.LOAN_FAIL.getCode().toString().equals(orderInfo.getOrderStatus().toString()) ) {
                    //放款失败，可借款
                    if (days<intervalDays) {
                        int failDays = DateUtil.getDayDiffDown(orderInfo.getLoanFailTime(), new Date());
                        int daysLeft = intervalDays - failDays;
                        judgeInfo.setWhetherLoan("1");
                        judgeInfo.setWhetherLoanMsg("您有已有存在订单，请" +daysLeft +"天后重新申请");
                        judgeInfo.setOrderFlag("5");

                    } else {
                        judgeInfo.setWhetherLoan("0");
                    }
                } else if (OrderStatus.LOAN_SUCCESS.getCode().toString().equals(orderInfo.getOrderStatus().toString())) {
                    //还款中
//                    int billCount = billContract.getBillInfos(userId).getData();
//                    if (billCount > 0) {
                        judgeInfo.setWhetherLoan("1");
                        judgeInfo.setWhetherLoanMsg("您有未还清的借款");
                        judgeInfo.setOrderFlag("3");
//                    } else {
//                        //账单已还清，可借款
//                        judgeInfo.setWhetherLoan("0");
//                    }
                } else if (OrderStatus.AUDIT_REFUSE.getCode().toString().equals(orderInfo.getOrderStatus().toString())) {
                    //审核拒绝，判断间隔是否超过14天
                    if (days < intervalDays) {
                        judgeInfo.setWhetherLoan("1");
                        judgeInfo.setOrderFlag("4");
                        judgeInfo.setWhetherLoanMsg("您有已存在的订单，请点击首页进度查询查看");
                    } else {
                        judgeInfo.setWhetherLoan("0");
                    }
                } else if (OrderStatus.LOAN_CLOSED.getCode().toString().equals(orderInfo.getOrderStatus().toString()) 
                		|| OrderStatus.REPAY_SUCCESS.getCode().toString().equals(orderInfo.getOrderStatus().toString())) {
                	//订单结清或关闭  可借款
                	judgeInfo.setWhetherLoan("0");
                }
            } else {
                //没有订单，表示第一次借款，可以借款
                judgeInfo.setWhetherLoan("0");
            }
            LOGGER.info("judge borrow result is " + JSONObject.toJSONString(judgeInfo));
        } catch (Exception e) {
            e.printStackTrace();
            judgeInfo.setWhetherLoan("1");
            judgeInfo.setWhetherLoanMsg(OrderConsts.DB_ERROR_MSG);
            LOGGER.error("judge borrow error! userId = " + userId, e);
        }
        return judgeInfo;
    }


    /**
     * 判断是否为测试人员
     *
     * @param mobile
     * @return boolean
     */
    private boolean judgeTestUserFlag(String mobile) {
        LOGGER.info("begin to judge  testUser Flag, mobile is " + mobile);
        boolean testFlag = false;
        try {
            List<InnerTest> list = testListDao.getObjectsByMobile(mobile);
            if (list != null && list.size() > 0) {
                testFlag = true;
            }
        } catch (Exception e) {
            LOGGER.error("get innerTest  list error! mobile = " + mobile, e);
        }
        LOGGER.info("judge testUser Flag result is" + testFlag);
        return testFlag;
    }

    /**
     *
     * 代扣借款结果查询
     *
     * @see com.nyd.order.service.NewOrderInfoService#queryResult(com.nyd.order.model.BaseInfo)
     */
    @Override
    public ResponseData queryResult(BaseInfo baseInfo) {
        LOGGER.info("首页认证结果查询:{}",JSON.toJSONString(baseInfo));
        try {
            ResponseData response = ResponseData.success();
            UserLimitDto userLimitDto = new UserLimitDto();
            userLimitDto.setProductType("nyd");
            userLimitDto.setUserId(baseInfo.getUserId());
            userLimitDto.setAppName(baseInfo.getAppName());
            if("wwj".equals(baseInfo.getAppName())) {
            	userLimitDto.setAppName("wwj");
            }else if(baseInfo.getAppName() == null || baseInfo.getAppName() == "" ){
            	userLimitDto.setAppName("nyd");
            }
           /* ResponseData<AssessDto> data = assessService.getassess(userLimitDto);
            if(null == data || !"0".equals(data.getStatus())) {
                LOGGER.error("调用风控前置响应失败");
                return response;
            }
            LOGGER.info("用户编号:{};分数:{};等级:{}",baseInfo.getUserId(),data.getData().getAssessScore(),data.getData().getAssessLevel());
            if(StringUtils.isBlank(data.getData().getAssessScore()) ||
                    StringUtils.isBlank(data.getData().getAssessLevel())) {
                LOGGER.error("调用风控前置响应的评估分数和评估等级为空");
                return response;
            }*/
            if(null == baseInfo.getOrderNo() || "".equals(baseInfo.getOrderNo())) {
                return response;
            }
            List<OrderInfo> orderList = orderDao.getBorrowInfoByOrderNo(baseInfo.getOrderNo());
            String  orderNo1 = null;
            if (orderList != null && orderList.size() > 0) {
                orderNo1 = orderList.get(0).getOrderNo();
            }else {
                return response;
            }
            //存入redis中
            if(redisTemplate.hasKey(OrderConsts.AUDIT+orderNo1)) {
                LOGGER.info("查询数据库借款结果最终状态");
                BorrowResultVo borrowResult = new BorrowResultVo();

                try {
                    OrderInfo orderInfo = orderList.get(0);
                    BeanUtils.copyProperties(orderInfo, borrowResult);
                    String finalEvaluation = "";
                   /* ResponseData<AssessDetailDto> res = reportDetailQueryService.getAssessInfo(baseInfo.getUserId(),borrowResult.getOrderNo());
                    if(null == res || null ==  res.getData() || null == res.getData().getAssessDetailModel() || null == res.getData().getAssessDetailModel().getFinalEvaluation()){
                        finalEvaluation = "";
                    }else{
                        finalEvaluation = res.getData().getAssessDetailModel().getFinalEvaluation();
                    }
                    if(finalEvaluation.length()>0){
                        //截取等级ABCDE;
                        finalEvaluation = finalEvaluation.substring(9,10);
                    }*/

                    finalEvaluation = "B";
                    LOGGER.info(" 新增 获取风险等级:" + finalEvaluation);
                    borrowResult.setFinalEvaluation(finalEvaluation);

                    List<OrderStatusLogInfo> statusList = orderStatusLogDao.getObjectsByOrderNo(borrowResult.getOrderNo());
                    if (statusList != null && statusList.size() > 0) {
                        List<OrderLogVo> logList = new ArrayList<>();
                        for (OrderStatusLogInfo logInfo : statusList) {
                            if (orderInfo.getIfSign() == 0 && OrderStatus.WAIT_LOAN.getCode().equals(orderInfo.getOrderStatus()) && OrderStatus.WAIT_LOAN.getCode().equals(logInfo.getAfterStatus())) {
                                continue;
                            }
                            OrderLogVo orderLog = new OrderLogVo();
                            orderLog.setStatusCode(logInfo.getAfterStatus());
                            orderLog.setStatusTime(DateUtil.dateToString(logInfo.getCreateTime()));
                            logList.add(orderLog);
                        }
                        borrowResult.setOrderStatusList(logList);
                    }
                    response.setData(borrowResult);
                    LOGGER.info("获取并打印借款结果{}:",JSON.toJSONString(response));
                } catch (Exception e) {
                    LOGGER.error("获取借款结果失败, userId = " + baseInfo.getUserId(), e);
                    response = ResponseData.error("查询借款结果发生异常");
                }
                return response;
            }else {
                //修改审核状态
                StepInfo stepInfo = new StepInfo();
                stepInfo.setUserId(baseInfo.getUserId());
                stepInfo.setPreAuditFlag("1");
//                stepInfo.setPreAuditLevel(data.getData().getAssessLevel());
                userStepContractNyd.updateUserStep(stepInfo);
                try {
                    userStepContractNyd.sendMsgToUserToApplication(baseInfo.getUserId());
                } catch (Exception e) {
                    LOGGER.error("sendMsgToUserToApplication error, userId = " + baseInfo.getUserId(), e);
                }
//            	BorrowResultVo borrowResult = new BorrowResultVo();
//            	List<OrderInfo> orderList = orderDao.getObjectsByUserId(baseInfo.getUserId());
//                OrderInfo orderInfo = null;
//                if (orderList != null && orderList.size() > 0) {
//                    orderInfo = orderList.get(0);
//                    BeanUtils.copyProperties(orderInfo, borrowResult);
//                } else {
//                    return response;
//                }
                List<OrderDetailInfo> list = orderDetailDao.getObjectsByUserId(baseInfo.getUserId());
                if(null == list || list.isEmpty()) {
                    LOGGER.error("DB中订单详情表查询不到该用户:{}",baseInfo.getUserId());
                    return response;
                }
                LOGGER.info("DB中订单详情表查询结果:{}",JSON.toJSONString(list.get(0)));
                //获取金融产品信息
                ResponseData<ProductInfo> pData = productContract.getProductInfo(list.get(0).getProductCode());
                if(!"0".equals(pData.getStatus())) {
                    return response;
                }
                String orderNo = list.get(0).getOrderNo();

                ProductInfo productInfo = pData.getData();
                OrderMessage msg = new OrderMessage();
                msg.setUserId(baseInfo.getUserId());
                msg.setOrderNo(orderNo);
                msg.setProductType("xxd");
                msg.setBorrowType(productInfo.getProductType().toString());
                //调mq的审核
                orderToAuditProducer.sendMsg(msg);

                //存入到redis中,并把该订单号保存7天
                redisTemplate.opsForValue().set(OrderConsts.AUDIT+orderNo,orderNo,  7, TimeUnit.DAYS);
                LOGGER.info("商户号:{},订单号:{},存入到redis并发送到MQ审核",baseInfo.getUserId(),orderNo);
                //放入订单号
                Map<String, Object> map = new HashMap<>();
                map.put("orderNo",orderNo);
                map.put("orderStatusList",new ArrayList<>());

                response.setData(map);
                return response;
            }
        } catch (Exception e) {
            LOGGER.info("代扣查询结果异常:{}",e);
            return ResponseData.error("服务器开小差");
        }
    }

    /**
     * 确认银行卡信息是否存在，如不存在查询划扣绑卡接口，查询到绑卡信息则保存
     *
     * @param orderInfo
     * @return
     */
    private ResponseData confirmBankInfo(BorrowConfirmDto orderInfo) throws Exception {
        ResponseData<List<BankInfo>> bankInfos = userBankContract.getBankInfosByBankAccout(orderInfo.getBankAccount());
        if ("0".equals(bankInfos.getStatus())) {
            if (bankInfos.getData() != null && bankInfos.getData().size() > 0) {
                return ResponseData.success();
            } else {
                UserInfo user = userIdentityContract.getUserInfo(orderInfo.getUserId()).getData();
                if (user == null) {
                    LOGGER.info("未查询到用户信息");
                    return ResponseData.error("未查询到用户信息");
                }
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("idcardNo", user.getIdNumber());
                params.put("source", orderInfo.getAppName());
                params.put("state", 3);
                List<Map<String, String>> bankList = null;
                try {
                    String sendPost = HttpUtil.sendPost(orderProperties.getCardListQuery(), JSON.toJSONString(params));
                    LOGGER.info("银行卡补偿查询返回信息：" + sendPost);
                    JSONObject jsonObject = JSONObject.parseObject(sendPost);
                    bankList = (List<Map<String, String>>) jsonObject.get("data");
                } catch (Exception e) {
                    LOGGER.error("查询银行卡列表异常" + e.getMessage());
                    return ResponseData.error("查询银行卡列表异常");
                }
                if (bankList != null && bankList.size() > 0) {
                    for (Map<String, String> bankInfo : bankList) {
                        if (!bankInfo.get("cardNo").equals(orderInfo.getBankAccount())) {
                            continue;
                        }
                        BankInfo bank = new BankInfo();
                        bank.setAccountName(user.getRealName());
                        bank.setAccountNumber(bankInfo.get("cardNo"));
                        bank.setBankAccount(bankInfo.get("cardNo"));
                        bank.setBankName(bankInfo.get("bankName"));
                        bank.setReservedPhone(bankInfo.get("phone"));
                        bank.setUserId(orderInfo.getUserId());
                        LOGGER.info("更新t_user_bank表记录：" + JSON.toJSONString(bank));
                        userBankContract.saveBankInfoNoJudge(bank);
                    }
                    //银行卡信息
                } else {
                    LOGGER.info("代扣服务没有查询到银行卡信息");
                    return ResponseData.error("没有查询到银行卡信息");
                }
            }
        } else {
            LOGGER.info("查询银行卡信息失败" + bankInfos.getMsg());
            return ResponseData.error("查询银行卡信息失败");
        }
        return ResponseData.success();
    }


    @Override
    public ResponseData withholdCallBack(OrderMessage message) {
        LOGGER.info("进入了代扣完成回调,message:"+message.toString());
        try {
            List<WithholdOrder> objectsPayOrderNo = withholdOrderDao.getObjectsPayOrderNo(message.getPayOrderNo());
            if (objectsPayOrderNo.size()>0){
                WithholdOrder withholdOrder = objectsPayOrderNo.get(0);
                RepayInfo repayInfo = new RepayInfo();
                if (message.getResult() == 1) {
                    withholdOrder.setOrderStatus(2);
                    repayInfo.setRepayStatus("0");
                }else {
                    withholdOrder.setOrderStatus(3);
                    repayInfo.setRepayStatus("1");
                }
                withholdOrderDao.update(withholdOrder);

                WithholdOrder entity = withholdOrderDao.getObjectsPayOrderNo(message.getPayOrderNo()).get(0);
                //记录代扣付费记录
                repayInfo.setBillNo(withholdOrder.getMemberId());
                repayInfo.setRepayAmount(withholdOrder.getPayAmount());
                repayInfo.setRepayChannel("fuiou");
                repayInfo.setRepayTime(new Date());
                repayInfo.setRepayNo(withholdOrder.getPayOrderNo());
                repayInfo.setRepayType(RepayType.MFEE.getCode());
                repayInfo.setUserId(withholdOrder.getUserId());
                if (StringUtils.isBlank(entity.getAppName())){
                    repayInfo.setAppName("xxd");
                }else{
                    repayInfo.setAppName(entity.getAppName());
                }
                LOGGER.info("代扣支付会员费，给repay发送mq消息的对象："+JSON.toJSON(repayInfo));
                try {
//                    rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                    LOGGER.info("代扣支付会员费成功,发送repay_MQ成功");
                }catch (Exception e){
                    LOGGER.info("代扣支付完成，给repay发送mq消息的对象异常",e);
                }
                if (message.getResult() == 1) {
                    SmsRequest vo = new SmsRequest();
                    try {
//                        ResponseData<AccountDto> accountDtoResponseData = userAccountContract.getAccount(withholdOrder.getUserId());
                        ResponseData<List<BankInfo>> responseData = userBankContract.getBankInfos(withholdOrder.getUserId());
                        if (responseData!=null&&"0".equals(responseData.getStatus())) {
                            List<BankInfo> list = responseData.getData();
                            if (list!=null&&list.size()>0) {
                                BankInfo bankInfo = list.get(0);
                                vo.setCellphone(bankInfo.getReservedPhone());
                            }
                        }
                    } catch (Exception e) {
                        LOGGER.error("getAccount by userId has exception! userId is "+withholdOrder.getUserId(),e);
                    }
                    vo.setSmsType(38);
                    if (StringUtils.isNotBlank(entity.getAppName())) {
                        vo.setAppName(entity.getAppName());
                    } else {
                        vo.setAppName("xxd");
                    }
                    String phone = orderProperties.getWithholdPhone();
                    Map<String, Object> map = new HashMap<>();
                    map.put("phone",phone);
                    vo.setMap(map);
                    try {
//                        sendSmsService.sendSingleSms(vo);
                    } catch (Exception e) {
                        LOGGER.error("withholdCallBack send msg error ",e);
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error("代扣回调发生异常",e);
            return ResponseData.error();
        }
        return ResponseData.success();
    }

    @Override
    public ResponseData withholdCallBackMsg(WithholdCallBackInfo withholdCallBackInfo) {
        LOGGER.info("进入了代扣发短信回调,:"+withholdCallBackInfo.toString());
        try {
            List<WithholdOrder> objectsPayOrderNo = withholdOrderDao.getObjectsPayOrderNo(withholdCallBackInfo.getPayOrderNo());
            if (objectsPayOrderNo!=null&&objectsPayOrderNo.size()>0) {
                WithholdOrder withholdOrder = objectsPayOrderNo.get(0);
                if (withholdCallBackInfo!=null&&withholdCallBackInfo.getResult()==1) { //代扣成功 发送短信
                    SmsRequest vo = new SmsRequest();
                    try {
//                        ResponseData<AccountDto> accountDtoResponseData = userAccountContract.getAccount(withholdOrder.getUserId());
                        ResponseData<List<BankInfo>> responseData = userBankContract.getBankInfos(withholdOrder.getUserId());
                        if (responseData!=null&&"0".equals(responseData.getStatus())) {
                            List<BankInfo> list = responseData.getData();
                            if (list!=null&&list.size()>0) {
                                BankInfo bankInfo = list.get(0);
                                vo.setCellphone(bankInfo.getReservedPhone());
                            }
                        }
                    } catch (Exception e) {
                        LOGGER.error("getAccount by userId has exception! userId is "+withholdOrder.getUserId(),e);
                    }
                    vo.setSmsType(39);
                    if (StringUtils.isNotBlank(withholdOrder.getAppName())) {
                        vo.setAppName(withholdOrder.getAppName());
                    } else {
                        vo.setAppName("xxd");
                    }
                    String phone = "";
//                    if("xqj1".equals(vo.getAppName())){
//                        phone = orderProperties.getXqjieWithholdPhone();
//                    }else if("wwj".equals(vo.getAppName())) {
//                        phone=orderProperties.getWwjWithholdPhone();
//                    }else if("fmh".equals(vo.getAppName())) {
//                        phone=orderProperties.getFmhWithholdPhone();
//                    }else if("xxx".equals(vo.getAppName())) {
//                        phone=orderProperties.getXxxWithholdPhone();
//                    } else{
                        phone = orderProperties.getWithholdPhone();
//                    }
                    Map<String, Object> map = new HashMap<>();
                    map.put("prince",withholdOrder.getPayAmount());
                    map.put("amount",withholdCallBackInfo.getAmount());
                    map.put("sumAmount",withholdCallBackInfo.getSumAmount());
                    map.put("phone",phone);
                    vo.setMap(map);
                    try {
                        sendSmsService.sendSingleSms(vo);
                    } catch (Exception e) {
                        LOGGER.error("withholdCallBackMsg send msg error ",e);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("代扣回调发短信发生异常",e);
            return ResponseData.error();
        }
        return ResponseData.success();
    }

    @Override
    public ResponseData updateOrderNoticeStatus(OrderUpdatInfo info) {
        if (StringUtils.isEmpty(info.getOrderNo()) || info.getIfNotice() == null) {
            LOGGER.error("查询订单号为空");
            return ResponseData.error("更新信息有误！");
        }
        List<OrderInfo> list = null;
        try {
            list = orderDao.getObjectsByOrderNo(info.getOrderNo());
        } catch (Exception e) {
            LOGGER.error("查询订单异常：{}", info.getOrderNo() + e.getMessage());
            return ResponseData.error("查询订单信息异常！");
        }
        if (list != null) {
            OrderInfo order = list.get(0);
            order.setIfNotice(info.getIfNotice());
            try {
                orderDao.update(order);
                //orderContract.updateOrderInfo(order);
                return ResponseData.success();
            } catch (Exception e) {
                LOGGER.error("更新订单异常：{}", e.getMessage());
                return ResponseData.error("更新订单信息异常！");
            }
        }
        return null;
    }

    private void signOtherAgreement(String orderNo) {
        //
        try {
            List<OrderInfo> orderList = orderDao.getObjectsByOrderNo(orderNo);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("orderNo", orderNo);
            if (orderList != null && orderList.size() > 0) {
                OrderInfo orderInfo1 = orderList.get(0);
                jsonObject.put("userId", orderInfo1.getUserId());
                jsonObject.put("loanMoney", orderInfo1.getLoanAmount());
                jsonObject.put("loanDay", orderInfo1.getBorrowTime());
                jsonObject.put("loanRate", orderInfo1.getAnnualizedRate());
            }
            List<OrderDetailInfo> detailList = orderDetailDao.getObjectsByOrderNo(orderNo);
            if (detailList != null && detailList.size() > 0) {
                OrderDetailInfo orderDetailInfo = detailList.get(0);
                jsonObject.put("userName", orderDetailInfo.getRealName());
                jsonObject.put("idNumber", orderDetailInfo.getIdNumber());
                jsonObject.put("mobile", orderDetailInfo.getMobile());
                jsonObject.put("model", orderDetailInfo.getModel());
                jsonObject.put("brand", orderDetailInfo.getBrand());
                jsonObject.put("deviceId", orderDetailInfo.getDeviceId());
            }
            agreeMentContract.signOtherAgreeMent(jsonObject);
        } catch (Exception e) {
            LOGGER.error("signOtherAgreeMent failed,orderNo= " + orderNo, e);
        }
    }

    private void  insertActivityLog(String accountNumber,String userId) {
        ActivityFeeInfo activityFee = new ActivityFeeInfo();
        activityFee.setAccountNumber(accountNumber);
        activityFee.setUserId(userId);
        activityFee.setActivityType("firstLoan");//新用户完整资料奖励
        activityFee.setActivityMoney("12");//12元现金红包
        activityFee.setActivityLimitTime(null);
        activityFee.setUseFlag("0");
        activityFee.setMarks("用户首次借款奖励");
        activityFee.setAppName("nyd");
        activityFee.setDeleteFlag("0");
        try {
            cashRedBagService.insertActivityLog(activityFee);
        }catch(Exception e) {
            LOGGER.info("save t_activity_log tabel error"+e);
        }
    }

    @Override
    public ResponseData  queryPayOrderByBusinessOrderNo(String businessOrderNo){
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("businessOrderNo", businessOrderNo);
        LOGGER.info("queryPayOrderByBusinessOrderNo： " + JSON.toJSONString(param));
        try {
            ResponseData resp = restTemplateApi.postForObject("http://47.104.180.64:8086/common/pay/queryPayOrderByBusinessOrderNo", param,ResponseData.class);
            LOGGER.info("queryPayOrderByBusinessOrderNo：{} " , JSON.toJSONString(resp));
            return resp;
        }catch(Exception e) {
            LOGGER.error("queryPayOrderByBusinessOrderNo：{}",e.getMessage());
            return ResponseData.error();
        }
    }

  @Override
    public ResponseData<BorrowInfo> getBorrowInfoByOrderNo(String orderNo) {
       LOGGER.info("begin to get borrow detail , orderNo is " + orderNo);
        ResponseData response = ResponseData.success();
        if (orderNo == null && "".equals(orderNo)) {
            return response;
        }
        BorrowInfo borrowInfo = new BorrowInfo();
        try {
            List<OrderInfo> detailList = orderDao.getBorrowInfoByOrderNo(orderNo);
            if (detailList != null && detailList.size() > 0) {
                OrderInfo orderInfo = detailList.get(0);
                BeanUtils.copyProperties(orderInfo, borrowInfo);
                LOGGER.info("get borrowDetail success and result is:" + JSON.toJSONString(borrowInfo));
                LOGGER.info("get OrderInfo success and result is:" + JSON.toJSONString(orderInfo));
                LOGGER.info("getLoanTime is before" + orderInfo.getLoanTime());
                LOGGER.info("getLoanTime is after " + DateUtil.dateToString(orderInfo.getLoanTime()));
                borrowInfo.setLoanTime(DateUtil.dateToStringHms(orderInfo.getLoanTime()));
                if(orderInfo.getPayTime()==null){
                    borrowInfo.setPayTime("--");
                }else {
                    borrowInfo.setPayTime(DateUtil.dateToString(orderInfo.getPayTime()));
                }
                if(orderInfo.getPromiseRepaymentTime()==null){
                    borrowInfo.setPromiseRepaymentTime("--");
                }else {
                    borrowInfo.setPromiseRepaymentTime(DateUtil.dateToString(orderInfo.getPromiseRepaymentTime()));
                }
                CommonResponse<BillInfo> common = new CommonResponse<BillInfo>();
                common = zeusForLXYService.queryBillInfoByOrderNO(borrowInfo.getOrderNo());
				if("0".equals(common.getCode())){
				    LOGGER.info(orderNo+"。。。。");
                }else{
				    String billStatus = common.getData().getBillStatus();
				    borrowInfo.setBillStatus(billStatus);
					borrowInfo.setWaitRepayAmount(common.getData().getWaitRepayAmount());
                }
                response.setData(borrowInfo);
            }
            LOGGER.info("get borrow detail success and result is:" + JSON.toJSONString(response));
        } catch (Exception e) {
            LOGGER.error("get orderDetailInfo failed, orderNo = " + orderNo, e);
            response = ResponseData.error(OrderConsts.DB_ERROR_MSG);
        }
        return response;
    }

    @Override
    public ResponseData<BorrowInfo> getBorrowInfoByO(String orderNo) {
        LOGGER.info("begin to get borrow detail , orderNo is " + orderNo);
        ResponseData response = ResponseData.success();
        if (orderNo == null && "".equals(orderNo)) {
            return response;
        }
        BorrowInfo borrowInfo = new BorrowInfo();
        try {
            List<OrderInfo> detailList = orderDao.getBorrowInfoByOrderNo(orderNo);
            if (detailList != null && detailList.size() > 0) {
                OrderInfo orderInfo = detailList.get(0);
                BeanUtils.copyProperties(orderInfo, borrowInfo);
                borrowInfo.setLoanTime(DateUtil.dateToString(orderInfo.getLoanTime()));

                if(orderInfo.getPayTime()==null){
                    borrowInfo.setPayTime("--");
                }else {
                    borrowInfo.setPayTime(DateUtil.dateToString(orderInfo.getPayTime()));
                }
                if(orderInfo.getPromiseRepaymentTime()==null){
                    borrowInfo.setPromiseRepaymentTime("--");
                }else {
                    borrowInfo.setPromiseRepaymentTime(DateUtil.dateToString(orderInfo.getPromiseRepaymentTime()));
                }
                response.setData(borrowInfo);
            }
            LOGGER.info("get borrow detail success and result is:" + JSON.toJSONString(response));
        } catch (Exception e) {
            LOGGER.error("get orderDetailInfo failed, orderNo = " + orderNo, e);
            response = ResponseData.error(OrderConsts.DB_ERROR_MSG);
        }
        return response;
    }

   @Override
    public ResponseData<List<BorrowInfo>> getBorrowInfoAll(String userId) {
        LOGGER.info("begin to get borrow detail , userId is " + userId);
        ResponseData response = ResponseData.success();
        if (userId == null && "".equals(userId)) {
            return response;
        }
        try{
            String sql = "select * from t_order where user_id='%s' and delete_flag=0 and (order_status=50 or order_status=1200 or order_status=40) Order By loan_time desc";
            List<OrderInfo> orderList = orderSqlService.queryT(String.format(sql, userId),OrderInfo.class);
            //List<OrderInfo> orderList = orderDao.getBorrowInfoByUserId(userId);
            if (orderList!=null  && orderList.size() > 0){
                List<BorrowInfo> borrowInfoList = new ArrayList<>();
                for (OrderInfo orderInfo : orderList) {
                    BorrowInfo borrowInfo = new BorrowInfo();
                    BeanUtils.copyProperties(orderInfo, borrowInfo);
                    borrowInfo.setLoanTime(DateUtil.dateToString(orderInfo.getLoanTime()));
                    if(orderInfo.getPayTime()==null){
                        borrowInfo.setPayTime("--");
                    }else {
                        borrowInfo.setPayTime(DateUtil.dateToString(orderInfo.getPayTime()));
                    }
                    if(orderInfo.getPromiseRepaymentTime()==null){
                        borrowInfo.setPromiseRepaymentTime("--");
                    }else {
                        borrowInfo.setPromiseRepaymentTime(DateUtil.dateToString(orderInfo.getPromiseRepaymentTime()));
                    }
                    borrowInfoList.add(borrowInfo);
                }
                response.setData(borrowInfoList);
            }
            LOGGER.info("get borrow record success and result is:" + JSON.toJSONString(response));
        }catch (Exception e){
            LOGGER.error("get order record error! userId = " + userId, e);
            response = ResponseData.error(OrderConsts.DB_ERROR_MSG);
        }
        return response;
    }

    public ResponseData confirmMoney(String orderNo){
        ResponseData responseData = new ResponseData();
        try {
            if (redisTemplate.hasKey(NYD_REDIS_PREFIX + orderNo)) {
                LOGGER.info("提现redis有值" + orderNo);
                return ResponseData.error("不能重复提交");
            } else {
                redisTemplate.opsForValue().set(NYD_REDIS_PREFIX + orderNo, "1", 70, TimeUnit.SECONDS);
            }

            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseData;
    }

	@Override
	public ResponseData checkOrderSuccess(User user) {
		ResponseData data =new ResponseData<>();
		OrderInfo order  = new OrderInfo();
		try {
			order = orderDao.getCheckOrderSuccess(user.getUserId());
			if(order!=null) {
				return data.success(order);
			}else {
				return data.error("该用户没有审核通过的订单");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return data.error("该用户没有审核通过的订单");
		}
		
	}

}
