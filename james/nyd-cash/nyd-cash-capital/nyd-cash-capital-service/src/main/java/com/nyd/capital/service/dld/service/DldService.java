package com.nyd.capital.service.dld.service;

import java.io.File;
import java.math.BigDecimal;
import java.util.Map;

import com.nyd.capital.model.dld.ContractSignatureParams;
import com.nyd.capital.model.dld.LoanCallBackParams;
import com.nyd.capital.model.dld.LoanCommitParams;
import com.nyd.capital.model.dld.LoanOrderQueryParams;
import com.nyd.capital.model.dld.OrderReportParams;
import com.nyd.capital.model.dld.RegisterUserParams;
import com.nyd.order.model.msg.OrderMessage;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 多贷点服务接口
 * @author zhangdk
 *
 */
public interface DldService {

	/**
	 * 注册借款人信息接口
	 * @param params
	 * @return
	 */
	ResponseData registerUser(RegisterUserParams params,String userId,String orderId,File file1,File file2);

	ResponseData registerUserByUserId(String userId,String orderNo);
	/**
	 * 提交借款接口
	 * @param params
	 * @return
	 */
	ResponseData loanSubmit(LoanCommitParams params,String orderNo, String userId);


	ResponseData loanSubmitByOrderNo(String userId,String orderNo,String custId);

	String callBack(Map<String, Object> map);
	
	String callBackRetry(LoanCallBackParams map);
	/**
	 * 上传借款还款补充协议接口
	 * @param params
	 * @return
	 */
	ResponseData uploadLoanPro(String params);
	/**
	 * 获取合同下载地址接口
	 * @param params
	 * @return
	 */
	ResponseData getContractDownloadUrl(ContractSignatureParams params);

	ResponseData getContractDownloadUrlByOrderNo(String orderNo);
	/**
	 * 获取订单报表信息接口
	 * @param params
	 * @return
	 */
	ResponseData getLoanReport(OrderReportParams params);
	/**
	 * 获取账户余额接口
	 * @param params
	 * @return
	 */
	BigDecimal userBalanceQuery();
	/**
	 * 借款订单查询
	 * @param params
	 * @return
	 */
	ResponseData loanOrderQuery(LoanOrderQueryParams params);

	ResponseData loanOrderQueryByOrderNo(String orderNo) ;

	ResponseData errerHandle(OrderMessage message);
}
