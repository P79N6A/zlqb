package com.nyd.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_order_proportion")
public class Proportion {
    //主键id
    @Id
    private Long id;
    //空中金融分流比例
    private Integer skyRatio;
    //稳通分流比例
    private Integer stableRatio;
    //未知c分流比例
    private Integer cRatio;
    //未知d分流比例
    private Integer dRatio;
    //日期类型
    private Integer dateType;
    //分流日期
    private Date ratioDate;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;

}
