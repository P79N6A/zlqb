package com.nyd.zeus.service.impls.zzl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nyd.msg.model.SmsRequest;
import com.nyd.msg.service.ISendSmsService;
import com.nyd.zeus.api.zzl.ZeusForZLQServise;
import com.nyd.zeus.api.zzl.ZeusSqlService;
import com.nyd.zeus.dao.mapper.RemitMapper;
import com.nyd.zeus.model.OrderRecordHisVo;
import com.nyd.zeus.model.common.CommonResponse;
import com.nyd.zeus.model.common.PagedResponse;
import com.nyd.zeus.model.helibao.util.Uuid;
import com.nyd.zeus.model.helibao.util.chanpay.DateUtils;
import com.nyd.zeus.model.sms.MsgListQueryVO;
import com.nyd.zeus.model.sms.SendMsgLoanVO;
import com.nyd.zeus.model.sms.SmsReplyEntity;
import com.nyd.zeus.service.rabbit.DuringLoanMqProducer;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;

@Transactional
@Service("zeusForZLQServise")
public class ZeusForZLQServiseImpl implements ZeusForZLQServise {

	public static final String REQ_MSG_CONTENT = "【助乐钱包】 尊敬的'%s'您好，'%s'提醒您，您有一笔'%s'元的款,在'%s'号到期，请于APP上及时还款，如需要人工服务请回复 hk 稍后将会有工作人员与您联系，请注意接听电话。仅此通知。";

	@Autowired
	private RabbitmqProducerProxy rabbitmqProducerProxy;

	@Autowired
	private RemitMapper mapper;

	@Autowired
	private ZeusSqlService zeusSqlService;
	
//	@Autowired
//	private ZeusSqlService sqlService;

	@Autowired
	private DuringLoanMqProducer duringLoanMqProducer;

	@Autowired
	private ISendSmsService iSendSmsService;

	@Override
	public OrderRecordHisVo findHisByOrder(String orderNo) {
		OrderRecordHisVo hisVo = mapper.getOverdueDays(orderNo);

		return hisVo;
	}

