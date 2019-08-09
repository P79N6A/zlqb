package com.nyd.user.service.impl.zzl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nyd.application.api.call.CallRecordService;
import com.nyd.application.api.call.QueryContratService;
import com.nyd.application.model.call.CarryBasicVO;
import com.nyd.application.model.call.CustInfoQuery;
import com.nyd.application.model.common.ChkUtil;
import com.nyd.application.model.common.Uuid;
import com.nyd.order.model.common.BeanCommonUtils;
import com.nyd.user.api.zzl.UserForHWTService;
import com.nyd.user.model.common.CommonResponse;
import com.nyd.user.model.common.PagedResponse;
import com.nyd.user.model.enums.ContratNameEnum;
import com.nyd.user.model.vo.CallRecordVO;
import com.nyd.user.model.vo.ContratUrlVo;
import com.nyd.user.model.vo.CustInfoQueryVO;
import com.tasfe.framework.support.model.ResponseData;

@Transactional
@Service("userForHWTServise")
public class UserForHWTServiceImpl implements UserForHWTService {

    private static Logger logger = LoggerFactory.getLogger(UserForHWTServiceImpl.class);

	@Autowired
	private CallRecordService callRecordService;
	
	@Autowired
	private QueryContratService queryContratService;
	
	
	@Override
	public PagedResponse<List<CallRecordVO>> queryCallRecord(CustInfoQueryVO vo) {
		PagedResponse<List<CallRecordVO>> common =  new PagedResponse<List<CallRecordVO>>();
		logger.info(" start" + JSON.toJSONString(vo));
		CustInfoQuery custInfoQuery = new CustInfoQuery();
		custInfoQuery.setMobile(vo.getMobile());
		custInfoQuery.setCustInfoId(vo.getCustInfoId());
		custInfoQuery.setOrdrId(vo.getOrderId());
		custInfoQuery.setPageNo(vo.getPageNo());
		custInfoQuery.setPageSize(vo.getPageSize());
		List<CallRecordVO>  retList = new ArrayList<CallRecordVO>(); 
		try {
			logger.info("2");
			com.nyd.application.model.common.PagedResponse<List<com.nyd.application.model.call.CallRecordVO>> pagecommon = callRecordService.getCallRecord(custInfoQuery);
			logger.info("pagecommon:" + JSON.toJSONString(pagecommon));
			retList = BeanCommonUtils.copyListProperties(pagecommon.getData(), CallRecordVO.class);
			logger.info("retList:"+ JSON.toJSONString(retList));
			common.setData(retList);
			common.setTotal(pagecommon.getTotal());
			common.setSuccess(true);
			common.setCode("1");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" 查询通话记录异常" + e.getMessage());
			common.setCode("0");
			common.setSuccess(false);
		}
		return common;
	}


	@Override
	public CommonResponse<JSONObject> saveCalls(JSONArray array, String name, String mobile, String userId) {
		logger.info(" 运营商通话记录入库开始：《《《");
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		try {
			logger.info(" name: " + name + "mobile :" + mobile + "userId: " + userId );
			if(!ChkUtil.isEmpty(array)&&array.size()>0){
				//先插入基础表
				logger.info("array:" + array.size());
				CarryBasicVO basic = new CarryBasicVO();
				basic.setUserId(userId);
				basic.setName(name);
				basic.setMobile(mobile);
				String id = Uuid.getUuid26();
				basic.setId(id);
				callRecordService.saveCarryCallBasic(basic);
				for(int n = 0; n<array.size(); n++){
					JSONObject calls =  JSONObject.parseObject(array.getString(n));;
					com.alibaba.fastjson.JSONArray jsonArrays  = com.alibaba.fastjson.JSONArray.parseArray(String.valueOf(calls.get("items")));
					if(null!=jsonArrays&&jsonArrays.size()>0){
						for(int i = 0;i<jsonArrays.size();i++){
							JSONObject json = JSONObject.parseObject(jsonArrays.getString(i));
							CarryBasicVO carryBasicVO = new CarryBasicVO();
							carryBasicVO.setName(name);
							carryBasicVO.setId(id);
							carryBasicVO.setUserId(userId);
							carryBasicVO.setMobile(mobile);
							carryBasicVO.setDialType(String.valueOf(json.get("dial_type")));
							if(ChkUtil.isEmpty(String.valueOf(json.get("duration")))||"null".equals((String.valueOf(json.get("duration"))))){
								carryBasicVO.setDuration(0);
							}else{
								carryBasicVO.setDuration(Integer.parseInt(String.valueOf(json.get("duration"))));
							}
							if(ChkUtil.isEmpty(String.valueOf(json.get("fee")))||"null".equals((String.valueOf(json.get("fee"))))){
								carryBasicVO.setFee(0);
							}else{
								carryBasicVO.setFee(Integer.parseInt(String.valueOf(json.get("fee"))));
							}
							carryBasicVO.setLocation(String.valueOf(json.get("location")));
							carryBasicVO.setTime(String.valueOf(json.get("time")));
							carryBasicVO.setPeerNumber(String.valueOf(json.get("peer_number")));
							callRecordService.saveCarryCalls(carryBasicVO);
						}
					}

					
				}
			}
			common.setSuccess(true);
		} catch (Exception e) {
			logger.error(" 运营商通话记录入库异常" + e + e.getMessage());
			common.setSuccess(false);
		}
		logger.info(" 运营商通话记录入库结束》》》》");
		return common;
	}


	@Override
	public CommonResponse<List<ContratUrlVo>> queryContrat(ContratUrlVo vo) {
		CommonResponse<List<ContratUrlVo>> common = new CommonResponse<List<ContratUrlVo>>();
		logger.info("协议信息查询,请求参数::"+JSONObject.toJSONString(vo));
		try {
			ResponseData pagedate = queryContratService.getSignAgreement(vo.getUserId(), vo.getOrderNo());
			List<ContratUrlVo> list = new ArrayList<ContratUrlVo>();

			if(!ChkUtil.isEmpty(pagedate.getData())){
				Map<String,Object> map = (Map<String, Object>) pagedate.getData();
//				map.forEach((key, value) -> {
//					ContratUrlVo retVO = new ContratUrlVo();
//					retVO.setContratName(ContratNameEnum.fromCode(key).getDesc());
//					retVO.setUrl(String.valueOf(value));
//					list.add(retVO);
//			    });
				for(String key : map.keySet()){
					ContratUrlVo retVO = new ContratUrlVo();
					String contratName = ContratNameEnum.fromDesc(key);
					if(!ChkUtil.isEmpty(contratName)){
						retVO.setContratName(contratName);
						retVO.setUrl(String.valueOf(map.get(key)));
						list.add(retVO);
					}
					
				}

			}
			common.setData(list);
			common.setCode("1");
			common.setMsg("操作成功");
			common.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("协议信息查询,请求参数:Vo:"+JSONObject.toJSONString(vo));
			logger.error(e.getMessage());
			common.setCode("0");
			common.setSuccess(false);
		}
		return common;
	}
	



}
