package com.nyd.batch.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TMember {
    /**
     * 
     */
    private Integer id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 会员类型
     */
    private String memberType;

    /**
     * 会员类型描述
     */
    private String memberTypeDescribe;

    /**
     * 到期时间
     */
    private Date expireTime;

    /**
     * 是否已删除 0：正常；1：已删除
     */
    private Byte deleteFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 最后修改人
     */
    private String updateBy;

  }