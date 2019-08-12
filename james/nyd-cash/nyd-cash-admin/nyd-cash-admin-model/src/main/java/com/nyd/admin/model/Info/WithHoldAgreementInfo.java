package com.nyd.admin.model.Info;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class WithHoldAgreementInfo implements Serializable {

    //协议ID
    private  String _id;

    //创建时间
    private Date create_time;

    //下载地址
    private String downloadUrl;

    //预览地址
    private String viewPdfUrl;
}
