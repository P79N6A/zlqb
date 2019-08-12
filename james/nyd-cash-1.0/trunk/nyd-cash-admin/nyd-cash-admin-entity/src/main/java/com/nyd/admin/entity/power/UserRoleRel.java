package com.nyd.admin.entity.power;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by zhujx on 2018/1/3.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_user_role_rel")
public class UserRoleRel {
    @Id
    private Long id;
    //用户id
    private Integer userId;
    //角色id
    private Integer roleId;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
}
