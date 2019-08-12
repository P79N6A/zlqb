package com.nyd.settlement.model.vo;

import com.nyd.settlement.model.annotation.RequireField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Cong Yuxiang
 * 2017/12/18
 **/
@Data
public class NydHlbVo implements Serializable{

    @RequireField
    private String userId;
    @RequireField
    private String billNo;
    @RequireField
    private String bankNo;
    @RequireField
    private BigDecimal amount;
    private String name;
    private String idCard;
}
