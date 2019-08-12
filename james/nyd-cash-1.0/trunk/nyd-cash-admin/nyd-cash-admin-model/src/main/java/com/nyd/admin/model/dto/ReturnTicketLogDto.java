package com.nyd.admin.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 批量发券日志对象
 * @Author: wucx
 * @Date: 2018/9/12 16:54
 */
@Data
public class ReturnTicketLogDto  implements Serializable {
    //用户姓名
    private String userName;

    //手机号
    private String mobile;

    //客户编号
    private String userId;

    //唯一标识
    private String premiumId;

    //发放状态  0：成功 ；1：失败
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
