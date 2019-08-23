package com.nyd.zeus.api.zzl;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.nyd.zeus.model.BillInfo;
import com.nyd.zeus.model.BillRepayListVO;
import com.nyd.zeus.model.common.CommonResponse;
import com.nyd.zeus.model.common.PagedResponse;
import com.nyd.zeus.model.common.response.BillRepayVo;

public interface ZeusForZQServise {
	
	CommonResponse<BillInfo> queryBillInfoByOrderNO(String orderNo);

	CommonResponse<JSONObject> saveBill(BillInfo billInfo);

	CommonResponse<JSONObject> getBillProduct(String orderId) throws Exception;

	PagedResponse<List<BillRepayVo>> getBillRepayList(BillRepayListVO vo);
	
	
	JSONObject checkBillProduct(String orderId) throws Exception;
}
