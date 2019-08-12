package com.nyd.user.service.impl.zzl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.nyd.application.api.sms.SmsRecordServise;
import com.nyd.user.api.zzl.UserForGYTServise;
import com.nyd.user.api.zzl.UserSqlService;
import com.nyd.user.model.common.CommonResponse;
import com.nyd.user.model.common.PagedResponse;
import com.nyd.user.model.vo.EmergencyContactVo;
import com.nyd.user.model.vo.sms.SmsRecordingVo;

@Service("userForGYTServise")
public class UserForGYTServiseImpl implements UserForGYTServise{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserForGYTServiseImpl.class);
	
	@Autowired
	private SmsRecordServise smsRecordServise;
	
	@Autowired
	private UserSqlService<EmergencyContactVo> userSqlService;

	@Override
	public CommonResponse<List<SmsRecordingVo>> smsRecording(SmsRecordingVo request) {
		CommonResponse<List<SmsRecordingVo>> response = new CommonResponse<>();
		try {
			response = smsRecordServise.smsRecording(request);
		} catch (Exception e) {
			LOGGER.error("短信记录查询异常,请求参数:request:"+JSONObject.toJSONString(request));
			LOGGER.error(e.getMessage());
			response.setCode("0");
			response.setSuccess(false);
		}
		return response;
	}

	@Override
	public PagedResponse<List<JSONObject>> callRecording(SmsRecordingVo request) {
		PagedResponse<List<JSONObject>> response = new PagedResponse<>();
		try {
			response = smsRecordServise.callRecording(request);
		} catch (Exception e) {
			LOGGER.error("运营商通讯录查询异常,请求参数:request:"+JSONObject.toJSONString(request));
			LOGGER.error(e.getMessage());
			response.setCode("0");
			response.setSuccess(false);
		}
		return response;
	}

	@Override
	public PagedResponse<List<JSONObject>> callList(SmsRecordingVo request) {
		PagedResponse<List<JSONObject>> response = new PagedResponse<>();
		try {
			response = smsRecordServise.callList(request);
			List<JSONObject> list = response.getData();
			for(JSONObject obj : list){
				String sql = "select relationship as relation from t_user_contact "
						+ "where user_id='"+request.getUserId()+"' and mobile = '"+obj.getString("tel")+"'";
				JSONObject relation = userSqlService.queryOne(sql);
				obj.put("relation", relation.containsKey("relation")?relation.getString("relation"):"");
			}
		} catch (Exception e) {
			LOGGER.error("手机通讯录查询异常,请求参数:request:"+JSONObject.toJSONString(request));
			LOGGER.error(e.getMessage());
			response.setCode("0");
			response.setSuccess(false);
		}
		return response;
	}

	//紧急联系人信息查询
	@Override
	public CommonResponse<List<EmergencyContactVo>> emergencyContact(String userId) {
		CommonResponse<List<EmergencyContactVo>> response = new CommonResponse<>();
		try {
			String sql = "select u.user_id as userId, u.real_name as name, a.account_number as phone, \"本人\" as relationMsg "
					+ "from t_user u "
					+ "LEFT JOIN t_account a on a.user_id=u.user_id where u.user_id = '"+userId+"' "
					+ "Union "
					+ "select uc.user_id as userId, uc.name as name, uc.mobile as phone, relationship as relationMsg "
					+ "from t_user_contact uc where uc.user_id = '"+userId+"' "
					+ "Union "
					+ "select j.user_id as userId, j.company as name, j.telephone as phone, \"公司电话\" as relationMsg "
					+ "from t_user_job j where j.user_id = '"+userId+"'";
			List<EmergencyContactVo> result = userSqlService.queryT(sql, EmergencyContactVo.class);
			response.setData(result);
		} catch (Exception e) {
			LOGGER.error("紧急联系人信息查询异常,请求参数:userId:"+JSONObject.toJSONString(userId));
			LOGGER.error(e.getMessage());
			response.setCode("0");
			response.setSuccess(false);
		}
		return response;
	}
	

}
