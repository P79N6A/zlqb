package com.nyd.settlement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Cong Yuxiang
 * 2018/2/1
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_pwd")
public class Pwd {
    @Id
    private Integer id;

    private String password;

    private Integer type;

    private String desc;

    private Date  createTime;

    private Date updateTime;
}