	@Override
	public PagedResponse<List<MsgListQueryVO>> queryMsgList(MsgListQueryVO vo) {
		PagedResponse<List<MsgListQueryVO>> common = new PagedResponse<List<MsgListQueryVO>>();
		StringBuffer querySql = new StringBuffer();
		querySql.append(" SELECT");
		querySql.append(" info.user_mobile mobile,");
		querySql.append(" info.user_name custName,");
		querySql.append(" IFNULL(sms.`status`,'0') status,");
		querySql.append(" sms.send_content sendContent,");
		querySql.append(" date_format(sms.create_time , '%Y-%m-%d %H:%i:%s') createTime,");
		querySql.append(" sms.create_user createUser,");
		querySql.append(" date_format(bill.promise_repayment_date , '%Y-%m-%d %H:%i:%s') expireTime,");
		querySql.append(" bill.wait_repay_amount waitRepayAmount,");
		// expireTime
		querySql.append(" date_format(bill.promise_repayment_date , '%Y-%m-%d') retExpireTime,");
		//querySql.append(" bill.*, ");
		querySql.append(" bill.bill_no bill_no, ");
		querySql.append(" pro.product_name productName");
		querySql.append(" FROM");
		querySql.append(" `t_bill` bill");
		querySql.append(" LEFT JOIN t_bill_sms_record sms ON bill.bill_no = sms.bill_no");
		querySql.append(" LEFT JOIN t_bill_extend_info info ON bill.order_no = info.order_no");
		querySql.append(" LEFT JOIN t_bill_product pro ON pro.order_no = bill.order_no ");
		querySql.append(" WHERE 1 = 1 ");
		querySql.append(" and TO_DAYS( bill.promise_repayment_date ) - TO_DAYS( NOW( ) ) > 0 ");
		querySql.append(" AND TO_DAYS( bill.promise_repayment_date ) - TO_DAYS( NOW( ) ) <3");
		// TODO hwt
		// 发送开始时间
		if (StringUtils.isNotEmpty(vo.getSendTimeBegin())) {
			querySql.append(
					" and date_format(sms.create_time , '%Y-%m-%d %H:%i:%s') >= '" + vo.getSendTimeBegin() + "'");
		}
		// 发送结束时间
		if (StringUtils.isNotEmpty(vo.getSendTimeEnd())) {
			querySql.append(" and date_format(sms.create_time , '%Y-%m-%d %H:%i:%s') <= '" + vo.getSendTimeEnd() + "'");
		}
		// 到期开始时间
		if (StringUtils.isNotEmpty(vo.getExpireTimeBegin())) {
			querySql.append(" and date_format(bill.promise_repayment_date , '%Y-%m-%d %H:%i:%s') >= '"
					+ vo.getExpireTimeBegin() + "'");
		}
		// 到期结束时间
		if (StringUtils.isNotEmpty(vo.getExpireTimeEnd())) {
			querySql.append(" and date_format(bill.promise_repayment_date , '%Y-%m-%d %H:%i:%s') <= '"
					+ vo.getExpireTimeEnd() + "'");
		}
		//
		if (StringUtils.isNotEmpty(vo.getMsgChannel())) {
			querySql.append(" and sms.channel = '" + vo.getMsgChannel() + "'");
		}
		// 受理人员
		if (StringUtils.isNotEmpty(vo.getCreateUser())) {
			querySql.append(" and sms.create_user = '" + vo.getCreateUser() + "'");
		}
		// 发送状态
		if (StringUtils.isNotEmpty(vo.getStatus())) {
			if ("1".equals(vo.getStatus())) {
				querySql.append(" and sms.`status` = '1'");
			} else {
				querySql.append(" and sms.`status` is null");
			}

		}
		// 回复状态
		if (StringUtils.isNotEmpty(vo.getReplyStatus())) {
			querySql.append(" ");
		}

		querySql.append(" ORDER BY sms.create_time desc");
		Long count = zeusSqlService.count(querySql.toString());
		List<MsgListQueryVO> list = zeusSqlService.pageT(querySql.toString(), vo.getPageNo(), vo.getPageSize(),
				MsgListQueryVO.class);
		for(MsgListQueryVO msgListQueryVO : list){
			List<JSONObject> retContent = new ArrayList<JSONObject>();
			String mobile = msgListQueryVO.getMobile();
			StringBuffer sb = new StringBuffer();
			if(StringUtils.isNotEmpty(mobile)){
				sb.append(" select content,deliver_time deliverTime from t_bill_sms_reply where mobile = '" + mobile +"'");
				List<SmsReplyEntity> replyList = zeusSqlService.queryT(sb.toString(), SmsReplyEntity.class);
				if(CollectionUtils.isNotEmpty(replyList)){
					for(SmsReplyEntity smsReplyEntity : replyList){
						JSONObject smsJson = new JSONObject();
						smsJson.put("content", smsReplyEntity.getContent());
						smsJson.put("deliverTime", smsReplyEntity.getDeliverTime());
						retContent.add(smsJson);
					}
				}
			}
			msgListQueryVO.setRetContent(retContent);
			
		}
		common.setSuccess(true);
		common.setData(list);
		common.setTotal(count);
		return common;
	}

	@Override
	public CommonResponse<Boolean> sendMsgProducer(SendMsgLoanVO vo) {
		CommonResponse<Boolean> common = new CommonResponse<Boolean>();
		rabbitmqProducerProxy.convertAndSend("during.sendMsgLoan", vo);
		common.setSuccess(true);
		return common;
	}

	@Override
	public CommonResponse<Boolean> saveMsgDetail(SendMsgLoanVO vo) {
		CommonResponse<Boolean> common = new CommonResponse<Boolean>();
		List<MsgListQueryVO> list = vo.getOrderLists();
		if (CollectionUtils.isNotEmpty(list)) {
			for (MsgListQueryVO msgListQueryVO : list) {
				String time = DateUtils.format(new Date(), DateUtils.STYLE_1);

				String reqContent = String.format(REQ_MSG_CONTENT, msgListQueryVO.getCustName(),
						msgListQueryVO.getCreateUser(), msgListQueryVO.getWaitRepayAmount(),
						msgListQueryVO.getRetExpireTime());
				String id = Uuid.getUuid26();
				StringBuffer insert = new StringBuffer();
				insert.append(
						" INSERT INTO `t_bill_sms_record` ( `id`, `bill_no`, `status`, `user_id`, `send_content`, `create_time`, `update_time`, `order_no`, `channel`, `create_user` )");
				insert.append(" VALUES");
				insert.append(" (").append(id + ",");
				if (StringUtils.isNotEmpty(msgListQueryVO.getBillNo())) {
					insert.append(" '" + msgListQueryVO.getBillNo() + "',");
				} else {
					insert.append(" '',");
				}
				insert.append(" '1',");
				if (StringUtils.isNotEmpty(msgListQueryVO.getUserId())) {
					insert.append(" '" + msgListQueryVO.getUserId() + "',");
				} else {
					insert.append(" '',");
				}
				insert.append(" '" + reqContent + "',");
				insert.append(" '" + time + "',");
				insert.append(" '" + time + "',");
				if (StringUtils.isNotEmpty(msgListQueryVO.getOrderNo())) {
					insert.append(" '" + msgListQueryVO.getOrderNo() + "',");
				} else {
					insert.append(" '',");
				}
				if (StringUtils.isNotEmpty(msgListQueryVO.getOrderNo())) {
					insert.append(" '" + msgListQueryVO.getOrderNo() + "',");
				} else {
					insert.append(" '',");
				}
				// channel 渠道为空
				insert.append(" '',");
//				create_user
				if (StringUtils.isNotEmpty(msgListQueryVO.getCreateUser())) {
					insert.append(" '" + msgListQueryVO.getCreateUser() + "'");
				} else {
					insert.append(" ''");
				}
				insert.append(" )");
				zeusSqlService.insertSql(insert.toString());
				common.setSuccess(true);
			}

		}
		return common;
	}

