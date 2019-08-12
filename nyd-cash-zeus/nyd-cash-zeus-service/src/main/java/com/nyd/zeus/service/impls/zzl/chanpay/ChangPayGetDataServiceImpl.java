package com.nyd.zeus.service.impls.zzl.chanpay;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nyd.zeus.api.zzl.chanpay.ChangPayGetDataService;
import com.nyd.zeus.model.helibao.util.chanpay.BaseConstant;
import com.nyd.zeus.model.helibao.util.chanpay.ChangPayUtil;
import com.nyd.zeus.model.helibao.util.chanpay.DateUtils;
import com.nyd.zeus.model.helibao.util.chanpay.RSA;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJieCancelCardVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJieCardBinVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJieMerchantVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJiePayVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJiePrePayVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJieQueryBindVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangJieQueryMerchantVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangPayBindCardVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangPaySendMsgVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.ChangjieQueryPayVO;
import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.PayConfigFileVO;




@Service
public class ChangPayGetDataServiceImpl implements ChangPayGetDataService {

	
	@Autowired
	private PayConfigFileService PayConfigFileService;
	


	
	/**
	 * 编码类型
	 */
	private static String charset = "UTF-8";
	
	@Override
	public Map<String, String> getSendMsgData(ChangPaySendMsgVO changPaySendMsgVO,PayConfigFileVO payConfigFileVO) throws Exception {

		String cardNum = changPaySendMsgVO.getCardNumber();
		String name = changPaySendMsgVO.getCustName();
		String custIc = changPaySendMsgVO.getCustIC();
		String mobile = changPaySendMsgVO.getCustMobile();
		String pubKey = payConfigFileVO.getPubKey();
		Map<String, String> origMap = new HashMap<String, String>();

		origMap.put("Version", payConfigFileVO.getVersion());  //1.0
		origMap.put("PartnerId", payConfigFileVO.getMemberId());//200000400059 生产测试参数 200001160097
		
		origMap.put("InputCharset", charset);// 字符集
		origMap.put("TradeDate", DateUtils.getyyyyMMdd());// 商户请求时间
		origMap.put("TradeTime", "141200");// 商户请求时间
		origMap.put("Memo", null);
		
		
		// 2.1 鉴权绑卡 api 业务参数
		origMap.put("Service", "nmg_biz_api_auth_req");// 鉴权绑卡的接口名(商户采集方式)；银行采集方式更换接口名称为：nmg_canal_api_auth_req
		
		String mchntSsn = changPaySendMsgVO.getMchssn(); // 流水号
		origMap.put("TrxId", changPaySendMsgVO.getSerialNo());// 订单号
		origMap.put("ExpiredTime", "90m");// 订单有效期
		origMap.put("MerUserId", mchntSsn);// 用户标识
		origMap.put("BkAcctTp", "01");// 卡类型（00 – 银行贷记卡;01 – 银行借记卡;）
		origMap.put("BkAcctNo", this.encrypt(cardNum, pubKey, charset));// 卡号
		origMap.put("IDTp", "01");// 证件类型 （目前只支持身份证 01：身份证）
		origMap.put("IDNo", this.encrypt(custIc, pubKey, charset));// 证件号
		origMap.put("CstmrNm", this.encrypt(name, pubKey, charset));// 持卡人姓名
		origMap.put("MobNo", this.encrypt(mobile, pubKey, charset));// 银行预留手机号
		//信用卡
//		origMap.put("CardCvn2", "004");// cvv2码
//		origMap.put("CardExprDt", "09/21");// 有效期
		
		String redisKey = "changPay" +name + "|"+ cardNum+ "|" + mobile +"|" + custIc;

//		redisService.del(redisKey);
//		redisService.set(redisKey, mchntSsn, 5*60);
		
		origMap.put("NotifyUrl", "http://dev.chanpay.com/receive.php");// 异步通知url
		origMap.put("SmsFlag", "1");
		origMap.put("Extension", "");
		return origMap;
	}

