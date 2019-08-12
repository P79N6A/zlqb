package com.nyd.capital.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.nyd.capital.model.qcgz.SubmitAssetRequest;
import com.nyd.capital.model.qcgz.SubmitAssetResponse;
import com.nyd.capital.service.qcgz.QcgzService;
import com.nyd.capital.service.qcgz.config.QcgzConfig;
import com.nyd.order.api.CapitalOrderRelationContract;
import com.nyd.order.api.OrderContract;
import com.nyd.order.api.OrderExceptionContract;
import com.nyd.order.entity.BalanceOrder;
import com.nyd.order.model.OrderInfo;
import com.nyd.user.api.UserAccountContract;
import com.nyd.user.api.UserBankContract;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.model.BankInfo;
import com.nyd.user.model.UserInfo;
import com.nyd.user.model.dto.AccountDto;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nyd.capital.model.WsmQuery;
import com.nyd.capital.service.FundService;
import com.nyd.capital.service.validate.ValidateException;

/**
 * @author dell
 * 七彩格子对接
 */
@Service
public class QcgzFundService implements FundService {
    Logger logger = LoggerFactory.getLogger(QcgzFundService.class);

    @Autowired(required = false)
    private OrderContract orderContract;

    @Autowired
    private QcgzConfig qcgzConfig;

    @Autowired
    private UserIdentityContract userIdentityContract;

    @Autowired
    private UserAccountContract userAccountContract;

    @Autowired
    private UserBankContract userBankContract;

    @Autowired
    private QcgzService qcgzService;

    @Autowired
    private CapitalOrderRelationContract capitalOrderRelationContract;
    
    @Autowired(required = false)
    private OrderExceptionContract orderExceptionContract;



    /**
     * 推送资产到七彩格子
     *
     * @param orderList 订单列表
     * @return
     */
    @Override
    public ResponseData sendOrder(List orderList) {
        logger.info("sendOrder params:" + JSON.toJSON(orderList));
        ResponseData remitStatus = ResponseData.success();
        SubmitAssetRequest request = (SubmitAssetRequest) orderList.get(0);
        logger.info("推送资产为:" + JSON.toJSONString(request));

        //借款请求成功
        OrderInfo orderInfo = null;
        try {
            orderInfo = orderContract.getOrderByOrderNo(request.getOrderId()).getData();
            if (orderInfo == null) {
                logger.info("根据订单号号查询订单不存在");
                return ResponseData.error();
            }
        } catch (Exception e) {
            logger.error("根据订单号查询订单发生异常");
            return ResponseData.error();
        }
        logger.info("orderInfo:" + JSON.toJSONString(orderInfo));

        try {
            //资产提交
            String assetId = "";
            ResponseData responseData = qcgzService.assetSubmit(request);
            BalanceOrder balanceOrder = new BalanceOrder();
            balanceOrder.setFundCode("qcgz");
            balanceOrder.setLoanTime(new Date());
            balanceOrder.setMobile(request.getMobile());
            balanceOrder.setName(request.getName());
            balanceOrder.setOrderNo(request.getOrderId());
            if ("0".equals(responseData.getStatus())) {
                SubmitAssetResponse.Datas data = (SubmitAssetResponse.Datas) responseData.getData();
                assetId = data.getAssetId();
                logger.info("推送资产成功assetId:" + assetId);
                balanceOrder.setAssetNo(assetId);
                remitStatus = ResponseData.success();
                try {
                    /**最好新建一个dubbo接口,与你所建表对应的实体类名字相对应,免的后面的人看不懂*/
                    capitalOrderRelationContract.saveBalanceOrder(balanceOrder);
                } catch (Exception e) {
                    logger.error("保存资产订单发生异常！");
                }
            } else {
                orderInfo.setOrderStatus(40);
              //生成异常订单记录
                try {
                	orderExceptionContract.saveByOrderInfo(orderInfo);
                }catch(Exception e) {
                	logger.error("生成异常订单信息异常：" + e.getMessage());
                }
                orderContract.updateOrderInfo(orderInfo);
                remitStatus.setStatus("2");
                return remitStatus;
            }

        } catch (Exception e) {
            logger.error("推送资产到七彩格子出错啦!", e);
        }

        return remitStatus;

    }

