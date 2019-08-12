package com.nyd.application.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Dengw on 2017/11/27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_appinfo")
public class AppInfo {
    @Id
    private Long id;
    //操作系统
    private String os;
    //版本
    private String version;
    //整包升级地址
    private String installUrl;
    //热更升级地址
    private String updateUrl;
    //备注
    private String remark;
    //app名称
    private String appName;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
}
