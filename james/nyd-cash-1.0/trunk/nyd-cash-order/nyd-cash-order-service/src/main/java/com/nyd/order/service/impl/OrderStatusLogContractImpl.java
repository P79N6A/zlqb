package com.nyd.order.service.impl;

import com.nyd.order.api.OrderStatusLogContract;
import com.nyd.order.dao.OrderStatusLogDao;
import com.nyd.order.model.OrderStatusLogInfo;
import com.nyd.order.service.OrderStatusLogService;
import com.nyd.order.service.consts.OrderConsts;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dengw on 2017/11/14
 */
@Service(value = "orderStatusLogContract")
public class OrderStatusLogContractImpl implements OrderStatusLogService,OrderStatusLogContract {
    private static Logger LOGGER = LoggerFactory.getLogger(OrderStatusLogContractImpl.class);

    @Autowired
    private OrderStatusLogDao orderStatusLogDao;

    @Override
    public ResponseData<List<OrderStatusLogInfo>> getOrderStatusLogByOrderNo(String orderNo) {
        LOGGER.info("api begin to get orderStatusLog, orderNo is " + orderNo);
        ResponseData responseData = ResponseData.success();
        try {
            List<OrderStatusLogInfo> statusList = orderStatusLogDao.getObjectsByOrderNo(orderNo);
            responseData.setData(statusList);
            LOGGER.info("api get orderStatusLog by orderNo success !");
        } catch (Exception e) {
            LOGGER.error("api get orderStatusLog by orderNo error! orderNo = " + orderNo, e);
            responseData = ResponseData.error(OrderConsts.DB_ERROR_MSG);
        }
        return responseData;
    }
}
