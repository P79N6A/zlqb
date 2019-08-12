package com.tasfe.sis.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Lait on 2017/7/14.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "resources")
public class Resources {
    @Id
    private Long id;

    // 父亲节点id
    private Long pid;

    // 资源的类型
    private Integer typ;

    // 资源名称
    private String name;

    // 资源的值
    private String val;

    // 资源的描述/备注说明
    private String descr;

    // 资源的状态
    private Integer status;

    // 排序
    private Integer sort;

    // 图标
    private String icon;

    // 深度
    private Integer level;

    // 打开方式(_bank,target)
    private Integer target;

}
