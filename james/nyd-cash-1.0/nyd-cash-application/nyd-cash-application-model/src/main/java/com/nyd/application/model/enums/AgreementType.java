package com.nyd.application.model.enums;

/**
 * Created by hwei on 2017/11/25.
 */
public enum AgreementType {
    /**
     * type 名不要起太长，后面需要用来组合生成协议编号，编号长度有限制小于32位
     */
    Electronic("electronic","电子签名授权协议书"),
    Credit("credit","征信查询授权书"),
    Server("server","借款居间服务协议"),
    MemberServer("membServer","评估及推荐服务协议"),
    AutorRpay("autoRepay","自动还款服务协议"),
    Mortgage("mortgage","抵押合同"),
    LoanUsage("loanUage","借款用途承诺书"),
    Recharge("recharge","充值协议"),
    Login("login","平台用户注册服务协议"),
    Prip("prip","隐私政策"),
    DaiKou("daik","代扣协议"),
    ZhunRu("zhunru","准入风险批核服务协议"),
    GeRen("geren","个人信息查询及使用授权书"),
	
	//信用批核服务协议需要根据-->银行渠道    changjie 常捷  xunlian 讯联or新渠道 3期
    CreditApproval("creditApproval","信用批核服务协议");
	
    private String type;
    private String description;

    AgreementType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
    
    /**
	 * 自己定义一个静态方法,通过code返回枚举常量对象
	 * @param code
	 * @return
	 * @author lk
	 */
	public static AgreementType getDescriptionName(String type){
		
		for (AgreementType agreementType : values()) {
			if(agreementType.getType().equals(type)){
				return agreementType;
			}
		}
		return null;
	}

}