	@Override
	public Map<String, String> getBindCardData(ChangPayBindCardVO changPayBindCardVO,PayConfigFileVO payConfigFileVO) throws Exception {

		String mchntSsn = changPayBindCardVO.getMchntSsn();

		Map<String, String> origMap = new HashMap<String, String>();
		//
		origMap.put("Version", payConfigFileVO.getVersion());
		origMap.put("PartnerId", payConfigFileVO.getMemberId());
		
		origMap.put("InputCharset", charset);// 字符集
		origMap.put("TradeDate", DateUtils.getyyyyMMdd());// 商户请求时间
		origMap.put("TradeTime", DateUtils.gethhhhmmss());// 商户请求时间
		origMap.put("Memo", null);
		origMap.put("Service", "nmg_api_auth_sms");// 鉴权绑卡确认的接口名
		// 2.1 鉴权绑卡  业务参数
		String trxId = getSerialNum(); // 流水号
		origMap.put("TrxId", trxId);// 订单号
		origMap.put("OriAuthTrxId", mchntSsn);// 原鉴权绑卡订单号
		origMap.put("SmsCode", changPayBindCardVO.getMsgCode());// 鉴权短信验证码
		origMap.put("NotifyUrl", "http://dev.chanpay.com/receive.php");// 异步通知地址
		return origMap;
	}

	@Override
	public Map<String, String> getCancelBindData(ChangJieCancelCardVO cancelCardVO,PayConfigFileVO payConfigFileVO) throws Exception {

		String cardNum = cancelCardVO.getCardNumber();
		//卡号前6位
		String fistCard = cardNum.substring(0, 6);
		//卡号后4位
		String lastCard = cardNum.substring(cardNum.length()-4,cardNum.length());
		Map<String, String> origMap = new HashMap<String, String>();
		// 2.1 基本参数
		origMap.put("Version", payConfigFileVO.getVersion());
		origMap.put("PartnerId", payConfigFileVO.getMemberId());//200000400059 生产测试参数
		
		origMap.put("InputCharset", charset);// 字符集
		origMap.put("TradeDate", DateUtils.getyyyyMMdd());// 商户请求时间
		origMap.put("TradeTime", DateUtils.gethhhhmmss());// 商户请求时间
		origMap.put("Memo", null);
		origMap.put("Service", "nmg_api_auth_unbind");// 用户鉴权解绑接口名
		// 2.2 业务参数
		String trxId = Long.toString(System.currentTimeMillis());		
		origMap.put("TrxId", trxId);// 商户网站唯一订单号
		origMap.put("MerchantNo", payConfigFileVO.getMemberId());
		origMap.put("MerUserId", cancelCardVO.getProtocolno()); // 用户标识
		origMap.put("UnbindType", "1"); // 解绑模式。0为物理解绑，1为逻辑解绑
//		origMap.put("CardId", "");// 卡号标识
		origMap.put("CardBegin", fistCard);// 卡号前6位
		origMap.put("CardEnd", lastCard);// 卡号后4位
		origMap.put("Extension", "");// 扩展字段	
		return origMap;
	}



	@Override
	public Map<String, String> getQueryBindData(ChangJieQueryBindVO queryBindVO,PayConfigFileVO payConfigFileVO) throws Exception {

		Map<String, String> origMap = new HashMap<String, String>();

		origMap.put("Version", payConfigFileVO.getVersion());
		origMap.put("PartnerId", payConfigFileVO.getMemberId());
		origMap.put("InputCharset", charset);// 字符集
		origMap.put("TradeDate", DateUtils.getyyyyMMdd());// 商户请求时间
		origMap.put("TradeTime", "141200");// 商户请求时间
		origMap.put("Memo", null);
		origMap.put("Service", "nmg_api_auth_info_qry");// 用户鉴权绑卡信息查询接口名
		// 2.2 业务参数
		origMap.put("TrxId", String.valueOf(System.currentTimeMillis()));// 商户网站唯一订单号
		origMap.put("MerUserId", queryBindVO.getMerUserId()); // 用户标识
		//origMap.put("CardBegin", "430000");// 卡号前6位
		//origMap.put("CardEnd", "4700");// 卡号后4位
		origMap.put("BkAcctTp", "01");// 卡类型（00 – 银行贷记卡;01 – 银行借记卡）
		return 	origMap;
	}
	

