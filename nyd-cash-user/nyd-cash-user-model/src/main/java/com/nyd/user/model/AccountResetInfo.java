package com.nyd.user.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountResetInfo implements Serializable{
	
    private String oldAccountNumber;
    private String newAccountNumber;
    private String userId;
    private String ibankUserId;
    private Integer deleteFlag;
}
