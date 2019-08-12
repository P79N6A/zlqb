package com.nyd.application.model.request;

import lombok.Data;

/**
 * Created by hwei on 2017/11/18.
 */
@Data
public class ExTSignAutoModel {
    private String transactionId;//交易号
    private String contractId;//合同编号
    private String docTitle;//文档标题
    private String clientRole;//客户角色
    private String signKeyword;//定位关键字
    private String keyWordStrategy;//签章策略 0 所有关键字签章 1第一个关键字签章 2 最后一个关键字签章



}
