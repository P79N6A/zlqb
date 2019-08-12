package com.nyd.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.nyd.order.api.OrderWentongContract;
import com.nyd.order.dao.OrderWentongDao;
import com.nyd.order.dao.mapper.ProportionMapper;
import com.nyd.order.entity.OrderWentong;
import com.nyd.order.entity.Proportion;
import com.nyd.order.model.Gem;
import com.nyd.order.model.OrderWentongInfo;
import com.nyd.order.service.consts.OrderConsts;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Cong Yuxiang
 * 2018/4/24
 **/
@Service("orderWentongContract")
public class OrderWentongContractImpl implements OrderWentongContract {
    final Logger LOGGER = LoggerFactory.getLogger(OrderWentongContractImpl.class);
    @Autowired
    private OrderWentongDao orderWentongDao;
    @Autowired
    private ProportionMapper proportionMapper;

    @Override
    public ResponseData<List<OrderWentongInfo>> getOrderWTByTime(String startDate, String endDate, String name, String mobile) {
        LOGGER.info(name + mobile + "api begin to getOrderWTByTime::: " + startDate + "~" + endDate);
        if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
            ResponseData responseData = ResponseData.error("日期为空");
            responseData.setData(new ArrayList<OrderWentongInfo>());
            return responseData;
        }
        ResponseData responseData = ResponseData.success();
        try {
            List<OrderWentongInfo> detailList = orderWentongDao.getOrderWTByTime(startDate, endDate, name, mobile);
            responseData.setData(detailList);
            LOGGER.info("api getOrderWTByTime  success !"+ JSON.toJSONString(detailList));
        } catch (Exception e) {
            responseData = ResponseData.error(OrderConsts.DB_ERROR_MSG);
            LOGGER.error("api getOrderWTByTime error", e);
        }
        return responseData;
    }

    @Override
    public ResponseData save(OrderWentongInfo orderWentongInfo) {
        LOGGER.info("保存稳通信息"+JSON.toJSONString(orderWentongInfo));
        if (orderWentongInfo != null) {
            try {
                OrderWentong orderWentong = new OrderWentong();
                orderWentong.setLoanTime(DateUtils.parseDate(orderWentongInfo.getLoanTime(), "yyyy-MM-dd HH:mm:ss"));
                orderWentong.setMobile(orderWentongInfo.getMobile());
                orderWentong.setName(orderWentongInfo.getName());
                orderWentong.setOrderNo(orderWentongInfo.getOrderNo());
                orderWentong.setUserId(orderWentongInfo.getUserId());
                orderWentongDao.save(orderWentong);
                LOGGER.info(orderWentong.getMobile()+"保存稳通信息成功");
                return ResponseData.success();
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("保存报异常", e);
                return ResponseData.error(e.getMessage());
            }
        } else {
            return ResponseData.error("对象为空");
        }
    }

    /*@Override
    public void save(OrderWentong orderWentong) throws Exception {
        orderWentongDao.save(orderWentong);
    }*/


    @Override
    public ResponseData<String> getChannel() {
        LOGGER.info("api begin to get channel");
        ResponseData responseData = ResponseData.success();
        try {
            List<Proportion> proportions = proportionMapper.selectDate();
            Proportion proportion = null;
            if (proportions.size() != 0) {
                proportion = proportions.get(0);
            } else {
                proportion = proportionMapper.selectDefaultRadio();
            }

            //分流逻辑
            int random = new Random().nextInt(10);
            // 比例
            int prizeRate = 0;
            //设置分流概率
            List<Gem> gums = new ArrayList<>();
            gums.add(new Gem("wt", proportion.getStableRatio()));
            gums.add(new Gem("kzjr", proportion.getSkyRatio()));
            gums.add(new Gem("c", proportion.getCRatio()));
            gums.add(new Gem("d", proportion.getDRatio()));

            String channel = null;
            Iterator<Gem> it = gums.iterator();
            while (it.hasNext()) {
                Gem gem = it.next();
                prizeRate += gem.getPriority();
                if (random < prizeRate) {
                    channel = gem.getName();
                    break;
                }
            }
            responseData.setData(channel);
        } catch (Exception e) {
            e.printStackTrace();
            responseData = ResponseData.error(e.getMessage());
            LOGGER.error("api get channel error!", e);
        }

        return responseData;
    }

}
