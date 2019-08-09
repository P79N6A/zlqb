package com.nyd.user.api.zzl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.user.model.common.CommonResponse;
import com.nyd.user.model.vo.AppConfirmOpenVO;
import com.nyd.user.model.vo.AppPreCardVO;
import com.sun.org.apache.regexp.internal.RE;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

public interface UserBankCardService {
	
	/**
	 * 预绑卡
	 * @param vo
	 * @return
	 */
	public ResponseData preBindingBank(AppPreCardVO vo);
	
	/**
	 * 确认绑卡
	 * @param vo
	 * @return
	 */
	public ResponseData confirmBindCard(AppConfirmOpenVO vo);


		public ResponseData<List<JSONObject>> queryBankList(String userId) throws Exception;

}
