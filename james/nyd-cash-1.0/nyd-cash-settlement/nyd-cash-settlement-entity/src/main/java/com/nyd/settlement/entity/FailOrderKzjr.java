package com.nyd.settlement.entity;

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
@Table(name = "t_failorder_kzjr")
public class FailOrderKzjr {
    @Id
    private Long id;

    private String accountId;
    private String orderNo;
    private BigDecimal amount;
    private Integer duration;
    private Integer reason;
    private String description;
    private Date createTime;
    private Date updateTime;
}
