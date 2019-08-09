package com.nyd.capital.service.qcgz.impl;

import com.alibaba.fastjson.JSON;
import com.nyd.capital.api.service.QcgzApi;
import com.nyd.capital.model.RemitMessage;
import com.nyd.capital.model.enums.FundSourceEnum;
import com.nyd.capital.model.enums.RemitStatus;
import com.nyd.capital.model.qcgz.*;
import com.nyd.capital.model.qcgz.enums.BankNameResetEnum;
import com.nyd.capital.service.FundFactory;
import com.nyd.capital.service.FundService;
import com.nyd.capital.service.jx.config.OpenPageConstant;
import com.nyd.capital.service.qcgz.QcgzService;
import com.nyd.capital.service.qcgz.business.QcgzBusiness;
import com.nyd.capital.service.qcgz.component.QcgzComponent;
import com.nyd.capital.service.qcgz.config.QcgzConfig;
import com.nyd.capital.service.qcgz.utils.QcgzUtils;
import com.nyd.capital.service.validate.ValidateUtil;
import com.nyd.order.api.CapitalOrderRelationContract;
import com.nyd.order.api.OrderContract;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.enums.BorrowConfirmChannel;
import com.nyd.zeus.api.RemitContract;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("qcgzApi")
public class QcgzServiceImpl implements QcgzService, QcgzApi {
    Logger logger = LoggerFactory.getLogger(QcgzServiceImpl.class);

    @Autowired
    private QcgzComponent qcgzComponent;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private QcgzConfig qcgzConfig;

    @Autowired
    private ValidateUtil validateUtil;

    @Autowired
    private QcgzBusiness qcgzBusiness;

    @Autowired
    private CapitalOrderRelationContract capitalOrderRelationContract;

    @Autowired
    private RemitContract remitContract;
    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;
    @Autowired
    private OrderContract orderContract;
    @Autowired
    private FundFactory fundFactory;
    /**
     * 资产提交
     *
     * @param request
     * @return
     */
    @Override
    public ResponseData assetSubmit(SubmitAssetRequest request) {
        logger.info("给七彩格子提交资产,请求参数(缺少sign):" + JSON.toJSON(request));
        try {
            Map<String, String> assetSubmitMap = QcgzUtils.getMapWithoutSignSubmitAsset(request);
            logger.info("资产提交,请求参数添加到map集合:" + JSON.toJSON(assetSubmitMap));
            String sign = qcgzComponent.getSign(assetSubmitMap);
            logger.info("资产提交请求参数所需sign:" + sign);
            request.setSign(sign);
            logger.info("qcgz资产提交完整请求参数:" + JSON.toJSONString(request));

            //参数校验
            validateUtil.validate(request);

            //发起请求
            MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add("channelCode", request.getChannelCode());
            map.add("orderId", request.getOrderId());
            map.add("name", request.getName());

            if (StringUtils.isNotBlank(request.getInvestorList())) {
                map.add("investorList", request.getInvestorList());
            }

            map.add("bidType", String.valueOf(request.getPeriodsType()));
            map.add("sex", String.valueOf(request.getSex()));
            map.add("mobile", request.getMobile());
            map.add("idCardNumber", request.getIdCardNumber());
            map.add("bankName", getResetBankName(request.getBankName()));
            map.add("bankCardNo", request.getBankCardNo());
            if (StringUtils.isNotBlank(request.getLoanUse())) {
                map.add("loanUse", request.getLoanUse());
            }

            if (request.getIncome() != null) {
                map.add("income", String.valueOf(request.getIncome()));
            }

            if (StringUtils.isNotBlank(request.getAddress())) {
                map.add("address", request.getAddress());
            }

            if (StringUtils.isNotBlank(request.getBirthPlace())) {
                map.add("birthPlace", request.getBirthPlace());
            }

            map.add("marriageState", String.valueOf(request.getMarriageState()));
            map.add("periods", String.valueOf(request.getPeriods()));
            map.add("periodsType", String.valueOf(request.getPeriodsType()));
            map.add("amount", String.valueOf(request.getAmount()));
            map.add("rates", String.valueOf(request.getRates()));
            map.add("sign", request.getSign());
            logger.info("submit asset request map:" + JSON.toJSON(map));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(map, headers);

            ResponseEntity<SubmitAssetResponse> result = restTemplate.exchange(qcgzConfig.getSubmitAssetUrl(), HttpMethod.POST, entity, SubmitAssetResponse.class);
            SubmitAssetResponse submitAssetResponse = result.getBody();
            logger.info("提交资产后,返回的内容:" + JSON.toJSON(result.getBody()));
            if (submitAssetResponse.getStatus() == 0) {
                ResponseData responseData = ResponseData.success();
                SubmitAssetResponse.Datas data = submitAssetResponse.getData();
                String assetId = data.getAssetId();
                logger.info("资产编号:" + assetId);
                responseData.setData(data);
                return responseData;
            } else {
                logger.info("资产提交失败原因:" + result.getBody().getMsg());
                return ResponseData.error(result.getBody().getMsg());
            }
        } catch (Exception e) {
            logger.error("给七彩格子提交资产出错", e);
            return ResponseData.error("服务器开小差,请稍后再试");
        }

    }


