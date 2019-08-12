package com.nyd.application.model.request;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * Created by hwei on 2017/11/17.
 */
@Data
public class GenerateContractModel {
    private String docTitle;//文档标题
    private String templateId;//模板编号
    private String contractId;//合同编号
    private String fontSize;//字体大小 如 10,12,12.5,14,不传默认9
    private String fontType;//字体类型 0 宋体 1 仿宋 2 黑体 3 楷体 4 微软雅黑
    private JSONObject parameterMap;//pdf的填充内容，key为文本域，value为值，value穿字符串类型

}
