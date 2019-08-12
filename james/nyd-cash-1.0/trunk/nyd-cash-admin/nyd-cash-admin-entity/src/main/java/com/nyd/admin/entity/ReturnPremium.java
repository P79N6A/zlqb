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
 * 退费标记表
 * @author cm
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_return_premium")
public class ReturnPremium  {

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

    //备注
    private String remark;

    //标记退券类型(1:小银券;2:其他退券)
    private Integer refundTicketType;

    //状态 1 已退费;  2未退费; 3退费失败
    private Integer state;

    //发放时间
    private Date ticketProvideTime;

    //发送金额
    private BigDecimal ticketAmount;

    //添加时间
    private Date createTime;

    //是否已删除  0代表未删除; 1代表已删除
    private Integer deleteFlag;

    //更新时间
    private Date updateTime;

    //修改人
    private String updateBy;
}
