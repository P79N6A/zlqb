//package com.zhiwang.zfm.controller.webapp.order;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.nyd.zeus.api.zzl.ZeusForZLQServise;
//import com.nyd.zeus.model.common.CommonResponse;
//import com.nyd.zeus.model.common.PagedResponse;
//import com.nyd.zeus.model.sms.MsgListQueryVO;
//import com.nyd.zeus.model.sms.SendMsgLoanVO;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//
//@RestController
//@RequestMapping("/web/postlending")
//@Api(description="贷后")
//public class AfterLoanMsgController {
//
//
//	@Autowired
//	private ZeusForZLQServise zeusForZLQServise;
//	
//
//	
//	
//
//
//	@ApiOperation(value = "贷后发短信列表")
//	@RequestMapping(value = "/msgList", method = RequestMethod.POST)
//	public PagedResponse<List<MsgListQueryVO>> queryCallRecord(@RequestBody MsgListQueryVO vo) {
//		PagedResponse<List<MsgListQueryVO>> common = new PagedResponse<List<MsgListQueryVO>>();
//		common = zeusForZLQServise.queryMsgList(vo);
//		return common;
//	}
//	
//	@ApiOperation(value = "发短信")
//	@RequestMapping(value = "/sendSms", method = RequestMethod.POST)
//	public CommonResponse<Boolean> sendSms(@RequestBody SendMsgLoanVO vo) {
//		CommonResponse<Boolean> common = new CommonResponse<Boolean>();
//		zeusForZLQServise.sendMsgProducer(vo);
//		return common;
//	}
//
//
//}
