//package com.nyd.order.ws.controller;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//import com.nyd.order.model.OrderCheckQuery;
//import com.nyd.order.model.vo.OrderCheckVo;
//import com.nyd.order.service.OrderCheckService;
//import com.tasfe.framework.support.model.ResponseData;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@RestController
//@RequestMapping("/order")
//public class OrderCheckController {
//	private static final Logger LOGGER = LoggerFactory.getLogger(OrderCheckController.class);
//	
//	@Autowired
//    private OrderCheckService orderCheckService;
//	
//	 /**
//     * 订单审核详情
//     *
//     * @param orderCheckQuery
//     * @return ResponseData
//     */
//    @RequestMapping(value = "/orderCheck/detail", method = RequestMethod.POST, produces = "application/json")
//    public ResponseData orderCheck(@RequestBody OrderCheckQuery orderCheckQuery) throws Throwable {
//        ResponseData responseData = new ResponseData();
//        		com.nyd.order.model.OrderCheckVo resp = new OrderCheckVo();
//        		responseData.setData(resp);
//        		responseData.success();
//        return responseData;
//    }
//	
//}
