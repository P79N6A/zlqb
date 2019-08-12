package com.nyd.application.model.request;

import lombok.Data;

/**
 * Created by hwei on 2017/11/25.
 */
@Data
public class AgreementTemplateModel {
    private String type;//模板类型
    private String key;//模板在七牛的key
    private String appName;//app名称
}
