package com.creativearts.nyd.collectionPay.model.zzl.helibao.enums;

/**
 * 应用类型枚举
 * @author admin
 *
 */
public enum AppTypeEnum {
	
	UNKNOWN("", ""),
	IOS("IOS", "IOS客户端"),
	AND("AND", "安卓客户端	"),
	H5("AND", "H5网页"),
	WX("WX", "微信"),
	OTHER("OTHER","其他");

	
	private String code;

	private String desc;

	AppTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
	
	public static AppTypeEnum fromCode(String code) {
		for (AppTypeEnum item : AppTypeEnum.values()) {
			if (item.getCode().equals(code)) {
				return item;
			}
		}
		return UNKNOWN;
	}

}
