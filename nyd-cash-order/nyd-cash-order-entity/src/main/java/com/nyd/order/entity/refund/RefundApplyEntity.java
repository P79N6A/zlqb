package com.nyd.order.entity.refund;

import java.io.Serializable;
import lombok.Data;

@Data
public class RefundApplyEntity implements Serializable {

	private static final long serialVersionUID = 7476825801532315544L;
	
	private String id;
    private String order_no;
    private String refund_date;
    private String real_refund_date;
    private String cust_id;
    private String phone;
    private String name;
    private String refund_amount;
    private String real_refund_amount;
    private String user_id;
    private String user_name;
    private String serialNum;
    private String apply_remarks;
    private String submitTime;
    private String remarks;
    private String status;
    private String create_time;
    private String update_time;
    private String handle_user_id;
    private String handle_user_name;
}
