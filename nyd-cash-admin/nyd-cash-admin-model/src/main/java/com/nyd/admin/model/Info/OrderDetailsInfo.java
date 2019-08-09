package com.nyd.admin.model.Info;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wucx
 * @Date: 2018/9/6 11:50
 */
@Data
public class OrderDetailsInfo implements Serializable {

    //姓名
    private String realName;

    //申请时间
    private String createTime;

    //手机号
    private String mobile;

    //申请平台（0：侬要贷 1：银码头）
    private Integer channel;

    //申请产品种类
    private String productCode;

    //申请结果
    private String auditStatus;

    //产品名字
    private String productName;

    //订单状态（0:初始化,10:审核中,20:审核通过,30:待放款,40:放款失败,50:已放款,1000:审核拒绝）
    private Integer orderStatus;

    //放款机构
    private String fundCode;

    //提现日期
    private String payTime;

}
