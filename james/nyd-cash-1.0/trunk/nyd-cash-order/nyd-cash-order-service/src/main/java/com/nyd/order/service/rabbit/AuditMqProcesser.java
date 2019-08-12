package com.nyd.order.service.rabbit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.creativearts.das.query.api.message.AuditResultMessage;
import com.ibank.order.model.scope.AuditFailToScopeDto;
import com.nyd.msg.model.SmsRequest;
import com.nyd.msg.service.ISendSmsService;
import com.nyd.order.api.OrderChannelContract;
import com.nyd.order.api.spring.OrderToBillByProductService;
import com.nyd.order.dao.OrderDao;
import com.nyd.order.dao.OrderDetailDao;
import com.nyd.order.dao.OrderStatusLogDao;
import com.nyd.order.dao.TestListDao;
import com.nyd.order.dao.mapper.WithholdOrderMapper;
import com.nyd.order.entity.InnerTest;
import com.nyd.order.entity.OrderStatusLog;
import com.nyd.order.entity.WithholdOrder;
import com.nyd.order.model.OrderDetailInfo;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.dto.RequestData;
import com.nyd.order.model.dto.SubmitWithholdDto;
import com.nyd.order.model.enums.OrderStatus;
import com.nyd.order.model.vo.ProductOrderVo;
import com.nyd.order.service.util.DateUtil;
import com.nyd.order.service.util.HttpUtil;
import com.nyd.order.service.util.OrderProperties;
import com.nyd.product.api.ProductContract;
import com.nyd.product.model.ProductInfo;
import com.nyd.user.api.UserSourceContract;
import com.nyd.user.api.UserStepContract;
import com.nyd.user.entity.LoginLog;
import com.nyd.user.model.StepInfo;
import com.tasfe.framework.rabbitmq.RabbitmqMessageProcesser;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.persistence.Id;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Dengw on 2017/11/27.
 */
public class AuditMqProcesser extends BaseProcesser implements RabbitmqMessageProcesser<AuditResultMessage> {
    private static Logger LOGGER = LoggerFactory.getLogger(AuditMqProcesser.class);

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderStatusLogDao orderStatusLogDao;

    @Autowired
    private OrderProperties orderProperties;

    @Autowired
    private OrderDetailDao orderDetailDao;

    @Autowired
    private ProductContract productContract;

    @Autowired
    private UserStepContract userStepContract;

    @Autowired
    private UserSourceContract userSourceContract;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OrderChannelContract orderChannelContract;

    @Autowired
    private TestListDao testListDao;

    @Autowired
    private WithholdOrderMapper withholdOrderDao;
    
    @Autowired
    private ISendSmsService sendSmsService;

    @Autowired
    private OrderToBillByProductService orderToBillByProductService;


