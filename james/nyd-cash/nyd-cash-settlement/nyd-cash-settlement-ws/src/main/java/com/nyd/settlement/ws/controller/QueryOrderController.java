package com.nyd.settlement.ws.controller;

import com.nyd.settlement.model.dto.OrderCancelDto;
import com.nyd.settlement.model.dto.QueryDto;
import com.nyd.settlement.service.OrderCancelService;
import com.nyd.settlement.service.QueryOrderService;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 * @author Peng
 * @create 2018-01-15 17:05
 **/
@RestController
@RequestMapping("/settlement")
public class QueryOrderController {
    private static Logger LOGGER = LoggerFactory.getLogger(QueryOrderController.class);
    @Autowired
    QueryOrderService queryOrderService;
    @Autowired
    OrderCancelService orderCancelService;

    /**
     * 订单数据查询
     * @param dto
     * @return
     */
    @RequestMapping("/queryOrder")
    public ResponseData queryOrder(@RequestBody QueryDto dto){
        try {
            ResponseData responseData = ResponseData.success();
            responseData.setData(queryOrderService.findPage(dto));
            return responseData;
        } catch (ParseException e) {
            LOGGER.error("订单数据查询报错",e);
            return ResponseData.error("订单数据查询查询报错");
        }
    }

    /**
     * 取消放款流水查询
     * @param dto
     * @return
     */
    @RequestMapping("/queryOrderCancel ")
    public ResponseData queryOrderCancel(@RequestBody QueryDto dto){
        try {
            return orderCancelService.getOrderCancel(dto);
        } catch (Exception e) {
            LOGGER.error("取消放款流水查询报错",e);
            return ResponseData.error("取消放款流水查询报错");
        }
    }

    /**
     * 待放款订单查询
     * @param dto
     * @return
     */
    @RequestMapping("/queryWaitOrder")
    public ResponseData queryWaitOrder(@RequestBody QueryDto dto){
        try {
            ResponseData responseData = ResponseData.success();
            responseData.setData(queryOrderService.waitOrder(dto));
            return responseData;
        } catch (Exception e) {
            LOGGER.error("待放款订单查询报错",e);
            return ResponseData.error("待放款订单查询报错");
        }
    }

    /**
     * 取消放款
     * @param dto
     * @return
     */
    @RequestMapping("/cancelOrder")
    public ResponseData cancelOrder(@RequestBody OrderCancelDto dto,HttpServletRequest request){
        String updateBy = request.getHeader("accountNo");
        dto.setUpdateBy(updateBy);
        try {
            return orderCancelService.cancelOrder(dto);
        } catch (Exception e) {
            LOGGER.error("取消放款报错",e);
            return ResponseData.error("取消放款报错");
        }
    }
}
