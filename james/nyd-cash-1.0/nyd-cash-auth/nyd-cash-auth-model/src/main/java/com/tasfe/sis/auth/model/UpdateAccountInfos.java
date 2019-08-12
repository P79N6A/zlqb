package com.tasfe.sis.auth.model;

import lombok.Data;

/**
 * Created by hefusang on 2017/8/22.
 */
@Data
public class UpdateAccountInfos {

    private String pwd;

    private String nick;

    private String status;

    private String typ;

    private String accountId;

    private String roleNames;

}
