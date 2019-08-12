package com.tasfe.zh.base.entity;

import javax.persistence.Id;
import java.util.Date;

public class BaseEntity {

    @Id
    private Long id;

    private Date crateTime;

    private Date updateTime;

}
