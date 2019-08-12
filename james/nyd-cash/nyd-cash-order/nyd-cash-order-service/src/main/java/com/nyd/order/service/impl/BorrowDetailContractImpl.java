package com.nyd.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.nyd.order.api.BorrowDetailContract;
import com.nyd.order.dao.OrderDao;
import com.nyd.order.dao.mapper.OrderMapper;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.vo.BorrowDetailVo;
import com.nyd.order.service.consts.OrderConsts;
import com.nyd.order.service.util.DateUtil;
import com.tasfe.framework.support.model.ResponseData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "borrowDetailContract")
public class BorrowDetailContractImpl implements BorrowDetailContract {
    private static Logger logger = LoggerFactory.getLogger(BorrowDetailContractImpl.class);

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 根据根据订单编号查找借款详情
     * @param orderNo
     * @return
     */
    @Override
    public ResponseData<BorrowDetailVo> getBorrowDetailByOrderNo(String orderNo) {
        logger.info("begin to get borrow detail, orderNo is " + orderNo);
        ResponseData responseData = ResponseData.success();
        if(orderNo == null){
            return responseData;
        }
        BorrowDetailVo borrowDetail = new BorrowDetailVo();
        try {
//            List<OrderInfo> detailList = orderDao.getObjectsByOrderNo(orderNo);
            List<OrderInfo> dataList = orderMapper.getObjectsByOrderNo(orderNo);
            if(dataList != null && dataList.size()>0){
                OrderInfo orderInfo = dataList.get(0);
                logger.info(""+ JSON.toJSONString(orderInfo));
//                BeanUtils.copyProperties(orderInfo,borrowDetail);
                borrowDetail.setOrderNo(orderInfo.getOrderNo());
                borrowDetail.setInterest(orderInfo.getInterest());
                borrowDetail.setRepayTotalAmount(orderInfo.getRepayTotalAmount());
                borrowDetail.setLoanAmount(orderInfo.getLoanAmount());
                borrowDetail.setOrderStatus(orderInfo.getOrderStatus());
                borrowDetail.setBorrowTime(orderInfo.getBorrowTime());
                borrowDetail.setLoanTime(DateUtil.dateToString(orderInfo.getLoanTime()));
                borrowDetail.setPayTime(DateUtil.dateToString(orderInfo.getPayTime()));
                borrowDetail.setPromiseRepaymentTime(DateUtil.dateToString(orderInfo.getPromiseRepaymentTime()));
                borrowDetail.setActualRepaymentTime(DateUtil.dateToString(orderInfo.getActualRepaymentTime()));
                logger.info("借款详情："+JSON.toJSONString(borrowDetail));
                responseData.setData(borrowDetail);
            }
            logger.info("get borrow detail success !");
        } catch (Exception e) {
            logger.error("get orderDetailInfo failed, orderNo = " + orderNo ,e);
            responseData = ResponseData.error(OrderConsts.DB_ERROR_MSG);
        }
        return responseData;
    }

    /**
     * 根据根据银码头订单号查找借款详情
     * @param ibankOrderNo
     * @return
     */
    @Override
    public ResponseData<BorrowDetailVo> getBorrowDetailByIbankOrderNo(String ibankOrderNo) {
        logger.info("begin to get borrow detail, orderNo is " + ibankOrderNo);
        ResponseData responseData = ResponseData.success();
        if(ibankOrderNo == null){
            return responseData;
        }
        BorrowDetailVo borrowDetail = new BorrowDetailVo();
        try {
//            List<OrderInfo> detailList = orderDao.getObjectsByIbankOrderNo(ibankOrderNo);
            List<OrderInfo> detailList = orderMapper.getObjectsByIbankOrderNo(ibankOrderNo);
            if(detailList != null && detailList.size()>0){
                OrderInfo orderInfo = detailList.get(0);
                logger.info(""+ JSON.toJSONString(orderInfo));
//                BeanUtils.copyProperties(orderInfo,borrowDetail);
                borrowDetail.setOrderNo(orderInfo.getOrderNo());
                borrowDetail.setInterest(orderInfo.getInterest());
                borrowDetail.setRepayTotalAmount(orderInfo.getRepayTotalAmount());
                borrowDetail.setLoanAmount(orderInfo.getLoanAmount());
                borrowDetail.setOrderStatus(orderInfo.getOrderStatus());
                borrowDetail.setBorrowTime(orderInfo.getBorrowTime());
                borrowDetail.setLoanTime(DateUtil.dateToString(orderInfo.getLoanTime()));
                borrowDetail.setPayTime(DateUtil.dateToString(orderInfo.getPayTime()));
                borrowDetail.setPromiseRepaymentTime(DateUtil.dateToString(orderInfo.getPromiseRepaymentTime()));
                borrowDetail.setActualRepaymentTime(DateUtil.dateToString(orderInfo.getActualRepaymentTime()));
                logger.info("借款详情："+JSON.toJSONString(borrowDetail));
                responseData.setData(borrowDetail);
            }
            logger.info("get borrow detail success !");
        } catch (Exception e) {
            logger.error("get orderDetailInfo failed, orderNo = " + ibankOrderNo ,e);
            responseData = ResponseData.error(OrderConsts.DB_ERROR_MSG);
        }
        return responseData;
    }
}
