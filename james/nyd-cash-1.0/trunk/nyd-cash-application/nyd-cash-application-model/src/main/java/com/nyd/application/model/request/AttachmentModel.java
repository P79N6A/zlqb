package com.nyd.application.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Dengw on 2017/11/25
 * 图片信息
 */
@Data
public class AttachmentModel implements Serializable {
    //用户ID
    private String userId;
    //附件类型
    private String type;
    //附件
    private String file;
    //附件名称
    private String fileName;
}
