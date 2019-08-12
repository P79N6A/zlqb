package com.nyd.application.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

/**
 * Created by hwei on 2017/11/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MuBanRequestModel {
    private String templateId; //模板编号
    private String pdfUrl;//模板地址  pdfUrl和file 两个参数必须一
    private File file;//文件流    pdfUrl和file 两个参数必须一


}