	@Override
	public Map<String, String> getPayAnother(ChangJieMerchantVO changJieMerchantVO,PayConfigFileVO payConfigFileVO) throws Exception {
		
		String pubKey = payConfigFileVO.getPubKey();
		Map<String, String> origMap = new HashMap<String, String>();
		String amt = changJieMerchantVO.getAmt();
		// 2.1 基本参数
		origMap.put(BaseConstant.SERVICE, "cjt_dsf");// 
		origMap.put(BaseConstant.VERSION, payConfigFileVO.getVersion());
		origMap.put(BaseConstant.PARTNER_ID, payConfigFileVO.getMemberId()); //生产环境测试商户号
		origMap.put(BaseConstant.TRADE_DATE, BaseConstant.DATE);
		origMap.put(BaseConstant.TRADE_TIME, BaseConstant.TIME);
		origMap.put(BaseConstant.INPUT_CHARSET, BaseConstant.CHARSET);// 字符集
		origMap.put(BaseConstant.MEMO, "");// 备注
		Map<String, String> map = origMap;
		map.put("OutTradeNo", changJieMerchantVO.getSerialNum()); // 商户网站唯一订单号
		map.put("TransCode", "T10000"); // 交易码
		//map.put("OutTradeNo", ChangPayUtil.generateOutTradeNo()); // 商户网站唯一订单号
		//map.put("CorpAcctNo", "62170000000000000"); // Y环境企业账号
		//map.put("CorpAcctNo", "62233333333333"); // 企业账号 （T环境）  
		//map.put("CorpAcctNo", "1223332343");  // 文档写的可空
		map.put("BusinessType", "0"); // 业务类型
		map.put("BankCommonName", changJieMerchantVO.getBankName()); // 通用银行名称
		map.put("BankCode", "");//对公必填
		map.put("AccountType", "00"); // 账户类型
		map.put("AcctNo", this.encrypt(changJieMerchantVO.getAccntno(),pubKey, BaseConstant.CHARSET)); // 对手人账号(此处需要用真实的账号信息)
		map.put("AcctName", this.encrypt(changJieMerchantVO.getAccntnm(),pubKey, BaseConstant.CHARSET)); // 对手人账户名称
		map.put("TransAmt", amt);   //金额 元
		
		//************** 以下信息可空  *******************
//		map.put("Province", "甘肃省"); // 省份信息
//		map.put("City", "兰州市"); // 城市信息
//		map.put("BranchBankName", "中国建设银行股份有限公司兰州新港城支行"); // 对手行行名
//		map.put("BranchBankCode", "105821005604");
//		map.put("DrctBankCode", "105821005604");
//		map.put("Currency", "CNY");
//		map.put("LiceneceType", "01");
//		map.put("LiceneceNo", ChanPayUtil.encrypt("622225199209190017", BaseConstant.MERCHANT_PUBLIC_KEY, BaseConstant.CHARSET));
//		map.put("Phone", ChanPayUtil.encrypt("17001090000", BaseConstant.MERCHANT_PUBLIC_KEY, BaseConstant.CHARSET));
//		map.put("AcctExp", "exp");
//		map.put("AcctCvv2", ChanPayUtil.encrypt("cvv", BaseConstant.MERCHANT_PUBLIC_KEY, BaseConstant.CHARSET));
//		map.put("CorpCheckNo", "201703061413");
//		map.put("Summary", "");
		
		map.put("CorpPushUrl", changJieMerchantVO.getCallBackUrl());		
		map.put("PostScript", "用途");
		return map;
	
	}

