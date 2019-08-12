package com.tasfe.zh.base.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Lait on 2017/8/8.
 * 统一定义id的entity基类.
 * 基类统一定义id的属性名称、数据类型、列名映射及生成策略.
 *
 * @MappedSuperclass 声明为父类，不生成实体表
 */
@MappedSuperclass
@Access(AccessType.FIELD)
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class BaseEntity<PK extends Serializable> extends IdEntity<PK> {

    @Column(name = "create_user_id", length = 32, updatable = false, nullable = true)
    private PK createUserId;
    @Column(name = "update_user_id", length = 32, nullable = true)
    private PK updateUserId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", updatable = false, nullable = true)
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time", nullable = true)
    private Date updateTime;

}
