/**
 * Copyright (c) 2015-2017, Javen Zhou  (javen205@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.creativearts.nyd.pay.service.unionpay;

import com.creativearts.nyd.pay.config.utils.DateKit;
import com.creativearts.nyd.pay.config.utils.StrKit;
import com.creativearts.nyd.pay.service.unionpay.sdk.AcpService;
import com.creativearts.nyd.pay.service.unionpay.sdk.SDKConfig;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 *
 */
public class UnionPayApiConfig {
	private UnionPayApiConfig() {
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String version;
		private String encoding;
		private String signMethod;
		/**
		 * 取值：
		 00：查询交易，01：消费，02：预授权，03：预授权完成，04：退货，05：圈存，11：代收，12：代付，13：账单支付，14：转账（保留），21：批量交易，22：批量查询，31：消费撤销，32：预授权撤销，33：预授权完成撤销，71：余额查询，72：实名认证-建立绑定关系，73：账单查询，74：解除绑定关系，75：查询绑定关系，77：发送短信验证码交易，78：开通查询交易，79：开通交易，94：IC卡脚本通知 95：查询更新加密公钥证书
		 */
		private String txnType;
		/**
		 * 依据实际交易类型填写。默认取值：00
		 */
		private String txnSubType;
		/**
		 * 依据实际业务场景填写(目前仅使用后 4 位，签名 2 位 默认为 00) 默认取值：000000 具体取值范围： 000201：B2C 网关支付 000301：认证支付 2.0 000302：评级支付 000401：代付 000501：代收 000601：账单支付 000801：跨行收单 000901：绑定支付 001001：订购 000202：B2B
		 */
		private String bizType;
		/**
		 * 05：语音07：互联网08：移动 16：数字机顶盒
		 */
		private String channelType;
		private String accessType;
		private String merId;
		private String frontUrl;
		private String backUrl;
		private String orderId;
		private String currencyCode;
		private String txnAmt;
		private String txnTime;
		private String payTimeout;
		private String accNo;
		private String reqReserved;
		private String orderDesc;
		private String acqInsCode;
		private String merCatCode;
		private String merName;
		private String merAbbr;
		private String origQryId;
		private String settleDate;
		private String fileType;
		private String bussCode;
		private String billQueryInfo;
		private String qrNo;
		private String termId;
		//默认取值：01具体取值范围：01：银行卡02：存折03：IC卡04：对公账户取值“03”表示以IC终端发起的IC卡交易，当IC作为普通银行卡进行支付时，此域填写为“01”　取值“04”时，参看注 5.3 保留域（reserved）相关子域说明
		private String accType;
		private String encryptCertId;
		private String customerInfo;

		public Map<String, String> createMap() {
			Map<String, String> map = new HashMap<String, String>();
			if (StrKit.isBlank(version)) {
				version = "5.1.0";
			}
			if (StrKit.isBlank(encoding)) {
				encoding = "UTF-8";
			}
			if (StrKit.isBlank(signMethod)) {
				signMethod = "01";
			}
			if (StrKit.isBlank(txnType)) {
				txnType = "01";
			}
			if (StrKit.isBlank(txnSubType)) {
				txnSubType = "01";
			}
			if (StrKit.isBlank(bizType)) {
				bizType = "000201";
			}
			if (StrKit.isBlank(channelType)) {
				channelType = "07";
			}
			if (StrKit.isBlank(accessType)) {
				accessType = "0";
			}
			if (StrKit.isBlank(merId)) {
				throw new IllegalArgumentException("merId 值不能为 null");
			}
			if (StrKit.isBlank(backUrl)) {
				backUrl = SDKConfig.getConfig().getBackUrl();
			}
			if (StrKit.isBlank(frontUrl)) {
				frontUrl = SDKConfig.getConfig().getFrontUrl();
			}

			if (StrKit.isBlank(orderId)) {
				orderId = String.valueOf(System.currentTimeMillis());
			}
			if (orderId.contains("_") || orderId.contains("-")) {
				throw new IllegalArgumentException("orderId 值不应含“-”或“_”");
			}
			if (StrKit.isBlank(currencyCode)) {
				currencyCode = "156";
			}
			if (StrKit.isBlank(txnAmt)) {
				txnAmt = "1";
			}
			if (StrKit.isBlank(txnTime)) {
				txnTime = DateKit.toStr(new Date(), DateKit.UnionTimeStampPattern);
			}
			if (StrKit.isBlank(payTimeout)) {
				payTimeout = DateKit.toStr(new Date(), 15 * 60 * 1000, DateKit.UnionTimeStampPattern);
			}
			

			map.put("version", version);
			map.put("encoding", encoding);
			map.put("signMethod", signMethod);
			map.put("txnType", txnType);
			map.put("txnSubType", txnSubType);
			map.put("bizType", bizType);
			map.put("channelType", channelType);
			map.put("accessType", accessType);
			map.put("merId", merId);
			map.put("frontUrl", frontUrl);
			map.put("backUrl", backUrl);
			map.put("orderId", orderId);
			map.put("currencyCode", currencyCode);
			map.put("txnAmt", txnAmt);
			map.put("txnTime", txnTime);
			map.put("payTimeout", payTimeout);
			map.put("accNo", accNo);
			map.put("reqReserved", reqReserved);
			map.put("orderDesc", orderDesc);
			map.put("acqInsCode", acqInsCode);
			map.put("merCatCode", merCatCode);
			map.put("merName", merName);
			map.put("merAbbr", merAbbr);
			map.put("origQryId", origQryId);
			map.put("settleDate", settleDate);
			map.put("fileType", fileType);
			map.put("bussCode", bussCode);
			map.put("billQueryInfo", billQueryInfo);
			map.put("qrNo", qrNo);
			map.put("termId", termId);
			map.put("accType", accType);
			map.put("encryptCertId", encryptCertId);
			map.put("customerInfo", customerInfo);
//			System.out.println("*********************2");
//			System.out.println(JSON.toJSONString(map));
			return setSignMap(map);
		}
		
		public Map<String, String> setSignMap(Map<String, String> map) {
			// 报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
			return AcpService.sign(map, encoding);
		}
		

		public Builder setVersion(String version) {
			this.version = version;
			return this;
		}

		public Builder setEncoding(String encoding) {
			this.encoding = encoding;
			return this;
		}

		public Builder setSignMethod(String signMethod) {
			this.signMethod = signMethod;
			return this;
		}

		public Builder setTxnType(String txnType) {
			this.txnType = txnType;
			return this;
		}

		public Builder setTxnSubType(String txnSubType) {
			this.txnSubType = txnSubType;
			return this;
		}

		public Builder setBizType(String bizType) {
			this.bizType = bizType;
			return this;
		}

		public Builder setChannelType(String channelType) {
			this.channelType = channelType;
			return this;
		}

		public Builder setAccessType(String accessType) {
			this.accessType = accessType;
			return this;
		}

		public Builder setMerId(String merId) {
			this.merId = merId;
			return this;
		}

		public Builder setFrontUrl(String frontUrl) {
			this.frontUrl = frontUrl;
			return this;
		}

		public Builder setBackUrl(String backUrl) {
			this.backUrl = backUrl;
			return this;
		}

		public Builder setOrderId(String orderId) {
			this.orderId = orderId;
			return this;
		}

		public Builder setCurrencyCode(String currencyCode) {
			this.currencyCode = currencyCode;
			return this;
		}

		public Builder setTxnAmt(String txnAmt) {
			this.txnAmt = txnAmt;
			return this;
		}

		public Builder setTxnTime(String txnTime) {
			this.txnTime = txnTime;
			return this;
		}

		public Builder setPayTimeout(String payTimeout) {
			this.payTimeout = payTimeout;
			return this;
		}

		public Builder setAccNo(String accNo) {
			this.accNo = accNo;
			return this;
		}

		public Builder setReqReserved(String reqReserved) {
			this.reqReserved = reqReserved;
			return this;
		}

		public Builder setOrderDesc(String orderDesc) {
			this.orderDesc = orderDesc;
			return this;
		}

		public Builder setAcqInsCode(String acqInsCode) {
			this.acqInsCode = acqInsCode;
			return this;
		}

		public Builder setMerCatCode(String merCatCode) {
			this.merCatCode = merCatCode;
			return this;
		}

		public Builder setMerName(String merName) {
			this.merName = merName;
			return this;
		}

		public Builder setMerAbbr(String merAbbr) {
			this.merAbbr = merAbbr;
			return this;
		}

		public Builder setOrigQryId(String origQryId) {
			this.origQryId = origQryId;
			return this;
		}

		public Builder setSettleDate(String settleDate) {
			this.settleDate = settleDate;
			return this;
		}

		public Builder setFileType(String fileType) {
			this.fileType = fileType;
			return this;
		}

		public Builder setBussCode(String bussCode) {
			this.bussCode = bussCode;
			return this;
		}

		public Builder setBillQueryInfo(String billQueryInfo) {
			this.billQueryInfo = billQueryInfo;
			return this;
		}

		public Builder setQrNo(String qrNo) {
			this.qrNo = qrNo;
			return this;
		}

		public Builder setTermId(String termId) {
			this.termId = termId;
			return this;
		}

		public Builder setAccType(String accType) {
			this.accType = accType;
			return this;
		}

		public Builder setEncryptCertId(String encryptCertId) {
			this.encryptCertId = encryptCertId;
			return this;
		}

		public Builder setCustomerInfo(String customerInfo) {
			this.customerInfo = customerInfo;
			return this;
		}
		
		
	}

}
