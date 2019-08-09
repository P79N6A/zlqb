package com.nyd.zeus.dao.enums;

import org.apache.commons.lang.StringUtils;

/**
 * Created by zhujx on 2017/11/20.
 */
public enum UrgeStatusEnum {

	URGE_ZERO(0,"初始化"),
	URGE_ONE(1,"待催收"),
	URGE_TWO(2,"催收中"),
	URGE_THIRD(3,"承诺还款"),
	URGE_FOUR(4,"已结清");

	UrgeStatusEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    private int code;

    private String value;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public static String getUrgeName(String code){
    	if(StringUtils.isBlank(code)){
    		return "";
    	}
    	Integer i = Integer.valueOf(code);
    	for(UrgeStatusEnum urge:UrgeStatusEnum.values()){
    		if(urge.getCode() == i){
    			return urge.getValue();
    		}
    	}
    	return "";
    }
}
