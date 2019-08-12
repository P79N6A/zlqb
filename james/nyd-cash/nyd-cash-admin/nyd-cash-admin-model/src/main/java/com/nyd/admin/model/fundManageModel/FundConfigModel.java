package com.nyd.admin.model.fundManageModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by hwei on 2017/12/29.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FundConfigModel implements Serializable{
    //投资期限类型
    private Integer termType;
    //利率
    private BigDecimal returnRate;
    //百分比利率
    private String ratePercentage;
}
