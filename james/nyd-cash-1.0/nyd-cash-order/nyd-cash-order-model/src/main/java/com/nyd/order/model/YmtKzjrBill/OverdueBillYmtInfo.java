package com.nyd.order.model.YmtKzjrBill;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OverdueBillYmtInfo implements Serializable {

    //用户id
    private String userId;

    //账单编号
    private String billNo;

    //订单编号
    private String orderNo;

    //子订单编号
    private String orderSno;

    //账单状态
    private String billStatus;

    //滞纳金
    private BigDecimal overdueFine;

    //逾期金额
    private BigDecimal overdueAmount;

    //逾期天数
    private Integer overdueDays;

    //是否已删除
    private Integer deleteFlag;

    //添加时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //修改人
    private String updateBy;

}
