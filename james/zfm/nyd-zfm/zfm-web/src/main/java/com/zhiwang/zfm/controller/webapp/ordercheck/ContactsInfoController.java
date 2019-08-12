//package com.zhiwang.zfm.controller.webapp.ordercheck;
//
//
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//import com.zhiwang.zfm.common.response.bean.ordercheck.ContactsInfoVo;
//import com.zhiwang.zfm.common.response.bean.ordercheck.UserNoParam;
//import com.zhiwang.zfm.common.response.CommonResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@RestController
//@RequestMapping("/user")
//public class ContactsInfoController {
//	private static final Logger LOGGER = LoggerFactory.getLogger(ContactsInfoController.class);
//	
//	 /**
//     * 联系人信息
//     *
//     * @param userNoParam
//     * @return ResponseData
//     */
//    @RequestMapping(value = "/contactsInfo", method = RequestMethod.POST, produces = "application/json")
//    public CommonResponse contactsInfo(@ModelAttribute UserNoParam userNoParam) throws Throwable {
//    	CommonResponse responseData = new CommonResponse();
//    	ContactsInfoVo resp = new ContactsInfoVo();
//        responseData.setData(resp);
//        responseData.setSuccess(true);
//        return responseData;
//    }
//   
//	
//}
