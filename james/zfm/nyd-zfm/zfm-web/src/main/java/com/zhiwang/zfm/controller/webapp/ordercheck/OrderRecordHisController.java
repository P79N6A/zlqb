//package com.zhiwang.zfm.controller.webapp.ordercheck;
//
//
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//import com.zhiwang.zfm.common.response.bean.ordercheck.OrderBasicInfoParam;
//import com.zhiwang.zfm.common.response.bean.ordercheck.OrderRecordHisVo;
//import com.zhiwang.zfm.common.response.CommonResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@RestController
//@RequestMapping("/order")
//public class OrderRecordHisController {
//	private static final Logger LOGGER = LoggerFactory.getLogger(OrderRecordHisController.class);
//	
//	 /**
//     * 历史申请记录
//     *
//     * @param orderNoParam
//     * @return ResponseData
//     */
//    @RequestMapping(value = "/orderRecordHis", method = RequestMethod.POST, produces = "application/json")
//    public CommonResponse contactsInfo(@ModelAttribute OrderBasicInfoParam orderNoParam) throws Throwable {
//    	CommonResponse responseData = new CommonResponse();
//    	OrderRecordHisVo resp = new OrderRecordHisVo();
//        responseData.setData(resp);
//        responseData.setSuccess(true);
//        return responseData;
//    }
//   
//	
//}
