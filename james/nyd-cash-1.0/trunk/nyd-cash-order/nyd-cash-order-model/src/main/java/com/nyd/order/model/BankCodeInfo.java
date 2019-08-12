package com.nyd.order.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class BankCodeInfo implements Serializable {
    /**
     *银行编码
     */
    private String bankCode;
    /**
     *银行名称
     */
    private String bankName;

}
