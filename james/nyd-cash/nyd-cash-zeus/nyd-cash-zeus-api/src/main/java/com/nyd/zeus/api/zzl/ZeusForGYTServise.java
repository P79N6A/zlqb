package com.nyd.zeus.api.zzl;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.nyd.zeus.model.attendance.AttendanceRequest;
import com.nyd.zeus.model.attendance.CreditUserVo;
import com.nyd.zeus.model.attendance.TimeAttendance;
import com.nyd.zeus.model.common.CommonResponse;
import com.nyd.zeus.model.common.PagedResponse;
public interface ZeusForGYTServise {
	
	//列表查询
	public PagedResponse<List<TimeAttendance>> queryList(AttendanceRequest request);
	
	//更新出勤状态
	CommonResponse<JSONObject> updateAttendance(TimeAttendance request);
	
	CommonResponse<JSONObject> saveAttendance(List<CreditUserVo> userList);

}
