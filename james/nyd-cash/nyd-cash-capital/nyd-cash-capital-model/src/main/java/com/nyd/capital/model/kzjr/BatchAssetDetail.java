package com.nyd.capital.model.kzjr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Cong Yuxiang
 * 2017/12/18
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BatchAssetDetail implements Serializable{
    private String productCode;
    private String orderId;
    private String accountId;
    private BigDecimal amount;
    private Integer type;
    private Integer refundDay;
    private Integer duration;
}
