package com.tasfe.sis.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by hefusang on 2017/8/22.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountInfos extends PageInfos {

    private String account;

    private String pwd;

    private String status;

    private String roleNames;

    private String accountId;

    private String nick;

    private String typ;

}
