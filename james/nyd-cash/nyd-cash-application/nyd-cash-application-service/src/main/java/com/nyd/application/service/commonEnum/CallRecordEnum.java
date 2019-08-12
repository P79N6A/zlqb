package com.nyd.application.service.commonEnum;



/**
 * 通话类型
 * 2 呼出，1 呼入,3 未接
 * @author admin
 *
 */
public enum CallRecordEnum {
	
	/** 未知:"" */
	UNKNOWN("", ""),
	/** 1:待分配催收公司 */
	CALL_IN("1", "呼入"),
	/** 2:待分配催收员 */
	CALL_OUT("2", "呼出"),
	/** 3:待催收订单 */
	NO_RESP("3", "未接");

	
	private String code;

	private String desc;

	CallRecordEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
	
	public static CallRecordEnum fromCode(String code) {
		for (CallRecordEnum item : CallRecordEnum.values()) {
			if (item.getCode().equals(code)) {
				return item;
			}
		}
		return UNKNOWN;
	}

}
