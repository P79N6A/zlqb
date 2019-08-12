package com.nyd.capital.model.kzjr;

import com.nyd.capital.model.annotation.RequireField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Cong Yuxiang
 * 2017/12/14
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KzjrProductConfigInfo implements Serializable{

    private Long id;
    @RequireField
    private String productCode;
    @RequireField
    private Date useDate;
    @RequireField
    private Integer duration;
    private Integer status;

    private BigDecimal totalAmount;
    private BigDecimal remainAmount;
    private Integer priority;
    private Integer fullStatus;
}