	@Override
	public CommonResponse<Boolean> doSendMsg(SendMsgLoanVO vo) {
		CommonResponse<Boolean> common = new CommonResponse<Boolean>();
		List<MsgListQueryVO> list = vo.getOrderLists();
		for (MsgListQueryVO msgListQueryVO : list) {
			SmsRequest sms = new SmsRequest();
			sms.setAppName("助乐钱包");
			sms.setCellphone(msgListQueryVO.getMobile());
			sms.setSmsType(50);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("custName", msgListQueryVO.getCustName());
			map.put("price", msgListQueryVO.getWaitRepayAmount());
			map.put("empName", msgListQueryVO.getCreateUser());
			map.put("expireTime", msgListQueryVO.getRetExpireTime());
			sms.setMap(map);
			//发送短信
			iSendSmsService.sendSingleSms(sms);
			//保存发送短信数据
			saveMsgDetail(vo);
		}
		return common;
	}

	public static void main(String[] args) {
		
	}

	@Override
	public CommonResponse<JSONObject> doReceiveMsg() throws Exception{
		CommonResponse<JSONObject> retCommon = new CommonResponse<JSONObject>();
		com.nyd.msg.model.CommonResponse<JSONArray> common = iSendSmsService.smsReport();
		if(common.isSuccess()){
			JSONArray array = common.getData();
			if(null!=array && array.size()>0){
				for(int i =0; i<array.size();i++){
					JSONObject json = JSONObject.parseObject(String.valueOf(array.get(i)));
					SmsReplyEntity sms = new SmsReplyEntity();
					//发送号码
					String phone = String.valueOf(json.get("phone"));
					if(StringUtils.isEmpty(phone)){
						sms.setPhone("");
					}else{
						sms.setPhone(phone);
					}
					//发送时间
					String dealTime = String.valueOf(json.get("delivertime"));
					if(StringUtils.isEmpty(dealTime)){
						sms.setDeliverTime("");
					}else{
						sms.setDeliverTime(dealTime);
					}
					//发送内容
					String content =  String.valueOf(json.get("content"));
					if(StringUtils.isEmpty(content)){
						sms.setContent("");
					}else{
						sms.setContent(content);
					}
					saveReceiveMsg(sms);
					
				}
			}
		}
		retCommon.setSuccess(true);
		return retCommon;
	}

	@Override
	public CommonResponse<JSONObject> saveReceiveMsg(com.nyd.zeus.model.sms.SmsReplyEntity sms) throws Exception{
		CommonResponse<JSONObject> common = new CommonResponse<JSONObject>();
		StringBuffer insertSql = new StringBuffer();
		String time = DateUtils.format(new Date(), DateUtils.STYLE_1);
		String id = Uuid.getUuid26();
		insertSql.append(" INSERT INTO `t_bill_sms_reply` ( `id`, `mobile`, `content`, `deliver_time`, `create_time` )");
		insertSql.append(" VALUES (");
		insertSql.append(" '" + id + "',");
		insertSql.append(" '" + sms.getPhone()+ "',");
		insertSql.append(" '" + sms.getContent() + "',");
		insertSql.append(" '" + sms.getDeliverTime()+ "',");
		insertSql.append(" '" + time +"'");
		insertSql.append(" )");
		zeusSqlService.insertSql(insertSql.toString());
		common.setSuccess(true);
		return common;
	}



}
