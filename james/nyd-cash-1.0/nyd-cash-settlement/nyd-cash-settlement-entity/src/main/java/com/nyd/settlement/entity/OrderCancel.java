package com.nyd.settlement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Peng
 * @create 2018-01-16 11:19
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_order_cancel")
public class OrderCancel {
    //id
    @Id
    private Long id;
    //账号
    private String orderNo;
    //客户姓名
    private String realName;
    //手机号
    private String mobile;
    //备注
    private String remark;
    //是否有效
    private String status;
    //无效原因
    private String failReason;
    //是否已删除
    private Integer deleteFlag;
    //创建时间
    private String createTime;
    //更新时间
    private String updateTime;
    //更改人
    private String updateBy;
}
