package com.nyd.order.service.impl;

import com.nyd.order.api.OrderDetailContract;
import com.nyd.order.dao.OrderDetailDao;
import com.nyd.order.model.OrderDetailInfo;
import com.nyd.order.service.OrderDetailInfoService;
import com.nyd.order.service.consts.OrderConsts;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Dengw on 2017/11/14
 */
@Service(value = "orderDetailContract")
public class OrderDetailContractImpl implements OrderDetailInfoService,OrderDetailContract {
    private static Logger LOGGER = LoggerFactory.getLogger(OrderDetailContractImpl.class);
    @Autowired
    private OrderDetailDao orderDetailDao;

    @Override
    public ResponseData<OrderDetailInfo> getOrderDetailByOrderNo(String orderNo) {
        LOGGER.info("api begin to get orderDetail, orderNo is " + orderNo);
        ResponseData responseData = ResponseData.success();
        try {
            List<OrderDetailInfo> detailList = orderDetailDao.getObjectsByOrderNo(orderNo);
            if(detailList != null && detailList.size()>0){
                responseData.setData(detailList.get(0));
            }
            LOGGER.info("api get orderDetail by orderNo success !");
        } catch (Exception e) {
            responseData = ResponseData.error(OrderConsts.DB_ERROR_MSG);
            LOGGER.error("api get orderDetail by orderNo error! orderNo = " + orderNo, e);
        }
        return responseData;
    }

    @Override
    public ResponseData<List<OrderDetailInfo>> getOrderDetailsByUserId(String userId) {
        LOGGER.info("api begin to get orderDetail, userId is " + userId);
        ResponseData responseData = ResponseData.success();
        try {
            List<OrderDetailInfo> detailList = orderDetailDao.getObjectsByUserId(userId);
            responseData.setData(detailList);
            LOGGER.info("api get orderDetail by userId success !");
        } catch (Exception e) {
            responseData = ResponseData.error(OrderConsts.DB_ERROR_MSG);
            LOGGER.error("api get orderDetail by userId error! userId = " + userId, e);
        }
        return responseData;
    }

    @Override
    public ResponseData<List<OrderDetailInfo>> getOrderDetailsByIdCardNo(String idNumber) {
        LOGGER.info("api begin to get orderDetail, idCard is " + idNumber);
        ResponseData responseData = ResponseData.success();
        if (StringUtils.isBlank(idNumber)) {
            responseData = ResponseData.error(OrderConsts.NO_PRAMA);
            return responseData;
        }
        try {
            List<OrderDetailInfo> detailList = orderDetailDao.getOrderDetailsByIdCardNo(idNumber);
            responseData.setData(detailList);
            LOGGER.info("api get orderDetail by idCard success !");
        } catch (Exception e) {
            responseData = ResponseData.error(OrderConsts.DB_ERROR_MSG);
            LOGGER.error("api get orderDetails by idCard error! idCard = " + idNumber, e);
        }

        return responseData;
    }

    @Override
    public ResponseData<List<OrderDetailInfo>> getOrderDetailsByMobile(String mobile) {
        LOGGER.info("api begin to get orderDetail, mobile is " + mobile);
        ResponseData responseData = ResponseData.success();
        if (StringUtils.isBlank(mobile)) {
            responseData = ResponseData.error(OrderConsts.NO_PRAMA);
            return responseData;
        }
        try {
            List<OrderDetailInfo> detailList = orderDetailDao.getOrderDetailsByMobile(mobile);
            responseData.setData(detailList);
            LOGGER.info("api get orderDetail by mobile success !");
        } catch (Exception e) {
            responseData = ResponseData.error(OrderConsts.DB_ERROR_MSG);
            LOGGER.error("api get orderDetails by mobile error! mobile = " + mobile, e);
        }

        return responseData;
    }

	@Override
	public ResponseData updateClickVip(String orderNo) {
		LOGGER.info("更新是否认领VIP新口子标识订单号：" + orderNo);
		ResponseData responseData = ResponseData.success();
		OrderDetailInfo info  = new OrderDetailInfo();
		info.setOrderNo(orderNo);
		info.setClickVipFlag(1);
		try {
			orderDetailDao.update(info);
		} catch (Exception e) {
			LOGGER.error("更新是否使用vip新口子标识失败：" + e.getMessage());
			return responseData.error("更新是否使用VIP新口子失败");
		}
		return responseData;
		
	}
}
