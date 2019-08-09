package com.nyd.admin.entity;

import lombok.Data;
import lombok.Value;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name="t_download_agreement_log")
public class DownloadAgreementLog implements Serializable{

    @Id
    private Long id;

    //代扣协议id
    private String agreementId;

    //下载人员
    private String downloadPerson;

    //是否已删除
    private Integer deleteFlag;

    //添加时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //修改人
    private String updateBy;
}
