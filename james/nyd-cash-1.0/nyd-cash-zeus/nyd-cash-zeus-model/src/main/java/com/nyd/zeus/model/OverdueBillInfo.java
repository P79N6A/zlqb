package com.nyd.zeus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhujx on 2017/11/18.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OverdueBillInfo implements Serializable {

    //用户id
    private String userId;

    //账单编号
    private String billNo;

    //订单编号
    private String orderNo;

    //账单状态
    private String billStatus;

    //滞纳金
    private BigDecimal overdueFine;

    //逾期金额
    private BigDecimal overdueAmount;

    //逾期天数
    private Integer overdueDays;

    //催收减免金额
    private BigDecimal collectionDerateAmount;

    //是否已删除
    private Integer deleteFlag;

    //添加时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //修改人
    private String updateBy;

}
