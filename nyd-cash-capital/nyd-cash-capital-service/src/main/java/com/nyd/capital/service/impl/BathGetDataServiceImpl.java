package com.nyd.capital.service.impl;

import com.alibaba.fastjson.JSON;
import com.nyd.capital.model.enums.FundSourceEnum;
import com.nyd.capital.model.enums.RemitStatus;
import com.nyd.capital.model.kzjr.BathFundResult;
import com.nyd.capital.service.BathGetDataService;
import com.nyd.capital.service.FundFactory;
import com.nyd.capital.service.FundService;
import com.nyd.capital.service.UserAccountService;
import com.nyd.order.api.OrderContract;
import com.nyd.order.api.OrderDetailContract;
import com.nyd.order.model.OrderDetailInfo;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.msg.OrderMessage;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 21:22 2018/5/14
 */
@Service
public class BathGetDataServiceImpl implements BathGetDataService {

    private static Logger logger = LoggerFactory.getLogger(BathGetDataServiceImpl.class);

    @Autowired
    private OrderContract orderContract;

    @Autowired
    private FundFactory fundFactory;

    @Autowired
    private OrderDetailContract orderDetailContract;

    @Autowired
    private UserAccountService userAccountService;

    @Override
    public ResponseData<OrderInfo> getByOrderNo(String OrderNo) {
        ResponseData responseData = ResponseData.success();
        if(StringUtils.isBlank(OrderNo)){
            responseData = ResponseData.error("订单号为空");
            return responseData;
        }
        return orderContract.getOrderByOrderNo(OrderNo);
    }

    @Override
    public ResponseData<OrderDetailInfo> getDetailByOrderNo(String OrderNo) {
        return orderDetailContract.getOrderDetailByOrderNo(OrderNo);
    }

    @Override
    public ResponseData assetSubmit(OrderMessage message,BathFundResult bathFundResult) {
        logger.info("组装参数 message："+ JSON.toJSONString(message)+" bathFundResult:"+JSON.toJSONString(bathFundResult));
        ResponseData responseData = ResponseData.success();
        String userId = message.getUserId();
        String orderNo = message.getOrderNo();
        String fundCode = message.getFundCode();

        FundService fundService = fundFactory.buildChannel(FundSourceEnum.KZJR);

        List<String> sendOrderNos = bathFundResult.getSendOrderNos();
        Map<String , String> exceptionOrderNos = bathFundResult.getExceptionOrderNos();
        List orders = null;
        try {
            orders = fundService.generateOrders(userId,orderNo,message.getChannel()); // 拼装所有请求空中金融的订单列表
        } catch (Exception e) {
            responseData = ResponseData.error();
            logger.error(userId + "-" + orderNo + "生成订单错误" + e.getMessage());
        }
        if(orders == null||orders.size()==0){
            logger.error("组装订单数据失败"+orderNo);
            exceptionOrderNos.put(orderNo,"组装订单数据失败");
        }else{
            ResponseData result = fundService.sendOrder(orders); // 推送资产到资金方
            if(result == ResponseData.success()){
                logger.info("提交成功等待kzjr异步通知");
            }else{
                logger.error("kzjr提交订单返回失败" + orderNo);
                exceptionOrderNos.put(orderNo,"kzjr提交订单返回失败");
            }
        }
        responseData.setData(bathFundResult);
        return responseData;
    }
}
