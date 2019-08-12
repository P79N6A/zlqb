package com.nyd.admin.model.Info;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: wucx
 * @Date: 2018/9/6 16:57
 */
@Data
public class IntegratedVolumeInfo implements Serializable {

    //日期
    private String createTime;

    //姓名
    private String userName;

    //手机号
    private String mobile;

    //客户编号
    private String userId;

    //券类型
    private Integer refundTicketType;

    //券发放状态
    private Integer state;

    //发放时间
    private String ticketProvideTime;

    //券发放金额
    private BigDecimal ticketAmount;

    //备注
    private String remark;

    //唯一标识
    private String premiumId;

}
