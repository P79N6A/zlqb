//package com.nyd.order.service.impl;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.ibank.order.api.OrderContract;
//import com.nyd.capital.api.service.JxApi;
//import com.nyd.capital.api.service.RemitService;
//import com.nyd.capital.model.enums.ResultEnum;
//import com.nyd.capital.model.jx.JxQueryPushStatusRequest;
//import com.nyd.capital.model.jx.JxQueryPushStatusResponse;
//import com.nyd.capital.model.response.KzjrStatusReponse;
//import com.nyd.capital.model.vo.CheckStatusVo;
//import com.nyd.capital.model.vo.OpenAccountVo;
//import com.nyd.member.api.MemberContract;
//import com.nyd.member.model.MemberModel;
//import com.nyd.order.api.OrderChannelContract;
//import com.nyd.order.api.OrderWentongContract;
//import com.nyd.order.dao.*;
//import com.nyd.order.entity.*;
//import com.nyd.order.model.JudgeInfo;
//import com.nyd.order.model.JudgeMemberInfo;
//import com.nyd.order.model.OrderInfo;
//import com.nyd.order.model.OrderStatusLogInfo;
//import com.nyd.order.model.dto.BorrowConfirmDto;
//import com.nyd.order.model.dto.BorrowDto;
//import com.nyd.order.model.enums.BorrowConfirmChannel;
//import com.nyd.order.model.enums.OrderStatus;
//import com.nyd.order.model.msg.OrderMessage;
//import com.nyd.order.model.vo.*;
//import com.nyd.order.service.OrderInfoService;
//import com.nyd.order.service.consts.OrderConsts;
//import com.nyd.order.service.rabbit.OrderToAuditProducer;
//import com.nyd.order.service.rabbit.OrderToPayProducer;
//import com.nyd.order.service.util.DateUtil;
//import com.nyd.order.service.util.OrderProperties;
//import com.nyd.product.api.ProductContract;
//import com.nyd.product.model.ProductInfo;
//import com.nyd.user.api.UserAccountContract;
//import com.nyd.user.api.UserBankContract;
//import com.nyd.user.api.UserIdentityContract;
//import com.nyd.capital.entity.UserJx;
//import com.nyd.user.model.*;
//import com.nyd.zeus.api.BillContract;
//import com.tasfe.framework.support.model.ResponseData;
//import com.tasfe.framework.uid.service.BizCode;
//import com.tasfe.framework.uid.service.IdGenerator;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//
///**
// * Created by dengw on 2017/11/8.
// */
//@Service
//public class OrderServiceInfoImpl implements OrderInfoService {
//    private static Logger LOGGER = LoggerFactory.getLogger(OrderServiceInfoImpl.class);
//
//
//    private static final String NYD_REDIS_PREFIX = "nydorderp";
//
//    @Autowired
//    private OrderDao orderDao;
//
//    @Autowired
//    private BalanceOrderDao balanceOrderDao;
//
//    @Autowired
//    private OrderDetailDao orderDetailDao;
//
//    @Autowired
//    private OrderStatusLogDao orderStatusLogDao;
//
//    @Autowired(required = false)
//    private ProductContract productContract;
//
//    @Autowired(required = false)
//    private UserBankContract userBankContract;
//
//    @Autowired(required = false)
//    private UserIdentityContract userIdentityContract;
//
//    @Autowired(required = false)
//    private UserAccountContract userAccountContract;
//
//    @Autowired(required = false)
//    private MemberContract memberContract;
//
//    @Autowired
//    private IdGenerator idGenerator;
//
//    @Autowired
//    private OrderToAuditProducer orderToAuditProducer;
//
//    @Autowired
//    private OrderToPayProducer orderToPayProducer;
//
//    @Autowired(required = false)
//    private RemitService remitService;
//
//    @Autowired(required = false)
//    private BillContract billContract;
//
//    @Autowired
//    private OrderProperties orderProperties;
//
//    @Autowired
//    private TestListDao testListDao;
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    @Autowired
//    private OrderContract orderContractYmt;
//
//    @Autowired
//    private OrderWentongContract orderWentongContract;
//
//    @Autowired
//    private OrderChannelContract orderChannelContract;
//
//    @Autowired
//    private JxApi jxApi;
//
//
//    /**
//     * 借款信息
//     *
//     * @param borrowDto
//     * @return ResponseData
//     */
//    @Override
//    public ResponseData<BorrowVo> getBorrowInfo(BorrowDto borrowDto) {
//        LOGGER.info("begin to get borrowInfo, userId is " + borrowDto.getUserId());
//        ResponseData response = ResponseData.success();
//        BorrowVo borrowVo = new BorrowVo();
//        borrowVo.setProductCode(borrowDto.getProductCode());
//        borrowVo.setMobile(borrowDto.getAccountNumber());
//        //获取金融产品信息
//        ResponseData<ProductInfo> productResponse = productContract.getProductInfo(borrowDto.getProductCode());
//        if ("0".equals(productResponse.getStatus()) && productResponse.getData() != null) {
//            ProductInfo productInfo = productResponse.getData();
//            borrowVo.setInterestRate(productInfo.getInterestRate());
//            List<FinanceVo> amountList = new ArrayList<>();
//            //组装借款金额list
//            for (BigDecimal i = productInfo.getMinPrincipal(); i.compareTo(productInfo.getMaxPrincipal()) <= 0; ) {
//                FinanceVo vo = new FinanceVo();
//                vo.setValue(String.valueOf(i));
//                vo.setText(vo.getValue() + "元");
//                amountList.add(vo);
//                i = i.add(productInfo.getPrincipalStep());
//            }
//            borrowVo.setAmountList(amountList);
//            List<FinanceVo> dayList = new ArrayList<>();
//            //组装借款天数list
//            for (int i = productInfo.getMinLoanDay(); i <= productInfo.getMaxLoanDay(); i += productInfo.getLoanDayStep()) {
//                FinanceVo vo = new FinanceVo();
//                vo.setValue(String.valueOf(i));
//                vo.setText(vo.getValue() + "天");
//                dayList.add(vo);
//            }
//            borrowVo.setDayList(dayList);
//        }
//        //获取银行卡信息
//        ResponseData<List<BankInfo>> bankResponse = userBankContract.getBankInfos(borrowDto.getUserId());
//        if ("0".equals(bankResponse.getStatus()) && bankResponse.getData() != null) {
//            List<BankInfo> bankInfoList = bankResponse.getData();
//            List<BankVo> bankList = new ArrayList<BankVo>();
//            if (bankInfoList != null & bankInfoList.size() > 0) {
//                for (BankInfo bankInfo : bankInfoList) {
//                    BankVo bank = new BankVo();
//                    BeanUtils.copyProperties(bankInfo, bank);
//                    bankList.add(bank);
//                }
//                //银行卡信息
//                borrowVo.setBankList(bankList);
//            }
//        }
//        //获取会员信息
//        ResponseData<MemberModel> memberResponse = memberContract.getMember(borrowDto.getUserId());
//        if ("0".equals(memberResponse.getStatus()) && memberResponse.getData() != null) {
//            MemberModel model = memberResponse.getData();
//            if (new Date().before(model.getExpireTime())) {
//                borrowVo.setIfAssess(1);
//            } else {
//                borrowVo.setIfAssess(2);
//            }
//            borrowVo.setExpireTime(DateUtil.dateToStringHms(model.getExpireTime()));
//            borrowVo.setMemberExpireDay(DateUtil.getDayDiffUp(new Date(), model.getExpireTime()));
//        } else {
//            borrowVo.setIfAssess(0);
//        }
//        response.setData(borrowVo);
//        LOGGER.info("get borrowInfo success !");
//        return response;
//    }
//
//    /**
//     * 借款确认
//     *
//     * @param borrowConfirmDto BorrowConfirmDto
//     * @param isFirst          boolean 如果第二次认证 checkIfSendSms（）需要重复 10次 获取数据
//     * @return ResponseData
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public ResponseData borrowInfoConfirm(BorrowConfirmDto borrowConfirmDto, boolean isFirst) throws Exception {
//        LOGGER.info("begin to borrow confirm, userId is " + borrowConfirmDto.getUserId());
//
//        String tmp = "";
//        if (isFirst) {
//            tmp = "1";
//        } else {
//            tmp = "0";
//        }
//        if (redisTemplate.hasKey(tmp + NYD_REDIS_PREFIX + borrowConfirmDto.getUserId())) {
//            LOGGER.info("有值" + borrowConfirmDto.getUserId());
//            ResponseData r = ResponseData.error("不能重复提交");
//            r.setData(null);
//            return r;
//        } else {
//            redisTemplate.opsForValue().set(tmp + NYD_REDIS_PREFIX + borrowConfirmDto.getUserId(), "1", 70, TimeUnit.SECONDS);
//        }
//
//        ResponseData response = ResponseData.success();
//
//        redisTemplate.opsForValue().set(OrderConsts.REDIS_LOAN_KEY + borrowConfirmDto.getUserId(), JSON.toJSONString(borrowConfirmDto), 200, TimeUnit.MINUTES);
//
//        //判断是否具有借款条件
//        JudgeInfo judgeInfo = judgeBorrow(borrowConfirmDto.getUserId());
//        if ("1".equals(judgeInfo.getWhetherLoan())) {
//            if ("0".equals(judgeInfo.getUnProcessOrderExist())) {
//                ResponseData responseData = ResponseData.error(judgeInfo.getWhetherLoanMsg());
//                responseData.setCode("1000");//1000 告诉银码头 在侬要贷 有未处理订单
//                return responseData;
//            } else {
//                return ResponseData.error(judgeInfo.getWhetherLoanMsg());
//            }
//        }
//
//        String memberId = null;
//        String memberType = null;
//        BigDecimal memberFee = new BigDecimal(0);
//        //判断会员是否有效, 仅限于侬要贷需要判断 Add by Jiawei Cheng 2018-3027 21:51
//        //由于侬要贷之前并没有channel字段 所以 判断 channel 是否为null 为null 表示 来源 nyd
//        if (borrowConfirmDto.getChannel() == null || BorrowConfirmChannel.NYD.getChannel().equals(borrowConfirmDto.getChannel())) {
//            if (borrowConfirmDto.getType() == null || "".equals(borrowConfirmDto.getType())) {
//                JudgeMemberInfo judgeMember = judgeMember(borrowConfirmDto.getUserId());
//                if (!judgeMember.isMemberFlag()) {
//                    return ResponseData.error(OrderConsts.MEMBER_ERROR);
//                } else {
//                    memberId = judgeMember.getMemberId();
//                    memberFee = judgeMember.getMemberFee();
//                    memberType = judgeMember.getMemberType();
//                }
//            }
//           /* else {
//                //生成会员ID
//                memberId = idGenerator.generatorId(BizCode.MEMBER).toString();
//            }*/
//        }
//        String fundCode = null;
//        if (isFirst) {
//            fundCode = "kzjr";
//            ResponseData<String> fundCodeSwap = orderWentongContract.getChannel();
//            if ("0".equals(fundCodeSwap.getStatus())) {
//                fundCode = fundCodeSwap.getData();
//            }
//        } else {
//            fundCode = "kzjr";
//        }
//        LOGGER.info("放款渠道" + fundCode);
//
//
//        // 空中金融开户、解绑卡、短信验证码（V2）等判断流程
//
//        BorrowConfirmVo borrowConfirmVo = null;
//        boolean valiKzjrSmsIsTrue = true;
//
//        boolean isWt = true;
//        if ("kzjr".equals(fundCode)) {
//            isWt = false;
//            if (borrowConfirmDto.getMsgCode() != null) { // 短信不为空，进行解绑操作
//                if (!P2pProcessAccount(borrowConfirmDto)) { // V3版本为处理空中金融新户开户老户绑卡
//                    valiKzjrSmsIsTrue = false;
////                return ResponseData.error(OrderConsts.SMSCODE_ERROR);
//                }
//            } else { // 短信为空
//
//                //校验是否要发送p2p短信 （空中金融V3包含开户需要跳转的URL地址、和解绑银行卡的短信验证码）
//                borrowConfirmVo = checkIfSendSms(borrowConfirmDto, isFirst);
//
//
////            if("1".equals(borrowConfirmVo.getP2pStatus())){
////                LOGGER.warn("check whether p2p sendSms failed, userId is " + borrowConfirmDto.getUserId());
////                return ResponseData.error(OrderConsts.DB_ERROR_MSG);
////            }else
//                if (isFirst) {
//                    if (0 == borrowConfirmVo.getFlag()) { // 0 发解绑银行卡的验证码
//                        borrowConfirmVo.setP2pSendFlag(true);
//                        response.setData(borrowConfirmVo);
//                        return response;
//                    } else if (1 == borrowConfirmVo.getFlag()) { // 1开户
//                        LOGGER.warn("open account, userId is " + borrowConfirmDto.getUserId());
//                        borrowConfirmVo.setP2pSendFlag(false);
//                        response.setData(borrowConfirmVo);
////                    redisTemplate.opsForValue().set(OrderConsts.REDIS_LOAN_KEY + borrowConfirmDto.getUserId(), JSON.toJSONString(borrowConfirmDto), 100, TimeUnit.MINUTES);
//                        return response;
//                    }
//                }
////            else if(3 == .getFlag()){
////                LOGGER.warn(.getFlag()+"status " + borrowConfirmDto.getUserId());
////                return ResponseData.error("开户中");
////            }
//            }
//        }
//
//        Order order = new Order();
//        Date date = new Date();
//        BeanUtils.copyProperties(borrowConfirmDto, order);
//        //判断是否内部测试人员
//        if (judgeTestUserFlag(borrowConfirmDto.getMobile())) {
//            order.setTestStatus(0);
//        } else {
//            order.setTestStatus(1);
//        }
//        //大户的ID服务，生成订单ID
//        String orderNo = idGenerator.generatorId(BizCode.ORDER_NYD).toString();
//        order.setOrderNo(orderNo);
//        order.setOrderStatus(OrderStatus.AUDIT.getCode());
//        //申请时间
//        order.setLoanTime(date);
//        //获取金融产品信息
//        ProductInfo productInfo = productContract.getProductInfo(borrowConfirmDto.getProductCode()).getData();
//        if (productInfo == null) {
//            LOGGER.error("get fianceProduct failed, ProductCode = " + borrowConfirmDto.getProductCode());
//            return ResponseData.error(OrderConsts.NO_PRODUCT);
//        }
//        String business = productInfo.getBusiness();
//        order.setBusiness(business);
//        //获取资金渠道
//        //String fundCode = productContract.getFundInfo(borrowConfirmDto.getProductCode()).getData();
//
//        if (isWt) {
//            order.setFundCode("wt");
//        } else {
//            if (borrowConfirmVo == null) {
//                if (valiKzjrSmsIsTrue) {
//                    order.setFundCode("kzjr");
//                } else {
//                    order.setFundCode("wt");
//                }
//            } else {
//                if (isFirst) {
//                    if ("1".equals(borrowConfirmVo.getP2pStatus()) || 3 == borrowConfirmVo.getFlag()) {
//                        order.setFundCode("wt");
//                    } else if (2 == borrowConfirmVo.getFlag()) {
//                        order.setFundCode("kzjr");
//                    } else {
//                        order.setFundCode("unknown");
//                    }
//                } else {
//                    if ("1".equals(borrowConfirmVo.getP2pStatus())) {
//                        order.setFundCode("wt");
//                    } else if (2 == borrowConfirmVo.getFlag()) {
//                        order.setFundCode("kzjr");
//                    } else {
//                        order.setFundCode("wt");
//                    }
//                }
//            }
//        }
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        calendar.add(Calendar.DAY_OF_MONTH, +borrowConfirmDto.getBorrowTime());//+1今天的时间加一天
//        date = calendar.getTime();
////        order.setPromiseRepaymentTime(date);
//        order.setMemberId(memberId);
//        order.setMemberFee(memberFee);
////        order.setMemberType(borrowConfirmDto.getType());
//        order.setMemberType(memberType);
//        order.setRealLoanAmount(borrowConfirmDto.getLoanAmount());
//        // ----------- Update by Jiawei Cheng 2018-3-28 14:07 对订单新增保存来源渠道
//        if (borrowConfirmDto.getChannel() == null) {
//            order.setChannel(BorrowConfirmChannel.NYD.getChannel());
//        } else {
//            order.setChannel(borrowConfirmDto.getChannel());
//        }
//        if (borrowConfirmDto.getChannel() != null && BorrowConfirmChannel.YMT.getChannel().equals(borrowConfirmDto.getChannel())) {
//            order.setIbankOrderNo(borrowConfirmDto.getIbankOrderNo());
////            order.setInterest(borrowConfirmDto.getLoanAmount().divide(new BigDecimal(500)).multiply(new BigDecimal(0.5)).multiply(new BigDecimal(borrowConfirmDto.getBorrowTime())));
////            order.setRepayTotalAmount(borrowConfirmDto.getLoanAmount().add(order.getInterest()));
//            order.setOrderStatus(OrderStatus.WAIT_LOAN.getCode());//银码头 不通过审核 直接 待放款。
//            order.setAuditStatus("1");
//        }
//        // -----------
//
//        //保存订单
//        try {
//            orderDao.save(order);
//            if (order.getChannel().equals(BorrowConfirmChannel.YMT.getChannel())) {
//                orderContractYmt.updateOrderDetailStatus(borrowConfirmDto.getIbankOrderNo(), OrderStatus.WAIT_LOAN.getCode());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            LOGGER.error("save order failed  or dubbo update fail , orderNo = " + orderNo, e);
//            throw e;
//        }
//        OrderDetail orderDetail = new OrderDetail();
//        orderDetail.setMobile(borrowConfirmDto.getMobile());
//        orderDetail.setOrderNo(orderNo);
//        BeanUtils.copyProperties(borrowConfirmDto, orderDetail);
//        //金融产品相关信息
//        orderDetail.setProductCode(productInfo.getProductCode());
//        orderDetail.setProductType(productInfo.getProductType());
//        orderDetail.setProductPeriods(productInfo.getProductPeriods());
//        orderDetail.setInterestRate(productInfo.getInterestRate());
//        //调用户系统rpc接口获取用户信息，保存至订单详情
//        UserInfo userInfo = userIdentityContract.getUserInfo(borrowConfirmDto.getUserId()).getData();
//        orderDetail.setIdType(userInfo.getCertificateType());
//        orderDetail.setIdNumber(userInfo.getIdNumber());
//        orderDetail.setRealName(userInfo.getRealName());
//        //调用户系统rpc接口获取用户来源
//        String source = userAccountContract.getAccountSource(borrowConfirmDto.getMobile()).getData();
//        orderDetail.setSource(source);
//        //保存订单详情
//        try {
//            orderDetailDao.save(orderDetail);
//        } catch (Exception e) {
//            LOGGER.error("save orderDetail failed, orderNo = " + orderNo, e);
//            throw e;
//        }
//
//
//        // 如果是银码头，则更改订单状态为等待放款
//        if (borrowConfirmDto.getChannel() != null && borrowConfirmDto.getChannel().equals(BorrowConfirmChannel.YMT.getChannel())) {
//            OrderStatusLog orderStatusLog1 = new OrderStatusLog();
//            orderStatusLog1.setOrderNo(orderNo);
//            orderStatusLog1.setBeforeStatus(OrderStatus.INIT.getCode());
//            orderStatusLog1.setAfterStatus(OrderStatus.AUDIT.getCode());
//            OrderStatusLog orderStatusLog2 = new OrderStatusLog();
//            orderStatusLog2.setOrderNo(orderNo);
//            orderStatusLog2.setBeforeStatus(OrderStatus.AUDIT.getCode());
//            orderStatusLog2.setAfterStatus(OrderStatus.AUDIT_SUCCESS.getCode());
//            OrderStatusLog orderStatusLog3 = new OrderStatusLog();
//            orderStatusLog3.setOrderNo(orderNo);
//            orderStatusLog3.setBeforeStatus(OrderStatus.AUDIT_SUCCESS.getCode());
//            orderStatusLog3.setAfterStatus(OrderStatus.WAIT_LOAN.getCode());
//            try {
//                orderStatusLogDao.save(orderStatusLog1);
//                orderStatusLogDao.save(orderStatusLog2);
//                orderStatusLogDao.save(orderStatusLog3);
//            } catch (Exception e) {
//                LOGGER.error("save orderStatusLog failed, orderNo = " + orderNo, e);
//                throw e;
//            }
//        } else { // 如果是侬要贷，则更改订单状态为等待审核
//            OrderStatusLog orderStatusLog = new OrderStatusLog();
//            orderStatusLog.setOrderNo(orderNo);
//            orderStatusLog.setBeforeStatus(OrderStatus.INIT.getCode());
//            orderStatusLog.setAfterStatus(OrderStatus.AUDIT.getCode());
//            try {
//                orderStatusLogDao.save(orderStatusLog);
//            } catch (Exception e) {
//                LOGGER.error("save orderStatusLog failed, orderNo = " + orderNo, e);
//                throw e;
//            }
//        }
//
//
//        BorrowConfirmVo vo = new BorrowConfirmVo();
//        vo.setOrderNo(orderNo);
//        response.setData(vo);
//
//        //往放款发送消息
//        OrderMessage msg = new OrderMessage();
//        msg.setUserId(borrowConfirmDto.getUserId());
//        msg.setOrderNo(orderNo);
//        msg.setProductType(business);
//        msg.setBorrowType(productInfo.getProductType().toString());
//        msg.setFundCode(order.getFundCode());
//
//        // 如果是银码头，则发送放款消息
//        LOGGER.info("传入的渠道" + borrowConfirmDto.getChannel());
//        if (borrowConfirmDto.getChannel() != null && BorrowConfirmChannel.YMT.getChannel().equals(borrowConfirmDto.getChannel())) {
//            msg.setChannel(BorrowConfirmChannel.YMT.getChannel());
//            LOGGER.info("银码头");
//            orderToPayProducer.sendMsg(msg); // 发送到放款
//        } else {
//            msg.setChannel(BorrowConfirmChannel.NYD.getChannel());
//            LOGGER.info("侬要贷");
//            orderToAuditProducer.sendMsg(msg); // 发送到审核
//        }
//
//        //生成订单
//        redisTemplate.opsForValue().set(OrderConsts.REDIS_LOAN_ACCOUNT_STATUS + borrowConfirmDto.getUserId(), 2 + "_" + orderNo, 100, TimeUnit.MINUTES);
//
//
//        LOGGER.info("borrow confirm success, userId is " + borrowConfirmDto.getUserId() + ", orderNo is " + orderNo);
//        return response;
//    }
//
//
//    /**
//     * 借款确认
//     *
//     * @param borrowConfirmDto BorrowConfirmDto
//     * @return ResponseData
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public ResponseData newBorrowInfoConfirm(BorrowConfirmDto borrowConfirmDto, boolean isFirst) throws Exception {
//        LOGGER.info("begin to borrow confirm, userId is " + borrowConfirmDto.getUserId());
//        LOGGER.info("开始进行借款确认：" + JSON.toJSONString(borrowConfirmDto));
//        try {
//            if (isFirst) {
//                if (redisTemplate.hasKey(NYD_REDIS_PREFIX + borrowConfirmDto.getUserId())) {
//                    LOGGER.info("有值" + borrowConfirmDto.getUserId());
//                    ResponseData r = ResponseData.error("不能重复提交");
//                    r.setData(null);
//                    return r;
//                } else {
//                    redisTemplate.opsForValue().set(NYD_REDIS_PREFIX + borrowConfirmDto.getUserId(), "1", 70, TimeUnit.SECONDS);
//                }
//            }
//            //删除key
//            redisTemplate.delete("kzjr2qcgz" + borrowConfirmDto.getUserId());
//            //判断是否具有借款条件
//            JudgeInfo judgeInfo = judgeBorrow(borrowConfirmDto.getUserId());
//            if ("1".equals(judgeInfo.getWhetherLoan())) {
//                if ("0".equals(judgeInfo.getUnProcessOrderExist())) {
//                    ResponseData responseData = ResponseData.error(judgeInfo.getWhetherLoanMsg());
//                    responseData.setCode("1000");//1000 告诉银码头 在侬要贷 有未处理订单
//                    return responseData;
//                } else {
//                    return ResponseData.error(judgeInfo.getWhetherLoanMsg());
//                }
//            }
//
//            String memberId = null;
//            String memberType = null;
//            BigDecimal memberFee = new BigDecimal(0);
//            //判断会员是否有效, 仅限于侬要贷需要判断 Add by Jiawei Cheng 2018-3027 21:51
//            //由于侬要贷之前并没有channel字段 所以 判断 channel 是否为null 为null 表示 来源 nyd
//            if (borrowConfirmDto.getChannel() == null || BorrowConfirmChannel.NYD.getChannel().equals(borrowConfirmDto.getChannel())) {
//                if (borrowConfirmDto.getType() == null || "".equals(borrowConfirmDto.getType())) {
//                    JudgeMemberInfo judgeMember = judgeMember(borrowConfirmDto.getUserId());
//                    if (!judgeMember.isMemberFlag()) {
//                        return ResponseData.error(OrderConsts.MEMBER_ERROR);
//                    } else {
//                        memberId = judgeMember.getMemberId();
//                        memberFee = judgeMember.getMemberFee();
//                        memberType = judgeMember.getMemberType();
//                    }
//                }
//            }
//            redisTemplate.opsForValue().set(OrderConsts.REDIS_LOAN_KEY + borrowConfirmDto.getUserId(), JSON.toJSONString(borrowConfirmDto), 200, TimeUnit.MINUTES);
//            //分流处理
//            String channel = null;
//
//            //调用户系统rpc接口获取用户信息，保存至订单详情
//            UserInfo userInfo = userIdentityContract.getUserInfo(borrowConfirmDto.getUserId()).getData();
//            String orderJxOn = orderProperties.getOrderJxOn();
//            if ("0".equals(orderJxOn ) && borrowConfirmDto.getChannel() != null && borrowConfirmDto.getChannel() == 1) {
//                channel = "qcgz";
//            } else {
//                if (isFirst) {
//                    //为测试添加白名单
//                    List<InnerTest> objectsByMobile = testListDao.getObjectsByMobile(borrowConfirmDto.getMobile());
//
//                    if (objectsByMobile != null && objectsByMobile.size() > 0 && (objectsByMobile.get(0).getRealName().contains("qcgz") || objectsByMobile.get(0).getRealName().contains("jx") || objectsByMobile.get(0).getRealName().contains("kzjr"))) {
//                        LOGGER.info("白名单用户:" + objectsByMobile.get(0).getRealName());
//                        if (objectsByMobile.get(0).getRealName().contains("qcgz")) {
//                            channel = "qcgz";
//                        } else if (objectsByMobile.get(0).getRealName().contains("jx")) {
//                            channel = "jx";
//                        } else if (objectsByMobile.get(0).getRealName().contains("kzjr")) {
//                            channel = "kzjr";
//                        }
//                    } else {
//                        ResponseData contractChannel = orderChannelContract.getChannel();
//                        if ("0".equals(contractChannel.getStatus())) {
//                            channel = (String) contractChannel.getData();
//                        } else {
//                            channel = "qcgz";
//                        }
//                    }
//                } else {
//                    channel = borrowConfirmDto.getFundCode();
//                }
//            }
//            LOGGER.info("资产渠道为:" + channel);
//            //此时订单与订单详情都已生成，开始根据渠道进行相应处理
//            if (!("qcgz".equals(channel))) {
//                //如果不是七彩格子，会要进行开户操作
//                if ("kzjr".equals(channel)) {
//                    //如果是空中金融
//                    if (isFirst) {
//                        ResponseData responseData = selectOpenPage(borrowConfirmDto, isFirst);
//
//                        if ("kzjr".equals(responseData.getData()) || "qcgz".equals(responseData.getData())) {
//                            channel = (String) responseData.getData();
//                        } else {
//                            return responseData;
//                        }
//                    } else {
//                        ResponseData responseData = selectOpenPage(borrowConfirmDto, false);
//                        if ("0".equals(responseData.getStatus())) {
//                            channel = (String) responseData.getData();
//                        } else {
//                            channel = "qcgz";
//                        }
//
//                    }
//
//                } else if ("jx".equals(channel)) {
//                    //如果是即信
//                    if (isFirst) {
//                        ResponseData responseData = selectOpenPageForJx(borrowConfirmDto, userInfo,isFirst);
//                        if ("qcgz".equals(responseData.getData())) {
//                            channel = (String) responseData.getData();
//                        } else {
//                            Map<String, String> map = (Map<String, String>) responseData.getData();
//                            if ("6".equals(map.get("stage"))) {
//                                channel = "jx";
//                            } else {
//                                LOGGER.info("该用户判定去走即信开户流程:" + userInfo.toString() + "responseData:" + JSON.toJSONString(responseData));
//                                return responseData;
//                            }
//                        }
//                    } else {
//
//                        ResponseData responseData = selectOpenPageForJx(borrowConfirmDto, userInfo,isFirst);
//                        if ("qcgz".equals(responseData.getData())) {
//                            channel = "qcgz";
//                        } else {
//                            Map<String, String> map = (Map<String, String>) responseData.getData();
//                            String stage = map.get("stage");
//                            if (!"6".equals(stage)) {
//                                return responseData;
//                            } else {
//                                channel = "jx";
//                            }
//                        }
//                    }
//                }
//            }
//            Order order = new Order();
//            Date date = new Date();
//            BeanUtils.copyProperties(borrowConfirmDto, order);
//            //判断是否内部测试人员
//            if (judgeTestUserFlag(borrowConfirmDto.getMobile())) {
//                order.setTestStatus(0);
//            } else {
//                order.setTestStatus(1);
//            }
//            //大户的ID服务，生成订单ID
//            String orderNo = idGenerator.generatorId(BizCode.ORDER_NYD).toString();
//            order.setOrderNo(orderNo);
//            order.setOrderStatus(OrderStatus.AUDIT.getCode());
//            //申请时间
//            order.setLoanTime(date);
//            //获取金融产品信息
//            ProductInfo productInfo = productContract.getProductInfo(borrowConfirmDto.getProductCode()).getData();
//            if (productInfo == null) {
//                LOGGER.error("get fianceProduct failed, ProductCode = " + borrowConfirmDto.getProductCode());
//                return ResponseData.error(OrderConsts.NO_PRODUCT);
//            }
//            String business = productInfo.getBusiness();
//            order.setBusiness(business);
//            //获取资金渠道
//            order.setFundCode(channel);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            calendar.add(Calendar.DAY_OF_MONTH, +borrowConfirmDto.getBorrowTime());
//            date = calendar.getTime();
////            order.setPromiseRepaymentTime(date);
//            order.setMemberId(memberId);
//            order.setMemberFee(memberFee);
//            order.setMemberType(memberType);
//            order.setRealLoanAmount(borrowConfirmDto.getLoanAmount());
//            // ----------- Update by Jiawei Cheng 2018-3-28 14:07 对订单新增保存来源渠道
//            if (borrowConfirmDto.getChannel() == null) {
//                order.setChannel(BorrowConfirmChannel.NYD.getChannel());
//            } else {
//                order.setChannel(borrowConfirmDto.getChannel());
//            }
//            if (borrowConfirmDto.getChannel() != null && BorrowConfirmChannel.YMT.getChannel().equals(borrowConfirmDto.getChannel())) {
//                order.setIbankOrderNo(borrowConfirmDto.getIbankOrderNo());
//                order.setOrderStatus(OrderStatus.WAIT_LOAN.getCode());//银码头 不通过审核 直接 待放款。
//                order.setAuditStatus("1");
//            }
//
//            ResponseData response = ResponseData.success();
//
//
//            //保存订单
//            try {
//                orderDao.save(order);
//                if (order.getChannel().equals(BorrowConfirmChannel.YMT.getChannel())) {
//                    orderContractYmt.updateOrderDetailStatus(borrowConfirmDto.getIbankOrderNo(), OrderStatus.WAIT_LOAN.getCode());
//                }
//            } catch (Exception e) {
//                LOGGER.error("save order failed  or dubbo update fail , orderNo = " + orderNo, e);
//                throw e;
//            }
//            OrderDetail orderDetail = new OrderDetail();
//            orderDetail.setMobile(borrowConfirmDto.getMobile());
//            orderDetail.setOrderNo(orderNo);
//            BeanUtils.copyProperties(borrowConfirmDto, orderDetail);
//            //金融产品相关信息
//            orderDetail.setProductCode(productInfo.getProductCode());
//            orderDetail.setProductType(productInfo.getProductType());
//            orderDetail.setProductPeriods(productInfo.getProductPeriods());
//            orderDetail.setInterestRate(productInfo.getInterestRate());
//
//            orderDetail.setIdType(userInfo.getCertificateType());
//            orderDetail.setIdNumber(userInfo.getIdNumber());
//            orderDetail.setRealName(userInfo.getRealName());
//            //调用户系统rpc接口获取用户来源
//            String source = userAccountContract.getAccountSource(borrowConfirmDto.getMobile()).getData();
//            orderDetail.setSource(source);
//            //保存订单详情
//            try {
//                orderDetailDao.save(orderDetail);
//            } catch (Exception e) {
//                LOGGER.error("save orderDetail failed, orderNo = " + orderNo, e);
//                throw e;
//            }
//
//
//            // 如果是银码头，则更改订单状态为等待放款
//            if (borrowConfirmDto.getChannel() != null && borrowConfirmDto.getChannel().equals(BorrowConfirmChannel.YMT.getChannel())) {
//                OrderStatusLog orderStatusLog1 = new OrderStatusLog();
//                orderStatusLog1.setOrderNo(orderNo);
//                orderStatusLog1.setBeforeStatus(OrderStatus.INIT.getCode());
//                orderStatusLog1.setAfterStatus(OrderStatus.AUDIT.getCode());
//                OrderStatusLog orderStatusLog2 = new OrderStatusLog();
//                orderStatusLog2.setOrderNo(orderNo);
//                orderStatusLog2.setBeforeStatus(OrderStatus.AUDIT.getCode());
//                orderStatusLog2.setAfterStatus(OrderStatus.AUDIT_SUCCESS.getCode());
//                OrderStatusLog orderStatusLog3 = new OrderStatusLog();
//                orderStatusLog3.setOrderNo(orderNo);
//                orderStatusLog3.setBeforeStatus(OrderStatus.AUDIT_SUCCESS.getCode());
//                orderStatusLog3.setAfterStatus(OrderStatus.WAIT_LOAN.getCode());
//                try {
//                    orderStatusLogDao.save(orderStatusLog1);
//                    orderStatusLogDao.save(orderStatusLog2);
//                    orderStatusLogDao.save(orderStatusLog3);
//                } catch (Exception e) {
//                    LOGGER.error("save orderStatusLog failed, orderNo = " + orderNo, e);
//                    throw e;
//                }
//            } else { // 如果是侬要贷，则更改订单状态为等待审核
//                OrderStatusLog orderStatusLog = new OrderStatusLog();
//                orderStatusLog.setOrderNo(orderNo);
//                orderStatusLog.setBeforeStatus(OrderStatus.INIT.getCode());
//                orderStatusLog.setAfterStatus(OrderStatus.AUDIT.getCode());
//                try {
//                    orderStatusLogDao.save(orderStatusLog);
//                } catch (Exception e) {
//                    LOGGER.error("save orderStatusLog failed, orderNo = " + orderNo, e);
//                    throw e;
//                }
//            }
//
//
//            BorrowConfirmVo vo = new BorrowConfirmVo();
//            vo.setOrderNo(orderNo);
//            vo.setFundCode(channel);
//            response.setData(vo);
//
//            //往放款发送消息
//            OrderMessage msg = new OrderMessage();
//            msg.setUserId(borrowConfirmDto.getUserId());
//            msg.setOrderNo(orderNo);
//            msg.setProductType(business);
//            msg.setBorrowType(productInfo.getProductType().toString());
//            msg.setFundCode(order.getFundCode());
//
//            // 如果是银码头，则发送放款消息
//            LOGGER.info("传入的渠道" + borrowConfirmDto.getChannel());
//            if (borrowConfirmDto.getChannel() != null && BorrowConfirmChannel.YMT.getChannel().equals(borrowConfirmDto.getChannel())) {
//                msg.setChannel(BorrowConfirmChannel.YMT.getChannel());
//                LOGGER.info("银码头");
//                orderToPayProducer.sendMsg(msg); // 发送到放款
//            } else {
//                msg.setChannel(BorrowConfirmChannel.NYD.getChannel());
//                LOGGER.info("侬要贷");
//                orderToAuditProducer.sendMsg(msg); // 发送到审核
//            }
//
//            //生成订单
//            redisTemplate.opsForValue().set(OrderConsts.REDIS_LOAN_ACCOUNT_STATUS + borrowConfirmDto.getUserId(), 2 + "_" + orderNo, 100, TimeUnit.MINUTES);
//
//
//            LOGGER.info("borrow confirm success, userId is " + borrowConfirmDto.getUserId() + ", orderNo is " + orderNo);
//            if (!isFirst && "jx".equals(channel)) {
//                Map<String, String> map = new HashMap<>();
//                map.put("fundCode", "jx");
//                map.put("result", "2");
//                map.put("stage", "6");
//                response.setData(map);
//                return response;
//            }
//            return response;
//
//        } catch (Exception e) {
//            throw e;
//        }
//
//    }
//
//    /**
//     * 查询用户是否在即信开户
//     *
//     * @param borrowConfirmDto
//     * @return
//     */
//    private ResponseData selectOpenPageForJx(BorrowConfirmDto borrowConfirmDto, UserInfo userInfo,boolean isFirst) {
//        ResponseData response = ResponseData.success();
//        try {
//            LOGGER.info("查询用户在即信的开户情况:" + userInfo.toString());
//            //查询数据库
//            ResponseData userJxByUserId = jxApi.getUserJxByUserId(userInfo.getUserId());
//            List<UserJx> userJxs = (List<UserJx>) userJxByUserId.getData();
//            Map<String, String> map = new HashMap<>();
//            if (userJxs.size() > 0 && userJxs.get(0).getStage() == 4) {
//                //该用户为老用户
//                map.put("fundCode", "jx");
//                map.put("result", "2");
//                map.put("stage", "6");
//                response.setData(map);
//            } else {
//                JxQueryPushStatusRequest jxQueryPushStatusRequest = new JxQueryPushStatusRequest();
//                jxQueryPushStatusRequest.setBankCardNumber(borrowConfirmDto.getBankAccount());
//                jxQueryPushStatusRequest.setIdCardNumber(userInfo.getIdNumber());
//                jxQueryPushStatusRequest.setMobile(borrowConfirmDto.getMobile());
//                jxQueryPushStatusRequest.setRealName(userInfo.getRealName());
//                ResponseData responseData = null;
//                try {
//                    responseData = jxApi.queryPushStatus(jxQueryPushStatusRequest);
//                    LOGGER.info("查询用户在即信开户情况结果: " + JSON.toJSONString(responseData));
//                } catch (Exception e) {
//                    LOGGER.info("调用dubbo查询该用户在即信开户情况失败,分配到七彩格子,userInfo:" + userInfo.toString());
//                    response.setData("qcgz");
//                }
//                if ("0".equals(responseData.getStatus())) {
//                    JxQueryPushStatusResponse jxQueryPushStatusResponse = (JxQueryPushStatusResponse) responseData.getData();
//                    if (!jxQueryPushStatusResponse.isAllowPass()){
//                        response.setData("qcgz");
//                        LOGGER.info("中网国投关闭了通道，统一将用户分到七彩格子，,userInfo:" + userInfo.toString());
//                    }else
//                    if (jxQueryPushStatusResponse.isAllowPass() && StringUtils.isNotBlank(jxQueryPushStatusResponse.getMemberId()) && jxQueryPushStatusResponse.isHasOpenAccount() && jxQueryPushStatusResponse.isBankCardHasBound() && !jxQueryPushStatusResponse.isBindingCardDifferent() && jxQueryPushStatusResponse.isTradePasswordHasSet() && jxQueryPushStatusResponse.isPaymentDelegationHasSigned() && jxQueryPushStatusResponse.isRepaymentDelegationHasSigned() && jxQueryPushStatusResponse.isAccreditFdd()) {
//                        //信息全部填写,则跳过爬虫流程,直接去推单外审(查询用户是否去到开通存管页面)
//                        map.put("fundCode", "jx");
//                        map.put("result", "2");
//                        map.put("stage", "6");
//                        response.setData(map);
//                        UserJx userJx = userJxs.get(0);
//                        if (userJx.getMemberId() == null) {
//                            userJx.setMemberId(jxQueryPushStatusResponse.getMemberId());
//                            userJx.setStage(1);
//                            jxApi.updateUserJx(userJx);
//                        }
//                        LOGGER.info("该用户已在即信开户,跳过开户流程:" + map + ",userInfo:" + userInfo.toString());
//                    } else if ((isFirst && StringUtils.isNotBlank(jxQueryPushStatusResponse.getMemberId()) && jxQueryPushStatusResponse.isBindingCardDifferent()) || (StringUtils.isBlank(jxQueryPushStatusResponse.getMemberId()) && jxQueryPushStatusResponse.isIdCardIsUsed())) {
//                        response.setData("qcgz");
//                        LOGGER.info("该用户绑定信息不一致,暂时不处理,分配到七彩格子,userInfo:" + userInfo.toString());
//                    } else {
//                        //判定用户进行到了哪一步
//                        String stage = null;
//                        if (StringUtils.isBlank(jxQueryPushStatusResponse.getMemberId())) {
//                            stage = "1";
//                        } else if (!jxQueryPushStatusResponse.isTradePasswordHasSet()) {
//                            stage = "2";
//                        } else if (!jxQueryPushStatusResponse.isPaymentDelegationHasSigned()) {
//                            stage = "3";
//                        } else if (!jxQueryPushStatusResponse.isRepaymentDelegationHasSigned()) {
//                            stage = "4";
//                        } else if (!jxQueryPushStatusResponse.isAccreditFdd()) {
//                            stage = "5";
//                        }
//                        map.put("fundCode", "jx");
//                        map.put("result", "2");
//                        map.put("stage", stage);
//                        response.setData(map);
//                        LOGGER.info("该用户需要去即信开户或完善开户:" + map + ",userInfo:" + userInfo.toString());
//                    }
//
//                } else {
//                    response.setData("qcgz");
//                    LOGGER.info("查询该用户在即信开户情况失败,分配到七彩格子,userInfo:" + userInfo.toString());
//                }
//            }
//        } catch (Exception e) {
//            response.setData("qcgz");
//            LOGGER.info("查询该用户在即信开户情况发生异常,分配到七彩格子,userInfo:" + userInfo.toString(),e);
//        }
//
//        return response;
//    }
//
//    /**
//     * 空中金融渠道处理，第一次时进行校验是否需要进行开户，第二次时进行判断是否开户成功
//     *
//     * @param borrowConfirmDto
//     * @param isFirst
//     * @return
//     */
//    @Override
//    public ResponseData selectOpenPage(BorrowConfirmDto borrowConfirmDto, boolean isFirst) {
//        ResponseData response = ResponseData.success();
//
//        // 空中金融开户、解绑卡、短信验证码（V2）等判断流程
//
//        BorrowConfirmVo borrowConfirmVo = null;
//        boolean valiKzjrSmsIsTrue = true;
//
//        // 短信不为空，进行解绑操作
//        if (borrowConfirmDto.getMsgCode() != null) {
//            // V3版本为处理空中金融新户开户老户绑卡
//            if (!P2pProcessAccount(borrowConfirmDto)) {
//                valiKzjrSmsIsTrue = false;
//            }
//        } else {
//            // 短信为空
//            //校验是否要发送p2p短信 （空中金融V3包含开户需要跳转的URL地址、和解绑银行卡的短信验证码）
//            borrowConfirmVo = checkIfSendSms(borrowConfirmDto, isFirst);
//
//            if (isFirst) {
//                if (0 == borrowConfirmVo.getFlag()) { // 0 发解绑银行卡的验证码
//                    borrowConfirmVo.setP2pSendFlag(true);
//                    response.setData(borrowConfirmVo);
//                    return response;
//                } else if (1 == borrowConfirmVo.getFlag()) { // 1开户
//                    LOGGER.warn("open account, userId is " + borrowConfirmDto.getUserId());
//                    borrowConfirmVo.setP2pSendFlag(false);
//                    response.setData(borrowConfirmVo);
//                    return response;
//                }
//            }
//        }
//        String fundCode = null;
//        if (borrowConfirmVo == null) {
//            if (valiKzjrSmsIsTrue) {
//                fundCode = "kzjr";
//            } else {
//                fundCode = "qcgz";
//            }
//        } else {
//            if (isFirst) {
//                if ("1".equals(borrowConfirmVo.getP2pStatus()) || 3 == borrowConfirmVo.getFlag()) {
//                    fundCode = "qcgz";
//                } else if (2 == borrowConfirmVo.getFlag()) {
//                    fundCode = "kzjr";
//                } else {
//                    fundCode = "unknown";
//                }
//            } else {
//                if ("1".equals(borrowConfirmVo.getP2pStatus())) {
//                    fundCode = "qcgz";
//                } else if (2 == borrowConfirmVo.getFlag()) {
//                    fundCode = "kzjr";
//                } else {
//                    fundCode = "qcgz";
//                }
//            }
//        }
//        return response.setData(fundCode);
//    }
//
//    /**
//     * 借款结果
//     *
//     * @param userId
//     * @return ResponseData
//     */
//    @Override
//    public ResponseData<BorrowResultVo> getBorrowResult(String userId) {
//        LOGGER.info("begin to get borrow result, userId is " + userId);
//        ResponseData response = ResponseData.success();
//        if (userId == null) {
//            return response;
//        }
//        BorrowResultVo borrowResult = new BorrowResultVo();
//        try {
//            List<OrderInfo> orderList = orderDao.getObjectsByUserId(userId);
//            if (orderList != null && orderList.size() > 0) {
//                OrderInfo orderInfo = orderList.get(0);
//                BeanUtils.copyProperties(orderInfo, borrowResult);
//            } else {
//                return response;
//            }
//            List<OrderStatusLogInfo> statusList = orderStatusLogDao.getObjectsByOrderNo(borrowResult.getOrderNo());
//            if (statusList != null && statusList.size() > 0) {
//                List<OrderLogVo> logList = new ArrayList<>();
//                for (OrderStatusLogInfo logInfo : statusList) {
//                    OrderLogVo orderLog = new OrderLogVo();
//                    orderLog.setStatusCode(logInfo.getAfterStatus());
//                    orderLog.setStatusTime(DateUtil.dateToString(logInfo.getCreateTime()));
//                    logList.add(orderLog);
//                }
//                borrowResult.setOrderStatusList(logList);
//            }
//            response.setData(borrowResult);
//            LOGGER.info("get borrow result success !");
//        } catch (Exception e) {
//            LOGGER.error("get orderInfo failed, userId = " + userId, e);
//            response = ResponseData.error(OrderConsts.DB_ERROR_MSG);
//        }
//        return response;
//    }
//
//    /**
//     * 借款详情
//     *
//     * @param orderNo
//     * @return ResponseData
//     */
//    @Override
//    public ResponseData<BorrowDetailVo> getBorrowDetail(String orderNo) {
//        LOGGER.info("begin to get borrow detail, orderNo is " + orderNo);
//        ResponseData response = ResponseData.success();
//        if (orderNo == null) {
//            return response;
//        }
//        BorrowDetailVo borrowDetail = new BorrowDetailVo();
//        try {
//            List<OrderInfo> detailList = orderDao.getObjectsByOrderNo(orderNo);
//            if (detailList != null && detailList.size() > 0) {
//                OrderInfo orderInfo = detailList.get(0);
//                BeanUtils.copyProperties(orderInfo, borrowDetail);
//                borrowDetail.setLoanTime(DateUtil.dateToString(orderInfo.getLoanTime()));
//                borrowDetail.setPayTime(DateUtil.dateToString(orderInfo.getPayTime()));
//                borrowDetail.setPromiseRepaymentTime(DateUtil.dateToString(orderInfo.getPromiseRepaymentTime()));
//                response.setData(borrowDetail);
//            }
//            LOGGER.info("get borrow detail success !");
//        } catch (Exception e) {
//            LOGGER.error("get orderDetailInfo failed, orderNo = " + orderNo, e);
//            response = ResponseData.error(OrderConsts.DB_ERROR_MSG);
//        }
//        return response;
//    }
//
//    /**
//     * 所有借款记录
//     *
//     * @param userId
//     * @return ResponseData
//     */
//    @Override
//    public ResponseData<List<BorrowRecordVo>> getBorrowAll(String userId) {
//        LOGGER.info("begin to get borrow record, userId is " + userId);
//        ResponseData response = ResponseData.success();
//        if (userId == null) {
//            return response;
//        }
//        try {
//            List<OrderInfo> orderList = orderDao.getObjectsByUserId(userId);
//            if (orderList != null && orderList.size() > 0) {
//                List<BorrowRecordVo> borrowList = new ArrayList<BorrowRecordVo>();
//                for (OrderInfo orderInfo : orderList) {
//                    BorrowRecordVo borrowRecord = new BorrowRecordVo();
//                    BeanUtils.copyProperties(orderInfo, borrowRecord);
//                    borrowList.add(borrowRecord);
//                }
//                response.setData(borrowList);
//            }
//            LOGGER.info("get borrow record success !");
//        } catch (Exception e) {
//            LOGGER.error("get order record error! userId = " + userId, e);
//            response = ResponseData.error(OrderConsts.DB_ERROR_MSG);
//        }
//        return response;
//    }
//
//
//    /**
//     * 判断是否存在审核中，待放款的订单
//     *
//     * @param userId
//     * @return boolean
//     */
//    private JudgeInfo judgeBorrow(String userId) {
//        LOGGER.info("begin to Judge borrow, userId is " + userId);
//        JudgeInfo judgeInfo = new JudgeInfo();
//        try {
//            List<OrderInfo> orderList = orderDao.getLastOrderByUserId(userId);
//            if (orderList != null && orderList.size() > 0) {
//                OrderInfo orderInfo = orderList.get(0);
//                //状态小于40，不可借款
//                if (orderInfo.getOrderStatus() < 40) {
//                    judgeInfo.setWhetherLoan("1");
//                    judgeInfo.setWhetherLoanMsg("您有一笔借款在审核中！");
//                    judgeInfo.setUnProcessOrderExist("0");
//                } else if (orderInfo.getOrderStatus() == 40) {
//                    //放款失败，可借款
//                    judgeInfo.setWhetherLoan("0");
//                } else if (orderInfo.getOrderStatus() == 50) {
//                    //放款成功，判断账单是否还清
//                    int billCount = billContract.getBillInfos(userId).getData();
//                    if (billCount > 0) {
//                        judgeInfo.setWhetherLoan("1");
//                        judgeInfo.setWhetherLoanMsg("您有未还清的借款！");
//                        judgeInfo.setUnProcessOrderExist("0");
//                    } else {
//                        //账单已还清，可借款
//                        judgeInfo.setWhetherLoan("0");
//                    }
//                } else if (orderInfo.getOrderStatus() == 1000) {
//                    //审核拒绝，判断间隔是否超过7天
//                    int days = DateUtil.getDayDiffDown(orderInfo.getLoanTime(), new Date());
//                    int intervalDays = Integer.valueOf(orderProperties.getIntervalDays());
//                    if (days >= intervalDays) {
//                        judgeInfo.setWhetherLoan("0");
//                    } else {
//                        int remainDays = intervalDays - days;
//                        judgeInfo.setWhetherLoan("1");
//                        judgeInfo.setWhetherLoanMsg("亲，请过" + remainDays + "天再来！");
//                    }
//                }
//            } else {
//                //没有订单，表示第一次借款，可以借款
//                judgeInfo.setWhetherLoan("0");
//            }
//            LOGGER.info("judge borrow result is " + JSONObject.toJSONString(judgeInfo));
//        } catch (Exception e) {
//            e.printStackTrace();
//            judgeInfo.setWhetherLoan("1");
//            judgeInfo.setWhetherLoanMsg(OrderConsts.DB_ERROR_MSG);
//            LOGGER.error("judge borrow error! userId = " + userId, e);
//        }
//        return judgeInfo;
//    }
//
//    /**
//     * 判断会员是否过期
//     *
//     * @param userId
//     * @return boolean
//     */
//    private JudgeMemberInfo judgeMember(String userId) {
//        LOGGER.info("begin to judge member, userId is " + userId);
//        JudgeMemberInfo memberInfo = new JudgeMemberInfo();
//        boolean memberFlag = false;
//        try {
//            //获取会员信息
//            ResponseData<MemberModel> memberResponse = memberContract.getMember(userId);
//            if ("0".equals(memberResponse.getStatus())) {
//                if (memberResponse.getData() != null) {
//                    MemberModel model = memberResponse.getData();
//                    int expireDay = DateUtil.getDayDiffUp(new Date(), model.getExpireTime());
//                    int memberDay = Integer.valueOf(orderProperties.getMemberDays());
//                    if (expireDay > memberDay) {
//                        memberFlag = true;
//                        memberInfo.setMemberId(model.getMemberId());
//                    }
//                    memberInfo.setMemberFee(model.getMemberFee());
//                    memberInfo.setMemberType(model.getMemberType());
//                }
//            }
//        } catch (Exception e) {
//            LOGGER.error("get member info error! userId = " + userId, e);
//        }
//        LOGGER.info("judge member result is" + memberFlag);
//        memberInfo.setMemberFlag(memberFlag);
//        return memberInfo;
//    }
//
//    /**
//     * 判断是否为测试人员
//     *
//     * @param mobile
//     * @return boolean
//     */
//    private boolean judgeTestUserFlag(String mobile) {
//        LOGGER.info("begin to judge testUser Flag, mobile is " + mobile);
//        boolean testFlag = false;
//        try {
//            List<InnerTest> list = testListDao.getObjectsByMobile(mobile);
//            if (list != null && list.size() > 0) {
//                testFlag = true;
//            }
//        } catch (Exception e) {
//            LOGGER.error("get innerTest list error! mobile = " + mobile, e);
//        }
//        LOGGER.info("judge testUser Flag result is" + testFlag);
//        return testFlag;
//    }
//
//    /**
//     * V2校验是否要发送p2p短信
//     * V3版本为跳转到开户页面或者申请解绑银行卡的短信验证码
//     *
//     * @param borrowConfirmDto BorrowConfirmDto
//     * @return boolean
//     */
//    private BorrowConfirmVo checkIfSendSms(BorrowConfirmDto borrowConfirmDto, boolean isFirst) {
//        LOGGER.info("begin to check whether p2p sendSms, userId is " + borrowConfirmDto.getUserId());
//        BorrowConfirmVo result = new BorrowConfirmVo();
//        try {
//            CheckStatusVo param = new CheckStatusVo();
//            param.setUserId(borrowConfirmDto.getUserId());
//            param.setMobile(borrowConfirmDto.getMobile());
//            param.setAccNo(borrowConfirmDto.getBankAccount());
//            param.setIsFirst(isFirst);
//            param.setLoanAmount(borrowConfirmDto.getLoanAmount().toString());
//            param.setBorrowTime(borrowConfirmDto.getBorrowTime());
//            if (borrowConfirmDto.getChannel() == null) {
//                param.setChannel(BorrowConfirmChannel.NYD.getChannel());
//            } else {
//                param.setChannel(borrowConfirmDto.getChannel());
//            }
//
//            int loopCount = 0;
//            int count = 0;
//            if (isFirst) {
//                loopCount = 1;
//            } else {//第二次认证 需要重复 10次 获取数据
//                loopCount = 10;
//                count = 10;
//            }
//
//            boolean statusFlag = false;
//
//            LOGGER.info("*****start" + loopCount + isFirst);
//
//            while (loopCount > 0) {
//                ResponseData<KzjrStatusReponse> response = remitService.checkStatusKzjr(param); // 根据空中金融的账号状态，进行开户、解绑申请短信验证码操作
//                while ("0".equals(response.getStatus()) && response.getData().getFlag() == 1 && count > 1) { //因空中金融可能未更新银行开户状态，所以需要循环等待获取
//                    response = remitService.checkStatusKzjr(param);
//                    LOGGER.info("userId is " + borrowConfirmDto.getUserId(), count);
//                    Thread.sleep(3000);
//                    count--;
//                }
//                LOGGER.info(loopCount + "check whether p2p checkStatusKzjr result is " + JSON.toJSONString(response));
//                if ("0".equals(response.getStatus())) {
//                    KzjrStatusReponse dto = response.getData();
//                    result.setP2pId(dto.getP2pId());
//                    result.setFlag(dto.getFlag());
//                    result.setUrl(dto.getUrl());
//                    if (dto.getFlag() == 3) { // 3正在开户中
//                        redisTemplate.opsForValue().set(OrderConsts.REDIS_LOAN_ACCOUNT_STATUS + borrowConfirmDto.getUserId(), "3", 100, TimeUnit.MINUTES);
//                    } else if (dto.getFlag() == 2) { //2 正常
////                        redisTemplate.opsForValue().set(OrderConsts.REDIS_LOAN_ACCOUNT_STATUS + borrowConfirmDto.getUserId(), 2, 100, TimeUnit.MINUTES);
//
//                        statusFlag = true;
//                        break;
//                    } else { // 0需要发短信验证码  1开户 (V3 版本为1）
//                        redisTemplate.opsForValue().set(OrderConsts.REDIS_LOAN_ACCOUNT_STATUS + borrowConfirmDto.getUserId(), "1", 100, TimeUnit.MINUTES);
//                        break;
//                    }
//                } else {
//                    result.setP2pStatus("1");
//                }
//                loopCount--;
//                if (!isFirst) {
//                    Thread.sleep(1000);
//                }
//            }
//            if (!statusFlag) {
//                redisTemplate.opsForValue().set(OrderConsts.REDIS_LOAN_ACCOUNT_STATUS + borrowConfirmDto.getUserId(), "1", 100, TimeUnit.MINUTES);
//
//            }
//
//        } catch (Exception e) {
//            LOGGER.error("check whether p2p sendSms error, userId is " + borrowConfirmDto.getUserId(), e);
//        }
//        return result;
//    }
//
//    /**
//     * 校验p2p短信 (V3版本为处理空中金融新户开户老户绑卡)
//     *
//     * @param borrowConfirmDto BorrowConfirmDto
//     * @return boolean
//     */
//    private boolean P2pProcessAccount(BorrowConfirmDto borrowConfirmDto) {
//        LOGGER.info("begin to p2p process account, userId is " + borrowConfirmDto.getUserId());
//        boolean verifyFlag = false;
//        try {
//            OpenAccountVo param = new OpenAccountVo();
//            param.setUserId(borrowConfirmDto.getUserId());
//            param.setIdType(1);
//            param.setMobile(borrowConfirmDto.getMobile());
//            param.setP2pId(borrowConfirmDto.getP2pId());
//            param.setSmsCode(borrowConfirmDto.getMsgCode());
//            param.setCardNo(borrowConfirmDto.getBankAccount());
//            //调用户系统rpc接口获取用户信息
//            UserInfo userInfo = userIdentityContract.getUserInfo(borrowConfirmDto.getUserId()).getData();
//            param.setName(userInfo.getRealName());
//            param.setIdNo(userInfo.getIdNumber());
//            ResultEnum resultEnum = remitService.processAccount(param); // 处理空中金融新户开户老户绑卡
//            if (ResultEnum.CORRECT == resultEnum) {
//                verifyFlag = true;
//            }
//            LOGGER.info("p2p process account result is " + verifyFlag);
//        } catch (Exception e) {
//            LOGGER.error("p2p process account error, userId is " + borrowConfirmDto.getUserId(), e);
//        }
//        return verifyFlag;
//    }
//
//}
