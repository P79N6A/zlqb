package com.nyd.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.capital.api.service.QcgzApi;
import com.nyd.capital.model.qcgz.SubmitAssetRequest;
import com.nyd.capital.model.qcgz.SubmitAssetResponse;
import com.nyd.member.api.MemberContract;
import com.nyd.member.model.MemberModel;
import com.nyd.order.api.OrderContract;
import com.nyd.order.api.zzl.OrderSqlService;
import com.nyd.order.dao.*;
import com.nyd.order.dao.mapper.OrderMapper;
import com.nyd.order.dao.mapper.WithholdOrderMapper;
import com.nyd.order.entity.*;
import com.nyd.order.model.JudgeInfo;
import com.nyd.order.model.JudgeMemberInfo;
import com.nyd.order.model.KzjrPageInfo;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.WithholdOrderInfo;
import com.nyd.order.model.WithholdTaskConfig;
import com.nyd.order.model.dto.BorrowConfirmDto;
import com.nyd.order.model.enums.BorrowConfirmChannel;
import com.nyd.order.model.enums.OrderStatus;
import com.nyd.order.model.msg.OrderMessage;
import com.nyd.order.model.vo.BorrowConfirmVo;
import com.nyd.order.service.NewOrderInfoService;
import com.nyd.order.service.OrderInfoService;
import com.nyd.order.service.consts.OrderConsts;
import com.nyd.order.service.rabbit.OrderToAuditProducer;
import com.nyd.order.service.rabbit.OrderToPayProducer;
import com.nyd.order.service.util.DateUtil;
import com.nyd.order.service.util.OrderProperties;
import com.nyd.product.api.ProductContract;
import com.nyd.product.model.ProductInfo;
import com.nyd.user.api.UserAccountContract;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.model.UserInfo;
import com.nyd.zeus.api.BillContract;
import com.nyd.zeus.model.RemitInfo;
import com.tasfe.framework.support.model.ResponseData;
import com.tasfe.framework.uid.service.BizCode;
import com.tasfe.framework.uid.service.IdGenerator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dengw on 2017/11/14
 */
@Service(value = "orderContract")
public class OrderContractImpl implements OrderContract {
    private static Logger LOGGER = LoggerFactory.getLogger(OrderContractImpl.class);

    private static final String YMT_REDIS_PREFIX = "ymtorderp";

    @Autowired
    private OrderProperties orderProperties;

    @Autowired(required = false)
    private BillContract billContract;

    @Autowired
    private NewOrderInfoService orderInfoService;

    @Autowired
    private UserAccountContract userAccountContract;

    @Autowired
    private ProductContract productContract;

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired(required = false)
    private MemberContract memberContract;
    @Autowired
    private TestListDao testListDao;

    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private com.ibank.order.api.OrderContract orderContractYmt;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderDetailDao orderDetailDao;

    @Autowired
    private OrderStatusLogDao orderStatusLogDao;
    @Autowired(required = false)
    private UserIdentityContract userIdentityContract;

    @Autowired
    private OrderToAuditProducer orderToAuditProducer;

    @Autowired
    private OrderToPayProducer orderToPayProducer;

    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private OrderSqlService orderSqlService;

    @Autowired
	PocketOrderDao pocketOrderDao;
    @Autowired
    private WithholdOrderMapper withholdOrderDao;
    @Autowired
    private WithholdTaskConfigDao withholdTaskConfigDao;
    @Autowired
    private WithholdOrderDao withholdDao;

    @Override
    public ResponseData<OrderInfo> getOrderByOrderNo(String orderNo) {
        LOGGER.info("api begin to get order, orderNo is " + orderNo);
        ResponseData responseData = ResponseData.success();
        try {
            List<OrderInfo> orderList = orderDao.getObjectsByOrderNo(orderNo);
            if (orderList != null && orderList.size() > 0) {
                responseData.setData(orderList.get(0));
            }
            LOGGER.info("api get order by orderNo success !");
        } catch (Exception e) {
            LOGGER.error("api get order by orderNo error! orderNo = " + orderNo, e);
            responseData = ResponseData.error(OrderConsts.DB_ERROR_MSG);
        }
        return responseData;
    }

    @Override
    public ResponseData<WithholdOrderInfo> getWithholdOrderByMemberId(String memberId) {
        LOGGER.info("api begin to get withholdOrderInfo, memberId is " + memberId);
        ResponseData responseData = ResponseData.success();
        try {
            List<WithholdOrder> objectsByMemberId = withholdOrderDao.getObjectsByMemberId(memberId);
            if (objectsByMemberId != null && objectsByMemberId.size() > 0) {
                WithholdOrder withholdOrder = objectsByMemberId.get(0);
                WithholdOrderInfo withholdOrderInfo = new WithholdOrderInfo();
                BeanUtils.copyProperties(withholdOrder, withholdOrderInfo);
                responseData.setData(withholdOrderInfo);
            }
            LOGGER.info("api get withholdOrderInfo by memberId success !");
        } catch (Exception e) {
            LOGGER.error("api get withholdOrderInfo by memberId error! orderNo = " + memberId, e);
            responseData = ResponseData.error(OrderConsts.DB_ERROR_MSG);
        }
        return responseData;
    }

