package com.nyd.application.service.impl.sms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.application.api.ApplicationSqlService;
import com.nyd.application.api.sms.SmsRecordServise;
import com.nyd.application.model.common.ChkUtil;
import com.nyd.application.model.common.DateUtils;
import com.nyd.user.model.common.CommonResponse;
import com.nyd.user.model.common.PagedResponse;
import com.nyd.user.model.vo.sms.SmsRecordingVo;

@Service(value ="smsRecordServise")
public class SmsRecordServiseImpl implements SmsRecordServise {

	private static final Logger LOGGER = LoggerFactory.getLogger(SmsRecordServiseImpl.class);

	@Autowired
	private ApplicationSqlService<T> applicationSqlService;

	/**
	 * 短信记录查询
	 */
	@Override
	public CommonResponse<List<SmsRecordingVo>> smsRecording(SmsRecordingVo request) {
		LOGGER.info("短信记录查询开始,请求参数:keywordList:"+JSONObject.toJSONString(request));
		CommonResponse<List<SmsRecordingVo>> response = new CommonResponse<>();
		try{
			String sql = "select a.name, s.call_no as callNo, s.content, from_unixtime(s.time,'%Y-%m-%d %H:%i:%s') as createtime "
					+ "from t_sms_info s "
					+ "LEFT JOIN t_address_book a on a.tel=s.call_no and a.user_id = s.user_id "
					+ "where DATE_SUB(CURDATE(), INTERVAL 30 DAY) <= from_unixtime(s.time,'%Y-%m-%d %H:%i:%s') "
					+ "and s.user_id='"+request.getUserId()+"' order by s.time desc";
			List<JSONObject> smsList= applicationSqlService.query(sql);
			List<SmsRecordingVo> voList = new ArrayList<>();
			if(!CollectionUtils.isEmpty(smsList)){
				for(JSONObject obj : smsList){
					SmsRecordingVo vo = JSON.toJavaObject(obj,SmsRecordingVo.class);
					List<String> keywordList = request.getKeywordList();
					if (!CollectionUtils.isEmpty(keywordList)) {
						String content = vo.getContent();
						if(!ChkUtil.isEmpty(content)){
							for(String keyword : keywordList){
								content = content.replaceAll("(?i)"+keyword,
										"<span style=\"color:red\">" + keyword + "</span>");
							}
							vo.setContent(content);
						}
					}
					voList.add(vo);
				}
			}
			response.setData(voList);
			response.setCode("1");
			response.setMsg("操作成功");
			response.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error("短信记录查询异常,请求参数:keywordList:"+JSONObject.toJSONString(request));
			LOGGER.error(e.getMessage());
			response.setCode("0");
			response.setSuccess(false);
		}
		return response;
	}

