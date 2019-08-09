package com.nyd.application.service.impl.call;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.nyd.application.api.ApplicationSqlService;
import com.nyd.application.api.call.CallRecordService;
import com.nyd.application.dao.CarryCallsDao;
import com.nyd.application.dao.CarrybasicDao;
import com.nyd.application.model.call.CallInfoVO;
import com.nyd.application.model.call.CallRecordVO;
import com.nyd.application.model.call.CarryBasicVO;
import com.nyd.application.model.call.CustInfoQuery;
import com.nyd.application.model.common.ChkUtil;
import com.nyd.application.model.common.DateUtils;
import com.nyd.application.model.common.ListTool;
import com.nyd.application.model.common.PagedResponse;
import com.nyd.application.model.common.Uuid;
import com.nyd.application.service.commonEnum.CallRecordEnum;
import com.nyd.user.model.common.CommonResponse;

/**
 * 通话记录
 * 
 * @author admin
 *
 */

@Service(value = "callRecordService")
public class CallRecordImpl implements CallRecordService {

    private static Logger logger = LoggerFactory.getLogger(CallRecordImpl.class);

	
	@Autowired
	private ApplicationSqlService<T> applicationSqlService;

	@Autowired
	private CarrybasicDao carrybasicDao;

	@Autowired
	private CarryCallsDao carryCallsDao;

	@Override
	public PagedResponse<List<CallRecordVO>> getCallRecord(CustInfoQuery custInfoQuery) throws Exception {
		PagedResponse<List<CallRecordVO>> common = new PagedResponse<List<CallRecordVO>>();
		List<CallRecordVO> retList = new ArrayList<CallRecordVO>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select id,type,phone_no phoneNo,name,");
		sb.append(" date_format(create_time, '%Y-%m-%d %H:%i:%s') createTime,");
		sb.append(" call_time callTime, ");
		sb.append(" date_format(update_time, '%Y-%m-%d %H:%i:%s') updateTime,");
		sb.append(" call_no callNo,hour,version_name versionName,");
		sb.append(" phone_os phoneOs,");
		sb.append(" duration");
		sb.append(" from t_call_info");
		sb.append(" where 1= 1  ");
		if (StringUtils.isNotBlank(custInfoQuery.getMobile())){
			sb.append(" and phone_no = '" + custInfoQuery.getMobile() + "'");
		}
		//sb.append(" DATE_SUB(CURDATE(), INTERVAL 1 MONTH) <= STR_TO_DATE(call_time, '%Y-%m-%d %H:%i:%s') ");
		String startTime =  (String) DateUtils.getDayTRange().get("startDate");
		 String endTime = (String) DateUtils.getDayTRange().get("endDate");
		sb.append(" and call_time>='").append(startTime).append("'");
		sb.append(" and call_time<'").append(endTime).append("'");
       
		if (StringUtils.isNotBlank(custInfoQuery.getName())){
			sb.append("and name = '" + custInfoQuery.getName() + "' ");
		}
		if (StringUtils.isNotBlank(custInfoQuery.getCallNo())){
			sb.append("and call_no = '" + custInfoQuery.getCallNo() +"' ");
         }
		
		String querySql = sb.toString() + " order by call_time desc";
		logger.info("手机通话记录查询sql:"+sb);
		List<JSONObject> list = applicationSqlService.page(querySql, custInfoQuery.getPageNo(), custInfoQuery.getPageSize());
		Long count = applicationSqlService.count(sb.toString());
		List<CallInfoVO> callinfoList = new ArrayList<CallInfoVO>();
		callinfoList = new ListTool().parseToObject(list, CallInfoVO.class);
		//callinfoList = BeanCommonUtils.copyListProperties(list, CallInfoVO.class);
		for (CallInfoVO vo : callinfoList) {
			CallRecordVO retVO = new CallRecordVO();
			retVO.setCallLength(vo.getDuration());
			retVO.setMobile(vo.getCallNo());
			retVO.setName(vo.getName());
			retVO.setCallTime(vo.getCallTime());
			if (CallRecordEnum.CALL_IN.getCode().equals(vo.getType())) {
				retVO.setCallStaus(CallRecordEnum.CALL_IN.getDesc());
			}
			if (CallRecordEnum.CALL_OUT.getCode().equals(vo.getType())) {
				retVO.setCallStaus(CallRecordEnum.CALL_OUT.getDesc());
			}
			if (CallRecordEnum.NO_RESP.getCode().equals(vo.getType())) {
				retVO.setCallStaus(CallRecordEnum.NO_RESP.getDesc());
			}
			retList.add(retVO);
		}
		common.setTotal(count);
		common.setSuccess(true);
		common.setData(retList);
		return common;
	}

