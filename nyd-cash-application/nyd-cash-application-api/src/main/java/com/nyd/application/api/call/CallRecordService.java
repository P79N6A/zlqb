package com.nyd.application.api.call;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.nyd.application.model.call.CallRecordVO;
import com.nyd.application.model.call.CarryBasicVO;
import com.nyd.application.model.call.CustInfoQuery;
import com.nyd.application.model.common.PagedResponse;
import com.nyd.user.model.common.CommonResponse;

public interface CallRecordService {
	
	public PagedResponse<List<CallRecordVO>> getCallRecord(CustInfoQuery custInfoQuery) throws Exception;
	
	public CommonResponse<JSONObject> saveCarryCalls(CarryBasicVO carryBasicVO);

	public void saveCarryCallBasic(CarryBasicVO carryBasicVO);

}
