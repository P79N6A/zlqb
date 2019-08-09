package com.nyd.user.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * @author liuqiu
 */
@Data
@ToString
@Table(name = "t_geetest_config")
public class GeetestConfig {
    private String appCode;
    private String appName;
    private String geetestId;
    private String geetestKey;
    private String type;
    private String deleteFlag;
    private String createTime;
    private String updateTime;
    private String updateBy;

    private String onePassId;
    private String onePassKey;
}
