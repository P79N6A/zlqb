package com.nyd.admin.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: wucx
 * @Date: 2018/9/7 11:07
 */
@Data
public class BatchCouponDto implements Serializable {

    //券类型
    private Integer refundTicketType;

    //发放金额
    private BigDecimal ticketAmount;

    //券发放操作人
    private String updateBy;

    //唯一标识集合
    private List<BatchUserDto> premiumIdList;

}