    @Override
    public void processMessage(AuditResultMessage message) {
        try {
            if (message != null) {
                LOGGER.info("receive msg from audit, msg is " + message.toString());
                OrderInfo sourceOrderInfo = null;

                int i = 5;

                while (i > 0 && sourceOrderInfo == null) {
                    try {
                        List<OrderInfo> list = orderDao.getObjectsByOrderNo(message.getOrderNo());
                        if (list != null && list.size() > 0) {
                            sourceOrderInfo = list.get(0);
                            if (OrderStatus.AUDIT.getCode() < list.get(0).getOrderStatus()) {
                                LOGGER.info("update order status failed, current status gt update status! orderNo=" + message.getOrderNo());
                                return;
                            }
                        }
                    } catch (Exception e) {
                        LOGGER.error("get orderInfo failed, orderNo = " + message.getOrderNo(), e);
                    }
                    i--;
                }

                ProductOrderVo product = new ProductOrderVo();

                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setOrderNo(message.getOrderNo());
                orderInfo.setAuditStatus(message.getAuditStatus());
                orderInfo.setAuditReason(message.getAuditReason());
                OrderStatusLog orderStatusLog = new OrderStatusLog();
                orderStatusLog.setOrderNo(message.getOrderNo());
                orderStatusLog.setBeforeStatus(OrderStatus.AUDIT.getCode());
                if ("1".equals(message.getAuditStatus().toString())) {

                    LOGGER.info("风控通过 通知bill"+message.getOrderNo());
                    product.setOrderNo(message.getOrderNo());
                    product.setUserId(message.getUserId());
                    orderToBillByProductService.getProductInfo(product);

                    //审核通过,发送短信通知用户
                    this.sendMobileSms(36, message.getOrderNo());

                    orderInfo.setWhoAudit(0);
                    orderInfo.setOrderStatus(OrderStatus.AUDIT_SUCCESS.getCode());
                    orderStatusLog.setAfterStatus(OrderStatus.AUDIT_SUCCESS.getCode());
                } else if ("2".equals(message.getAuditStatus().toString())) {
                    //人工审核
                    LOGGER.info("订单进去人工审核", JSON.toJSONString(message));
                    orderInfo.setOrderStatus(OrderStatus.AUDIT.getCode());
                    orderStatusLog.setAfterStatus(OrderStatus.AUDIT.getCode());
                    orderInfo.setWhoAudit(1);
                } else {
                    //订单拒绝
                    LOGGER.info("风控订单拒绝", JSON.toJSONString(message));
                    orderInfo.setOrderStatus(OrderStatus.AUDIT_REFUSE.getCode());
                    orderStatusLog.setAfterStatus(OrderStatus.AUDIT_REFUSE.getCode());
                    orderInfo.setWhoAudit(0);

                }
                try {
                    orderDao.update(orderInfo);
                    orderStatusLogDao.save(orderStatusLog);
                } catch (Exception e) {
                    LOGGER.error("receive msg from audit,but update order failed, orderNo = " + message.getOrderNo(), e);
                    return;
                }
        }

        } catch (
                Exception e) {
            LOGGER.error("最终审核结果出错啦", e);
        }

    }

    /**
     * 发送短信
     */
    public void sendMobileSms(int smsType,String orderNo){
            LOGGER.info("begin send msg to user, orderNo is " + orderNo);
            SmsRequest sms = new SmsRequest();
            try {
                List<OrderDetailInfo> detailList = orderDetailDao.getObjectsByOrderNo(orderNo);
                if(detailList != null && detailList.size()>0){
                    sms.setSmsType(smsType);
                    ResponseData data = userSourceContract.selectUserSourceByMobile(detailList.get(0).getMobile());
                    String appName = null;
                    if ("0".equals(data.getStatus())){
                        LoginLog loginLog =(LoginLog)data.getData();
                        if (loginLog != null){
                            appName = loginLog.getAppName();
                        }
                    }
                    if (appName != null) {
                        sms.setAppName(appName);
                    }
                    LOGGER.info("开始发送短信告知审核结果,orderNo is"+orderNo);
                    sms.setCellphone(detailList.get(0).getMobile());
                    sendSmsService.sendSingleSms(sms);
                    LOGGER.info("send mobile sms success, smsRequest is" + JSON.toJSONString(sms));
                }
            } catch (Exception e) {
                LOGGER.error("send msg to user error! orderNo = " + orderNo, e);
            }
        }
    
