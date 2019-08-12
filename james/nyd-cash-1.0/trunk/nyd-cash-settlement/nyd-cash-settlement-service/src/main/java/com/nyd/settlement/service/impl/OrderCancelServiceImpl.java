package com.nyd.settlement.service.impl;

import com.nyd.settlement.dao.OrderCancelDao;
import com.nyd.settlement.dao.ds.DataSourceContextHolder;
import com.nyd.settlement.dao.mapper.OrderCancelMapper;
import com.nyd.settlement.model.dto.OrderCancelDto;
import com.nyd.settlement.model.dto.OrderDto;
import com.nyd.settlement.model.dto.OrderStatusLogDto;
import com.nyd.settlement.model.dto.QueryDto;
import com.nyd.settlement.model.vo.OrderCancelVo;
import com.nyd.settlement.service.KzjrService;
import com.nyd.settlement.service.OrderCancelService;
import com.nyd.settlement.service.aspect.RoutingDataSource;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

/**
 * @author Peng
 * @create 2018-01-16 11:51
 **/
@Service
public class OrderCancelServiceImpl implements OrderCancelService {
    private static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(OrderCancelServiceImpl.class);

    @Autowired
    OrderCancelDao orderCancelDao;
    @Autowired
    OrderCancelMapper orderCancelMapper;
    @Autowired
    KzjrService kzjrService;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ORDER)
    @Override
    public ResponseData getOrderCancel(QueryDto dto) {
        ResponseData responseData = ResponseData.success();
        try {
            List<OrderCancelVo> cancelList = orderCancelDao.getOrderCancel(dto);
            responseData.setData(cancelList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseData;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ORDER)
    @Override
    public ResponseData cancelOrder(OrderCancelDto dto) {
        //获取订单状态，进行判断
//
        String orderStatus  = orderCancelMapper.queryOrderStatus(dto);
        System.out.print("账单状态:" + orderStatus);
        OrderCancelDto orderCancelDto = new OrderCancelDto();
        OrderDto orderDto = new OrderDto();
        try {
            if("30".equals(orderStatus)){
                //保存取消退款流水
                orderCancelDto.setOrderNo(dto.getOrderNo());
                orderCancelDto.setRealName(dto.getRealName());
                orderCancelDto.setMobile(dto.getMobile());
                orderCancelDto.setRemark(dto.getRemark());
                orderCancelDto.setUpdateBy(dto.getUpdateBy());
                orderCancelDto.setFailReason("");
                orderCancelDto.setStatus("0");
                orderCancelDao.save(orderCancelDto);
                //改变订单状态
                orderDto.setOrderNo(dto.getOrderNo());
                orderDto.setOrderStatus(40);
                orderDto.setPayFailReason(dto.getRemark());
                orderDto.setPayTime(new Date());
                orderDto.setFailType(2);
                orderCancelDao.updateOrder(orderDto);
                //记录订单流水
                OrderStatusLogDto orderStatusLogDto = new OrderStatusLogDto();
                orderStatusLogDto.setOrderNo(dto.getOrderNo());
                orderStatusLogDto.setBeforeStatus(30);
                orderStatusLogDto.setAfterStatus(40);
                orderStatusLogDto.setUpdateBy(dto.getUpdateBy());
                orderCancelDao.saveOrderStatusLog(orderStatusLogDto);
                //删除空中金融放款失败表
                kzjrService.deleteKzjr(dto.getOrderNo());
            }else if(null == orderStatus){
                return ResponseData.error("非法的订单号");
            }else{
                //保存取消退款流水（无效）
                orderCancelDto.setOrderNo(dto.getOrderNo());
                orderCancelDto.setRealName(dto.getRealName());
                orderCancelDto.setMobile(dto.getMobile());
                orderCancelDto.setRemark(dto.getRemark());
                orderCancelDto.setUpdateBy(dto.getUpdateBy());
                orderCancelDto.setFailReason("取消失败！该订单已放款！");
                orderCancelDto.setStatus("1");
                orderCancelDao.save(orderCancelDto);
                return ResponseData.error("取消失败！该订单已放款！");
            }
        } catch (Exception e) {
            LOGGER.error("取消失败" + e);
            return ResponseData.error("取消失败");
        }
        return ResponseData.success();
    }

}
