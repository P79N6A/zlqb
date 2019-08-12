package com.creativearts.nyd.pay.service.alipay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 支付宝支付
 */
public class AliPayApi {

	/**
	 * APP支付
	 * @param model
	 * @param notifyUrl
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String startAppPayStr(AlipayTradeAppPayModel model, String notifyUrl) throws AlipayApiException {
		AlipayTradeAppPayResponse response = appPay(model,notifyUrl);
		return response.getBody();
	}
	
	/**
	 * APP支付
	 * https://doc.open.alipay.com/docs/doc.htm?treeId=54&articleId=106370&docType=1
	 * @param model
	 * @param notifyUrl
	 * @return {AlipayTradeAppPayResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradeAppPayResponse appPay(AlipayTradeAppPayModel model, String notifyUrl) throws AlipayApiException {
		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		//这里和普通的接口调用不同，使用的是sdkExecute
        AlipayTradeAppPayResponse response = AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().sdkExecute(request);
		return response;
	}

	/**
	 * WAP支付
	 * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.dfHHR3&treeId=203&articleId=105285&docType=1
	 * @param response
	 * @param model
	 * @param returnUrl
	 * @param notifyUrl
	 * @throws {AlipayApiException}
	 * @throws IOException
	 */
	public static void wapPay(HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl, String notifyUrl) throws AlipayApiException, IOException {
		String form = wapPayToString(response, model, returnUrl, notifyUrl);
		HttpServletResponse httpResponse = response;
		httpResponse.setContentType("text/html;charset=" + AliPayApiConfigKit.getAliPayApiConfig().getCharset());
		httpResponse.getWriter().write(form);// 直接将完整的表单html输出到页面
		httpResponse.getWriter().flush();
		System.out.println("###############################################");
	}
	/**
	 * WAP支付
	 * @param response
	 * @param model
	 * @param returnUrl
	 * @param notifyUrl
	 * @return {String}
	 * @throws {AlipayApiException}
	 * @throws IOException
	 */
	public static String wapPayToString(HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl, String notifyUrl) throws AlipayApiException, IOException {
		AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();// 创建API对应的request
		alipayRequest.setReturnUrl(returnUrl);
		alipayRequest.setNotifyUrl(notifyUrl);// 在公共参数中设置回跳和通知地址
		alipayRequest.setBizModel(model);// 填充业务参数
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().pageExecute(alipayRequest).getBody(); // 调用SDK生成表单
	}