    /**
     * 针对拒绝订单请求万花筒所依赖的对象(AuditFailToScopeDto)进行封装
     *
     * @param orderNo
     */
    private void sendFailOrderToScope(String orderNo) {
        LOGGER.info("发送失败订单到万花筒:" + orderNo);
        //创建发送到万花筒的对象
        AuditFailToScopeDto auditFailToScopeDto = new AuditFailToScopeDto();
        try {
            List<OrderInfo> list = orderDao.getObjectsByOrderNo(orderNo);
            if (list != null && list.size() > 0) {
                OrderInfo orderInfo = list.get(0);
                LOGGER.info("根据订单号找到的订单信息:" + JSON.toJSON(orderInfo));
                //贷款金额
                auditFailToScopeDto.setLoanAmount(orderInfo.getLoanAmount().toString());
                //贷款期数
                auditFailToScopeDto.setLoanPeriods(orderInfo.getBorrowPeriods());
                //贷款时间
                auditFailToScopeDto.setLoanTime(DateUtil.dateToStringHms(orderInfo.getLoanTime()));
                //主订单号
                auditFailToScopeDto.setSourceOrderId(orderNo);
                //用户id
                auditFailToScopeDto.setUserId(orderInfo.getUserId());

                ResponseData<StepInfo> stepInfoResponseData = userStepContract.getUserStep(orderInfo.getUserId());
                if ("0".equals(stepInfoResponseData.getStatus())) {
                    StepInfo stepInfo = stepInfoResponseData.getData();
                    LOGGER.info("根据订单号找到的stepInfo:" + JSON.toJSON(stepInfo));
                    if (StringUtils.isNotBlank(stepInfo.getPreAuditLevel())) {
                        //用户审核等级
                        auditFailToScopeDto.setUserLevel(stepInfo.getPreAuditLevel());
                    }
                }

                List<OrderDetailInfo> orderDetailInfoList = orderDetailDao.getObjectsByOrderNo(orderNo);
                OrderDetailInfo orderDetailInfo = null;
                if (orderDetailInfoList != null && orderDetailInfoList.size() > 0) {
                    orderDetailInfo = orderDetailInfoList.get(0);
                    LOGGER.info("根据订单号找到的订单详情:" + JSON.toJSON(orderDetailInfo));
                    //用户手机号
                    auditFailToScopeDto.setMobilePhone(orderDetailInfo.getMobile());
                }

                if (StringUtils.isNotBlank(orderDetailInfo.getProductCode())) {
                    ResponseData<ProductInfo> responseData = productContract.getProductInfo(orderDetailInfo.getProductCode());
                    if ("0".equals(responseData.getStatus())) {
                        ProductInfo productInfo = responseData.getData();
                        LOGGER.info("根据订单号找到的产品信息:" + JSON.toJSON(productInfo));
                        if (productInfo != null) {
                            //贷款类型
                            auditFailToScopeDto.setLoanType(productInfo.getProductType());
                        }
                    }
                }

                /*if (StringUtils.isNotBlank(orderDetailInfo.getMobile())) {
                    ResponseData data = userSourceContract.selectUserSourceByMobile(orderDetailInfo.getMobile());
                    if ("0".equals(data.getStatus())) {
                        LoginLog loginLog = (LoginLog) data.getData();
                        //业务订单来源
                        if (StringUtils.isNotBlank(loginLog.getAppName())) {
                            //业务订单来源
                            auditFailToScopeDto.setSource(loginLog.getAppName());
                        } else {
                            //业务订单来源
                            auditFailToScopeDto.setSource("nyd");
                        }
                    }
                }*/
                auditFailToScopeDto.setSource("nyd");
                LOGGER.info("拒绝订单发送到万花筒的对象:" + JSON.toJSON(auditFailToScopeDto));

                //向万花筒发起请求
                ResponseData response = submitAuditFail(auditFailToScopeDto);
                LOGGER.info("拒绝订单发送到万花筒,返回结果:" + JSON.toJSON(response));
            }
        } catch (Exception e) {
            LOGGER.error("发送失败订单到万花筒失败", e);
        }
    }

    /**
     * 拒绝订单,向万花筒发起请求
     *
     * @param auditFailToScopeDto
     * @return
     */
    private ResponseData submitAuditFail(AuditFailToScopeDto auditFailToScopeDto) {
        LOGGER.info("失败订单,请求万花筒 start param is" + JSON.toJSONString(auditFailToScopeDto));
        ResponseData responseData = ResponseData.success();
        String jsonObj = JSON.toJSONString(auditFailToScopeDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> httpEntity = new HttpEntity(jsonObj, headers);
        String requestUrl = orderProperties.getScopeSubmitUrl();
        LOGGER.error("万花筒请求链接地址为:{}", requestUrl);
        ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity, String.class);

        if (response.getStatusCodeValue() != 200) {
            LOGGER.error("请求万花筒失败,建议重新发起请求,参数：" + JSON.toJSONString(auditFailToScopeDto) + " 请求url:" + requestUrl);
        } else {
            JSONObject responseJson = JSONObject.parseObject(response.getBody());
            String status = responseJson.getString("status");
            if ("0".equals(status)) {
                responseData.setMsg(responseJson.getString("msg"));
            } else {
                responseData = responseData.error(responseJson.getString("msg"));
            }
        }
        LOGGER.info("请求万花筒返回response：" + JSON.toJSONString(responseData));
        return responseData;
    }


}