    @Override
    public boolean saveLoanResult(String result) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String queryOrderInfo(WsmQuery query) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List generateOrdersTest() {
        // TODO Auto-generated method stub
        return null;
    }


    /**
     * 推送到七彩格子的订单信息
     *
     * @param userId  UserId
     * @param orderNo 订单号
     * @param channel
     * @return
     * @throws ValidateException
     */
    @Override
    public List generateOrders(String userId, String orderNo, Integer channel) throws ValidateException {
        List list = new ArrayList();
        try {

            OrderInfo orderInfo = orderContract.getOrderByOrderNo(orderNo).getData();
            logger.info("订单信息:" + JSON.toJSON(orderInfo));

            UserInfo userInfo = null;
            ResponseData<UserInfo> responseData = userIdentityContract.getUserInfo(userId);
            if ("0".equals(responseData.getStatus())) {
                userInfo = responseData.getData();
                logger.info("userId用户身份证信息:" + JSON.toJSON(userInfo));
            }

            AccountDto accountDto = null;
            ResponseData<AccountDto> data = userAccountContract.getAccount(userId);
            if ("0".equals(data.getStatus())) {
                accountDto = data.getData();
                logger.info("userId用户账号信息:" + JSON.toJSON(userInfo));
            }

            BankInfo bankInfo = null;
            ResponseData<List<BankInfo>> bankInfos = userBankContract.getBankInfos(userId);
            if ("0".equals(data.getStatus())) {
                List<BankInfo> bankInfoList = bankInfos.getData();
                bankInfo = bankInfoList.get(0);
            }

            int loopCount = 10;
            while (orderInfo == null && loopCount > 0) {
                orderInfo = orderContract.getOrderByOrderNo(orderNo).getData();
                loopCount--;
            }
            if (orderInfo == null) {
                logger.error("userId:" + userId + "orderNo:" + orderNo + "orderInfo为null");
            }

            SubmitAssetRequest request = new SubmitAssetRequest();
            //渠道号
            request.setChannelCode(qcgzConfig.getChannelCode());
            //侬要贷单号
            request.setOrderId(orderNo);

            //姓名
            if (StringUtils.isNotBlank(userInfo.getRealName())) {
                request.setName(userInfo.getRealName());
            }

            // 性别   1 :表示男性    2: 表示女性
            if (StringUtils.isNotBlank(userInfo.getGender())) {
                if ("男".equals(userInfo.getGender())) {
                    request.setSex(1);
                } else if ("女".equals(userInfo.getGender())) {
                    request.setSex(2);
                }
            }

            //手机号
            if (StringUtils.isNotBlank(accountDto.getAccountNumber())) {
                request.setMobile(accountDto.getAccountNumber());
            }

            //身份证号
            if (StringUtils.isNotBlank(userInfo.getIdNumber())) {
                request.setIdCardNumber(userInfo.getIdNumber());
            }

            //银行名称         
            request.setBankName(orderInfo.getBankName());          

            //银行卡号          
            request.setBankCardNo(orderInfo.getBankAccount());
            

            //借款期限
            request.setPeriods(orderInfo.getBorrowTime());

            //期限类型
            request.setPeriodsType(orderInfo.getBorrowPeriods());

            //借款金额
            if (orderInfo.getLoanAmount() != null) {
                request.setAmount(orderInfo.getLoanAmount());
            }

            //标的类型
            request.setBidType("1");


            if (orderInfo.getAnnualizedRate() != null) {
                request.setRates(orderInfo.getAnnualizedRate());
            }

            list.add(request);
        } catch (Exception e) {
            logger.error("generateOrders has error", e);
        }

        return list;
    }

}
