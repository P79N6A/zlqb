package com.nyd.order.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 渠道资产和侬要贷订单关联关系
 * @author cm
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="capital_order_relation")
public class CapitalOrderRelation {
    //主键id
    @Id
    private Long id;

    //资产渠道编号
    private String channelCode;

    //资产编号
    private String assetId;

    //侬要贷订单号
    private String orderNo;

    //状态   1 待放款 2 放款成功 3 放款失败',
    private int state;

    //添加时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //修改人
    private String updateBy;


}
