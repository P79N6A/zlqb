package com.zhiwang.zfm.controller.webapp.pay;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.nyd.zeus.api.zzl.ZeusForOrderPayBackServise;

@RestController
@RequestMapping(value = "/api/test")
@Api(description = "Paychannel")
public class PayChannelController {

	@Autowired
	private ZeusForOrderPayBackServise zeusForOrderPayBackServise;

	@RequestMapping(value = "/getPaychannel", method = RequestMethod.POST)
	public String getPaychannel(
			@ApiParam(required = true, name = "count", value = "count") @RequestParam(value = "count", required = true) int count)
			throws Exception {
		List<String> resList = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			resList.add(zeusForOrderPayBackServise.getPaychannel());
		}

		JSONObject json = new JSONObject();

		Map<String, List<String>> map = resList.stream().collect(
				Collectors.groupingBy(str -> str));
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			json.put(key, map.getOrDefault(key, new ArrayList<>()).size());
		}
		return JSONObject.toJSONString(json);
	}

}
