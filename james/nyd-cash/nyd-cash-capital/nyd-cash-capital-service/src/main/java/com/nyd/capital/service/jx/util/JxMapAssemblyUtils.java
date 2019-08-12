/**
 * Project Name:nyd-cash-capital-service
 * File Name:JxMapAssemblyUtils.java
 * Package Name:com.nyd.capital.service.jx.util
 * Date:2018年7月30日下午2:53:03
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.nyd.capital.service.jx.util;
/**
 * ClassName:JxMapAssemblyUtils <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2018年7月30日 下午2:53:03 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.nyd.capital.model.jx.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.nyd.capital.service.jx.config.JxConfig;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JxMapAssemblyUtils {
	private static SimpleDateFormat sdfYmd = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat sdfHms = new SimpleDateFormat("HHmmss");
	/**
	 * 
	 * bulidParametersMap:请求参数的公用map参数. <br/>
	 *
	 * @author wangzhch
	 * @return
	 * @since JDK 1.8
	 */
	private static Map<String, String> bulidParametersMap(JxConfig jxConfig) {
		Map<String, String> map = new HashMap<>();
		map.put("version", jxConfig.getVersion());
		map.put("instCode", jxConfig.getInstCode());
		map.put("channel", jxConfig.getChannel());
		map.put("txDate", sdfYmd.format(new Date()));
		map.put("txTime", sdfHms.format(new Date()));
		map.put("seqNo", String.valueOf((int)((Math.random()*9+1)*100000)));
		map.put("acqRes", jxConfig.getAcqRes());
		return map;
	}
	/**
	 * 
	 * bulidParametersMultiValueMap:http请求的公用参数. <br/>
	 *
	 * @author wangzhch
	 * @param requestMap
	 * @return
	 * @since JDK 1.8
	 */
	public static MultiValueMap<String, Object> bulidParametersMultiValueMap(Map<String, String> requestMap) {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("version", requestMap.get("version"));
		map.add("txCode", requestMap.get("txCode"));
		map.add("instCode", requestMap.get("instCode"));
		map.add("channel", requestMap.get("channel"));
		map.add("txDate", requestMap.get("txDate"));
		map.add("txTime", requestMap.get("txTime"));
		map.add("seqNo", requestMap.get("seqNo"));
		map.add("acqRes", requestMap.get("acqRes"));
		return map;
	}
	/**
	 * 
	 * getMapWithoutSignQueryPushStatus:封装获取推单查询key值的Map. <br/>
	 *
	 * @author wangzhch
	 * @param jxQueryPushStatusRequest
	 * @return
	 * @since JDK 1.8
	 */
	public static Map<String, String> getMapWithoutSignQueryPushStatus(
			JxQueryPushStatusRequest jxQueryPushStatusRequest,JxConfig jxConfig) {
//		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> map = bulidParametersMap(jxConfig);
		map.put("txCode", jxConfig.getMembersStatus());
		map.put("mobile", jxQueryPushStatusRequest.getMobile());
		map.put("realName", jxQueryPushStatusRequest.getRealName());
		map.put("idCardNumber", jxQueryPushStatusRequest.getIdCardNumber());
		map.put("bankCardNumber", jxQueryPushStatusRequest.getBankCardNumber());
		return map;
	}

	/**
	 * 
	 * getMapWithoutSignQueryPushAudit:封装获取推单外审接口key值的Map. <br/>
	 *
	 * @author wangzhch
	 * @param jxPushAuditRequest
	 * @return
	 * @since JDK 1.8
	 */
	public static Map<String, String> getMapWithoutSignQueryPushAudit(JxPushAuditRequest jxPushAuditRequest,JxConfig jxConfig) {
		Map<String,String> map = bulidParametersMap(jxConfig);
		if ("zwgt".equals(jxConfig.getChannel())) {
			map.put("marriageState", jxPushAuditRequest.getMarriageState());
			map.put("education", jxPushAuditRequest.getEducation());
			map.put("jobOffice", jxPushAuditRequest.getJobOffice());
			map.put("jobCity", jxPushAuditRequest.getJobCity());
		}
		map.put("txCode", jxConfig.getPreloan());
		map.put("mobile", jxPushAuditRequest.getMobile());
		map.put("realName", jxPushAuditRequest.getRealName());
		map.put("idCardNumber", jxPushAuditRequest.getIdCardNumber());
		map.put("phaseCount", String.valueOf(jxPushAuditRequest.getPhaseCount()));
		map.put("batchCode", jxPushAuditRequest.getBatchCode());
		map.put("amount", String.valueOf(jxPushAuditRequest.getAmount()));
		map.put("idCardPictureFront", jxPushAuditRequest.getIdCardPictureFront());
		map.put("idCardPictureBack", jxPushAuditRequest.getIdCardPictureBack());
		map.put("liveIdentification", jxPushAuditRequest.getLiveIdentification());
		
		//以下是缺省值,现在写成定值,后期改造
		map.put("income", "20000");//收入
		map.put("incomeUnit", "上海证券中心");//收入单位
		map.put("contact1Name", "张三父");//第一联系人相关信息
		map.put("contact1Relation", "父子");
		map.put("contact1Phone", "13211112222");
		map.put("contact2Name", "张三母");//第一联系人相关信息
		map.put("contact2Relation", "母子");
		map.put("contact2Phone", "13233334444");
		return map;
	}

	/**
	 * 
	 * getMapWithoutSignPushAuditConfirm:封装获取推单外审确认接口key值的Map. <br/>
	 *
	 * @author wangzhch
	 * @param jxpushAuditConfirmRequest
	 * @param jxConfig 
	 * @return
	 * @since JDK 1.8
	 */
	public static Map<String, String> getMapWithoutSignPushAuditConfirm(
			JxPushAuditConfirmRequest jxpushAuditConfirmRequest, JxConfig jxConfig) {
		Map<String,String> map = bulidParametersMap(jxConfig);
		map.put("txCode", jxConfig.getPreloanConfirm());
		map.put("loanOrderId", String.valueOf(jxpushAuditConfirmRequest.getLoanOrderId()));
		map.put("outOrderId", jxpushAuditConfirmRequest.getOutOrderId());
		map.put("userAgreement", jxpushAuditConfirmRequest.getUserAgreement());
		return map;
	}
	/**
	 * 
	 * getMapWithoutSignPushAuditResult:封装获取推单外审结果查询接口key值的Map. <br/>
	 *
	 * @author wangzhch
	 * @param jxQueryPushAuditResultRequest
	 * @return
	 * @since JDK 1.8
	 */
	public static Map<String, String> getMapWithoutSignPushAuditResult(
			JxQueryPushAuditResultRequest jxQueryPushAuditResultRequest,JxConfig jxConfig) {
		Map<String,String> map = bulidParametersMap(jxConfig);
		map.put("txCode", jxConfig.getPreloanQuery());
		map.put("loanOrderId", String.valueOf(jxQueryPushAuditResultRequest.getLoanOrderId()));
		return map;
	}

	public static Map<String, String> getMapQueryLoanPhases(JxQueryLoanPhasesRequest jxQueryLoanPhasesRequest, JxConfig jxConfig) {
		Map<String,String> map = bulidParametersMap(jxConfig);
		map.put("txCode", jxConfig.getLoanPhases());
		map.put("loanId", jxQueryLoanPhasesRequest.getLoanId());
		map.put("status", String.valueOf(jxQueryLoanPhasesRequest.getStatus()));
		return map;
	}

	public static Map<String, String> getMapRepayments(JxRepaymentsRequest jxRepaymentsRequest, JxConfig jxConfig) {
		Map<String,String> map = bulidParametersMap(jxConfig);
		map.put("txCode", jxConfig.getRepayments());
		map.put("loanPhaseId", String.valueOf(jxRepaymentsRequest.getLoanPhaseId()));
		map.put("type", String.valueOf(jxRepaymentsRequest.getType()));
		map.put("requestAmount", String.valueOf(jxRepaymentsRequest.getRequestAmount()));
		return map;
	}
	
	/**
	 * 将json字符串转为Map结构 如果json复杂，结果可能是map嵌套map
	 * 
	 * @param jsonStr
	 *            入参，json格式字符串
	 * @return 返回一个map
	 */
	public static Map<String, Object> json2Map(String jsonStr) {
		Map<String, Object> map = new HashMap<>();
		if (jsonStr != null && !"".equals(jsonStr)) {
			// 最外层解析
			JSONObject json = JSONObject.fromObject(jsonStr);
			for (Object k : json.keySet()) {
				Object v = json.get(k);
				// 如果内层还是数组的话，继续解析
				if (v instanceof JSONArray) {
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					Iterator<JSONObject> it = ((JSONArray) v).iterator();
					while (it.hasNext()) {
						JSONObject json2 = it.next();
						list.add(json2Map(json2.toString()));
					} 
					map.put(k.toString(), list);
				} else {
					map.put(k.toString(), v);
				}
			}
			return map;
		} else {
			return null;
		}
	}
}

