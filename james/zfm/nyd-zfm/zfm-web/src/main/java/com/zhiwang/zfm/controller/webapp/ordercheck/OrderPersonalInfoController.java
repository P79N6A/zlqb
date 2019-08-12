//package com.zhiwang.zfm.controller.webapp.ordercheck;
//
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//import com.zhiwang.zfm.common.response.bean.ordercheck.OrderBasicInfoParam;
//import com.zhiwang.zfm.common.response.bean.ordercheck.OrderPersonalInfoVo;
//import com.zhiwang.zfm.common.response.CommonResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@RestController
//@RequestMapping("/order")
//public class OrderPersonalInfoController {
//	private static final Logger LOGGER = LoggerFactory.getLogger(OrderPersonalInfoController.class);
//	
//	 /**
//     * 订单审核详情
//     *
//     * @param orderCheckQuery
//     * @return ResponseData
//     */
//    @RequestMapping(value = "/orderPersonalInfo", method = RequestMethod.POST, produces = "application/json")
//    public CommonResponse orderPersonalInfo(@ModelAttribute OrderBasicInfoParam orderParam) throws Throwable {
//    	CommonResponse responseData = new CommonResponse();
//    	OrderPersonalInfoVo resp = new OrderPersonalInfoVo();
//        responseData.setData(resp);
//        responseData.isSuccess();
//        return responseData;
//    }
//    
//}
