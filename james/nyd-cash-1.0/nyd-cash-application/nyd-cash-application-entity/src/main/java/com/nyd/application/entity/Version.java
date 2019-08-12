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
@Table(name = "t_version")
public class Version {
    @Id
    private Long id;
    //操作系统
    private String os;
    //版本
    private Integer version;
    //版本名称
    private String versionName;
    //更新地址
    private String downloadUrl;
    //是否强制更新
    private Integer force;
    //备注
    private String remark;
    //app名称
    private String appName;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
}
