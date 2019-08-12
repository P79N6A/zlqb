package com.nyd.admin.model.Vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class WithHoldAgreementVo implements Serializable{

    private  String _id;
    //创建时间
    private String time;

    //下载地址
    private String downloadUrl;

    //预览地址
    private String viewPdfUrl;


}
