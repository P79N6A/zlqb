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
 * Created by Dengw on 2017/12/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_kzjr_product_config")
public class KzjrProductConfig {
    @Id
    private Long id;
    //p2p产品code
    private String productCode;
    //p2p原有产品code
    private String oldProductCode;
    //使用起始日期
    private Date useDate;
    //使用终止日期
    private Integer duration;
    //0使用,1废弃
    private Integer status;
    //产品总额
    private BigDecimal totalAmount;
    //剩余额度
    private BigDecimal remainAmount;
    //优先级
    private Integer priority;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;

    private Date startDate;
    private Date endDate;
}
