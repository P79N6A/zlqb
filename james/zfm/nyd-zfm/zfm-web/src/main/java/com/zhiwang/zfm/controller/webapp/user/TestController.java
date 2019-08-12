package com.zhiwang.zfm.controller.webapp.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nyd.user.api.zzl.UserForHWTService;
import com.nyd.user.model.common.PagedResponse;
import com.nyd.user.model.vo.CallRecordVO;
import com.nyd.user.model.vo.CustInfoQueryVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/wanghu/test")
@Api(description="test")
public class TestController {
	@Autowired
	private UserForHWTService userForHWTService;


	@ApiOperation(value = "/test")
	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public PagedResponse<List<CallRecordVO>> queryCallRecord(@RequestBody CustInfoQueryVO vo) {
		PagedResponse<List<CallRecordVO>> common = new PagedResponse<List<CallRecordVO>>();
		JSONArray retarray = new JSONArray();
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		JSONObject child = new JSONObject();
		child.put("time", "2019-06-16 18:38:22");
		child.put("fee", 0);
		array.add(child);
		json.put("items", array);
		System.out.println(json.toString());
		retarray.add(json);
		String aa = "";
		userForHWTService.saveCalls(retarray,"何文童", "15800379380", "15800379380");
		return common;
	}

}
