package com.creativearts.nyd.pay.model;

import com.creativearts.nyd.pay.model.annotation.RequireField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Cong Yuxiang
 * 2018/3/8
 **/
@Data
public class NydPayVo implements Serializable {
    private String platformType;
    private String userId;

    private String billNo;
    @RequireField
    private String bankNo;
    @RequireField
    private BigDecimal amount;
}