	/**
	 * 条形码支付、声波支付
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7629065.0.0.XVqALk&apiId=850&docType=4
	 * @param model
	 * @param notifyUrl
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String tradePay(AlipayTradePayModel model, String notifyUrl) throws AlipayApiException {
		AlipayTradePayResponse response = tradePayToResponse(model,notifyUrl);
		return response.getBody();
	}
	/**
	 * 条形码支付、声波支付
	 * @param model
	 * @param notifyUrl
	 * @return {AlipayTradePayResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradePayResponse tradePayToResponse(AlipayTradePayModel model, String notifyUrl) throws AlipayApiException {
		AlipayTradePayRequest request = new AlipayTradePayRequest();
		request.setBizModel(model);// 填充业务参数
		request.setNotifyUrl(notifyUrl);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request); // 通过AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient()调用API，获得对应的response类
	}
	
	
	
	/**
	 * 扫码支付
	 * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.i0UVZn&treeId=193&articleId=105170&docType=1#s4
	 * @param model
	 * @param notifyUrl
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String tradePrecreatePay(AlipayTradePrecreateModel model, String notifyUrl) throws AlipayApiException {
		AlipayTradePrecreateResponse response = tradePrecreatePayToResponse(model,notifyUrl);
		return response.getBody();
	}
	/**
	 * 扫码支付
	 * @param model
	 * @param notifyUrl
	 * @return {AlipayTradePrecreateResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradePrecreateResponse tradePrecreatePayToResponse(AlipayTradePrecreateModel model, String notifyUrl) throws AlipayApiException {
		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	
	/**
	 * 单笔转账到支付宝账户
	 * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.54Ty29&treeId=193&articleId=106236&docType=1
	 * @param model
	 * @return {boolean}
	 * @throws {AlipayApiException}
	 */
	public static boolean transfer(AlipayFundTransToaccountTransferModel model) throws AlipayApiException {
		AlipayFundTransToaccountTransferResponse response = transferToResponse(model);
		String result = response.getBody();
		if (response.isSuccess()) {
			return true;
		} else {
			//调用查询接口查询数据
			JSONObject jsonObject = JSONObject.parseObject(result);
			String out_biz_no = jsonObject.getJSONObject("alipay_fund_trans_toaccount_transfer_response").getString("out_biz_no");
			AlipayFundTransOrderQueryModel queryModel = new AlipayFundTransOrderQueryModel();
			model.setOutBizNo(out_biz_no);
			boolean isSuccess = transferQuery(queryModel);
			if (isSuccess) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 单笔转账到支付宝账户
	 * @param model
	 * @return {AlipayFundTransToaccountTransferResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundTransToaccountTransferResponse transferToResponse(AlipayFundTransToaccountTransferModel model) throws AlipayApiException {
		AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	
	/**
	 * 转账查询接口
	 * @param model
	 * @return {boolean}
	 * @throws {AlipayApiException}
	 */
	public static boolean transferQuery(AlipayFundTransOrderQueryModel model) throws AlipayApiException {
		AlipayFundTransOrderQueryResponse response = transferQueryToResponse(model);
		if(response.isSuccess()){
			return true;
		}
		return false;
	}
	/**
	 * 转账查询接口
	 * @param model
	 * @return {AlipayFundTransOrderQueryResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundTransOrderQueryResponse transferQueryToResponse(AlipayFundTransOrderQueryModel model) throws AlipayApiException {
		AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	
	/**
	 * 交易查询接口
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7395905.0.0.8H2JzG&docType=4&apiId=757
	 * @param model
	 * @return {boolean}
	 * @throws {AlipayApiException}
	 */
	public static boolean isTradeQuery(AlipayTradeQueryModel model) throws AlipayApiException {
		AlipayTradeQueryResponse response = tradeQuery(model);
		if(response.isSuccess()){
			return true;
		}
		return false;
	}
	/**
	 * 交易查询接口
	 * @param model
	 * @return {AlipayTradeQueryResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradeQueryResponse tradeQuery(AlipayTradeQueryModel model) throws AlipayApiException {
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	
	
	/**
	 * 交易撤销接口
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7395905.0.0.XInh6e&docType=4&apiId=866
	 * @param model
	 * @return {boolean}
	 * @throws {AlipayApiException}
	 */
	public static boolean isTradeCancel(AlipayTradeCancelModel model) throws AlipayApiException {
		AlipayTradeCancelResponse response = tradeCancel(model);
		if(response.isSuccess()){
			return true;
		}
		return false;
	}
	/**
	 * 交易撤销接口
	 * @param model
	 * @return {AlipayTradeCancelResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradeCancelResponse tradeCancel(AlipayTradeCancelModel model) throws AlipayApiException {
		AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
		request.setBizModel(model);
		AlipayTradeCancelResponse response = AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
		return response;
	}
	/**
	 * 关闭订单
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7629065.0.0.21yRUe&apiId=1058&docType=4
	 * @param model
	 * @return {boolean}
	 * @throws {AlipayApiException}
	 */
	public static boolean isTradeClose(AlipayTradeCloseModel model) throws AlipayApiException {
		AlipayTradeCloseResponse response = tradeClose(model);
		if(response.isSuccess()){
			return true;
		}
		return false;
	}
	/**
	 * 关闭订单
	 * @param model
	 * @return {AlipayTradeCloseResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradeCloseResponse tradeClose(AlipayTradeCloseModel model) throws AlipayApiException {
		AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
		
	}
	/**
	 * 统一收单交易创建接口
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7629065.0.0.21yRUe&apiId=1046&docType=4
	 * @param model
	 * @param notifyUrl
	 * @return {AlipayTradeCreateResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradeCreateResponse tradeCreate(AlipayTradeCreateModel model, String notifyUrl) throws AlipayApiException {
		AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	
	
	/**
	 * 退款
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7395905.0.0.SAyEeI&docType=4&apiId=759
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String tradeRefund(AlipayTradeRefundModel model) throws AlipayApiException {
		AlipayTradeRefundResponse response = tradeRefundToResponse(model);
		return response.getBody();
	}
	/**
	 * 退款
	 * @param model
	 * @return {AlipayTradeRefundResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradeRefundResponse tradeRefundToResponse(AlipayTradeRefundModel model) throws AlipayApiException {
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	
	/**
	 * 退款查询
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7629065.0.0.KQeTSa&apiId=1049&docType=4
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String tradeRefundQuery(AlipayTradeFastpayRefundQueryModel model) throws AlipayApiException {
		AlipayTradeFastpayRefundQueryResponse response = tradeRefundQueryToResponse(model);
		return response.getBody();
	}
	/**
	 * 退款查询
	 * @param model
	 * @return {AlipayTradeFastpayRefundQueryResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradeFastpayRefundQueryResponse tradeRefundQueryToResponse(AlipayTradeFastpayRefundQueryModel model) throws AlipayApiException {
		AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	
	
	/**
	 * 查询对账单下载地址
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String billDownloadurlQuery(AlipayDataDataserviceBillDownloadurlQueryModel model) throws AlipayApiException {
		AlipayDataDataserviceBillDownloadurlQueryResponse response =  billDownloadurlQueryToResponse(model);
		return response.getBillDownloadUrl();
	}
	/**
	 * 查询对账单下载地址
	 * @param model
	 * @return {AlipayDataDataserviceBillDownloadurlQueryResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayDataDataserviceBillDownloadurlQueryResponse billDownloadurlQueryToResponse (AlipayDataDataserviceBillDownloadurlQueryModel model) throws AlipayApiException {
		AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	
	
	/**
	 * 交易结算接口
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7395905.0.0.nl0RS3&docType=4&apiId=1147
	 * @param model
	 * @return {boolean}
	 * @throws {AlipayApiException}
	 */
	public static boolean isTradeOrderSettle(AlipayTradeOrderSettleModel model) throws AlipayApiException {
		AlipayTradeOrderSettleResponse response  = tradeOrderSettle(model);
		if(response.isSuccess()){
			return true;
		}
		return false;
	}
	/**
	 * 交易结算接口
	 * @param model
	 * @return {AlipayTradeOrderSettleResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradeOrderSettleResponse tradeOrderSettle(AlipayTradeOrderSettleModel model) throws AlipayApiException {
		AlipayTradeOrderSettleRequest request = new AlipayTradeOrderSettleRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	
	/**
	 * 电脑网站支付(PC支付)
	 * @param httpResponse
	 * @param model
	 * @param notifyUrl
	 * @param returnUrl
	 * @throws {AlipayApiException}
	 * @throws IOException
	 */
	public static void tradePage(HttpServletResponse httpResponse, AlipayTradePayModel model, String notifyUrl, String returnUrl) throws AlipayApiException, IOException{
		AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		request.setReturnUrl(returnUrl);
		String form  = AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().pageExecute(request).getBody();//调用SDK生成表单
		httpResponse.setContentType("text/html;charset=" + AliPayApiConfigKit.getAliPayApiConfig().getCharset());
		httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
	    httpResponse.getWriter().flush();
	    httpResponse.getWriter().close();
	}
	/**
	 * 资金预授权冻结接口
	 * https://docs.open.alipay.com/318/106384/
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String authOrderFreeze(AlipayFundAuthOrderFreezeModel model) throws AlipayApiException {
		AlipayFundAuthOrderFreezeResponse response = authOrderFreezeToResponse(model);
		return response.getBody();
	}
	/**
	 * 资金预授权冻结接口
	 * @param model
	 * @return {AlipayFundAuthOrderFreezeResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundAuthOrderFreezeResponse authOrderFreezeToResponse(AlipayFundAuthOrderFreezeModel model) throws AlipayApiException {
		AlipayFundAuthOrderFreezeRequest request = new AlipayFundAuthOrderFreezeRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	
	/**
	 * 资金授权解冻接口
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String authOrderUnfreeze(AlipayFundAuthOrderUnfreezeModel model) throws AlipayApiException {
		AlipayFundAuthOrderUnfreezeResponse response = authOrderUnfreezeToResponse(model);
		return response.getBody();
	}
	
	/**
	 * 资金授权解冻接口
	 * @param model
	 * @return {AlipayFundAuthOrderUnfreezeResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundAuthOrderUnfreezeResponse authOrderUnfreezeToResponse(AlipayFundAuthOrderUnfreezeModel model) throws AlipayApiException {
		AlipayFundAuthOrderUnfreezeRequest request = new AlipayFundAuthOrderUnfreezeRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	
	/**
	 * 资金授权发码接口
	 * https://docs.open.alipay.com/318/106384/
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String authOrderVoucherCreate(AlipayFundAuthOrderVoucherCreateModel model) throws AlipayApiException {
		AlipayFundAuthOrderVoucherCreateResponse response = authOrderVoucherCreateToResponse(model);
		return response.getBody();
	}
	/**
	 * 资金预授权冻结接口
	 * @param model
	 * @return {AlipayFundAuthOrderVoucherCreateResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundAuthOrderVoucherCreateResponse authOrderVoucherCreateToResponse(AlipayFundAuthOrderVoucherCreateModel model) throws AlipayApiException {
		AlipayFundAuthOrderVoucherCreateRequest request = new AlipayFundAuthOrderVoucherCreateRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	
	/**
	 * 资金授权撤销接口
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String authOperationCancel(AlipayFundAuthOperationCancelModel model) throws AlipayApiException {
		AlipayFundAuthOperationCancelResponse response = authOperationCancelToResponse(model);
		return response.getBody();
	}
	
	/**
	 * 资金授权撤销接口
	 * @param model
	 * @return {AlipayFundAuthOperationCancelResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundAuthOperationCancelResponse authOperationCancelToResponse(AlipayFundAuthOperationCancelModel model) throws AlipayApiException {
		AlipayFundAuthOperationCancelRequest request = new AlipayFundAuthOperationCancelRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	
	
	/**
	 * 资金授权操作查询接口
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String authOperationDetailQuery(AlipayFundAuthOperationDetailQueryModel model) throws AlipayApiException {
		AlipayFundAuthOperationDetailQueryResponse response = authOperationDetailQueryToResponse(model);
		return response.getBody();
	}
	
	/**
	 * 资金授权操作查询接口
	 * @param model
	 * @return {AlipayFundAuthOperationDetailQueryResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundAuthOperationDetailQueryResponse authOperationDetailQueryToResponse(AlipayFundAuthOperationDetailQueryModel model) throws AlipayApiException {
		AlipayFundAuthOperationDetailQueryRequest request = new AlipayFundAuthOperationDetailQueryRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	/**
	 * 红包无线支付接口
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String fundCouponOrderAppPay(AlipayFundCouponOrderAppPayModel model) throws AlipayApiException {
		AlipayFundCouponOrderAppPayResponse response = fundCouponOrderAppPayToResponse(model);
		return response.getBody();
	}
	/**
	 * 红包无线支付接口
	 * @param model
	 * @return {AlipayFundCouponOrderAppPayResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundCouponOrderAppPayResponse fundCouponOrderAppPayToResponse(AlipayFundCouponOrderAppPayModel model) throws AlipayApiException {
		AlipayFundCouponOrderAppPayRequest request = new AlipayFundCouponOrderAppPayRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	
	/**
	 * 红包页面支付接口
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String fundCouponOrderPagePay(AlipayFundCouponOrderPagePayModel model) throws AlipayApiException {
		AlipayFundCouponOrderPagePayResponse response = fundCouponOrderPagePayToResponse(model);
		return response.getBody();
	}
	/**
	 * 红包页面支付接口
	 * @param model
	 * @return {AlipayFundCouponOrderPagePayResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundCouponOrderPagePayResponse fundCouponOrderPagePayToResponse(AlipayFundCouponOrderPagePayModel model) throws AlipayApiException {
		AlipayFundCouponOrderPagePayRequest request = new AlipayFundCouponOrderPagePayRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	/**
	 * 红包协议支付接口
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String fundCouponOrderAgreementPay(AlipayFundCouponOrderAgreementPayModel model) throws AlipayApiException {
		AlipayFundCouponOrderAgreementPayResponse response = fundCouponOrderAgreementPayToResponse(model);
		return response.getBody();
	}
	/**
	 * 红包协议支付接口
	 * @param model
	 * @return {AlipayFundCouponOrderAgreementPayResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundCouponOrderAgreementPayResponse fundCouponOrderAgreementPayToResponse(AlipayFundCouponOrderAgreementPayModel model) throws AlipayApiException {
		AlipayFundCouponOrderAgreementPayRequest request = new AlipayFundCouponOrderAgreementPayRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	/**
	 * 红包打款接口
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String fundCouponOrderDisburse(AlipayFundCouponOrderDisburseModel model) throws AlipayApiException {
		AlipayFundCouponOrderDisburseResponse response = fundCouponOrderDisburseToResponse(model);
		return response.getBody();
	}
	/**
	 * 红包打款接口
	 * @param model
	 * @return {AlipayFundCouponOrderDisburseResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundCouponOrderDisburseResponse fundCouponOrderDisburseToResponse(AlipayFundCouponOrderDisburseModel model) throws AlipayApiException {
		AlipayFundCouponOrderDisburseRequest request = new AlipayFundCouponOrderDisburseRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	/**
	 * 红包退回接口
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String fundCouponOrderRefund(AlipayFundCouponOrderRefundModel model) throws AlipayApiException {
		AlipayFundCouponOrderRefundResponse response = fundCouponOrderRefundToResponse(model);
		return response.getBody();
	}
	/**
	 * 红包退回接口
	 * @param model
	 * @return {AlipayFundCouponOrderRefundResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundCouponOrderRefundResponse fundCouponOrderRefundToResponse(AlipayFundCouponOrderRefundModel model) throws AlipayApiException {
		AlipayFundCouponOrderRefundRequest request = new AlipayFundCouponOrderRefundRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	/**
	 * 红包退回接口
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String fundCouponOperationQuery(AlipayFundCouponOperationQueryModel model) throws AlipayApiException {
		AlipayFundCouponOperationQueryResponse response = fundCouponOperationQueryToResponse(model);
		return response.getBody();
	}
	/**
	 * 红包退回接口
	 * @param model
	 * @return {AlipayFundCouponOperationQueryResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundCouponOperationQueryResponse fundCouponOperationQueryToResponse(AlipayFundCouponOperationQueryModel model) throws AlipayApiException {
		AlipayFundCouponOperationQueryRequest request = new AlipayFundCouponOperationQueryRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	
	/**
	 * 将异步通知的参数转化为Map
	 * @param request
	 * @return {Map<String, String>}
	 */
	public static Map<String, String> toMap(HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		return params;
	}
	
}
