package com.nyd.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Dengw on 17/11/6.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_product_overdue_fee_item")
public class ProductOverdueFeeItem {
    @Id
    private Long id;
    //金融产品code
    private String productCode;
    //滞纳金
    private BigDecimal overdueFine;
    //档位逾期费率计算天数
    private Integer gearOverdueFeeDays;
    //第一档预期费率
    private BigDecimal firstGearOverdueRate;
    //第二档预期费率
    private BigDecimal secondGearOverdueRate;
    //最大逾期费率
    private BigDecimal maxOverdueFeeRate;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
}