	@Override
	public Map<String, String> getQueryPayAnother(ChangJieQueryMerchantVO changJieQueryMerchantVO,PayConfigFileVO payConfigFileVO) throws Exception {
		Map<String, String> origMap = new HashMap<String, String>();
		// 2.1 基本参数
		origMap.put(BaseConstant.SERVICE, "cjt_dsf");// 
		origMap.put(BaseConstant.VERSION, payConfigFileVO.getVersion());
		origMap.put(BaseConstant.PARTNER_ID, payConfigFileVO.getMemberId()); //生产环境测试商户号
		origMap.put(BaseConstant.TRADE_DATE, BaseConstant.DATE);
		origMap.put(BaseConstant.TRADE_TIME, BaseConstant.TIME);
		origMap.put(BaseConstant.INPUT_CHARSET, BaseConstant.CHARSET);// 字符集
		origMap.put(BaseConstant.MEMO, "");// 备注
		origMap.put("TransCode", "C00000");
		origMap.put("OutTradeNo", ChangPayUtil.generateOutTradeNo());
		origMap.put("OriOutTradeNo", changJieQueryMerchantVO.getOrderno());
		return origMap;
	}
	
	/**
	 * 公共请求参数设置
	 */
	public Map<String, String> setCommonMap(Map<String, String> origMap) {
		// 2.1 基本参数
		origMap.put("Version", "1.0");
		origMap.put("PartnerId", "200001160097");//200000400059 生产测试参数
		
		origMap.put("InputCharset", charset);// 字符集
		origMap.put("TradeDate", "20170612");// 商户请求时间
		origMap.put("TradeTime", "141200");// 商户请求时间
		origMap.put("Memo", null);
		return origMap;
	}
	
