//package com.nyd.user.ws.controller.ordercheck;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.nyd.order.api.zzl.OrderForZLQServise;
//import com.nyd.user.model.ordercheck.OrderBasicInfoParam;
//import com.tasfe.framework.support.model.ResponseData;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@RestController
//@RequestMapping("/order")
//public class OrderRecordHisController {
//	private static final Logger LOGGER = LoggerFactory.getLogger(OrderRecordHisController.class);
//	@Autowired
//	private OrderForZLQServise orderForZLQServise;
//	 /**
//     * 历史申请记录
//     * @param orderNoParam
//     * @return ResponseData
//     */
//    @RequestMapping(value = "/orderRecordHis", method = RequestMethod.POST, produces = "application/json")
//    public ResponseData contactsInfo(@RequestBody OrderBasicInfoParam orderNoParam) throws Throwable {
//    	ResponseData responseData = new ResponseData();
//    	if(orderNoParam.getOrderNo() == null){
//            return responseData.success();
//        }else{
//        	//OrderForZLQServise.getBankInfos(orderNoParam.getOrderNo());
//        }
//    	
//    	//OrderRecordHisVo resp = new OrderRecordHisVo();
//        //responseData.setData(resp);
//        //responseData.setSuccess(true);
//        return responseData;
//    }
//   
//	
//}
