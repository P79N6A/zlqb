package com.nyd.application.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Dengw on 2017/11/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_banner")
public class Banner {
    @Id
    private Long id;
    //操作系统
    private String linkUrl;
    //版本
    private String fileUrl;
    //图片type
    private String type;
    //url标题
    private String title;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
    //app简称
    private String appName;
}
