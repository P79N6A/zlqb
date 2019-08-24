package com.nyd.zeus.service.impls.zzl;

import java.lang.reflect.Field;
import java.util.*;

import com.nyd.zeus.service.util.DateUtil;
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

	/**
	 * 更新出勤信息
	 *
	 * @Description:根据系统用户id获取贷中考勤信息，如果存在则修改，不存在则新增
	 * @param request
	 * @return
	 */
	@Override
	public CommonResponse<JSONObject> updateAttendance(TimeAttendance request) {
		LOGGER.info("贷中考勤--更新出勤状态开始入参：{}",JSON.toJSONString(request));
		CommonResponse<JSONObject> response = new CommonResponse<>();
		try {
			//根据系统用户id获取贷中考勤
			AttendanceRequest req=new AttendanceRequest();
			req.setSysUserId(request.getSysUserId());
			List<TimeAttendance> list = timeAttendanceMapper.queryList(req);
			if(list!=null&&list.size()!=0){
				request.setUpdateTime(new Date());
				timeAttendanceMapper.update(request);
			}else{
				request.setId(UUID.randomUUID().toString().replace("-", ""));
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

	/**
	 * 贷中考勤--根据用户id查询
	 * @param sysUserId
	 * @return
	 */
	public CommonResponse<TimeAttendance> queryBySysUserId(String sysUserId) {
		LOGGER.info("贷中考勤--根据用户id查询开始入参：sysUserId="+sysUserId);
		CommonResponse<TimeAttendance> response = new CommonResponse<TimeAttendance>();
		try {
			AttendanceRequest request=new AttendanceRequest();
			request.setSysUserId(sysUserId);
			List<TimeAttendance> list = timeAttendanceMapper.queryList(request);
			if(list!=null&&list.size()!=0){
				response.setData(list.get(0));
			}
			response.setCode("1");
			response.setMsg("操作成功");
			response.setSuccess(true);
		} catch (Exception e) {
			response.setData(null);
			response.setCode("0");
			response.setMsg("系统错误，请联系管理员！");
			response.setSuccess(false);
			LOGGER.error("贷中考勤--根据用户id查询系统错误：sysUserId="+sysUserId);
		}
		return response;
	}

	/**
	 * 查询今天的所有信审考勤信息集合
	 * @return
	 */
	public CommonResponse<List<TimeAttendance>> getNowAttendance(List<TimeAttendance> tList){
		LOGGER.info("查询今天的所有信审考勤信息集合 开始");
		CommonResponse<List<TimeAttendance>> response = new CommonResponse<List<TimeAttendance>>();
		try {
			AttendanceRequest request=new AttendanceRequest();
			Long listCount = timeAttendanceMapper.queryListCount(request);
			if(listCount==null||listCount.longValue()==0){
				response.setCode("1");
				response.setMsg("操作成功");
				response.setSuccess(true);
				return response;
			}
			List<TimeAttendance> list = timeAttendanceMapper.queryList(request);
			String weekDay=DateUtil.getDayWeekOfDate1(new Date());
			Map<String,String> tAMap=new HashMap<String,String>(list.size());
			for(TimeAttendance t:list){
				Map<String,Object> map=objectToMap(list.get(0));
				int state=(int)map.get(weekDay);
				if(state==1) {
					tAMap.put(t.getSysUserId(),t.getSysUserName());
				}
			}
			for(int i=0;i<tList.size();i++){
				TimeAttendance t=tList.get(i);
				if(tAMap.containsKey(t.getSysUserId())){
					tList.remove(i);
				}
			}
			response.setData(tList);
			response.setCode("1");
			response.setMsg("操作成功");
			response.setSuccess(true);
		} catch (Exception e) {
			response.setData(null);
			response.setCode("0");
			response.setMsg("系统错误，请联系管理员！");
			response.setSuccess(false);
			LOGGER.error("查询今天的所有信审考勤信息集合 系统错误：e="+e.getMessage());
		}
		return response;
	}
	/**
	 * 查询信审考勤信息列表
	 * @param list
	 * @return
	 */
	public CommonResponse<List<TimeAttendance>> queryTimeAttendanceList(List<TimeAttendance> list){
		CommonResponse<List<TimeAttendance>> response=new CommonResponse<List<TimeAttendance>>();
		try{
			if(list==null||list==null){
				response.setSuccess(true);
				response.setMsg("查询成功");
				return response;
			}
			//如果没有贷审人员的考勤信息，则设置默认考勤信息全部是出勤，如果有考勤信息，则返回查询到的考勤信息
			for(TimeAttendance t:list){
				AttendanceRequest request=new AttendanceRequest();
				request.setSysUserId(request.getSysUserId());
				List<TimeAttendance> tlist = timeAttendanceMapper.queryList(request);
				if(tlist!=null&&tlist.size()!=0){
					t=tlist.get(0);
				}else{
					t.setFriday(0);
					t.setMonday(0);
					t.setThursday(0);
					t.setTuesday(0);
					t.setWednesday(0);
					t.setSaturday(1);
					t.setSunday(1);
					t.setStatus(0);
				}
			}
			response.setSuccess(true);
			response.setMsg("查询成功");
			response.setData(list);
			return response;
		}catch (Exception e){
			LOGGER.error("查询信审考勤信息列表异常 e="+e.getMessage());
			response.setSuccess(false);
			response.setMsg("查询异常");
			response.setData(list);
			return response;
		}
	}
	private Map<String, Object> objectToMap(Object obj) throws Exception {
		if(obj == null){
			return null;
		}
		Map<String,Object> map = new HashMap<String,Object>();
		Field[] declaredFields = obj.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			//使private成员可以被访问、修改
			field.setAccessible(true);
			if(null != field.get(obj)) {
				map.put(field.getName(),field.get(obj));
			}
		}
		return map;
	}
}
