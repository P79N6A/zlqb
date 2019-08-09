package com.nyd.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Dengw on 2017/12/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KzjrProductConfigVo implements Serializable {
    //p2p产品code
    private String productCode;
    //p2p原有产品code
    private String oldProductCode;
    //使用起始日期
    private String useDate;
    //使用终止日期
    private Integer duration;
    //0使用,1废弃
    private String status;
    //产品总额
    private BigDecimal totalAmount;
    //剩余额度
    private BigDecimal remainAmount;
    //优先级
    private Integer priority;
}
