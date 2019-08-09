package com.nyd.capital.model.kzjr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2017/12/13
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserKzjrInfo implements Serializable{
    private Long id;
    private String userId;

    private String accountId;

    private String bankAccount;
    private Integer status;
}