    @Override
    public ResponseData<List<OrderInfo>> getOrdersByUserId(String userId) {
        LOGGER.info("api begin to get order, userId is " + userId);
        ResponseData responseData = ResponseData.success();
        try {
            List<OrderInfo> orderList = orderDao.getObjectsByUserId(userId);
            if (orderList != null && orderList.size() > 0) {
                responseData.setData(orderList);
            }
            LOGGER.info("api get order by userId success !");
        } catch (Exception e) {
            LOGGER.error("api get orders by userId error! userId = " + userId, e);
            responseData = ResponseData.error(OrderConsts.DB_ERROR_MSG);
        }
        return responseData;
    }

    /**
     * 判断是否存在审核中，待放款的订单，100为可借款
     * 状态小于40，不可借款
     * 存在未还清订单，不可借款
     * 状态1000，间隔未超过7天。不可借款
     * 状态1000，间隔超过7天。可借款
     * 不存在未还清订单，可借款
     *
     * @param userId
     */
    @Override
    public ResponseData<JudgeInfo> judgeBorrow(String userId, boolean task) {
        LOGGER.info("begin to Judge borrow, userId is " + userId);
        ResponseData responseData = ResponseData.success();
        try {
            JudgeInfo judgeInfo = new JudgeInfo();
            List<OrderInfo> orderList = orderDao.getLastOrderByUserId(userId);
            if (orderList != null && orderList.size() > 0) {
                OrderInfo orderInfo = orderList.get(0);
                //状态小于40，不可借款
                if ((!task) && orderInfo.getOrderStatus() < 40) {
                    judgeInfo.setWhetherLoan("1");
                    judgeInfo.setWhetherLoanMsg("您有一笔借款在审核中！");
                } else if (orderInfo.getOrderStatus() == 40) {
                    //放款失败，可借款
                    judgeInfo.setWhetherLoan("0");
                } else if (orderInfo.getOrderStatus() == 50) {
                    //放款成功，判断账单是否还清
                    int billCount = billContract.getBillInfos(userId).getData();
                    if (billCount > 0) {
                        judgeInfo.setWhetherLoan("1");
                        judgeInfo.setWhetherLoanMsg("您有未还清的借款！");
                    } else {
                        //账单已还清，可借款
                        judgeInfo.setWhetherLoan("0");
                    }
                } else if (orderInfo.getOrderStatus() == 1000) {
                    //审核拒绝，判断间隔是否超过7天
                    int days = DateUtil.getDayDiffDown(orderInfo.getLoanTime(), new Date());
                    int intervalDays = Integer.valueOf(orderProperties.getIntervalDays());
                    if (days >= intervalDays) {
                        judgeInfo.setWhetherLoan("0");
                    } else {
                    	judgeInfo.setWhetherLoan("1");
                        judgeInfo.setWhetherLoanMsg("您有已存在的订单，请点击首页进度查询查看");

                    }
                }
            } else {
                //没有订单，表示第一次借款，可以借款
                judgeInfo.setWhetherLoan("0");
            }
            responseData.setData(judgeInfo);
            LOGGER.info("judge borrow result is " + JSONObject.toJSONString(judgeInfo));
        } catch (Exception e) {
            LOGGER.error("judge borrow error! userId = " + userId, e);
            responseData = ResponseData.error(OrderConsts.DB_ERROR_MSG);
        }
        return responseData;
    }

    @Override
    public ResponseData<JudgeInfo> judgeBorrowNew(String userId, String appName) {
        LOGGER.info("begin to Judge borrownew, userId is " + userId +" ,appName is "+appName);
        ResponseData responseData = ResponseData.success();
        if (StringUtils.isBlank(appName)) {
            appName = "nyd";
        }
        try {
            JudgeInfo judgeInfo = new JudgeInfo();
            List<OrderInfo> orderList = orderDao.getLastOrderByUserId(userId);
            if (orderList != null && orderList.size() > 0) {
                OrderInfo orderInfo = orderList.get(0);
                if (orderInfo!=null) {
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

//                    }else if(orderInfo.getOrderStatus() == OrderStatus.WAIT_LOAN.getCode()){
//                        judgeInfo.setWhetherLoan("5");
//                        judgeInfo.setWhetherLoanMsg("您有已存在的订单，请点击首页进度查询查看");
                    } else if (OrderStatus.LOAN_FAIL.getCode().toString().equals(orderInfo.getOrderStatus().toString()) ) {
                        //放款失败，可借款
                        if (days<intervalDays) {
                            int failDays = DateUtil.getDayDiffDown(orderInfo.getLoanFailTime(), new Date());
                            int daysLeft = intervalDays - failDays;
                            judgeInfo.setOrderFlag("5");
                            judgeInfo.setWhetherLoan("1");
                            judgeInfo.setWhetherLoanMsg("您有已有存在订单，请" +daysLeft +"天后重新申请");
                        } else {
                            judgeInfo.setWhetherLoan("0");
                        }
                    } else if (OrderStatus.LOAN_SUCCESS.getCode().toString().equals(orderInfo.getOrderStatus().toString())) {
                        //还款中
//                        int billCount = billContract.getBillInfos(userId).getData();
//                        if (billCount > 0) {
                            judgeInfo.setWhetherLoan("1");
                            judgeInfo.setWhetherLoanMsg("您有未还清的借款");
                            judgeInfo.setOrderFlag("3");
//                        } else {
//                            //账单已还清，可借款
//                            judgeInfo.setWhetherLoan("0");
//                        }
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
                }
            } else {
                //没有订单，表示第一次借款，可以借款
                judgeInfo.setWhetherLoan("0");
            }
            responseData.setData(judgeInfo);
            LOGGER.info("judge borrownew result is " + JSONObject.toJSONString(judgeInfo));
        } catch (Exception e) {
            LOGGER.error("judge borrownew error! userId = " + userId, e);
            responseData = ResponseData.error(OrderConsts.DB_ERROR_MSG);
        }
        return responseData;
    }

