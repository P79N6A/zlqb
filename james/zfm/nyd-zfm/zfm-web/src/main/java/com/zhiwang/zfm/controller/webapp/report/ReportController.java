package com.zhiwang.zfm.controller.webapp.report;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.user.api.zzl.UserForGYTServise;
import com.nyd.user.api.zzl.UserForHWTService;
import com.nyd.user.api.zzl.UserForZQServise;
import com.nyd.user.model.common.CommonResponse;
import com.nyd.user.model.common.PagedResponse;
import com.nyd.user.model.vo.ContratUrlVo;
import com.nyd.user.model.vo.ImageUrlVo;
import com.nyd.user.model.vo.sms.SmsRecordingVo;
import com.zhiwang.zfm.common.util.ChkUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 报告查询
 *
 */
@RestController
@RequestMapping("/report")
@Api(description = "报告查询")
public class ReportController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);
	
	@Autowired
	private UserForZQServise userForZQServise;
	
	@Autowired
	private UserForGYTServise userForGYTServise;
	
	@Autowired
	private UserForHWTService userForHWTServise;
	
	
	@ApiOperation(value = "手机通讯录")
	@RequestMapping(value = "/callList", method = RequestMethod.POST)
    public PagedResponse<List<JSONObject>> callList(@RequestBody SmsRecordingVo request) throws Throwable{
    	LOGGER.info("手机通讯录查询请求开始入参：" + JSON.toJSONString(request));
    	PagedResponse<List<JSONObject>> response = userForGYTServise.callList(request);
        return response;
    }
	
	@ApiOperation(value = "手机短信记录")
	@RequestMapping(value = "/smsRecording", method = RequestMethod.POST)
    public CommonResponse<List<SmsRecordingVo>> smsRecording(@RequestBody SmsRecordingVo request) throws Throwable{
    	LOGGER.info("手机短信记录查询开始");
    	CommonResponse<List<SmsRecordingVo>> response = userForGYTServise.smsRecording(request);
        return response;
    }
	
	@ApiOperation(value = "运营商通话记录")
	@RequestMapping(value = "/operatorCallRecord", method = RequestMethod.POST)
    public PagedResponse<List<JSONObject>> operatorCallRecord(@RequestBody SmsRecordingVo request) throws Throwable{
    	LOGGER.info("运营商通话记录请求开始入参：" + JSON.toJSONString(request));
    	PagedResponse<List<JSONObject>> response = userForGYTServise.callRecording(request);
        return response;
    }
	
	@ApiOperation(value = "身份证正反面及活体照片查询")
	@RequestMapping(value = "/idcardUrl", method = RequestMethod.POST)
    public com.nyd.user.model.common.CommonResponse<ImageUrlVo> idcardUrl(@RequestBody ImageUrlVo vo) throws Throwable{
    	LOGGER.info("身份证正反面及活体照片查询开始");
    	com.nyd.user.model.common.CommonResponse<ImageUrlVo> response = new CommonResponse<>();
    	if(ChkUtil.isEmpty(vo.getUserId())){
    		response.setData(null);
			response.setCode("0");
			response.setMsg("必填参数缺失！");
			response.setSuccess(false);
			return response;
    	}
    	response = userForZQServise.queryImgUrl(vo);
        return response;
    }
	
	@ApiOperation(value = "协议信息")
	@RequestMapping(value = "/contrat", method = RequestMethod.POST)
    public com.nyd.user.model.common.CommonResponse<List<ContratUrlVo>> contrat(@RequestBody ContratUrlVo vo) throws Throwable{
    	LOGGER.info("协议信息列表查询开始");
    	com.nyd.user.model.common.CommonResponse<List<ContratUrlVo>> response = userForHWTServise
    			.queryContrat(vo);
        return response;
    }
	
	
	
}
