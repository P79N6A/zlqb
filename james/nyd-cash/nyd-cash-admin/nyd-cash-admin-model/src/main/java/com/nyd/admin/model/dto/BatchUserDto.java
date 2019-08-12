package com.nyd.admin.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: wucx
 * @Date: 2018/9/7 15:59
 */
@Data
public class BatchUserDto implements Serializable {

    //用户姓名
    private String userName;

    //手机号
    private String mobile;

    //客户编号
    private String userId;

    //唯一标识
    private String premiumId;

    //退费标记状态，1为已退费，2为未退费, 3为退费失败
    private Integer state;

    //发券类型
    private Integer type;

    //券发放时间
    private String ticketProvideTime;

    //发放金额
    private BigDecimal ticketAmount;

    //券发放操作人
    private String updateBy;
}
