package com.tasfe.sis.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by Lait on 2017/7/14.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account_role")
public class LoginRole {

    // 账户id
//    @Column(name = "aid")
    private Long aid;
    // 角色id
//    @Column(name = "rid")
    private Long rid;
}