    /**
     * 申请放款
     *
     * @param request
     * @return
     */
    @Override
    public ResponseData submitLoanApply(LoanApplyRequest request) {
        logger.info("给七彩格子申请放款,请求参数(缺少sign):" + JSON.toJSON(request));
        try {
            Map<String, String> loanMap = QcgzUtils.getMapWithoutSignSubmitLoanApply(request);
            logger.info("申请放款,请求参数添加到map集合:" + JSON.toJSON(loanMap));
            String sign = qcgzComponent.getSign(loanMap);
            logger.info("申请放款请求参数所需sign:" + sign);
            request.setSign(sign);
            logger.info("qcgz申请放款完整请求参数:" + JSON.toJSONString(request));

            //参数校验
            validateUtil.validate(request);

            //发起请求
            MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add("channelCode", request.getChannelCode());
            map.add("assetId", request.getAssetId());
            map.add("bankName", getResetBankName(request.getBankName()));
            map.add("bankCardNo", request.getBankCardNo());
            map.add("sign", request.getSign());
            logger.info("apply loan request map:" + JSON.toJSON(map));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(map, headers);
            ResponseEntity<LoanApplyResponse> result = restTemplate.exchange(qcgzConfig.getLoanApplyUrl(), HttpMethod.POST, entity, LoanApplyResponse.class);
            LoanApplyResponse loanApplyResponse = result.getBody();
            logger.info("申请放款后,返回的内容:" + JSON.toJSON(result.getBody()));
            if (loanApplyResponse.getStatus() == 0) {
                ResponseData responseData = ResponseData.success();
                LoanApplyResponse.Datas data = result.getBody().getData();
                String assetId = data.getAssetId();
                int loanStatus = data.getLoanStatus();
                logger.info("资产编号:" + assetId + ",放款处理状态:" + loanStatus);
                responseData.setData(data);
                return responseData;
            } else {
                logger.info("申请放款失败原因:" + result.getBody().getMsg());
                return ResponseData.error(result.getBody().getMsg());
            }


        } catch (Exception e) {
            logger.error("七彩格子申请放款出错", e);
            return ResponseData.error("服务器开小差,请稍后再试");
        }

    }


