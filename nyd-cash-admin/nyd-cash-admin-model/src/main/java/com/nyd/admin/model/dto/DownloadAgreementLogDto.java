package com.nyd.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DownloadAgreementLogDto implements Serializable {

    //代扣协议id
    private String agreementId;

    //下载人员
    private String downloadPerson;

}
