package com.nyd.application.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * PDF文件属性
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FilePDFInfo implements Serializable {
    private String userId;
    // qiniu 文件路径
    private String filePDFPath;
    // 文件报告编码
    private String reportCode;
}