    /**
     * 借款确认
     *
     * @param borrowConfirmDto BorrowConfirmDto
     * @return ResponseData<BaseInfo>
     */
    @Override
    public ResponseData<BorrowConfirmVo> borrowInfoConfirm(BorrowConfirmDto borrowConfirmDto) {
        LOGGER.info("outer begin call borrowInfoConfirm(), BorrowConfirmDto.UserId is:" + borrowConfirmDto.getUserId() + ", BorrowConfirmDto.IbankOrderNo:" + borrowConfirmDto.getIbankOrderNo());
        LOGGER.info("outer传入的参数" + JSON.toJSONString(borrowConfirmDto));
        if (redisTemplate.hasKey(YMT_REDIS_PREFIX + borrowConfirmDto.getUserId())) {
            LOGGER.info("outer 有值" + borrowConfirmDto.getUserId());
            ResponseData r = ResponseData.error("不能重复提交");
            r.setData(null);
            return r;
        } else {
            redisTemplate.opsForValue().set(YMT_REDIS_PREFIX + borrowConfirmDto.getUserId(), "1", 70, TimeUnit.SECONDS);
        }
        ResponseData responseData = ResponseData.success();
        try {
            // 根据account查询用户信息
            List<String> accounts = new ArrayList<>();
            accounts.add(borrowConfirmDto.getAccountNumber());
            ResponseData<Set<String>> userIds = this.userAccountContract.queryAccountByAccountList(accounts);
            LOGGER.info("根据account查询nyd userId结果为" + JSON.toJSONString(userIds));
            // 无法读取到用户
            if ("1".equals(userIds.getStatus()) || userIds.getData() == null || userIds.getData().size() == 0) {
                LOGGER.error("borrowInfoConfirm error! Can't find account by AccountNumber. BorrowConfirmDto.AccountNumber is:" + borrowConfirmDto.getAccountNumber() + ", BorrowConfirmDto.IbankOrderNo:" + borrowConfirmDto.getIbankOrderNo());
                responseData = ResponseData.error("没有获取的用户id");
                return responseData;
            }

            String userId = userIds.getData().iterator().next();

            borrowConfirmDto.setUserId(userId);

            // 根据productCode获取商品信息
            ResponseData<ProductInfo> productInfoRet = this.productContract.getProductInfo(borrowConfirmDto.getProductCode());

            if ("1".equals(productInfoRet.getStatus()) || productInfoRet.getData() == null) {
                LOGGER.error("borrowInfoConfirm error! Can't find product by ProductCode. BorrowConfirmDto.ProductCode is:" + borrowConfirmDto.getProductCode() + ", BorrowConfirmDto.IbankOrderNo:" + borrowConfirmDto.getIbankOrderNo());
                responseData = ResponseData.error(OrderConsts.NO_PRODUCT);
                return responseData;
            }
            ProductInfo productInfo = productInfoRet.getData();
            borrowConfirmDto.setInterest(productInfo.getInterestRate().multiply(BigDecimal.valueOf(borrowConfirmDto.getBorrowTime())).multiply(borrowConfirmDto.getLoanAmount()).divide(new BigDecimal(100)));
            borrowConfirmDto.setAnnualizedRate(productInfo.getInterestRate().multiply(BigDecimal.valueOf(360)));
            borrowConfirmDto.setRealLoanAmount(borrowConfirmDto.getLoanAmount());
            borrowConfirmDto.setRepayTotalAmount(borrowConfirmDto.getLoanAmount().add(borrowConfirmDto.getInterest()));

            // 调用订单确认接口
            responseData = orderInfoService.newBorrowInfoConfirm(borrowConfirmDto);
            BorrowConfirmVo borrowConfirmVo = new BorrowConfirmVo();
            if ("0".equals(responseData.getStatus())) {
                Order data = (Order) responseData.getData();
                borrowConfirmVo.setOrderNo(data.getOrderNo());
                borrowConfirmVo.setFundCode(data.getFundCode());
                LOGGER.info("outer 调用borrowInfoConfirm返回的结果" + JSON.toJSONString(borrowConfirmVo));
                return (ResponseData<BorrowConfirmVo>) ResponseData.success(borrowConfirmVo);
            }

            LOGGER.info("outer 调用borrowInfoConfirm返回的结果" + JSON.toJSONString(responseData));

            return responseData;
        } catch (Exception e) {
            LOGGER.error("borrowInfoConfirm error! BorrowConfirmDto.AccountNumber is:" + borrowConfirmDto.getAccountNumber() + ", BorrowConfirmDto.IbankOrderNo:" + borrowConfirmDto.getIbankOrderNo(), e);
            responseData = ResponseData.error(OrderConsts.DB_ERROR_MSG);
            return responseData;
        }
    }

