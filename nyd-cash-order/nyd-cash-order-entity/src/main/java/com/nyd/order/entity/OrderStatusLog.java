package com.nyd.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by zhujx on 2017/11/8.
 * 订单状态流转信息
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_order_status_log")
public class OrderStatusLog {
    //主键id
    @Id
    private Long id;
    //订单id
    private String orderNo;
    //订单变更前状态
    private Integer beforeStatus;
    //订单变更后状态
    private Integer afterStatus;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;

}
