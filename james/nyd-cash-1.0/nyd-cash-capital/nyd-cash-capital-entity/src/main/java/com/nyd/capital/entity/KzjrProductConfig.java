package com.nyd.capital.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Cong Yuxiang
 * 2017/12/14
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_kzjr_product_config")
public class KzjrProductConfig {
    @Id
    private Long id;
    private String productCode;
    private Date useDate;
    private Integer duration;
    private Integer status;
    private BigDecimal totalAmount;
    private BigDecimal remainAmount;
    private Integer priority;
    private Integer fullStatus;
    private Date createTime;
    private Date updateTime;

}
