package com.nyd.user.model.enums;


/**
 * 列表展示协议用（勿增加字段）
 * @author hwt
 *
 */
public enum ContratNameEnum {
	
	
    Electronic("electronic","《电子签名授权协议书》"),
    Credit("credit","《征信查询授权书》"),
    Server("server","《借款居间服务协议》"),
    MemberServer("membServer","《评估及推荐服务协议》"),
    AutorRpay("autoRepay","《自动还款服务协议》"),
    Mortgage("mortgage","《抵押合同》"),
    LoanUsage("loanUage","《借款用途承诺书》"),
    Recharge("recharge","《充值协议》"),
    Login("login","《平台用户注册服务协议》"),
    Prip("prip","《隐私政策》"),
    DaiKou("daik","《代扣协议》"),
    ZhunRu("zhunru","《准入风险批核及借款推荐服务协议》"),
    GeRen("geren","《个人信息查询及使用授权书》"),
    CreditApproval("creditApproval","《信用批核服务协议》");
    

	
	private String code;

	private String desc;

	ContratNameEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
	
	public static ContratNameEnum fromCode(String code) {
		for (ContratNameEnum item : ContratNameEnum.values()) {
			if (item.getCode().equals(code)) {
				return item;
			}
		}
		return null;
	}
	
	public static String fromDesc(String code) {
		for (ContratNameEnum item : ContratNameEnum.values()) {
			if (item.getCode().equals(code)) {
				return item.getDesc();
			}
		}
		return "";
	}
	

}
