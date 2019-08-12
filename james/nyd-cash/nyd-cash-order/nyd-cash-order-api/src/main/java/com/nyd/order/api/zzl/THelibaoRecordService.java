package com.nyd.order.api.zzl;

import java.util.List;

import com.nyd.order.model.OrderCashOut;
import com.nyd.order.model.THeLiBaoLoanRecord;
import com.nyd.order.model.THelibaoRecord;
import com.nyd.order.model.common.CommonResponse;

public interface THelibaoRecordService<T> {
	
	public THelibaoRecord getTHelibaoRecord(String userId);
	public int insertOrderRecord(THelibaoRecord T);
	public int insertTHeLiBaoLoanRecord(THeLiBaoLoanRecord T);
	public THeLiBaoLoanRecord  getTHeLiBaoLoanRecord(String userId);
	/**
	 * 得到提现数据
	 * @param orderId
	 * @return
	 */
	public OrderCashOut getOrder(String orderId);
	/**
	 * 获取为经过合利宝认证的数据
	 * @return
	 */
	public List<THelibaoRecord> getHelibaoRecordFail();
	
	public CommonResponse userRegister(THelibaoRecord T);
	
	
	/**
	 * 查询资质上传成功的数据
	 * @return
	 */
	public List<THelibaoRecord> getHelibaoRecordSuccess();

	public  CommonResponse queryHelibaoResult(THelibaoRecord vo) ;
	/**
	 * 提现成功 状态修改为 30 
	 */
	public  void updateStatus(String orderId);
}
