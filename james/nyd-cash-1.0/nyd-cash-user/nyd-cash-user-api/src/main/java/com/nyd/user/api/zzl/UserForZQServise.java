package com.nyd.user.api.zzl;

import com.alibaba.fastjson.JSONObject;
import com.nyd.user.model.BankInfo;
import com.nyd.user.model.common.CommonResponse;
import com.nyd.user.model.vo.ImageUrlVo;
import com.nyd.user.model.vo.OrderCustInfoVO;

public interface UserForZQServise {
	/**
	 * 获取详情-客户信息
	 * @param orderNo
	 * @return
	 */
	public CommonResponse<OrderCustInfoVO> getCustInfoByOrderNo(String orderNo);
	
	public BankInfo getAccountNo(String userId);

	JSONObject getAccountInfo(String userId);
	
	CommonResponse<ImageUrlVo> queryImgUrl(ImageUrlVo vo);

	JSONObject findUserDetailByUserId(String userId);
	
	String queryAssessmentAmount();
}
