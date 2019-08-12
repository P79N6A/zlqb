package com.nyd.zeus.api.zzl;

import java.util.List;

import com.nyd.zeus.model.BillDistributionRecordVo;
import com.nyd.zeus.model.BillExtendInfoVo;
import com.nyd.zeus.model.BillInfo;
import com.nyd.zeus.model.BillRemindFlowVo;
import com.nyd.zeus.model.ReadyDistributionRequest;
import com.nyd.zeus.model.ReadyDistributionVo;
import com.nyd.zeus.model.ReminderDistributionRequest;
import com.nyd.zeus.model.ReminderDistributionVo;
import com.nyd.zeus.model.common.PagedResponse;

public interface ZeusForOrderPayBackServise {

	/**
	 * 插入提醒信息
	 * 
	 * @param billId
	 * @param remindMsg
	 * @param remindUserId
	 * @param remindUserLoginName
	 */
	public void insertRemindMsg(String orderNo, String remindMsg,
			String remindUserId, String remindUserLoginName);

	/**
	 * 查询提醒流水
	 * 
	 * @param billNo
	 * @return
	 */
	public List<BillRemindFlowVo> queryRemindMsgList(String orderNo);

	/**
	 * 根据时间查询最近三天的数据
	 * 
	 * @param day
	 * @param status
	 * @return
	 */
	public PagedResponse<List<ReminderDistributionVo>> queryReminderDistributionList(
			ReminderDistributionRequest vo);

	/**
	 * 根据订单号查询账单信息
	 * 
	 * @param orderNo
	 * @return
	 */
	public BillInfo queryBillByOrderNo(String orderNo);

	/**
	 * 根据订单号，时间，状态查询条数
	 * 
	 * @param orderNo
	 * @param day
	 * @param status
	 * @return
	 */
	public Long queryBillRepayCount(String orderNo, String day, String status);

	/**
	 * 根据时间查询最近三天的数据
	 * 
	 * @param day
	 * @param status
	 * @return
	 */
	public PagedResponse<List<ReadyDistributionVo>> queryPostlendingDistributionList(
			ReadyDistributionRequest vo);

	/**
	 * 保存extendinfo信息
	 * 
	 * @param day
	 * @param status
	 * @return
	 */
	public int saveBillExtendInfo(BillExtendInfoVo vo);

	/**
	 * 保存BillDistributionRecordVo信息
	 * 
	 * @param day
	 * @param status
	 * @return
	 */
	public int doDistribution(BillDistributionRecordVo vo);
	
	/**
	 * 失效分配记录
	 * 
	 * @param day
	 * @param status
	 * @return
	 */
	public int doDistributionInvalid(List<String> orderNos, String userId, String userName);
	
	/**
	 * 获取支付路由
	 * @return
	 */
	public String getPaychannel();
	
}
