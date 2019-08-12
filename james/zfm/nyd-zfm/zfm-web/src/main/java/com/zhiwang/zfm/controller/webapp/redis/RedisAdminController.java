package com.zhiwang.zfm.controller.webapp.redis;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zhiwang.zfm.common.response.CommonResponse;
import com.zhiwang.zfm.common.util.ChkUtil;
import com.zhiwang.zfm.service.api.redis.RedisService;


@RestController
@RequestMapping("/api/redis")
@Api(description="REDIS-api")
public class RedisAdminController {

	@Autowired
	private RedisService redisService;
	
	
	@PostMapping("/getValue")
	@ApiOperation(value = "获取REDIS VALUE")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse getValue(@ApiParam(required = true, name = "key", value = "key[(放款:cfm-orderLoanSuccess+订单ID),(OCR错误次数:ocrSuccess+custInfoId+年月日)]") @RequestParam(value="key",required=true)String key) {
		String value = redisService.get(key);
		CommonResponse r = new CommonResponse();
		r.setSuccess(true);
		r.setData(value);
		return r;
	}
	
	@PostMapping("/delByKey")
	@ApiOperation(value = "删除REDIS VALUE")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse delByKey(@ApiParam(required = true, name = "key", value = "key") @RequestParam(value="key",required=true)String key) {
		redisService.del(key);
		CommonResponse r = new CommonResponse();
		r.setSuccess(true);
		return r;
	}
	
	@PostMapping("/set")
	@ApiOperation(value = "设置KEY")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse set(@ApiParam(required = true, name = "key", value = "key") @RequestParam(value="key",required=true)String key,
			@ApiParam(required = true, name = "value", value = "value") @RequestParam(value="key",required=true)String value,
			@ApiParam(required = false, name = "liveTime", value = "存活时间") @RequestParam(value="liveTime",required=false)String liveTime) {
		if(ChkUtil.isEmpty(liveTime)) {
			redisService.set(key, value);
			
		}else {
			redisService.set(key, value,Long.parseLong(liveTime));
			
		}
		CommonResponse r = new CommonResponse();
		r.setSuccess(true);
		return r;
	}
	
}
