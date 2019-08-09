package com.nyd.admin.entity.power;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Peng
 * @create 2017-12-18 11:00
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_user_info")
public class User {
    @Id
    private Long id;
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
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
    //催收分组
    private Long groupId;
    //联系电话
    private String contactPhone;
    //分机号
    private String telNum;
    //催收人员禁用标记
    private Integer disabledFlag;
}
