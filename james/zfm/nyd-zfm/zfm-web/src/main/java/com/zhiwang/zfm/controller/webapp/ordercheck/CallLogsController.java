//package com.zhiwang.zfm.controller.webapp.ordercheck;
//
//
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//import com.zhiwang.zfm.common.response.bean.ordercheck.CallLogsVo;
//import com.zhiwang.zfm.common.response.bean.ordercheck.UserNoParam;
//import com.zhiwang.zfm.common.response.CommonResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@RestController
//@RequestMapping("/user")
//public class CallLogsController {
//	private static final Logger LOGGER = LoggerFactory.getLogger(CallLogsController.class);
//	
//	 /**
//     * 手机通话记录
//     *
//     * @param userNoParam
//     * @return ResponseData
//     */
//    @RequestMapping(value = "/callLogs", method = RequestMethod.POST, produces = "application/json")
//    public CommonResponse callLogs(@ModelAttribute UserNoParam userNoParam) throws Throwable {
//    	CommonResponse responseData = new CommonResponse();
//    	CallLogsVo resp = new CallLogsVo();
//        responseData.setData(resp);
//        responseData.setSuccess(true);
//        return responseData;
//    }
//   
//	
//}
