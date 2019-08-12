package com.nyd.capital.model.qcgz.enums;

import org.apache.commons.lang3.StringUtils;

public enum BankNameResetEnum {

	BOB ("BOB","北京银行","北京银行"),
	GDB ("GDB","广发银行","广发银行"),
	CCB ("CCB","建设银行","中国建设银行"),
	ABC ("ABC","农业银行","中国农业银行"),
	ICBC ("ICBC", "工商银行","中国工商银行"),
	BOC ("BOC","中国银行","中国银行"),
	SZPAB ("SZPAB","平安银行" ,"平安银行股份有限公司"),
	CEB ("CEB","光大银行","中国光大银行"),
	CIB ("CIB","兴业银行","兴业银行"),
	CMB ("CMB","招商银行","招商银行"),
	COMM ("COMM","交通银行","交通银行"),
	SPDB ("SPDB","浦发银行","浦发银行"),
	SHBK ("SHBK","上海银行","上海银行"),
	CMBC ("CMBC","民生银行","中国民生银行"),
	CITIC ("CITIC","中信银行","中信银行");

    BankNameResetEnum(String code, String value,String resetName) {
        this.code = code;
        this.value = value;
        this.resetName = resetName;
    }

    private String code;

    private String value;
    
    private String resetName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static BankNameResetEnum getByCode(String code){
        if(StringUtils.isBlank(code)){
            return null;
        }
        for (BankNameResetEnum bs:BankNameResetEnum.values()){
            if(bs.getCode().equals(code)){
                return bs;
            }
        }
        return null;
    }
    public static BankNameResetEnum getByValue(String value){
    	if(StringUtils.isBlank(value)){
    		return null;
    	}
    	for (BankNameResetEnum bs:BankNameResetEnum.values()){
    		if(bs.getValue().equals(value)){
    			return bs;
    		}
    	}
    	return null;
    }

	public String getResetName() {
		return resetName;
	}

	public void setResetName(String resetName) {
		this.resetName = resetName;
	}
}
