package com.nyd.admin.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author jiaxy
 * @date 20180618
 */
@Data
@ToString
public class CollectionUserInfoVo implements Serializable {
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
    private String createTime;
    //修改时间
    private String updateTime;
    //最后修改人
    private String updateBy;
    //催收机构id
    private Long companyId;
    //催收机构名称
    private String companyName;
    //机构类型
    private String companyType;
    //催收分组id
    private Long groupId;
    //催收分组名称
    private String groupName;
    //联系电话
    private String contactPhone;
    //分机号
    private String telNum;
    //催收人员禁用标记
    private Integer disabledFlag;
    //角色id
    private Long roleId;
}
