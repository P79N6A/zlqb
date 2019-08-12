package com.nyd.capital.ws.controller;

import com.nyd.capital.model.enums.FundSourceEnum;
import com.nyd.capital.model.kzjr.BathFundCondition;
import com.nyd.capital.model.kzjr.BathFundResult;
import com.nyd.capital.model.kzjr.OpenPageInfo;
import com.nyd.capital.service.BathGetDataService;
import com.nyd.order.model.OrderDetailInfo;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.msg.OrderMessage;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: zhangp
 * @Description: 空中金融批量放款
 * @Date: 20:24 2018/5/14
 */
@RestController
@RequestMapping("/capital")
public class KzjrBathFundController {

    private static Logger logger = LoggerFactory.getLogger(KzjrBathFundController.class);
    @Autowired
    private BathGetDataService bathGetDataService;
    /**
     * 空中金融批量放款
     * @param bathFundCondition
     * @return
     */
    @RequestMapping(value = "/kzjr/bathfund" , method = RequestMethod.POST)
    public ResponseData getSmsCode(@RequestBody BathFundCondition bathFundCondition){
        ResponseData responseData = ResponseData.success();
        List<String> orderNos = bathFundCondition.getOrderNos();

        BathFundResult bathFundResult = new BathFundResult();
        List<String> sendOrderNos = bathFundResult.getSendOrderNos();
        Map<String , String> exceptionOrderNos = bathFundResult.getExceptionOrderNos();

        for(String orderNo : orderNos){
            ResponseData<OrderInfo> responseOrderInfo =  bathGetDataService.getByOrderNo(orderNo);
            sendOrderNos.add(orderNo);
            if(null == responseOrderInfo || !"0".equals(responseOrderInfo.getStatus())){
                exceptionOrderNos.put(orderNo,"获取订单信息失败,或者为null");
            }else{
                OrderInfo orderInfo = responseOrderInfo.getData();
                if(null!=orderInfo){
                    ResponseData<OrderDetailInfo> responseOrderDetailInfo=bathGetDataService.getDetailByOrderNo(orderNo);
                    if(null==responseOrderDetailInfo || !"0".equals(responseOrderDetailInfo.getStatus())){
                        logger.error("获取订单详情信息失败，orderNo:"+orderNo);
                        exceptionOrderNos.put(orderNo,"获取订单详情信息失败");
                    }else{
                        OrderDetailInfo orderDetailInfo = responseOrderDetailInfo.getData();
                        OrderMessage orderMessage = new OrderMessage();
                        orderMessage.setOrderNo(orderInfo.getOrderNo());
                        orderMessage.setUserId(orderInfo.getUserId());
                        orderMessage.setChannel(orderInfo.getChannel());
                        orderMessage.setFundCode(FundSourceEnum.KZJR.getCode());
                        ResponseData bathResponseData=bathGetDataService.assetSubmit(orderMessage,bathFundResult);
                    }
                }else{
                    exceptionOrderNos.put(orderNo,"获取订单信息为null");
                }
            }
        }
        responseData.setData(bathFundResult);
        return responseData;
    }
}
