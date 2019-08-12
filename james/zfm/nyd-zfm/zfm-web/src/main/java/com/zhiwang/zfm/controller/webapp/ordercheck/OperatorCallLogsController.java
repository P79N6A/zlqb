//package com.zhiwang.zfm.controller.webapp.ordercheck;
//
//
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//import com.zhiwang.zfm.common.response.bean.ordercheck.AddressBookVo;
//import com.zhiwang.zfm.common.response.bean.ordercheck.OperatorCallLogsVo;
//import com.zhiwang.zfm.common.response.bean.ordercheck.UserNoParam;
//import com.zhiwang.zfm.common.response.CommonResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@RestController
//@RequestMapping("/user")
//public class OperatorCallLogsController {
//	private static final Logger LOGGER = LoggerFactory.getLogger(OperatorCallLogsController.class);
//	
//	 /**
//     * 营运商通话记录
//     *
//     * @param userNoParam
//     * @return ResponseData
//     */
//    @RequestMapping(value = "/operatorCallLogs", method = RequestMethod.POST, produces = "application/json")
//    public CommonResponse operatorCallLogs(@ModelAttribute UserNoParam userNoParam) throws Throwable {
//    	CommonResponse responseData = new CommonResponse();
//    	OperatorCallLogsVo resp = new OperatorCallLogsVo();
//        responseData.setData(resp);
//        responseData.setSuccess(true);
//        return responseData;
//    }
//   
//	
//}
