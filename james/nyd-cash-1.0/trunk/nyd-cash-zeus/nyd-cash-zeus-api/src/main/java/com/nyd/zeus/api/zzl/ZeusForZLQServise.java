package com.nyd.zeus.api.zzl;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.nyd.zeus.model.OrderRecordHisVo;
import com.nyd.zeus.model.common.CommonResponse;
import com.nyd.zeus.model.common.PagedResponse;
import com.nyd.zeus.model.sms.MsgListQueryVO;
import com.nyd.zeus.model.sms.SendMsgLoanVO;
import com.nyd.zeus.model.sms.SmsReplyEntity;

public interface ZeusForZLQServise {
	
	public OrderRecordHisVo findHisByOrder(String orderNo);

	public PagedResponse<List<MsgListQueryVO>> queryMsgList(MsgListQueryVO vo);
	
	public CommonResponse<Boolean> sendMsgProducer(SendMsgLoanVO vo);
	
	public CommonResponse<Boolean> saveMsgDetail(SendMsgLoanVO vo);
	
	/**
	 * 给贷中客户发送催收短信
	 * @param vo
	 * @return
	 */
	public CommonResponse<Boolean> doSendMsg(SendMsgLoanVO vo);
	
	/**
	 * 调用三方短信接口查询客户返回消息
	 * @return
	 * @throws Exception
	 */
	public CommonResponse<JSONObject> doReceiveMsg() throws Exception;
	
	/**
	 * 保存贷中 客户返回消息
	 * @param sms
	 * @return
	 * @throws Exception
	 */
	public CommonResponse<JSONObject> saveReceiveMsg(SmsReplyEntity sms) throws Exception;

}
