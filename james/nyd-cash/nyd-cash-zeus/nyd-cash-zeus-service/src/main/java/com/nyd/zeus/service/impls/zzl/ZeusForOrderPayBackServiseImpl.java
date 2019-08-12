package com.nyd.zeus.service.impls.zzl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.nyd.order.model.YmtKzjrBill.enums.BillStatusEnum;
import com.nyd.zeus.api.zzl.ZeusForOrderPayBackServise;
import com.nyd.zeus.api.zzl.ZeusSqlService;
import com.nyd.zeus.dao.BillDistributionRecordDao;
import com.nyd.zeus.dao.BillExtendInfoDao;
import com.nyd.zeus.model.BillDistributionRecordVo;
import com.nyd.zeus.model.BillExtendInfoVo;
import com.nyd.zeus.model.BillInfo;
import com.nyd.zeus.model.BillRemindFlowVo;
import com.nyd.zeus.model.ReadyDistributionRequest;
import com.nyd.zeus.model.ReadyDistributionVo;
import com.nyd.zeus.model.ReminderDistributionRequest;
import com.nyd.zeus.model.ReminderDistributionVo;
import com.nyd.zeus.model.common.ListTool;
import com.nyd.zeus.model.common.PagedResponse;

@Service("zeusForOrderPayBackServise")
public class ZeusForOrderPayBackServiseImpl implements
		ZeusForOrderPayBackServise {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ZeusForOrderPayBackServiseImpl.class);

	@Autowired
	private ZeusSqlService<?> zeusSqlService;

	@Autowired
	private BillExtendInfoDao billExtendInfoDao;

	@Autowired
	private BillDistributionRecordDao billDistributionRecordDao;

	@Override
	@Transactional
	public void insertRemindMsg(String orderNo, String remindMsg,
			String remindUserId, String remindUserLoginName) {
		// 更新所有数据为非最新，再插入一条最新数据
		String updateSql = "update t_bill_remind_flow set is_new = '1' where order_no = '%s'";
		zeusSqlService.updateSql(String.format(updateSql, orderNo));
		String insertSql = "insert into t_bill_remind_flow (order_no, remind_msg, remind_user_id, remind_user_login_name, create_time, is_new) "
				+ "values('%s', '%s', '%s', '%s', now(), '0')";
		zeusSqlService.insertSql(String.format(insertSql, orderNo, remindMsg,
				remindUserId, remindUserLoginName));
	}

	@Override
	public List<BillRemindFlowVo> queryRemindMsgList(String orderNo) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select tbrf.remind_msg, tbrf.remind_user_login_name, date_format(tbrf.create_time,'%Y-%m-%d') create_time");
		sql.append(" from t_bill_remind_flow tbrf, t_bill tb where tb.order_no = tbrf.order_no");
		sql.append(" and tb.order_no = '").append(orderNo).append("'");
		sql.append(" order by tbrf.create_time desc");
		return (List<BillRemindFlowVo>) zeusSqlService.queryT(sql.toString(),
				BillRemindFlowVo.class);
	}

	@Override
	public PagedResponse<List<ReminderDistributionVo>> queryReminderDistributionList(
			ReminderDistributionRequest vo) {
		PagedResponse<List<ReminderDistributionVo>> pageResponse = new PagedResponse<List<ReminderDistributionVo>>();

		StringBuffer sql = new StringBuffer();
		sql.append(" select");
		// sql.append("   tbrf.remind_msg");
		sql.append("  tb.user_id");
		sql.append(" , tbdr.receive_user_name");
		sql.append(" , tbei.user_name");
		sql.append(" , tbei.user_mobile");
		sql.append(" , ifnull(date_format(tbei.apply_time,'%Y-%m-%d'),'') apply_time");
		sql.append(" , ifnull(date_format(tbei.loan_time,'%Y-%m-%d'),'') loan_time");
		sql.append(" , tbei.source");
		sql.append(" , tbei.loan_num");
		sql.append(" , tb.repay_principle");
		sql.append(" , tb.already_repay_amount");
		sql.append(" , tb.wait_repay_amount");
		sql.append(" , datediff(date_format(tb.promise_repayment_date,'%Y-%m-%d'),date_format(now(),'%Y-%m-%d')) lessdays");
		sql.append(" , ifnull(date_format(tb.promise_repayment_date,'%Y-%m-%d'),'') promise_repayment_date");
		sql.append(" , tb.cur_repay_amount");
		sql.append(" , tbei.credit_trial_user_name");
		sql.append(" , tb.order_no");
		sql.append(" from t_bill tb");
		sql.append(" left join t_bill_extend_info tbei on tbei.order_no = tb.order_no");
		sql.append(" left join t_bill_distribution_record tbdr on tbdr.order_no = tb.order_no and tbdr.status = 1 and tbdr.pay_status ='1'");
		sql.append(" where 1=1");
		sql.append(" and tb.bill_status = '")
				.append(BillStatusEnum.REPAY_ING.getCode()).append("'");
		sql.append(" and (tbdr.receive_user_id is not null or tbdr.receive_user_id !='')");

		if (StringUtils.isNotEmpty(vo.getUserId())) {
			sql.append(" and tbdr.receive_user_id = '").append(vo.getUserId())
					.append("'");
		}
		if (StringUtils.isNotEmpty(vo.getCustName())) {
			sql.append(" and tbei.user_name = '").append(vo.getCustName())
					.append("'");
		}
		if (StringUtils.isNotEmpty(vo.getMobile())) {
			sql.append(" and tbei.user_mobile = '").append(vo.getMobile())
					.append("'");
		}
		if (StringUtils.isNotEmpty(vo.getLoanNo())) {
			sql.append(" and tb.order_no = '").append(vo.getLoanNo())
					.append("'");
		}
		if (StringUtils.isNotEmpty(vo.getProductName())) {
			sql.append(" and tbp.product_name = '").append(vo.getProductName())
					.append("'");
		}
		if (StringUtils.isNotEmpty(vo.getSource())) {
			sql.append(" and tbei.source = '").append(vo.getSource())
					.append("'");
		}
		if (StringUtils.isNotEmpty(vo.getCreditTrialUserName())) {
			sql.append(" and tbei.credit_trial_user_name = '")
					.append(vo.getCreditTrialUserName()).append("'");
		}
		if (StringUtils.isNotEmpty(vo.getReceiveUserName())) {
			sql.append(" and tbdr.receive_user_name = '")
					.append(vo.getReceiveUserName()).append("'");
		}

		String buffStr ="";
		if(StringUtils.isNotEmpty(vo.getPaymentDays())){
			sql.append(" and (");
			StringBuffer buff = new StringBuffer();
			if (vo.getPaymentDays().contains("0")) {
				buff.append(" datediff(date_format(tb.promise_repayment_date,'%Y-%m-%d'),date_format(now(),'%Y-%m-%d')) = 0 or");
			}
			if (vo.getPaymentDays().contains("1")) {
				buff.append(" datediff(date_format(tb.promise_repayment_date,'%Y-%m-%d'),date_format(now(),'%Y-%m-%d')) = 1 or");
			}
			if (vo.getPaymentDays().contains("2")) {
				buff.append(" datediff(date_format(tb.promise_repayment_date,'%Y-%m-%d'),date_format(now(),'%Y-%m-%d')) = 2 or");
			}
			buffStr = buff.toString();
			buffStr = buffStr.substring(0, buffStr.length()-2);
			sql.append(buffStr);
			sql.append(" and 1=1)");
		}
		sql.append(" order by tb.promise_repayment_date");
		LOGGER.info("贷中-提醒列表:" + sql.toString());
		List<ReminderDistributionVo> billList = (List<ReminderDistributionVo>) zeusSqlService
				.pageT(sql.toString(), vo.getPageNo(), vo.getPageSize(),
						ReminderDistributionVo.class);
		long total = zeusSqlService.count(sql.toString());
		pageResponse.setData(billList);
		pageResponse.setTotal(total);
		pageResponse.setSuccess(true);
		return pageResponse;
	}

	@Override
	public BillInfo queryBillByOrderNo(String orderNo) {
		String sql = "select * from t_bill where order_no = '%s' limit 1";
		List<BillInfo> list = (List<BillInfo>) zeusSqlService.queryT(
				String.format(sql, orderNo), BillInfo.class);
		if (CollectionUtils.isNotEmpty(list))
			return list.get(0);
		else
			return null;
	}

	@Override
	public Long queryBillRepayCount(String orderNo, String day, String status) {
		String sql = "select * from t_bill_repay where order_no = '%s' and create_time >= '%s 00:00:00' and create_time <= '%s 23:59:59' and result_code = '%s'";
		return zeusSqlService.count(String.format(sql, orderNo, day, day,
				status));
	}

	/**
	 * 根据时间查询最近三天的数据
	 * 
	 * @param day
	 * @param status
	 * @return
	 */
	@Override
	public PagedResponse<List<ReadyDistributionVo>> queryPostlendingDistributionList(
			ReadyDistributionRequest vo) {
		PagedResponse<List<ReadyDistributionVo>> pageResponse = new PagedResponse<List<ReadyDistributionVo>>();
		String sql = "  SELECT  "
				+ " 	DATEDIFF(DATE_FORMAT(bill.promise_repayment_date,'%Y-%m-%d'),DATE_FORMAT(NOW(),'%Y-%m-%d')) lessDays, "
				 + " 	bill.*, " + " 	bille.user_name, "
				+ " 	bille.user_mobile, " + " bille.loan_num,	bille.credit_trial_user_name, "
				+ " 	bille.credit_trial_user_id, " + " 	bille.apply_time, "
				+ " 	bille.source ,bill.create_time as loanTime" + "  FROM  " + " 	t_bill bill  "
				+ "  inner JOIN  " + " 	t_bill_extend_info  bille  "
				+ "  on bill.order_no=bille.order_no   " 
				+ "  where  " + "  1=1  ";
	    if (StringUtils.isNotEmpty(vo.getCustName())) {
			sql += "  AND  bille.user_name='" + vo.getCustName() + "'  ";
		}
		if (StringUtils.isNotEmpty(vo.getMobile())) {
			sql += "  AND  bille.user_mobile='" + vo.getMobile() + "'  ";
		}
		if (StringUtils.isNotEmpty(vo.getLoanNo())) {
			sql += "  AND  bill.order_no='" + vo.getLoanNo() + "'  ";
		}
		
		if (StringUtils.isNotEmpty(vo.getSource())) {
			sql += "  AND  bille.source='" + vo.getSource() + "'  ";
		}
		sql += " and (";
		
		if(StringUtils.isNotEmpty(vo.getPaymentDays())){
			String[] sss = vo.getPaymentDays().split(",");
			for (String s : sss) {
				sql += " DATEDIFF(DATE_FORMAT(bill.promise_repayment_date,'%Y-%m-%d'),DATE_FORMAT(NOW(),'%Y-%m-%d'))= "+s+" or";
			}
			sql = sql.substring(0, sql.length()-2);
			sql += " and";
		}
        sql += " 1=1 )";
		sql +=  " and bill.bill_status='"+com.nyd.zeus.service.enums.BillStatusEnum.REPAY_ING.getCode()+"'"
				+ "  and (SELECT count(1) from t_bill_distribution_record where order_no=bill.order_no and status = 1 and pay_status='1')=0 "
				+ "  ORDER BY bill.promise_repayment_date  ";
		LOGGER.info("贷中-分配列表:" + sql);
		List<ReadyDistributionVo> billList = (List<ReadyDistributionVo>) zeusSqlService
				.pageT(sql, vo.getPageNo(), vo.getPageSize(),
						ReadyDistributionVo.class);
		long total = zeusSqlService.count(sql);
		pageResponse.setData(billList);
		pageResponse.setTotal(total);
		pageResponse.setSuccess(true);
		return pageResponse;

	}

	@Override
	public int saveBillExtendInfo(BillExtendInfoVo vo) {
		try {
			billExtendInfoDao.save(vo);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 保存BillDistributionRecordVo信息
	 * 
	 * @param day
	 * @param status
	 * @return
	 */
	public int saveDistribution(BillDistributionRecordVo vo) {
		try {
			billDistributionRecordDao.save(vo);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int doDistributionInvalid(List<String> orderNos, String userId,
			String userName) {
		int i = 0;
		if (new ListTool<String>().isNotEmpty(orderNos)) {
			for (String orderNo : orderNos) {
				try {
					StringBuffer sb = new StringBuffer();
					sb.append(" update t_bill_distribution_record");
					sb.append(" set status = 0");
					sb.append(" , update_time = now()");
					sb.append(" , withdraw_user_id = '").append(userId)
							.append("'");
					sb.append("   , withdraw_user_name = '").append(userName)
							.append("'");
					sb.append(" where status = 1");
					sb.append(" and order_no = '").append(orderNo).append("'");
					zeusSqlService.updateSql(sb.toString());
					i++;
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}
		return i;
	}

	@Override
	public int doDistribution(BillDistributionRecordVo vo) {
		int i = 0;
		try {
			String sql = "update  t_bill_distribution_record set status = 0 ,update_time=now() where order_no ='"
					+ vo.getOrderNo()
					+ "' and status=1 and pay_status='"
					+ vo.getPayStatus() + "'";
			zeusSqlService.updateSql(sql);
			billDistributionRecordDao.save(vo);
			return i;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public String getPaychannel() {
		String sql = "select * from t_paychannel_temp_ratio";
		List<JSONObject> list = zeusSqlService.query(sql);

		String channel = "changjie";
		if (CollectionUtils.isEmpty(list))
			return channel;

		List<Paychannel> limitList = new ArrayList<>();
		int startIndex = 0;
		for (JSONObject json : list) {
			try {
				int ratio = json.getInteger("ratio");
				if (ratio <= 0)
					continue;
				String code = json.getString("code");
				Paychannel channelObj = new ZeusForOrderPayBackServiseImpl().new Paychannel();
				channelObj.code = code;
				channelObj.begin = startIndex;
				startIndex += ratio;
				channelObj.end = startIndex;
				limitList.add(channelObj);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}

		Random r = new Random();
		int number = 1 + r.nextInt(startIndex);
		Optional<Paychannel> opt = limitList
				.stream()
				.filter(channelObj -> channelObj.begin < number
						&& number <= channelObj.end).findFirst();
		if (opt.isPresent())
			channel = opt.get().code;
		return channel;
	}

	private class Paychannel {
		String code;
		int begin;
		int end;
	}

}
