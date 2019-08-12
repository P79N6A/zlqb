package com.nyd.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 侬要贷发券记录表
 * @author cm
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_return_ticket_log")
public class ReturnTicketLog {

    //主键id
    @Id
    private Long id;

    //唯一标示
    private String premiumId;

    //手机号
    private String mobile;

    //用户id
    private String userId;

    //用户名
    private String userName;

    //发券的类型    1:小银券; 2:其他券类型
    private String type;

    //券发放时间
    private Date ticketProvideTime;

    //券发放金额
    private BigDecimal ticketAmount;

    //发放状态  0：成功 ；1：失败
    private Integer state;

    //添加时间
    private Date createTime;

    //是否已删除 0:表未删除;1:已删除
    private Integer deleteFlag;

    //更新时间
    private Date updateTime;

    //修改人(券发放人)
    private String updateBy;

}
