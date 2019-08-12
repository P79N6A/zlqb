package com.creativearts.nyd.collectionPay.model.zzl.helibao.enums;

/**
 * 行业及其场景类型列表
 * @author admin
 *
 */
public enum DealSceneTypeEnum {
	
	
	UNKNOWN("", ""),
	QUICKPAY("QUICKPAY", "标准场景"),
	FINANCE("FINANCE", "理财场景"),
	REPAYMENT("REPAYMENT", "还款"),
	INSURANCE("INSURANCE", "保险"),
	AIRTICKET("AIRTICKET","航空机票"),
	TRAVEL("TRAVEL", "旅游"),
	HOTEL("HOTEL", "酒店"),
	VIRTUALGOODS("VIRTUALGOODS", "虚拟商品或服务消费"),
	EDUCATION("EDUCATION", "培训教育"),
	O2O("O2O","商品服务消费"),
	OTHER("OTHER","其他");
	

	
	private String code;

	private String desc;

	DealSceneTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
	
	public static DealSceneTypeEnum fromCode(String code) {
		for (DealSceneTypeEnum item : DealSceneTypeEnum.values()) {
			if (item.getCode().equals(code)) {
				return item;
			}
		}
		return UNKNOWN;
	}

}