	@Override
	public CommonResponse<JSONObject> saveCarryCalls(CarryBasicVO carryBasicVO) {
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		try {
			String date = DateUtils.format(new Date(), DateUtils.STYLE_1);
			String basicid = carryBasicVO.getId();
			StringBuffer callSb = new StringBuffer();
			String callId = Uuid.getUuid26();
			callSb.append(" INSERT INTO `t_carry_calls` (");
			callSb.append(" `id`,");
			callSb.append(" `carry_id`,");
			callSb.append(" `time`,");
			callSb.append(" `duration`,");
			callSb.append(" `dial_type`,");
			callSb.append(" `peer_number`,");
			callSb.append(" `location`,");
			callSb.append(" `fee`,");
			callSb.append(" `status`,");
			callSb.append(" `create_time`,");
			callSb.append(" `update_time`");
			callSb.append(" ) VALUES (");
			callSb.append(" '" + callId +"',");
			callSb.append(" '" + basicid +"',");
			if(ChkUtil.isEmpty(carryBasicVO.getTime())){
				callSb.append(" '',");
			}else{
				callSb.append(" '" + carryBasicVO.getTime() +"',");
			}
			if(ChkUtil.isEmpty(carryBasicVO.getDuration())){
				callSb.append(" '',");
			}else{
				callSb.append(" '" + carryBasicVO.getDuration() +"',");
			}
			if(ChkUtil.isEmpty(carryBasicVO.getDialType())){
				callSb.append(" '',");
			}else{
				callSb.append(" '" + carryBasicVO.getDialType() +"',");
			}
			
			if(ChkUtil.isEmpty(carryBasicVO.getPeerNumber())){
				callSb.append(" '',");
			}else{
				callSb.append(" '" + carryBasicVO.getPeerNumber() +"',");
			}
			
			if(ChkUtil.isEmpty(carryBasicVO.getLocation())){
				callSb.append(" '',");
			}else{
				callSb.append(" '" + carryBasicVO.getLocation() +"',");
			}
			if(ChkUtil.isEmpty(carryBasicVO.getFee())){
				callSb.append(" '',");
			}else{
				callSb.append(" '" + carryBasicVO.getFee() +"',");
			}
			callSb.append(" '1',");
			callSb.append(" '" + date + "',");
			callSb.append(" '" + date + "'");
			callSb.append("  )");
			applicationSqlService.insertSql(callSb.toString());

			common.setSuccess(true);	
		} catch (Exception e) {
			common.setSuccess(false);	
			}
		return common;

	}

	@Override
	public void saveCarryCallBasic(CarryBasicVO carryBasicVO) {
		String date = DateUtils.format(new Date(), DateUtils.STYLE_1);
		StringBuffer sb = new StringBuffer();
		String basicid = carryBasicVO.getId();
		carryBasicVO.getId();
		sb.append(" INSERT INTO t_carry_basic (");
		sb.append(" `id`,");
		sb.append(" `name`,");
		sb.append(" `mobile`,");
		sb.append(" `user_id`,");
		sb.append(" `status`,");
		sb.append(" `create_time`,");
		sb.append(" `update_time`");
		sb.append(" ) VALUES (");
		sb.append(" '" + basicid + "',");
		if(ChkUtil.isEmpty( carryBasicVO.getName())){
			sb.append(" '',");
		}else{
			sb.append(" '" + carryBasicVO.getName() +"',");
		}
		if(ChkUtil.isEmpty(carryBasicVO.getMobile())){
			sb.append(" '',");
		}else{
			sb.append(" '" + carryBasicVO.getMobile() +"',");
		}
		if(ChkUtil.isEmpty( carryBasicVO.getUserId())){
			sb.append(" '',");
		}else{
			sb.append(" '" + carryBasicVO.getUserId() +"',");
		}
		sb.append(" '1',");
		sb.append(" '" + date + "',");
		sb.append(" '" + date + "'");
		sb.append("  )");
		logger.info(sb.toString());
		applicationSqlService.insertSql(sb.toString());	
		
	}

}
