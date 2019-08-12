package com.nyd.application.service.impl.call;

import java.util.Date;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nyd.application.api.ApplicationSqlService;
import com.nyd.application.api.call.SmsInfoOperateService;
import com.nyd.application.model.common.ChkUtil;
import com.nyd.application.model.common.DateUtils;
import com.nyd.application.model.mongo.SmsInfo;

@Service
public class SmsInfoOperateServiceImpl implements SmsInfoOperateService {

	@Autowired
	private ApplicationSqlService<T> applicationSqlService;

	@Override
	public void save(SmsInfo smsInfo) throws Exception {
		String time = DateUtils.format(new Date(), DateUtils.STYLE_1);
		StringBuffer sb = new StringBuffer();
		sb.append(" INSERT INTO `t_sms_info` (");
		sb.append(" `create_time`,");
		sb.append(" `update_time`,");
		sb.append(" `phone_no`,");
		sb.append(" `device_id`,");
		sb.append(" `phone_os`,");
		sb.append(" `version_name`,");
		sb.append(" `content`,");
		sb.append(" `device_phone_no`,");
		sb.append(" `time`,");
		sb.append(" `type`,");
		sb.append(" `call_no`,");
		sb.append(" `cust_info_id`");
		sb.append(" ) VALUES (");
		sb.append(" '" + time + "',");
		sb.append(" '" + time + "',");
		if (ChkUtil.isEmpty(smsInfo.getPhoneNo())) {
			sb.append(" '',");
		} else {
			sb.append(" '" + smsInfo.getPhoneNo() + "',");
		}
		//
		if (ChkUtil.isEmpty(smsInfo.getDeviceId())) {
			sb.append(" '',");
		} else {
			sb.append(" '" + smsInfo.getDeviceId() + "',");
		}
		//
		if (ChkUtil.isEmpty(smsInfo.getPhoneOs())) {
			sb.append(" '',");
		} else {
			sb.append(" '" + smsInfo.getPhoneOs() + "',");
		}

		//
		if (ChkUtil.isEmpty(smsInfo.getVersionName())) {
			sb.append(" '',");
		} else {
			sb.append(" '" + smsInfo.getVersionName() + "',");
		}
		// Content
		if (ChkUtil.isEmpty(smsInfo.getContent())) {
			sb.append(" '',");
		} else {
			sb.append(" '" + smsInfo.getContent() + "',");
		}

		// Content
		if (ChkUtil.isEmpty(smsInfo.getDevicePhoneNo())) {
			sb.append(" '',");
		} else {
			sb.append(" '" + smsInfo.getDevicePhoneNo() + "',");
		}

		// Content
		if (ChkUtil.isEmpty(smsInfo.getTime())) {
			sb.append(" '',");
		} else {
			sb.append(" '" + smsInfo.getTime() + "',");
		}
		// Type
		if (ChkUtil.isEmpty(smsInfo.getType())) {
			sb.append(" '',");
		} else {
			sb.append(" '" + smsInfo.getType() + "',");
		}
		// CallNo
		if (ChkUtil.isEmpty(smsInfo.getCallNo())) {
			sb.append(" '',");
		} else {
			sb.append(" '" + smsInfo.getCallNo() + "',");
		}
		sb.append(" ''");
		applicationSqlService.insertSql(sb.toString());

	}

}
