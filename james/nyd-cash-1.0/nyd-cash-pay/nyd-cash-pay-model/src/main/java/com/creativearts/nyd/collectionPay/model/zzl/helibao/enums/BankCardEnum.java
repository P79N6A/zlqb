package com.creativearts.nyd.collectionPay.model.zzl.helibao.enums;

/**
 * 银行卡列表枚举
 * @author admin
 *
 */
public enum BankCardEnum {
	
	
	UNKNOWN("", "",""),
	ICBC("ICBC", "工商银行","借记卡/贷记卡"),
	ABC("ABC", "农业银行","贷记卡"),
	BOC("BOC", "中国银行","贷记卡"),
	CCB("CCB", "建设银行","借记卡/贷记卡"),
	CMBCHINA("CMBCHINA","招商银行","贷记卡"),
	POST("POST", "邮政储蓄","贷记卡"),
	ECITIC("ECITIC", "中信银行","借记卡/贷记卡"),
	CEB("CEB", "光大银行","借记卡/贷记卡"),
	BOCO("BOCO", "交通银行","贷记卡"),
	CIB("CIB","兴业银行","贷记卡"),
	CMBC("CMBC", "民生银行","贷记卡"),
	PINGAN("PINGAN", "平安银行","借记卡/贷记卡"),
	CGB("CGB", "广发银行","借记卡/贷记卡"),
	BCCB("BCCB", "北京银行","借记卡/贷记卡"),
	HXB("HXB","华夏银行","借记卡/贷记卡"),
	SPDB("SPDB","浦发银行","贷记卡"),
	SHB("SHB","上海银行","贷记卡");

	
	private String code;//银行编码
	
	private String name;//银行名称

	private String desc;//支持卡种

	BankCardEnum(String code,String name, String desc) {
		this.code = code;
		this.name = name;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
	public String getDesc() {
		return desc;
	}
	
	public static BankCardEnum fromCode(String code) {
		for (BankCardEnum item : BankCardEnum.values()) {
			if (item.getCode().equals(code)) {
				return item;
			}
		}
		return UNKNOWN;
	}

}
