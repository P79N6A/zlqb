package com.nyd.zeus.service.impls.zzl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.zeus.api.zzl.ZeusForGYTServise;
import com.nyd.zeus.dao.mapper.TimeAttendanceMapper;
import com.nyd.zeus.model.attendance.AttendanceRequest;
import com.nyd.zeus.model.attendance.CreditUserVo;
import com.nyd.zeus.model.attendance.TimeAttendance;
import com.nyd.zeus.model.common.CommonResponse;
import com.nyd.zeus.model.common.PagedResponse;
import com.nyd.zeus.model.helibao.util.Uuid;

@Service("zeusForGYTServise")
public class ZeusForGYTServiseImpl implements ZeusForGYTServise{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ZeusForGYTServiseImpl.class);
	
	@Autowired
    private TimeAttendanceMapper timeAttendanceMapper;

	@Override
	public PagedResponse<List<TimeAttendance>> queryList(AttendanceRequest request) {
		LOGGER.info("贷中考勤--列表查询开始入参：{}",JSON.toJSONString(request));
		PagedResponse<List<TimeAttendance>> response = new PagedResponse<List<TimeAttendance>>();
		try {
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("sys_user_name", "desc");
			request.setOrderby(orderby);
			List<TimeAttendance> list = timeAttendanceMapper.queryList(request);
			Long listCount = timeAttendanceMapper.queryListCount(request);
			response.setData(list);
			response.setTotal(listCount);
			response.setPageNo(request.getPageNo());
			response.setPageSize(request.getPageSize());
			response.setCode("1");
			response.setMsg("操作成功");
			response.setSuccess(true);
		} catch (Exception e) {
			response.setData(null);
			response.setCode("0");
			response.setMsg("系统错误，请联系管理员！");
			response.setSuccess(false);
			LOGGER.error("贷中考勤--列表查询系统错误：{}",e.getMessage());
		}
		return response;
	}

	@Override
	public CommonResponse<JSONObject> updateAttendance(TimeAttendance request) {
		LOGGER.info("贷中考勤--更新出勤状态开始入参：{}",JSON.toJSONString(request));
		CommonResponse<JSONObject> response = new CommonResponse<>();
		try {
			timeAttendanceMapper.update(request);
			response.setData(null);
			response.setCode("1");
			response.setMsg("操作成功");
			response.setSuccess(true);
		} catch (Exception e) {
			response.setData(null);
			response.setCode("0");
			response.setMsg("系统错误，请联系管理员！");
			response.setSuccess(false);
			LOGGER.error("贷中考勤--更新出勤状态系统错误：{}",e,e.getMessage());
		}
		return response;
	}

	@Override
	public CommonResponse<JSONObject> saveAttendance(List<CreditUserVo> userList) {
		LOGGER.info("贷中考勤--初始化出勤记录开始入参userList：{}",JSON.toJSONString(userList));
		CommonResponse<JSONObject> response = new CommonResponse<>();
		try {
			for(CreditUserVo vo : userList){
				TimeAttendance request = new TimeAttendance();
				request.setId(UUID.randomUUID().toString().replace("-", "")+Uuid.getUuid24());
				request.setSysUserId(vo.getSysUserId());
				request.setSysUserName(vo.getLoginName());
				timeAttendanceMapper.save(request);
			}
			response.setData(null);
			response.setCode("1");
			response.setMsg("操作成功");
			response.setSuccess(true);
		} catch (Exception e) {
			response.setData(null);
			response.setCode("0");
			response.setMsg("系统错误，请联系管理员！");
			response.setSuccess(false);
			LOGGER.error("贷中考勤--初始化出勤记录系统错误：{}",e,e.getMessage());
		}
		return response;
	}
	
}
