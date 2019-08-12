package com.nyd.application.api.sms;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.nyd.user.model.common.CommonResponse;
import com.nyd.user.model.common.PagedResponse;
import com.nyd.user.model.vo.sms.SmsRecordingVo;

public interface SmsRecordServise {
	
	CommonResponse<List<SmsRecordingVo>> smsRecording(SmsRecordingVo request);
	
	PagedResponse<List<JSONObject>> callRecording(SmsRecordingVo request);
	
	PagedResponse<List<JSONObject>> callList(SmsRecordingVo request);
	
	

}
