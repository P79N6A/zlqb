package com.nyd.capital.model.kzjr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Cong Yuxiang
 * 2017/12/14
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FailOrderKzjrInfo {
    private Long id;
    private String accountId;
    private String orderNo;
    private BigDecimal amount;
    private Integer duration;
    private Integer reason;
    private String description;
    private Integer channel;

}
