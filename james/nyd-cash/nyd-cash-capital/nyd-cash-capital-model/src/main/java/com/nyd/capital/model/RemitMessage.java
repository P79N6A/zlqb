package com.nyd.capital.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Cong Yuxiang
 * 2017/12/4
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RemitMessage implements Serializable{
    private String orderNo;
    /**
     * 0是成功 非0失败
     */
    private String remitStatus;
    private BigDecimal remitAmount;
    private Date remitTime;
}
