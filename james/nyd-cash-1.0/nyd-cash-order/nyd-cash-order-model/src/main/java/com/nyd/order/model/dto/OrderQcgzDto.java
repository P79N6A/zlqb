package com.nyd.order.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liuqiu
 */
@Data
public class OrderQcgzDto implements Serializable {
    private String bankAccount;
    private String bankName;
    private String assetNo;
}
