package com.nyd.settlement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Peng
 * @create 2018-01-26 19:10
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_order_status_log")
public class OrderStatusLog {
    @Id
    private Long id;
    //订单号
    private String orderNo;
    //变更前状态
    private Integer beforeStatus;
    //变更后状态
    private Integer afterStatus;
    //最后修改人
    private String updateBy;

    private Integer deleteFlag;

    private Date createTime;

    private Date updateTime;
}
