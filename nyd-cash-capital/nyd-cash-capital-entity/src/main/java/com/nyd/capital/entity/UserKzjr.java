package com.nyd.capital.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Cong Yuxiang
 * 2017/12/13
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_user_kzjr")
public class UserKzjr implements Serializable {
    @Id
    private Long id;

    private String userId;

    private String accountId;

    private String bankAccount;
    private Integer status;

    private Date createTime;
    private Date updateTime;
}
