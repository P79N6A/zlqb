package com.nyd.zeus.api.zzl;

import java.util.List;
import java.util.Map;

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
	/**
	 * 贷中考勤--根据用户id查询
	 * @param sysUserId
	 * @return
	 */
	public CommonResponse<TimeAttendance> queryBySysUserId(String sysUserId);
	/**
	 * 查询今天的所有信审考勤信息集合
	 * @return
	 */
	public CommonResponse<List<TimeAttendance>> getNowAttendance(List<TimeAttendance> list);

	/**
	 * 查询信审考勤信息列表
	 * @param list
	 * @return
	 */
	public CommonResponse<List<TimeAttendance>> queryTimeAttendanceList(List<TimeAttendance> list);

}