    public ResponseData borrowConfirmQuery(String userIdOuter, String accountNumber) {

        try {
            LOGGER.info("outer查询的外部userId为" + userIdOuter + ",accountNumber为" + accountNumber);
            ResponseData responseData = ResponseData.success();
            JSONObject obj = new JSONObject();

            List<String> accounts = new ArrayList<>();
            accounts.add(accountNumber);
            ResponseData<Set<String>> userIds = this.userAccountContract.queryAccountByAccountList(accounts);
            LOGGER.info("borrowConfirmQuery查询nyd userId结果为" + JSON.toJSONString(userIds));
            // 无法读取到用户
            if ("1".equals(userIds.getStatus()) || userIds.getData() == null || userIds.getData().size() == 0) {
                LOGGER.error("borrowInfoConfirm error! Can't find account by AccountNumber. BorrowConfirmDto.AccountNumber is:" + accountNumber);
                responseData = ResponseData.error(OrderConsts.DB_ERROR_MSG);
                return responseData;
            }

            String userId = userIds.getData().iterator().next();

            if (StringUtils.isNotBlank(userId)) {
                String status = (String) redisTemplate.opsForValue().get(OrderConsts.REDIS_LOAN_ACCOUNT_STATUS + userId);
                LOGGER.info("前端查询status" + status);
                if (status == null) {
                    obj.put("result", "3");
                } else {
                    if (status.contains("_")) {
                        String[] statusSplist = status.split("_");
                        obj.put("result", statusSplist[0]);
                        obj.put("orderNo", statusSplist[1]);
                    } else {
                        obj.put("result", status);
                    }

                }
                responseData.setData(obj);

                LOGGER.info("outer页面查询开户状态返回的结果" + JSON.toJSONString(responseData));
                return responseData;

            } else {
                obj.put("result", 4);
                responseData.setData(obj);
                LOGGER.info("页面查询开户状态返回的结果" + JSON.toJSONString(responseData));
                return responseData;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.error("报异常了");
        }


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseData<KzjrPageInfo> kzjrPageErrorGenerateOrder(String userId) throws Exception {
        String borrowInfoStr = (String) redisTemplate.opsForValue().get(OrderConsts.REDIS_LOAN_KEY + userId);

        LOGGER.info("ordercontractImpl..." + borrowInfoStr);
        if (borrowInfoStr == null) {
            ResponseData responseData = ResponseData.error();
            responseData.setData(null);
            return responseData;
        }

        if (redisTemplate.hasKey("kzjr2qcgz" + userId)) {
            LOGGER.info("有值" + userId);
            ResponseData r = ResponseData.error("不能重复提交");
            r.setData(null);
            return r;
        } else {
            redisTemplate.opsForValue().set("kzjr2qcgz" + userId, "1", 2880, TimeUnit.MINUTES);
        }

        BorrowConfirmDto borrowConfirmDto = JSONObject.parseObject(borrowInfoStr, BorrowConfirmDto.class);

        String memberId = null;
        String memberType = null;
        BigDecimal memberFee = new BigDecimal(0);
        //判断会员是否有效, 仅限于侬要贷需要判断 Add by Jiawei Cheng 2018-3027 21:51
        //由于侬要贷之前并没有channel字段 所以 判断 channel 是否为null 为null 表示 来源 nyd
        if (borrowConfirmDto.getChannel() == null || BorrowConfirmChannel.NYD.getChannel().equals(borrowConfirmDto.getChannel())) {
            borrowConfirmDto.setChannel(BorrowConfirmChannel.NYD.getChannel());
            if (borrowConfirmDto.getType() == null || "".equals(borrowConfirmDto.getType())) {
                JudgeMemberInfo judgeMember = judgeMember(borrowConfirmDto.getUserId());
                if (!judgeMember.isMemberFlag()) {
                    ResponseData responseData = ResponseData.error(OrderConsts.MEMBER_ERROR);
                    responseData.setData(null);
                    return responseData;
                } else {
                    memberId = judgeMember.getMemberId();
                    memberFee = judgeMember.getMemberFee();
                    memberType = judgeMember.getMemberType();
                }
            }

        }


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
            ResponseData responseData = ResponseData.error(OrderConsts.NO_PRODUCT);
            return responseData;
        }
        String business = productInfo.getBusiness();
        order.setBusiness(business);
        order.setFundCode("qcgz");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +borrowConfirmDto.getBorrowTime());//+1今天的时间加一天
        date = calendar.getTime();
//        order.setPromiseRepaymentTime(date);
        order.setMemberId(memberId);
        order.setMemberFee(memberFee);
//        order.setMemberType(borrowConfirmDto.getType());
        order.setMemberType(memberType);
        order.setRealLoanAmount(borrowConfirmDto.getLoanAmount());
        // ----------- Update by Jiawei Cheng 2018-3-28 14:07 对订单新增保存来源渠道
        if (borrowConfirmDto.getChannel() == null) {
            order.setChannel(BorrowConfirmChannel.NYD.getChannel());
        } else {
            order.setChannel(borrowConfirmDto.getChannel());
        }
        if (borrowConfirmDto.getChannel() != null && BorrowConfirmChannel.YMT.getChannel().equals(borrowConfirmDto.getChannel())) {
            order.setIbankOrderNo(borrowConfirmDto.getIbankOrderNo());
//            order.setInterest(borrowConfirmDto.getLoanAmount().divide(new BigDecimal(500)).multiply(new BigDecimal(0.5)).multiply(new BigDecimal(borrowConfirmDto.getBorrowTime())));
            //利率修改
            order.setInterest(productInfo.getInterestRate().multiply(BigDecimal.valueOf(borrowConfirmDto.getBorrowTime())).multiply(borrowConfirmDto.getLoanAmount()).divide(new BigDecimal(100)));
            order.setRepayTotalAmount(borrowConfirmDto.getLoanAmount().add(order.getInterest()));
            order.setOrderStatus(OrderStatus.WAIT_LOAN.getCode());//银码头 不通过审核 直接 待放款。
            order.setAuditStatus("1");
        }
        // -----------

        //保存订单
        try {
            orderDao.save(order);
            if (order.getChannel().equals(BorrowConfirmChannel.YMT.getChannel())) {
                orderContractYmt.updateOrderDetailStatus(borrowConfirmDto.getIbankOrderNo(), OrderStatus.WAIT_LOAN.getCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("save order failed  or dubbo update fail , orderNo = " + orderNo, e);
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
        //调用户系统rpc接口获取用户信息，保存至订单详情
        UserInfo userInfo = userIdentityContract.getUserInfo(borrowConfirmDto.getUserId()).getData();
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
                LOGGER.error("save orderStatusLog failed, orderNo = " + orderNo, e);
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
                LOGGER.error("save orderStatusLog failed, orderNo = " + orderNo, e);
                throw e;
            }
        }

        //往放款发送消息
        OrderMessage msg = new OrderMessage();
        msg.setUserId(borrowConfirmDto.getUserId());
        msg.setOrderNo(orderNo);
        msg.setProductType("nyd");
        msg.setBorrowType(productInfo.getProductType().toString());
        msg.setFundCode(order.getFundCode());

        // 如果是银码头，则发送放款消息
        LOGGER.info("传入的渠道" + borrowConfirmDto.getChannel());
        if (borrowConfirmDto.getChannel() != null && BorrowConfirmChannel.YMT.getChannel().equals(borrowConfirmDto.getChannel())) {
            msg.setChannel(BorrowConfirmChannel.YMT.getChannel());
            LOGGER.info("银码头");
            orderToPayProducer.sendMsg(msg); // 发送到放款
        } else {
            msg.setChannel(BorrowConfirmChannel.NYD.getChannel());
            LOGGER.info("侬要贷");
            orderToAuditProducer.sendMsg(msg); // 发送到审核
        }


        LOGGER.info("borrow confirm success, userId is " + borrowConfirmDto.getUserId() + ", orderNo is " + orderNo);

        KzjrPageInfo kzjrPageInfo = new KzjrPageInfo();
        kzjrPageInfo.setOrderNo(orderNo);
        kzjrPageInfo.setBorrowTime(borrowConfirmDto.getBorrowTime() + "");
        kzjrPageInfo.setLoanAmount(borrowConfirmDto.getLoanAmount().toString());
        kzjrPageInfo.setUserId(borrowConfirmDto.getUserId());
        kzjrPageInfo.setChannel(borrowConfirmDto.getChannel());

        ResponseData result = ResponseData.success();
        result.setData(kzjrPageInfo);
        return result;


    }


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
            LOGGER.error("get member info error! userId = " + userId, e);
        }
        LOGGER.info("judge member result is" + memberFlag);
        memberInfo.setMemberFlag(memberFlag);
        return memberInfo;
    }

    /**
     * 判断是否为测试人员
     *
     * @param mobile
     * @return boolean
     */
    private boolean judgeTestUserFlag(String mobile) {
        LOGGER.info("begin to judge testUser Flag, mobile is " + mobile);
        boolean testFlag = false;
        try {
            List<InnerTest> list = testListDao.getObjectsByMobile(mobile);
            if (list != null && list.size() > 0) {
                testFlag = true;
            }
        } catch (Exception e) {
            LOGGER.error("get innerTest list error! mobile = " + mobile, e);
        }
        LOGGER.info("judge testUser Flag result is" + testFlag);
        return testFlag;
    }

    /**
     * 判断借款时间是否在规定时间内，规定时间内不可多次借款
     *
     * @param
     * @return boolean
     */
    /*private boolean judgeLoanTime(Date loanTime) {
        LOGGER.info("begin to Judge loan Time, loanTime is " + loanTime);
        boolean judgeFlag = false;
        try {
            int days = DateUtil.getDayDiff(loanTime,new Date());
            int intervalDays = Integer.valueOf(orderProperties.getIntervalDays());
            if(days > intervalDays){
                judgeFlag = true;
            }
            LOGGER.info("judge loanTime result is " + judgeFlag);
        } catch (Exception e) {
            LOGGER.error("judge loanTime error! loanTime = " + loanTime, e);
        }
        return judgeFlag;
    }*/
    @Override
    public ResponseData updateOrderInfo(OrderInfo orderInfo) {
        LOGGER.info("updateOrderInfo start,param is " + JSON.toJSONString(orderInfo));
        ResponseData responseData = ResponseData.success();
        if (null == orderInfo || StringUtils.isBlank(orderInfo.getOrderNo())) {
            responseData = ResponseData.error("参数异常");
            return responseData;
        }
        try {
            orderDao.update(orderInfo);
        } catch (Exception e) {
            LOGGER.info("updateOrderInfo exception,orderNo :" + orderInfo.getOrderNo(), e);
            responseData = ResponseData.error("服务器开小差");
        }
        return responseData;
    }

    @Override
    public ResponseData queryOrdersWhenOrderStatusIsWait(String fundCode) {
        LOGGER.info("根据资产渠道查询待放款的订单，fundCode is " + fundCode);
        try {
            List<OrderInfo> orderInfos = orderMapper.queryOrdersWhenOrderStatusIsWait(fundCode);
            return ResponseData.success(orderInfos);
        } catch (Exception e) {
            LOGGER.error("根据资产渠道查询待放款的订单发生异常",e);
            return ResponseData.error("根据资产渠道查询待放款的订单发生异常");
        }
    }

    @Override
    public OrderInfo getOrderByIbankOrderNo(String orderNo) {
        LOGGER.info("根据银码头订单号查询侬要贷的订单号，orderNo is " + orderNo);
        try {
            OrderInfo orderInfo = orderMapper.getOrderByIbankOrderNo(orderNo);
            return orderInfo;
        } catch (Exception e) {
            LOGGER.error("根据银码头订单号查询侬要贷的订单号发生异常",e);
            return null;
        }
    }
    @Override
    public OrderInfo getOrderInfoByOrderNo(String orderNo) {
        LOGGER.info("根据贷款编号查询侬要贷的订单号，orderNo is " + orderNo);
        try {
           // List<OrderInfo> orderInfoList = orderMapper.getOrderInfoByOrderNo(orderNo);
            
            String sql = "select * from t_order where order_no='%s'";
            List<OrderInfo> orderInfoList = orderSqlService.queryT(String.format(sql, orderNo), OrderInfo.class);
            if(orderInfoList!=null&&orderInfoList.size()!=0){
                return orderInfoList.get(0);
            }
            return null;
        } catch (Exception e) {
            LOGGER.error("根据贷款编号查询侬要贷的订单号发生异常",e.getMessage());
            return null;
        }
    }
	/*
	* Title: getByUserId
	* Description:
	* @param userId
	* @return
	*/
	@Override
	public ResponseData<PockerOrderEntity> getByUserId(String orderNo) {
		LOGGER.info("获取口袋订单 start, orderNo:{}",orderNo);
		ResponseData responseData =ResponseData.success();
		try {
			List<PockerOrderEntity> pockorders =pocketOrderDao.find(orderNo);
			if(null!=pockorders && pockorders.size()>0) {
				responseData.setData(pockorders.get(0));
			}
		} catch (Exception e) {
			LOGGER.error("获取口袋订单 exception, orderNo:{}",orderNo,e);
			responseData =ResponseData.error("服务器开小差");
		}
		return responseData;
	}

	/*
	* Title: savePockerOrderEntity
	* Description:
	* @param pockerOrderEntity
	* @return
	*/
	@Override
	public ResponseData savePockerOrderEntity(PockerOrderEntity pockerOrderEntity) {
		LOGGER.info("savePockerOrderEntity start, pockerOrderEntity:{}",JSON.toJSONString(pockerOrderEntity));
		ResponseData responseData =ResponseData.success();
		if(null==pockerOrderEntity || StringUtils.isBlank(pockerOrderEntity.getOrderNo())) {
			responseData = ResponseData.error("参数异常");
			return responseData;
		}
		try {
			pocketOrderDao.save(pockerOrderEntity);
		} catch (Exception e) {
			LOGGER.error("savePockerOrderEntity exception, orderNo:{}",pockerOrderEntity.getOrderNo(),e);
			responseData =ResponseData.error("服务器开小差");
		}
		return responseData;
	}

	/*
	* Title: updatePockerOrderEntity
	* Description:
	* @param pockerOrderEntity
	* @return
	*/
	@Override
	public ResponseData updatePockerOrderEntity(PockerOrderEntity pockerOrderEntity) {
		LOGGER.info("updatePockerOrderEntity start, pockerOrderEntity:{}",JSON.toJSONString(pockerOrderEntity));
		ResponseData responseData =ResponseData.success();
		if(null==pockerOrderEntity || StringUtils.isBlank(pockerOrderEntity.getOrderNo())) {
			responseData = ResponseData.error("参数异常");
			return responseData;
		}
		try {
			pocketOrderDao.updateByOrderNo(pockerOrderEntity);
		} catch (Exception e) {
			LOGGER.error("updatePockerOrderEntity exception, orderNo:{}",pockerOrderEntity.getOrderNo(),e);
			responseData =ResponseData.error("服务器开小差");
		}
		return responseData;
	}

