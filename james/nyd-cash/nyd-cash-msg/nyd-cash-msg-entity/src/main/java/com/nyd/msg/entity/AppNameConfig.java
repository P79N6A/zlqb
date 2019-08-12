package com.nyd.msg.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author liuqiu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "app_name_config")
/**
 * 马甲包名称配置表
 */
public class AppNameConfig implements Serializable {

    @Id
    private Long id;

    /**
     * app字母简称
     */
    private String appNameCode;

    /**
     * app中文名称
     */
    private String appNameValue;


    /**
     * 是否已删除
     */
    private Integer deleteFlag;

    /**
     * 添加时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateBy;
}


