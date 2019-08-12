package com.tasfe.sis.auth.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Lait on 2017/8/22.
 */
@Data
@Table(name = "user_account")
public class UserAccount {
    @Id
    private Long id;
    // 用户id
    private Long userId;
    // 账号id
    private Long accountId;
}
