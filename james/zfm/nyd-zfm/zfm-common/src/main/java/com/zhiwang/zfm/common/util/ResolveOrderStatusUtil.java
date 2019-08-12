package com.zhiwang.zfm.common.util;

public class ResolveOrderStatusUtil {

	/**
	 * 解析订单状态
	 * @param flag
	 * @return
	 */
	public static String analysisStatus(Integer flag){
		String orderStatus = "";
		switch (flag) {
		case 0:
			orderStatus = "申请中";
			break;
		case 1:
			orderStatus = "审批中";
			break;
		case 2:
			orderStatus = "待提现";
			break;
		case 3:
			orderStatus = "放款中";
			break;
		case 4:
			orderStatus = "还款中";
			break;
		case 5:
			orderStatus = "已结清";
			break;
		case 6:
			orderStatus = "失效";
			break;
		case 7:
			orderStatus = "拒单";
			break;
		case 8:
			orderStatus = "放款失败";
			break;
		default:
			orderStatus = "申请中";
			break;
		}
		return orderStatus;
	}
}
