package com.nyd.user.api.zzl;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nyd.user.model.common.CommonResponse;
import com.nyd.user.model.common.PagedResponse;
import com.nyd.user.model.vo.CallRecordVO;
import com.nyd.user.model.vo.ContratUrlVo;
import com.nyd.user.model.vo.CustInfoQueryVO;

public interface UserForHWTService {
	/**
	 * 获取手机通话记录
	 */
	public PagedResponse<List<CallRecordVO>> queryCallRecord(CustInfoQueryVO vo);
	
	public CommonResponse<JSONObject> saveCalls(JSONArray array,String name,String mobile,String userId);

	public CommonResponse<List<ContratUrlVo>> queryContrat(ContratUrlVo vo); 
}
