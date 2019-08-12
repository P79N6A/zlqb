package com.nyd.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Cong Yuxiang
 * 2018/4/24
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_order_wentong")
public class OrderWentong {
    //主键id
    @Id
    private Long id;
    //订单id
    private String orderNo;
    //用户ID
    private String userId;
    //手机号
    private String mobile;

    private String name;

    private Date loanTime;

    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
}
