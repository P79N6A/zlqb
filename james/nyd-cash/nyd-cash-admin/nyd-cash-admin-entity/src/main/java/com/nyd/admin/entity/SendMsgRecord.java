package com.nyd.admin.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "send_msg_record")
public class SendMsgRecord {
    @Id
    private Long id;
    /**手机号*/
    private String mobile;
    /**手机认证时间*/
    private Date mobileTime;
    private String appName;
    /**短信发送状态*/
    private Integer status;
    /**发送类型*/
    private Integer type;
    /**短信发送时间*/
    private Date sendSmsTime;
    private Integer deleteFlag;

    private Date createTime;

    private Date updateTime;
}
