package com.tasfe.zh.base.model;

/**
 * Created by Lait on 2017/8/1.
 */
public enum FileSuffix {
    //图片
    png, jpg, jpeg, gif,
    //文档
    txt, doc, docx,
    //表格
    xls, xlsx
    //其他...
    ;

    public static FileSuffix get(String suffix) {
        if (suffix != null) {
            try {
                return Enum.valueOf(FileSuffix.class, suffix.trim());
            } catch (IllegalArgumentException ex) {
            }
        }
        return null;
    }


}
