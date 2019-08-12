package com.nyd.admin.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 12:59 2018/10/1
 */
@Data
@Table(name = "t_unlogin_msg_log")
public class UnLoginMsgLog {
    @Id
    private Long id;
    /**手机号*/
    private String mobile;
    /**
     * ymt或马甲
     */
    private String appName;
    /**
     * 短信状态
     */
    private Integer status;
    /**发送短信类型 0:未登录发送短信*/
    private Integer type;
    /**发送次数*/
    private Integer sendCount;
    /**短信发送时间*/
    private Date sendSmsTime;
    /**是否已删除 0 ：正常 1 ：已删除*/
    private Integer deleteFlag;
    /**添加时间*/
    private Date createTime;
    /**修改时间*/
    private Date updateTime;
    /**最后修改人*/
    private String updateBy;
}