	@Override
	public PagedResponse<List<JSONObject>> callRecording(SmsRecordingVo request) {
		LOGGER.info("运营商通话记录查询开始,请求参数:request:"+JSONObject.toJSONString(request));
		PagedResponse<List<JSONObject>> response = new PagedResponse<>();
		List<JSONObject> list = new ArrayList<>();
		try {

			String basicIdSql="SELECT id FROM t_carry_basic WHERE user_id = '"+request.getUserId()+"' ORDER BY create_time DESC LIMIT 0,1";
			JSONObject obj = applicationSqlService.queryOne(basicIdSql);
			String basicId = "";
			if(!ChkUtil.isEmpty(obj) && obj.containsKey("id")){
				basicId = obj.getString("id");
			}else{
				response.setTotal(0);
				response.setPageNo(request.getPage());
				response.setPageSize(request.getSize());
				response.setData(list);
				response.setCode("1");
				response.setMsg("操作成功");
				response.setSuccess(true);
				return response;
			}
			String phone=request.getCallNo();
			if(StringUtils.isNotBlank(request.getCallNo())){
				phone=phoneDesensitization(phone);
			}
			StringBuffer sb = new StringBuffer();
			if (StringUtils.isNotBlank(request.getName()) && StringUtils.isNotBlank(request.getCallNo())){
				sb.append("select c.id, c.duration, c.dial_type, c.peer_number, c.time, a.name");
				sb.append(" from (select * from t_carry_calls where 1=1 ");
				sb.append(" and carry_id = '" + basicId +"' ");
	             if (StringUtils.isNotBlank(request.getCallNo())){
					sb.append(" and (peer_number = '" + request.getCallNo() +"' or peer_number ='"+phone+"') ");

				}
	             sb.append(" )c");
				sb.append(" inner JOIN (select * from t_address_book where 1=1 ");
				sb.append(" and user_id='").append(request.getUserId()).append("'");
				if (StringUtils.isNotBlank(request.getName())){
					sb.append(" and name = '" + request.getName() + "' ");
				}
				sb.append(" )a");
				sb.append("  on a.tel=c.peer_number ");
			}else if(StringUtils.isNotBlank(request.getName()) && StringUtils.isBlank(request.getCallNo())){
				sb.append("select c.id, c.duration, c.dial_type, c.peer_number, c.time, a.name");
				sb.append(" from (select * from t_carry_calls where 1=1 ");
				sb.append(" and carry_id = '" + basicId +"' ");
	             if (StringUtils.isNotBlank(request.getCallNo())){
					sb.append(" and (peer_number = '" + request.getCallNo() +"' or peer_number ='"+phone+"') ");

				}
	             sb.append(" )c");
				sb.append(" right JOIN (select * from t_address_book where 1=1 ");
				sb.append(" and user_id='").append(request.getUserId()).append("'");
				if (StringUtils.isNotBlank(request.getName())){
					sb.append(" and name = '" + request.getName() + "' ");
				}
				sb.append(" )a");
				sb.append("  on a.tel=c.peer_number ");
			}else{
				sb.append("select c.id, c.duration, c.dial_type, c.peer_number, c.time, a.name");
				sb.append(" from (select * from t_carry_calls where 1=1 ");
				sb.append(" and carry_id = '" + basicId +"' ");
	             if (StringUtils.isNotBlank(request.getCallNo())){
					sb.append(" and (peer_number = '" + request.getCallNo() +"' or peer_number ='"+phone+"') ");

				}
	            sb.append(" )c");
				sb.append(" left JOIN (select * from t_address_book where 1=1 ");
				sb.append(" and user_id='").append(request.getUserId()).append("'");
				if (StringUtils.isNotBlank(request.getName())){
					sb.append(" and name = '" + request.getName() + "' ");
				}
				sb.append(" )a");
				sb.append("  on a.tel=c.peer_number ");
			}
			
			
			String querySql = sb.toString()+" order by c.time desc";
			LOGGER.info("运营商通话记录查询sql:"+sb);
			list= applicationSqlService.page(querySql,request.getPage(),request.getSize());


			
			Long count = applicationSqlService.count(sb.toString());

			response.setTotal(count);
			response.setPageNo(request.getPage());
			response.setPageSize(request.getSize());
			response.setData(list);
			response.setCode("1");
			response.setMsg("操作成功");
			response.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error("运营商通话记录查询异常,请求参数:request:"+JSONObject.toJSONString(request));
			LOGGER.error(e.getMessage());
			response.setCode("0");
			response.setSuccess(false);
		}
		return response;
	}

