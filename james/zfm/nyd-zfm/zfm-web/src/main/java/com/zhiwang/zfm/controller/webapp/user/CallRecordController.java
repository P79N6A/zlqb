package com.zhiwang.zfm.controller.webapp.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nyd.user.api.zzl.UserForHWTService;
import com.nyd.user.api.zzl.UserForZLQServise;
import com.nyd.user.model.common.CommonResponse;
import com.nyd.user.model.common.PagedResponse;
import com.nyd.user.model.vo.CallRecordVO;
import com.nyd.user.model.vo.CustInfoQueryVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/call")
@Api(description="call")
public class CallRecordController {
	@Autowired
	private UserForHWTService userForHWTService;
	
	@Autowired
	private UserForZLQServise userForZLQServise;
	
	


	@ApiOperation(value = "手机通话记录")
	@RequestMapping(value = "/callRecord", method = RequestMethod.POST)
	public PagedResponse<List<CallRecordVO>> queryCallRecord(@RequestBody CustInfoQueryVO vo) {
		PagedResponse<List<CallRecordVO>> common = new PagedResponse<List<CallRecordVO>>();
		//common = userForHWTService.queryCallRecord(vo);
		common = userForZLQServise.queryCallRecord(vo);
		return common;
	}

}