    /**
     * 查询放款状态
     *
     * @param request
     * @return
     */
    @Override
    public ResponseData queryLoanApplyResult(QueryLoanApplyResultRequest request) {
        logger.info("七彩格子查询放款状态,请求参数(缺少sign):" + JSON.toJSON(request));
        try {
            Map<String, String> queryMap = QcgzUtils.getMapWithoutSignQueryLoanApplyResult(request);
            logger.info("查询放款状态,请求参数添加到map集合:" + JSON.toJSON(queryMap));
            String sign = qcgzComponent.getSign(queryMap);
            logger.info("查询放款状态请求参数所需sign:" + sign);
            request.setSign(sign);
            logger.info("qcgz查询放款状态完整请求参数:" + JSON.toJSONString(request));

            //参数校验
            validateUtil.validate(request);

            //发起请求
            MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add("channelCode", request.getChannelCode());
            map.add("assetId", request.getAssetId());
            map.add("sign", request.getSign());
            logger.info("query loan result request map:" + JSON.toJSON(map));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(map, headers);
            ResponseEntity<QueryLoanApplyResultResponse> result = restTemplate.exchange(qcgzConfig.getQueryLoanApplyResultUrl(), HttpMethod.POST, entity, QueryLoanApplyResultResponse.class);
            QueryLoanApplyResultResponse queryLoanApplyResultResponse = result.getBody();
            logger.info("查询放款状态,返回的内容:" + JSON.toJSON(result.getBody()));
            if (queryLoanApplyResultResponse.getStatus() == 0) {
                ResponseData responseData = ResponseData.success();
                QueryLoanApplyResultResponse.Datas data = result.getBody().getData();
                String assetId = data.getAssetId();
                int loanStatus = data.getLoanStatus();
                String loanTime = data.getLoanTime();
                logger.info("资产编号:" + assetId + ",放款处理状态:" + loanStatus + ",放款时间:" + loanTime);
                responseData.setData(data);
                return responseData;
            } else {
                logger.info("查询放款状态失败原因:" + result.getBody().getMsg());
                return ResponseData.error(result.getBody().getMsg());
            }

        } catch (Exception e) {
            logger.error("七彩格子查询放款状态出错", e);
            return ResponseData.error("服务器开小差,请稍后再试");
        }

    }


    /**
     * 放款成功通知
     *
     * @param request
     * @return
     */
    @Override
    public ResponseData loanSucceesNotify(LoanSuccessNotifyRequest request) {
        ResponseData responseData = ResponseData.success();
        logger.info("七彩格子放款成功通知,请求参数:" + JSON.toJSON(request));
        if (!StringUtils.isNotBlank(request.getChannelCode())) {
            responseData = ResponseData.error("渠道号为空");
            return responseData;
        }

        if (!StringUtils.isNotBlank(request.getAssetId())) {
            responseData = ResponseData.error("资产编号为空");
            return responseData;
        }


        if (!StringUtils.isNotBlank(String.valueOf(request.getLoanResult()))) {
            responseData = ResponseData.error("放款状态为空");
            return responseData;
        }

        if (!StringUtils.isNotBlank(request.getSign())) {
            responseData = ResponseData.error("签名为空");
            return responseData;
        }

        try {
            String callBack = qcgzBusiness.callBack(request);
            responseData.setData(callBack);

        } catch (Exception e) {
            logger.error("七彩格子放款通知出错", e);
            return ResponseData.error("服务器开小差,请稍后再试");
        }

        return responseData;

    }

    @Override
    public ResponseData callbackForFail(LoanFailListRequest request) {
        try {
            ResponseData<List<OrderInfo>> orderInfoResponseData = capitalOrderRelationContract.selectOrderInfos(request.getAssetIds());
            if (OpenPageConstant.STATUS_ONE.equals(orderInfoResponseData.getStatus())) {
                return ResponseData.error("查询订单异常");
            }
            List<OrderInfo> orderInfos = orderInfoResponseData.getData();
            for (OrderInfo orderInfo : orderInfos) {
                //查询放款时间
                ResponseData responseData = remitContract.selectTime(orderInfo.getAssetNo());
                if (OpenPageConstant.STATUS_ONE.equals(responseData.getStatus())){
                    return ResponseData.error("查询放款时间失败");
                }
                Date remitTime = (Date) responseData.getData();
                RemitMessage remitMessage = new RemitMessage();
                remitMessage.setRemitStatus("0");
                remitMessage.setRemitAmount(orderInfo.getLoanAmount());
                remitMessage.setOrderNo(orderInfo.getOrderNo());
                remitMessage.setRemitTime(remitTime);
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

                remitContract.updateStatus(orderInfo.getAssetNo());

            }

        } catch (Exception e) {
            logger.error("七彩格子放款通知出错", e);
            return ResponseData.error("服务器开小差,请稍后再试");
        }
        return ResponseData.success();
    }
    
    /**
     * 获取七彩格子对应银行名称
     * @param bankName
     * @return
     */
    private String getResetBankName(String bankName) {
    	String reset = "";
    	BankNameResetEnum bank = BankNameResetEnum.getByValue(bankName);
    	if(bank != null) {
    		reset = bank.getResetName();
    	}else {
    		reset = bankName;
    	}
    	return reset;
    }

}
