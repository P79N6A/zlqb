package com.nyd.order.model.enums;

/**
 * Created by Dengw on 2017/11/20
 */
public enum OrderStatus {
    INIT(0,"初始化"),
    AUDIT(10,"审核中"),
    AUDIT_SUCCESS(20,"审核通过"),
    WAIT_LOAN(30,"待放款"),
    LOAN_FAIL(40,"放款失败"),
    LOAN_SUCCESS(50,"已放款"),
    AUDIT_REFUSE(1000,"审核拒绝"),
    LOAN_CLOSED(1100,"关闭"),
    REPAY_SUCCESS(1200,"已结清");

    private Integer code;

    private String description;

    private OrderStatus(Integer code, String description){
        this.code = code;
        this.description = description;
    }

    public Integer getCode(){
        return this.code;
    }

    public String getDescription(){
        return this.description;
    }
    
    public static String getDescription(Integer code) {
    	 for (OrderStatus orderStatus : OrderStatus.values()) {
             if (String.valueOf(code).equals(orderStatus.getCode().toString())) {
                 return orderStatus.getDescription();
             }
         }
         return "";
	}
    

}
