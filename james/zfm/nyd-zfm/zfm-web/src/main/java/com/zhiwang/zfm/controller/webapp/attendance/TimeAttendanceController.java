//package com.zhiwang.zfm.controller.webapp.attendance;
//
//import java.util.List;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.nyd.zeus.api.zzl.ZeusForGYTServise;
//import com.nyd.zeus.model.attendance.AttendanceRequest;
//import com.nyd.zeus.model.attendance.TimeAttendance;
//import com.nyd.zeus.model.common.CommonResponse;
//import com.nyd.zeus.model.common.PagedResponse;
//import com.zhiwang.zfm.common.response.bean.sys.UserVO;
//import com.zhiwang.zfm.config.shiro.ShiroUtils;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//
//@RestController
//@RequestMapping("/timeAttendance")
//@Api(description = "贷中考勤")
//public class TimeAttendanceController {
//	
//	private static final Logger LOGGER = LoggerFactory.getLogger(TimeAttendanceController.class);
//	
//	@Autowired
//	private ZeusForGYTServise zeusForGYTServise;
//	
//	@ApiOperation(value = "考勤列表查询")
//	@RequestMapping(value = "/timeAttendanceList", method = RequestMethod.POST)
//    public PagedResponse<List<TimeAttendance>> timeAttendanceList(@RequestBody AttendanceRequest request){
//    	LOGGER.info("考勤列表查询请求开始入参：" + JSON.toJSONString(request));
//    	PagedResponse<List<TimeAttendance>> response = zeusForGYTServise.queryList(request);
//        return response;
//    }
//	
//	@ApiOperation(value = "更新出勤状态")
//	@RequestMapping(value = "/updateAttendance", method = RequestMethod.POST)
//    public CommonResponse<JSONObject> updateAttendance(@RequestBody TimeAttendance request){
//    	LOGGER.info("更新出勤状态请求开始入参：" + JSON.toJSONString(request));
//    	UserVO userVO=(UserVO) ShiroUtils.getSessionAttribute(ShiroUtils.EMPLOYEE_SESSION);
//    	request.setUpdateUserId(userVO.getId());
//    	request.setUpdateUserName(userVO.getLoginName());
//    	CommonResponse<JSONObject> response = zeusForGYTServise.updateAttendance(request);
//        return response;
//    }
//
//}
