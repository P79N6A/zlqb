//package com.zhiwang.zfm.controller.webapp.ordercheck;
//
//
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//import com.zhiwang.zfm.common.response.bean.ordercheck.AddressBookVo;
//import com.zhiwang.zfm.common.response.bean.ordercheck.UserNoParam;
//import com.zhiwang.zfm.common.response.CommonResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@RestController
//@RequestMapping("/user")
//public class AddressBookController {
//	private static final Logger LOGGER = LoggerFactory.getLogger(AddressBookController.class);
//	
//	 /**
//     * 手机通讯录
//     *
//     * @param userNoParam
//     * @return ResponseData
//     */
//    @RequestMapping(value = "/addressBook", method = RequestMethod.POST, produces = "application/json")
//    public CommonResponse addressBook(@ModelAttribute UserNoParam userNoParam) throws Throwable {
//    	CommonResponse responseData = new CommonResponse();
//    	AddressBookVo resp = new AddressBookVo();
//        responseData.setData(resp);
//        responseData.setSuccess(true);
//        return responseData;
//    }
//   
//	
//}