	@Override
	public PagedResponse<List<JSONObject>> callList(SmsRecordingVo request) {
		LOGGER.info("手机通讯录查询开始,请求参数:request:"+JSONObject.toJSONString(request));
		PagedResponse<List<JSONObject>> response = new PagedResponse<>();
		List<JSONObject> list = new ArrayList<>();
		try {
			String phone=request.getCallNo();
			if(StringUtils.isNotBlank(phone)){
				phone=phoneDesensitization(phone);
			}
			String basicIdSql="SELECT id FROM t_carry_basic WHERE user_id = '"+request.getUserId()+"' ORDER BY create_time DESC LIMIT 0,1";
			JSONObject obj = applicationSqlService.queryOne(basicIdSql);
			String basicId = "";
			if(!ChkUtil.isEmpty(obj) && obj.containsKey("id")){
				basicId = obj.getString("id");
			}else{
				response.setTotal(0);
				response.setPageNo(request.getPage());
				response.setPageSize(request.getSize());
				response.setData(list);
				response.setCode("1");
				response.setMsg("操作成功");
				response.setSuccess(true);
				return response;
			}
			StringBuffer sb  = new StringBuffer();
			sb.append("SELECT a.name, a.tel AS tel,");
			String str_counts=" and (peer_number = a.ytel or peer_number = a.tel) ";

			sb.append(" (SELECT COUNT(*) FROM t_carry_calls WHERE carry_id = '"+basicId+"' "+str_counts+") AS counts,");
			sb.append(" (SELECT IFNULL(sum(duration),0) FROM t_carry_calls WHERE carry_id = '"+basicId+"'  "+str_counts+") AS totalTime,");
			sb.append(" IFNULL((SELECT time FROM t_carry_calls WHERE carry_id = '"+basicId+"' "+str_counts+" ORDER BY create_time DESC LIMIT 0,1),' ') AS createTime");
			sb.append(" FROM (select case when length(t.tel)>=11 then INSERT(t.tel,length(t.tel)-7,4,'****') when length(t.tel)=7 then INSERT(t.tel,length(t.tel)-6,3,'***') else t.tel end ytel, t.* from t_address_book t  WHERE t.user_id = '"+ request.getUserId() +"' ");
			if (StringUtils.isNotBlank(request.getCallNo())){
				sb.append(" and (t.tel = '" + request.getCallNo() +"' or t.tel = '"+phone+"') ");

			}
			if (StringUtils.isNotBlank(request.getName())){
				sb.append(" and t.name = '" + request.getName() + "' ");
			}
            sb.append(" ) a");
			sb.append(" order by counts desc");
			LOGGER.info("手机通讯录sql:"+sb);
			list= applicationSqlService.page(sb.toString(),request.getPage(),request.getSize());
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("SELECT 1 FROM t_address_book a WHERE a.user_id = '"+request.getUserId()+"'");
			if (StringUtils.isNotBlank(request.getName())){
				stringBuffer.append(" and a.name = '" + request.getName() + "' ");
			}
			if (StringUtils.isNotBlank(request.getCallNo())){
				stringBuffer.append(" and (a.tel = '" + request.getCallNo() +"' or tel = '"+phone+"') ");

			}
			
			LOGGER.info("手机通讯录countSql:"+stringBuffer);
			Long count = applicationSqlService.count(stringBuffer.toString());
			response.setTotal(count);
			response.setPageNo(request.getPage());
			response.setPageSize(request.getSize());
			response.setData(list);
			response.setCode("1");
			response.setMsg("操作成功");
			response.setSuccess(true);
		} catch (Exception e) {
			LOGGER.error("手机通讯录查询异常,请求参数:request:"+JSONObject.toJSONString(request));
			LOGGER.error(e.getMessage());
			response.setCode("0");
			response.setSuccess(false);
		}
		return response;
	}

	/**
	 * 联系号码脱敏
	 * @param phoneNo
	 * @return
	 */
	private  String phoneDesensitization(String phoneNo) {
		try {
			phoneNo = phoneNo.replace(" ","");
			if (phoneNo.contains("*")) {
				return phoneNo;
			} else {
				if (phoneNo.length()>=8) {
					String sub = phoneNo.substring( phoneNo.length()-8,phoneNo.length()-4);
					String replacePhone = phoneNo.replace(sub,"****");
					return replacePhone;
				} else if (phoneNo.length() == 7){
					String sub = phoneNo.substring( phoneNo.length()-7,phoneNo.length()-4);
					String replacePhone = phoneNo.replace(sub,"***");
					return replacePhone;
				} else {
					return phoneNo;
				}
			}
		} catch (Exception e) {
			return phoneNo;
		}

	}

}
