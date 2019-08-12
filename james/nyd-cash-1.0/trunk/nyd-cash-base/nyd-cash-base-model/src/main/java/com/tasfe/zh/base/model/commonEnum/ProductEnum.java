package com.tasfe.zh.base.model.commonEnum;

/**
 * Created by hwei on 2017/11/4.
 * 产线枚举
 */
public enum ProductEnum {

    Cash("Cash","现金贷");
    private String productCode;
    private String productName;

    ProductEnum(String productCode, String productName) {
       this.productCode = productCode;
       this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getProductName() {
        return productName;
    }
}
