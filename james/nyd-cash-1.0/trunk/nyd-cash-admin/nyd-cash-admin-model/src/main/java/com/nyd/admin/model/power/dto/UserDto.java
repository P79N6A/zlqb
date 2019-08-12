package com.nyd.admin.model.power.dto;

import com.nyd.admin.model.paging.Paging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by zhujx on 2018/1/3.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto extends Paging implements Serializable {
    private Integer id;
    //账号
    private String accountNo;
    //密码
    private String password;
    //用户类型
    private Integer userType;
    //用户名
    private String userName;
    //身份证号
    private String idNumber;
    //邮箱
    private String email;
    //是否已删除
    private Integer deleteFlag;
    //催收分组
    private Long groupId;
    //联系电话
    private String contactPhone;
    //分机号
    private String telNum;
}
