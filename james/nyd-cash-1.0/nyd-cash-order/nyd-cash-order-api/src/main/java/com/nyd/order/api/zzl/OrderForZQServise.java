package com.nyd.order.api.zzl;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.nyd.order.model.TExecuteLoanRecord;
import com.nyd.order.model.TOrderLoanRecord;
import com.nyd.order.model.common.CommonResponse;
import com.nyd.order.model.common.PagedResponse;
import com.nyd.order.model.vo.AgreementListVO;
import com.nyd.order.model.vo.CenerateContractVo;
import com.nyd.order.model.vo.ConfirmRefunVo;
import com.nyd.order.model.vo.DeductInfoVO;
import com.nyd.order.model.vo.OrderProductInfoVO;
import com.nyd.order.model.vo.OrderlistVo;
import com.nyd.order.model.vo.RefundInfoVO;
import com.nyd.order.model.vo.SaveRefunRecordVo;

public interface OrderForZQServise {
	/**
	 * 条件查询订单列表
	 * @author 
	 * @param vo
	 * @return
	 */
	public PagedResponse<List<OrderlistVo>> getOrderList(OrderlistVo vo);
	
	/**
	 * 获取详情-产品信息
	 * @param orderNo
	 * @return
	 */
	public CommonResponse<OrderProductInfoVO> getOrderProduct(String orderNo);
	
	
	/**
	 * 
	 * @param orderNo
	 * @return
	 */
	public PagedResponse<DeductInfoVO> getDeductInfo(String orderNo);
	
	public PagedResponse<RefundInfoVO> getRefundInfo(String orderNo);
	
	
	public PagedResponse<List<AgreementListVO>> getAgreementList(String orderNo);
	
	/**
	 * 添加退款申请记录
	 * @param vo
	 * @return
	 */
	public CommonResponse<Object> saveRefundRecord(SaveRefunRecordVo vo);

	/**
	 * 退款
	 * @param vo
	 * @return
	 */
	public CommonResponse<Object> refundMoney(ConfirmRefunVo vo);

	/**
	 * 跑批查询待放款记录
	 * @return
	 */
	List<TOrderLoanRecord> getOrderLoanRecords();

	/**
	 * 进行放款
	 * @param vo
	 * @param bankInfo
	 * @return
	 */
	CommonResponse<JSONObject> executeOrderLoan(TOrderLoanRecord vo,JSONObject bankInfo);

	/**
	 * 跑批查询放款中反查询记录
	 * @return
	 */
	List<TExecuteLoanRecord> getOrderExecuteLoanRecord();

	/**
	 * 查询放款最终结果
	 * @param vo
	 * @param bankInfo
	 * @return
	 * @throws Exception
	 */
	CommonResponse<JSONObject> queryTransactionResult(TExecuteLoanRecord vo,JSONObject bankInfo) throws Exception;

	/**
	 * 放款生成还款计划
	 * @param orderNo
	 * @param bankInfo
	 * @return
	 * @throws Exception
	 */
	CommonResponse<JSONObject> generateCrmPayControl(String orderNo,JSONObject bankInfo) throws Exception;
	
	
	/**
	 * 放款生成合同
	 * @param vo
	 * @return 
	 */
	CommonResponse<Object> generateContract(CenerateContractVo vo);

	
}
