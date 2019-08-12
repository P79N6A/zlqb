package com.nyd.capital.model.enums;


/**
 * 
 * @author zhangdk
 *
 */
public enum KdlcQueryStatusEnum {

	/**
	 * 已作废
	 */
	QUERY_MIN_30("-30","已作废"),
	/**
	 * 审核不通过
	 */
	QUERY_MIN_31("-31","审核不通过"),
	/**
	 * 放款不通过
	 */
	QUERY_MIN_32("-32","放款不通过"),
	/**
	 * 已坏账
	 */
	QUERY_MIN_20("-20","已坏账"),
	/**
	 * 已逾期
	 */
	QUERY_MIN_11("-11","已逾期"),
	/**
	 * 待审核
	 */
	QUERY_0("0","待审核"),
	/**
	 * 审核通过
	 */
	QUERY_1("1","审核通过"),
	/**
	 * 签约成功
	 */
	QUERY_2("2","签约成功"),
	/**
	 * 待配资
	 */
	QUERY_10("10","待配资"),
	/**
	 * 待募资
	 */
	QUERY_11("11","待募资"),
	/**
	 * 待放款
	 */
	QUERY_20("20","待放款"),
	/**
	 * 已放款
	 */
	QUERY_21("21","已放款"),
	/**
	 * 放款中
	 */
	QUERY_22("22","放款中"),
	/**
	 * 部分还款
	 */
	QUERY_30("30","部分还款"),
	/**
	 * 已还款
	 */
	QUERY_31("31","已还款");

    private String code;

    private String msg;

    KdlcQueryStatusEnum(String code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
    
}
