package com.nyd.admin.model.power;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by zhujx on 2018/1/5.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserPowerInfo implements Serializable {

    //父id
    private Long pid;

    private Long powerId;
    //权限名称
    private String powerName;
    //权限KEY
    private String powerKey;
    //权限url
    private String powerUrl;
    //是否跳转
    private Integer jmp;
}
