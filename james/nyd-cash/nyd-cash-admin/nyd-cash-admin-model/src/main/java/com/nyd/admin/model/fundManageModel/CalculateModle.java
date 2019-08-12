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
public class CalculateModle implements Serializable{
    private BigDecimal investmentAmount;//投资金额
    private Integer investmentTerm;//投资期限
    private BigDecimal returnRate;//利率
}