//<<<<<<< HEAD
//    @Override
//    public ResponseData<PockerOrderEntity> getPocketOrderByOrderNo(String orderNo) {
//        LOGGER.info("获取口袋订单 start, orderNo:{}",orderNo);
//        ResponseData responseData =ResponseData.success();
//        try {
//            List<PockerOrderEntity> pockorders =pocketOrderDao.findByOrderNo(orderNo);
//            if(null!=pockorders && pockorders.size()>0) {
//                responseData.setData(pockorders.get(0));
//            }
//        } catch (Exception e) {
//            LOGGER.error("获取口袋订单 exception, orderNo:{}",orderNo,e);
//            responseData =ResponseData.error("服务器开小差");
//        }
//        return responseData;
//    }
//
//    @Override
//    public ResponseData<WithholdTaskConfig> selectTaskTime() {
//	    LOGGER.info("获取代扣任务开始时间");
//        ResponseData responseData =ResponseData.success();
//        try {
//            List<WithholdTaskConfig> select = withholdTaskConfigDao.select();
//            WithholdTaskConfig config = select.get(0);
//            responseData.setData(config);
//        }catch (Exception e){
//            LOGGER.error("获取代扣任务开始时间发生异常");
//            responseData =ResponseData.error("获取代扣任务开始时间发生异常");
//        }
//        return responseData;
//    }
//
//    @Override
//    public ResponseData updateTaskTime(Date startTime) {
//        LOGGER.info("更新代扣任务开始时间");
//        ResponseData responseData =ResponseData.success();
//        try {
//            withholdTaskConfigDao.update(startTime);
//        }catch (Exception e){
//            LOGGER.error("更新代扣任务开始时间发生异常");
//            responseData =ResponseData.error("更新代扣任务开始时间发生异常");
//        }
//        return responseData;
//    }
//
//
//=======
	@Override
	public ResponseData<PockerOrderEntity> getPockerOrderEntityByPocketNo(String pocketNo) {
		LOGGER.info("获取口袋订单 start, pocketNo:{}",pocketNo);
		ResponseData responseData =ResponseData.success();
		try {
			List<PockerOrderEntity> pockorders =pocketOrderDao.getPockerOrderEntityByPocketNo(pocketNo);
			if(null!=pockorders && pockorders.size()>0) {
				responseData.setData(pockorders.get(0));
			}
		} catch (Exception e) {
			LOGGER.error("获取口袋订单 exception, pocketNo:{}",pocketNo,e);
			responseData =ResponseData.error("服务器开小差");
		}
		return responseData;
	}

	@Override
	public ResponseData<PockerOrderEntity> getPocketOrderByOrderNo(String orderNo) {
//		LOGGER.info("获取口袋订单 start, orderNo:{}",orderNo);
//        ResponseData responseData =ResponseData.success();
//        try {
//            List<PockerOrderEntity> pockorders =pocketOrderDao.findByOrderNo(orderNo);
//            if(null!=pockorders && pockorders.size()>0) {
//                responseData.setData(pockorders.get(0));
//            }
//        } catch (Exception e) {
//            LOGGER.error("获取口袋订单 exception, orderNo:{}",orderNo,e);
//            responseData =ResponseData.error("服务器开小差");
//        }
        return null;
	}

	@Override
	public ResponseData<WithholdTaskConfig> selectTaskTime() {
		LOGGER.info("获取代扣任务开始时间");
        ResponseData responseData =ResponseData.success();
        try {
            List<WithholdTaskConfig> select = withholdTaskConfigDao.select();
            WithholdTaskConfig config = select.get(0);
            responseData.setData(config);
        }catch (Exception e){
            LOGGER.error("获取代扣任务开始时间发生异常");
            responseData =ResponseData.error("获取代扣任务开始时间发生异常");
        }
        return responseData;
	}
	@Override
	public ResponseData<WithholdTaskConfig> selectTaskTimeByCode(String code) {
		LOGGER.info("获取代扣任务开始时间");
		ResponseData responseData =ResponseData.success();
		try {
			List<WithholdTaskConfig> select = withholdTaskConfigDao.selectByCode(code);
			WithholdTaskConfig config = select.get(0);
			responseData.setData(config);
		}catch (Exception e){
			LOGGER.error("获取代扣任务开始时间发生异常");
			responseData =ResponseData.error("获取代扣任务开始时间发生异常");
		}
		return responseData;
	}

	@Override
	public ResponseData updateTaskTime(Date startTime) {
		LOGGER.info("更新代扣任务开始时间");
        ResponseData responseData =ResponseData.success();
        try {
            withholdTaskConfigDao.update(startTime);
        }catch (Exception e){
            LOGGER.error("更新代扣任务开始时间发生异常");
            responseData =ResponseData.error("更新代扣任务开始时间发生异常");
        }
        return responseData;
	}
	@Override
	public ResponseData updateTaskTimeByCode(Date startTime,String code) {
		LOGGER.info("更新代扣任务开始时间");
		ResponseData responseData =ResponseData.success();
		try {
			withholdTaskConfigDao.updateByCode(startTime,code);
		}catch (Exception e){
			LOGGER.error("更新代扣任务开始时间发生异常");
			responseData =ResponseData.error("更新代扣任务开始时间发生异常");
		}
		return responseData;
	}
	
	@Override
	public ResponseData getKdlcWaitLoan(String funCode) {
		ResponseData responseData =ResponseData.success();
		if(StringUtils.isBlank(funCode)) {
			responseData = ResponseData.error("参数异常");
			return responseData;
		}		
		try {
			List<OrderInfo>  watiLoanList =orderMapper.getWaitLoan(funCode);
			if(watiLoanList != null && watiLoanList.size() > 0) {
				responseData.setData(watiLoanList);
			}
		}catch(Exception e) {
			 LOGGER.error("查询口袋理财待放款的数据异常",e);
	         return ResponseData.error("查询口袋理财待放款的数据异常");
		}
		return responseData;

	}
	
	@Override
	public List<PockerOrderEntity> taskCreateStatusAllData() {
		try {
			List<PockerOrderEntity> kdlcQueryCreateOrderList = pocketOrderDao.kdlcQueryCreateOrder();
			LOGGER.error("查询所有状态为 创建订单处理中的订单, 总条数: {}{}",kdlcQueryCreateOrderList.size(),"条");
			return kdlcQueryCreateOrderList;
		} catch (Exception e) {
			LOGGER.error("查询所有状态为 创建订单处理中的订单 错误！");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ResponseData<List<OrderInfo>> getLastOrderByUserId(String userId) {
		ResponseData responseData = ResponseData.success();
		 try {
			 List<OrderInfo> orderList = orderDao.getLastOrderByUserId(userId);
	            if (orderList != null && orderList.size() > 0) {
	                responseData.setData(orderList);
	            }
	            LOGGER.info("api get order by userId success !");
	        } catch (Exception e) {
	            LOGGER.error("api get orders by userId error! userId = " + userId, e);
	            responseData = ResponseData.error(OrderConsts.DB_ERROR_MSG);
	        }
		return responseData;
	}
	
	@Override
    public ResponseData<WithholdOrderInfo> findWithholdOrderByMemberIdDesc(String memberId) {
        LOGGER.info("api begin to get withholdOrderInfo, memberId is " + memberId);
        ResponseData responseData = ResponseData.success();
        try {
            List<WithholdOrder> objectsByMemberId = withholdOrderDao.findObjectsByMemberIdDesc(memberId);
            if (objectsByMemberId != null && objectsByMemberId.size() > 0) {
                WithholdOrder withholdOrder = objectsByMemberId.get(0);
                WithholdOrderInfo withholdOrderInfo = new WithholdOrderInfo();
                BeanUtils.copyProperties(withholdOrder, withholdOrderInfo);
                responseData.setData(withholdOrderInfo);
            }
            LOGGER.info("api get withholdOrderInfo by memberId success !");
        } catch (Exception e) {
            LOGGER.error("api get withholdOrderInfo by memberId error! orderNo = " + memberId, e);
            responseData = ResponseData.error(OrderConsts.DB_ERROR_MSG);
        }
        return responseData;
    }
	
	 @Override
	    public ResponseData<List<OrderInfo>> getRefusedOrders(Date startTime, Date endTime) {
	        ResponseData responseData = ResponseData.success();
	        List<OrderInfo> successRemit = orderMapper.getRefusedOrders(startTime, endTime);
	        responseData.setData(successRemit);
	        return responseData;
	    }

    @Override
    public ResponseData saveWithholdOrder(WithholdOrder order) {
        try {
            LOGGER.info("order is" + order.toString());
            withholdOrderDao.save(order);
            return ResponseData.success();
        }catch (Exception e){
            LOGGER.error("保存代扣订单发生异常",e);
            return ResponseData.error();
        }
    }
    @Override
    public ResponseData updateWithHoldOrder(WithholdOrder order) {
        try {
            withholdOrderDao.update(order);
            return ResponseData.success();
        }catch (Exception e){
            LOGGER.error("修改代扣订单发生异常",e);
            return ResponseData.error();
        }
    }

    @Override
    public ResponseData selectWithholdOrder(String order) {
        try {
            List<WithholdOrder> objectsPayOrderNo = withholdDao.getObjectsPayOrderNo(order);
            if (objectsPayOrderNo == null || objectsPayOrderNo.size() == 0){
                return null;
            }
            return ResponseData.success(objectsPayOrderNo.get(0));
        }catch (Exception e){
            LOGGER.error("查询代扣订单发生异常",e);
            return ResponseData.error();
        }
    }

    @Override
    public ResponseData selectWithholdIng() {
        try {
            List<WithholdOrder> objectsPayOrderNo = withholdOrderDao.selectStatusOne();
            return ResponseData.success(objectsPayOrderNo);
        }catch (Exception e){
            LOGGER.error("查询代扣订单发生异常",e);
            return ResponseData.error();
        }
    }

}