	/**
	 * 加密，部分接口，有参数需要加密
	 * 
	 * @param src
	 *            原值
	 * @param publicKey
	 *            畅捷支付发送的平台公钥
	 * @param charset
	 *            UTF-8
	 * @return RSA加密后的密文
	 */
	private String encrypt(String src, String publicKey, String charset) {
		try {
			byte[] bytes = RSA.encryptByPublicKey(src.getBytes(charset), publicKey);
			return Base64.encodeBase64String(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Map<String, String> getPrePayData(ChangJiePrePayVO prePayVO, PayConfigFileVO payConfigFileVO)
			throws Exception {
		String cardNum = prePayVO.getCardNumber();
		String amt = prePayVO.getAmt();	
		//卡号前四位
		String fistCard = cardNum.substring(0, 6);
		//卡号后6位
		String lastCard = cardNum.substring(cardNum.length()-4,cardNum.length());
		Map<String, String> origMap = new HashMap<String, String>();
		// 2.1 基本参数
		origMap.put("Version", payConfigFileVO.getVersion());
		origMap.put("PartnerId", payConfigFileVO.getMemberId());
		origMap.put("InputCharset", charset);// 字符集
		origMap.put("TradeDate", DateUtils.getyyyyMMdd());// 商户请求时间
		origMap.put("TradeTime", "141200");// 商户请求时间
		origMap.put("Memo", null);
		origMap.put("Service", "nmg_biz_api_quick_payment");// 支付的接口名
		// 2.2 业务参数		
		String mchntorderid = "DK" + getSerialNum();
		origMap.put("TrxId", mchntorderid);// 订单号
		//origMap.put("TrxId", "201703131045234230112");// 订单号
		origMap.put("OrdrName", "畅捷支付");// 商品名称
		origMap.put("MerUserId", prePayVO.getProtocolno());// 用户标识
		origMap.put("SellerId", payConfigFileVO.getMemberId());// 子账户号
		origMap.put("SubMerchantNo", "");// 子商户号
		origMap.put("ExpiredTime", "40m");// 订单有效期
		origMap.put("CardBegin", fistCard);// 卡号前6位
		origMap.put("CardEnd", lastCard);// 卡号后4位
		origMap.put("TrxAmt", amt);// 交易金额 元
		origMap.put("TradeType", "11");// 交易类型
		origMap.put("SmsFlag", "0");
		origMap.put("NotifyUrl", prePayVO.getCallBackUrl());
		return origMap;
	}
	
	
	
	



	
	@Override
	public Map<String, String> getPayData(ChangJiePayVO changJiePayVO, PayConfigFileVO payConfigFileVO)
			throws Exception {
		Map<String, String> origMap = new HashMap<String, String>();
		// 2.1 基本参数
		origMap.put("Version", payConfigFileVO.getVersion());
		origMap.put("PartnerId", payConfigFileVO.getMemberId());
		
		origMap.put("InputCharset", charset);// 字符集
		origMap.put("TradeDate", DateUtils.getyyyyMMdd());// 商户请求时间
		origMap.put("TradeTime", "141200");// 商户请求时间
		origMap.put("Memo", null);
		origMap.put("Service", "nmg_api_quick_payment_smsconfirm");// 请求的接口名称
		// 2.2 业务参数
		String trxId = Long.toString(System.currentTimeMillis());		
		origMap.put("TrxId", trxId);// 订单号

		origMap.put("OriPayTrxId", changJiePayVO.getOriPayTrxId());// 原有支付请求订单号
		origMap.put("SmsCode", changJiePayVO.getSmsCode());// 短信验证码
		return origMap;
	}

	@Override
	public Map<String, String> getQueryPayData(ChangjieQueryPayVO changjieQueryPayVO,
			PayConfigFileVO payConfigFileVO) throws Exception {
		Map<String, String> origMap = new HashMap<String, String>();
		// 2.1 基本参数
		origMap.put("Version", payConfigFileVO.getVersion());
		origMap.put("PartnerId", payConfigFileVO.getMemberId());//200000400059 生产测试参数
		
		origMap.put("InputCharset", charset);// 字符集
		origMap.put("TradeDate", DateUtils.getyyyyMMdd());// 商户请求时间
		origMap.put("TradeTime", DateUtils.gethhhhmmss());// 商户请求时间
		origMap.put("Memo", null);

		origMap.put("Service", "nmg_api_query_trade");// 请求的接口名
		// 2.2 业务参数
		
		origMap.put("TrxId", Long.toString(System.currentTimeMillis()));// 订单号
		origMap.put("OrderTrxId", changjieQueryPayVO.getOrderTrxId());// 原业务请求订单号
		origMap.put("TradeType", "pay_order");// 原业务订单类型
		return origMap;
	}

	@Override
	public Map<String, String> getCardBinData(ChangJieCardBinVO changJieCardBinVO, PayConfigFileVO payConfigFileVO)
			throws Exception {
		
		Map<String, String> origMap = new HashMap<String, String>();
		// 2.1 基本参数
		origMap.put(BaseConstant.SERVICE, "cjt_dsf");// 鉴权绑卡确认的接口名
		origMap.put(BaseConstant.VERSION, payConfigFileVO.getVersion());
		origMap.put(BaseConstant.PARTNER_ID, payConfigFileVO.getMemberId()); //生产环境测试商户号
		origMap.put(BaseConstant.TRADE_DATE, BaseConstant.DATE);
		origMap.put(BaseConstant.TRADE_TIME, BaseConstant.TIME);
		origMap.put(BaseConstant.INPUT_CHARSET, BaseConstant.CHARSET);// 字符集
		origMap.put(BaseConstant.MEMO, "");// 备注
		Map<String, String> map = origMap;
		String outTradeNo = "CB" + System.currentTimeMillis();
		map.put("TransCode", "C00016");
		map.put("OutTradeNo", outTradeNo);
		map.put("AcctNo", this.encrypt(changJieCardBinVO.getCardNum(),
				payConfigFileVO.getPubKey(), BaseConstant.CHARSET));
		return map;
	}
	
	public  String getSerialNum(){
		return new SimpleDateFormat("YYYYMMDDHHmmssSSS").format(new Date()) + String.valueOf((Math.random()*1000000)).substring(0,5);
	}



	
}
