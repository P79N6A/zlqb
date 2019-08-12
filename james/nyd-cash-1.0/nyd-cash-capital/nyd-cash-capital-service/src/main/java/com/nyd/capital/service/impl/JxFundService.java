package com.nyd.capital.service.impl;

import com.alibaba.fastjson.JSON;
import com.nyd.application.api.DeviceInfoContract;
import com.nyd.capital.api.service.JxApi;
import com.nyd.capital.entity.UserJx;
import com.nyd.capital.model.WsmQuery;
import com.nyd.capital.model.enums.RemitStatus;
import com.nyd.capital.model.jx.*;
import com.nyd.capital.service.FundService;
import com.nyd.capital.service.jx.JxService;
import com.nyd.capital.service.jx.config.OpenPageConstant;
import com.nyd.capital.service.validate.ValidateException;
import com.nyd.order.api.CapitalOrderRelationContract;
import com.nyd.order.api.OrderContract;
import com.nyd.order.entity.BalanceOrder;
import com.nyd.order.model.OrderInfo;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.api.UserJobContract;
import com.nyd.user.model.JobInfo;
import com.nyd.user.model.UserDetailInfo;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class JxFundService implements FundService {

    Logger logger = LoggerFactory.getLogger(JxFundService.class);

    @Autowired(required = false)
    private OrderContract orderContract;
    @Autowired
    private JxApi jxApi;
    @Autowired
    private UserIdentityContract userIdentityContract;
    @Autowired
    private UserJobContract userJobContract;
    @Autowired
    private JxService jxService;
    @Autowired
    private CapitalOrderRelationContract capitalOrderRelationContract;
    @Autowired
    private DeviceInfoContract deviceInfoContractNyd;
    @Autowired
    private QcgzFundService qcgzFundService;
    @Override
    public ResponseData sendOrder(List orderList) {
        logger.info("sendOrder params:" + JSON.toJSON(orderList));
        ResponseData remitStatus = ResponseData.success();
            JxPushAuditRequest request = (JxPushAuditRequest) orderList.get(0);
            logger.info("推送资产为:" + JSON.toJSONString(request));
            try {
                //资产提交
                String orderNo = (String) orderList.get(1);
                String userId = (String) orderList.get(2);
                ResponseData responseData = jxService.pushAudit(request);
                if (OpenPageConstant.STATUS_ONE.equals(responseData.getStatus())) {
                    //根据订单号修改order
                    ResponseData responseData2 = capitalOrderRelationContract.updateFundCode(orderNo);
                    if ("1".equals(responseData2.getStatus())){
                        logger.error("修改订单渠道时发生异常");
                        return ResponseData.error();
                    }
                    //生成七彩格子推单参数
                    List list = qcgzFundService.generateOrders(userId, orderNo, null);
                    //推单到七彩格子
                    return qcgzFundService.sendOrder(list);
                }
                JxPushAuditResponse jxPushAuditResponse = (JxPushAuditResponse)responseData.getData();
                //进行推单外审结果查询
                JxQueryPushAuditResultRequest jxQueryPushAuditResultRequest = new JxQueryPushAuditResultRequest();
                jxQueryPushAuditResultRequest.setLoanOrderId(jxPushAuditResponse.getLoanOrderId());
                ResponseData responseData1 = jxService.queryPushAuditResult(jxQueryPushAuditResultRequest);
                if (OpenPageConstant.STATUS_ONE.equals(responseData1.getStatus())){
                    remitStatus =  ResponseData.error();
                    return remitStatus;
                }
                JxQueryPushAuditResultResponse jxQueryPushAuditResultResponse = (JxQueryPushAuditResultResponse)responseData1.getData();
                //推单外审结果
                Integer status = jxQueryPushAuditResultResponse.getStatus();
                //根据审核结果做处理
                BalanceOrder balanceOrder = new BalanceOrder();
                balanceOrder.setFundCode("jx");
                balanceOrder.setMobile(request.getMobile());
                balanceOrder.setName(request.getRealName());
                balanceOrder.setOrderNo((String)orderList.get(1));
                balanceOrder.setIfSuccess(1);

                if (status == 3){
                    JxPushAuditConfirmRequest jxPushAuditConfirmRequest = new JxPushAuditConfirmRequest();
                    jxPushAuditConfirmRequest.setOutOrderId(orderNo);
                    jxPushAuditConfirmRequest.setLoanOrderId(jxPushAuditResponse.getLoanOrderId());
                    ResponseData responseData2 = jxService.pushAuditConfirm(jxPushAuditConfirmRequest);
                    JxPushAuditConfirmResponse jxPushAuditConfirmResponse = (JxPushAuditConfirmResponse)responseData2.getData();
                    balanceOrder.setAssetNo(jxPushAuditConfirmResponse.getLoanId());
                    balanceOrder.setIfSuccess(0);
                    logger.info("推送资产成功assetId:" + jxPushAuditConfirmResponse.getLoanId());
                    //更改即信用户信息
                    ResponseData userJxByUserId = jxApi.getUserJxByUserId(userId);
                    List<UserJx> userJxs = ( List<UserJx>)userJxByUserId.getData();
                    UserJx userJx = userJxs.get(0);
                    //设置已过审
                    userJx.setStage(2);
                    //设置借款金额
                    userJx.setAmount(request.getAmount());
                    //设置标号
                    userJx.setLoanId(jxPushAuditConfirmResponse.getLoanId());
                    //修改该即信用户信息
                    jxApi.updateUserJx(userJx);
                    remitStatus = ResponseData.success();
                }else {
                    //此处是否进行转到qcgz暂时放着
                    //根据订单号修改order
                    ResponseData responseData2 = capitalOrderRelationContract.updateFundCode(orderNo);
                    if ("1".equals(responseData2.getStatus())){
                        logger.error("修改订单渠道时发生异常");
                        return ResponseData.error();
                    }
                    //生成七彩格子推单参数
                    List list = qcgzFundService.generateOrders(userId, orderNo, null);
                    //推单到七彩格子
                    return qcgzFundService.sendOrder(list);
                }

                try {
                    capitalOrderRelationContract.saveBalanceOrder(balanceOrder);
                } catch (Exception e) {
                    logger.error("保存资产订单发生异常！");
                }

            } catch (Exception e) {
                logger.error("推送资产到即信出错啦!", e);
            }

        return remitStatus;
    }

    @Override
    public boolean saveLoanResult(String result) {
        return false;
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
    public List generateOrders(String userId, String orderNo, Integer channel) throws ValidateException {
        List list = new ArrayList();
        try {

            OrderInfo orderInfo = orderContract.getOrderByOrderNo(orderNo).getData();
            logger.info("订单信息为:" + JSON.toJSON(orderInfo));

            ResponseData informationByUserId = jxApi.getInformationByUserId(userId);
            if (OpenPageConstant.STATUS_ONE.equals(informationByUserId.getStatus())) {
                logger.error("查询用户相关信息失败");
            }
            //获取用户详细信息
            UserDetailInfo userDetailInfo = userIdentityContract.getUserDetailInfo(userId).getData();
            //正面照
            String frontImg = deviceInfoContractNyd.getAttachmentModelUrl(userId,"1");
            //反面照
            String backImg = deviceInfoContractNyd.getAttachmentModelUrl(userId,"2");
            //获取用户的工作信息
            JobInfo jobInfo = userJobContract.getJobInfo(userId).getData();
            JxUserDetail userDetail = (JxUserDetail) informationByUserId.getData();
            int loopCount = 10;
            while (orderInfo == null && loopCount > 0) {
                // 防止dubbo出现错误
                orderInfo = orderContract.getOrderByOrderNo(orderNo).getData();
                loopCount--;
            }
            if (orderInfo == null) {
                logger.error("userId:" + userId + "orderNo:" + orderNo + "orderInfo为null");
            }
            JxPushAuditRequest jxPushAuditRequest = new JxPushAuditRequest();
            //手机号
            jxPushAuditRequest.setMobile(userDetail.getAccountNumber());
            //姓名
            jxPushAuditRequest.setRealName(userDetail.getRealName());
            //身份证号
            jxPushAuditRequest.setIdCardNumber(userDetail.getIdNumber());
            //借款期限
            jxPushAuditRequest.setPhaseCount(orderInfo.getBorrowTime());
            //借款金额
            jxPushAuditRequest.setAmount(orderInfo.getLoanAmount());
            //身份证正面
            jxPushAuditRequest.setIdCardPictureFront(frontImg);
            //身份证反面
            jxPushAuditRequest.setIdCardPictureBack(backImg);
            //活体
            jxPushAuditRequest.setLiveIdentification(userDetailInfo.getLivingAddress());
            //婚姻状况
            jxPushAuditRequest.setMarriageState(userDetailInfo.getMaritalStatus());
            //教育程度
            jxPushAuditRequest.setEducation(userDetailInfo.getHighestDegree());
            //职业
            jxPushAuditRequest.setJobOffice(jobInfo.getProfession());
            //工作城市
            jxPushAuditRequest.setJobCity(userDetailInfo.getLivingCity());

            list.add(jxPushAuditRequest);
            list.add(orderNo);
            list.add(userId);
        } catch (Exception e) {
            logger.error("generateOrders has error", e);
        }

        return list;
    }
}
