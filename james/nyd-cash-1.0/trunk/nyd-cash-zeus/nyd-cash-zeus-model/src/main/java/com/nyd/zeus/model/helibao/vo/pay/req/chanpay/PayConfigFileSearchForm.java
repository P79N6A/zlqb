package com.nyd.zeus.model.helibao.vo.pay.req.chanpay;

import com.nyd.zeus.model.common.BaseSearchForm;

public class PayConfigFileSearchForm extends BaseSearchForm {
	
		private String id;			//业务主键	private String code;			//富友商户对应code	private String memberId;			//商户号	private String version;			//版本号	private String pubKey;			//公钥	private String prdKey;			//私钥	private String payUrl;			//富友请求url	private String payKey;			//密钥	private String channel;			//	public String getId() {	    return this.id;	}	public void setId(String id) {	    this.id=id;	}	public String getCode() {	    return this.code;	}	public void setCode(String code) {	    this.code=code;	}	public String getMemberId() {	    return this.memberId;	}	public void setMemberId(String memberId) {	    this.memberId=memberId;	}	public String getVersion() {	    return this.version;	}	public void setVersion(String version) {	    this.version=version;	}	public String getPubKey() {	    return this.pubKey;	}	public void setPubKey(String pubKey) {	    this.pubKey=pubKey;	}	public String getPrdKey() {	    return this.prdKey;	}	public void setPrdKey(String prdKey) {	    this.prdKey=prdKey;	}	public String getPayUrl() {
		return payUrl;
	}
	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}
	public String getPayKey() {
		return payKey;
	}
	public void setPayKey(String payKey) {
		this.payKey = payKey;
	}
	public String getChannel() {	    return this.channel;	}	public void setChannel(String channel) {	    this.channel=channel;	}
	
}
