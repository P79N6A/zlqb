package com.tasfe.zh.bank.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by hefusang on 2017/8/8.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="bank")
public class Bank {
    @Id
    private Long id;
    // 银行名称
    private String name;
    // code
    private String code;
    // 状态
    private Integer status;
    // 法人
    private String legalName;
    // 创建时间
    private Date ctime;
    // 更新时间
    private Date utime;


}
