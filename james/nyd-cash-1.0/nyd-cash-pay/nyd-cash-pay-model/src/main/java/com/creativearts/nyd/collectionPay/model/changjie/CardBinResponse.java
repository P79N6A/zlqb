/**
 * Project Name:nyd-cash-pay-model
 * File Name:CardBinResponse.java
 * Package Name:com.creativearts.nyd.collectionPay.model.changjie
 * Date:2018年9月11日上午10:09:45
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.collectionPay.model.changjie;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * ClassName:CardBinResponse <br/>
 * Date:     2018年9月11日 上午10:09:45 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class CardBinResponse extends ChangJieCollectionPayResponse implements Serializable {
	private static final long serialVersionUID = 3258175696841962845L;
	
	/**原交易返回代码*/
	@JsonProperty("OriginalRetCode")
	private String originalRetCode;
	
//	/**商户号*/
//	@JsonProperty("CorpNo")
//	private String corpNo;
	
	/**商户名称*/
	@JsonProperty("CorpName")
	private String corpName;
	
	/**卡号是否有效*/
	@JsonProperty("IsValid")
	private String isValid;
	
	/**卡BIN*/
	@JsonProperty("CardBin")
	private String cardBin;
	
	/**卡名称*/
	@JsonProperty("CardName")
	private String cardName;
	
	/**卡类型*/
	@JsonProperty("CardType")
	private String cardType;
	
	/**开户行行号*/
	@JsonProperty("BranchBankCode")
	private String branchBankCode;
	
	/**开户行行名*/
	@JsonProperty("BranchBankName")
	private String branchBankName;
	
	/**通用银行名称*/
	@JsonProperty("BankCommonName")
	private String bankCommonName;
	
	/**开户行账号*/
	@JsonProperty("AcctNo")
	private String acctNo;
	
	/**原交易错误信息描述*/
	@JsonProperty("OriginalErrorMessage")
	private String originalErrorMessage;
	
	/**省*/
	@JsonProperty("Province")
	private String province;
	
	/**市*/
	@JsonProperty("City")
	private String city;
	
//	@JsonProperty("MarginChangesList")
//	private String marginChangesList;
//	
//	@JsonProperty("TradeList")
//	private String tradeList;
}

