package com.nyd.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.nyd.order.api.CapitalOrderRelationContract;
import com.nyd.order.dao.BalanceOrderDao;
import com.nyd.order.dao.CapitalOrderRelationDao;
import com.nyd.order.dao.mapper.BalanceOrderMapper;
import com.nyd.order.dao.mapper.OrderMapper;
import com.nyd.order.entity.BalanceOrder;
import com.nyd.order.entity.CapitalOrderRelation;
import com.nyd.order.model.CapitalOrderRelationInfo;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.dto.CapitalOrderRelationDto;
import com.nyd.order.model.dto.OrderQcgzDto;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liuqiu
 */
@Service(value = "capitalOrderRelationContract")
public class CapitalOrderRelationContractImpl implements CapitalOrderRelationContract {
    private static Logger logger = LoggerFactory.getLogger(CapitalOrderRelationContractImpl.class);

    @Autowired
    private CapitalOrderRelationDao capitalOrderRelationDao;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private BalanceOrderDao balanceOrderDao;
    @Autowired
    private BalanceOrderMapper balanceOrderMapper;


    @Override
    public ResponseData saveCapitalOrderRelation(CapitalOrderRelationInfo capitalOrderRelationInfo) {
        logger.info("保存渠道资产和侬要贷订单关联关系入参:" + JSON.toJSON(capitalOrderRelationInfo));
        ResponseData responseData = ResponseData.success();
        CapitalOrderRelation capitalOrderRelation = new CapitalOrderRelation();
        try {
            BeanUtils.copyProperties(capitalOrderRelationInfo, capitalOrderRelation);
            capitalOrderRelationDao.save(capitalOrderRelation);
        } catch (Exception e) {
            logger.error("save capitalOrderRelation fail", e);
            e.printStackTrace();
        }

        return responseData;
    }

    /**
     * 根据资产编号找到对应的渠道信息
     *
     * @param assetId
     * @return
     */
    @Override
    public ResponseData<CapitalOrderRelationDto> getCapitalOrderRelation(String assetId) {
        ResponseData responseData = ResponseData.success();
        try {
            List<CapitalOrderRelationDto> list = capitalOrderRelationDao.getCapitalOrderRelationByAssetId(assetId);
            if (list != null && list.size() > 0) {
                CapitalOrderRelationDto dto = list.get(0);
                responseData.setData(dto);
            }

        } catch (Exception e) {
            logger.error("getCapitalOrderRelation has error", e);
            e.printStackTrace();
        }
        return responseData;
    }

    @Override
    public ResponseData<OrderQcgzDto> selectAssetNo(String orderNo) {
        ResponseData<OrderQcgzDto> data = new ResponseData<>();
        OrderQcgzDto orderQcgzDto = orderMapper.selectAssetNo(orderNo);
        return data.setData(orderQcgzDto);
    }

    @Override
    public ResponseData<OrderInfo> selectOrderInfo(String assetNo) {
        ResponseData<OrderInfo> data = new ResponseData<>();
        OrderInfo orderInfo = orderMapper.selectOrderInfo(assetNo);
        return data.setData(orderInfo);
    }

    @Override
    public ResponseData<List<OrderInfo>> selectOrderInfos(List<String> assetIds) {
        ResponseData<List<OrderInfo>> data = new ResponseData<>();
        List<OrderInfo> orderInfos = orderMapper.selectOrderInfos(assetIds);
        return data.setData(orderInfos);
    }

    @Override
    public ResponseData updateFundCode(String orderNo) {
        try {
            logger.info("进行修改订单渠道：" + JSON.toJSONString(orderNo));
            orderMapper.updateFundCode(orderNo);
        } catch (Exception e) {
            logger.error("保进行修改订单渠道发生异常",e);
            return ResponseData.error("进行修改订单渠道发生异常" +e.getMessage());
        }
        return ResponseData.success();
    }

    @Override
    public ResponseData selectOrderInfosFromJx() {
        try {
            logger.info("查询当天即信需要还款的订单");
            List<OrderInfo> list = orderMapper.selectOrderInfosFromJx();
            return ResponseData.success(list);
        } catch (Exception e) {
            logger.error("查询当天即信需要还款的订单发生异常",e);
            return ResponseData.error("查询当天即信需要还款的订单发生异常" +e.getMessage());
        }
    }

    @Override
    public ResponseData saveBalanceOrder(BalanceOrder balanceOrder) {
        try {
            logger.info("进行资产订单保存：" + JSON.toJSONString(balanceOrder));
            balanceOrderDao.save(balanceOrder);
        } catch (Exception e) {
            logger.error("保存资产订单发生异常");
            return ResponseData.error("保存资产订单发生异常");
        }
        return ResponseData.success();
    }

    @Override
    public ResponseData setOrderNoForJx(String orderNo, String userId) {
        try {
            logger.info("进行资产订单号保存：" + JSON.toJSONString(userId));
            balanceOrderMapper.setOrderNoForJx(orderNo,userId);
        } catch (Exception e) {
            logger.error("保存资产订单号发生异常");
            return ResponseData.error("保存资产订单号发生异常");
        }
        return ResponseData.success();
    }

}
