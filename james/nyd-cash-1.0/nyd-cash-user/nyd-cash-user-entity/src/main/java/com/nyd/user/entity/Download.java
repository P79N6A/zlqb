package com.nyd.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by zhujx on 2017/12/27.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="t_download")
public class Download {

    @Id
    private Long id;

    //下载地址
    private String downloadUrl;

    //版本
    private String version;

    //操作系统
    private String os;

    //是否已删除
    private Integer deleteFlag;

    //添加时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //修改人
    private String updateBy;
    private String appName;

}
