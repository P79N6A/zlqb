package com.nyd.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Dengw on 2018/1/15
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_inner_white_user")
public class InnerWhiteUser {
    //主键id
    @Id
    private Long id;
    //手机号
    private String mobile;
    //姓名
    private String realName;
    //是否在用0在用，1弃用
    private Integer isInUse;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
}
