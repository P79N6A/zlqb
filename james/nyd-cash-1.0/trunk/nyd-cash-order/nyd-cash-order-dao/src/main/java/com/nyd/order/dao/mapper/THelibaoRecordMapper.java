package com.nyd.order.dao.mapper;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.nyd.order.model.OrderCashOut;
import com.nyd.order.model.THeLiBaoLoanRecord;
import com.nyd.order.model.THelibaoRecord;
import com.nyd.order.model.common.BaseMapper;
/**
 * THelibaoRecord Mapper
 *
 */
@Repository
public interface THelibaoRecordMapper<T> extends BaseMapper<T> {
	/**
	 * 
	 * @param userId
	 * @return
	 */
	public THelibaoRecord getEntityByUserId(String orderId);
	

	public int insertOrderRecord(THelibaoRecord T);
	/**
	 * 添加记录
	 * @param param
	 * @return
	 */
	public int insertOrderLoanRecord(THeLiBaoLoanRecord T);
	/**
	 * 查询记录
	 * @param param
	 * @return
	 */
	public THeLiBaoLoanRecord getOrderLoanRecord(String orderId);
	/**
	 * 得到提现数据
	 * @param orderId
	 * @return
	 */
	 public OrderCashOut getOrderCashOut(String orderId);
	
	THelibaoRecord findByUserId(String userId);
	/**
	 * 获取所有未认证合利宝的数据
	 * @return
	 */
	List<THelibaoRecord> getallUncertifiedOrder();
	
	int changeStatusByCustId(THelibaoRecord T);
	/**
	 * 获取所有状态注册合利宝成功进行校验的数据 
	 * @return
	 */
	List<THelibaoRecord> getallSuccessOrder();
	void updateOrderStatus(String orderId);
}
