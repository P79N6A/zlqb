package com.nyd.application.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Dengw on 2017/11/29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductModel implements Serializable {
    //金融产品code
    private String productCode;
    //金融产品名称
    private String productName;
    //产品图片
    private String fileUrl;
    //借款跳转地址
    private String linkUrl;
    //借款跳转地址Key
    private String linkKey;
    //额度
    private String principal;
    //借款周期
    private String loanDay;
    //借款动作描述
    private String actionDescription;
    //是否推荐:0推荐，1不推荐
    private String recommendFlag;
    //产品提示
    private String pointContent;
}
