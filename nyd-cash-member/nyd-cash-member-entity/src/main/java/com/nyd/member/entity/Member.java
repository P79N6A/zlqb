package com.nyd.member.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Dengw on 2017/12/6
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_member")
public class Member {
    @Id
    private Long id;
    //用户ID
    private String userId;
    //手机号
    private String mobile;
    //会员类型
    private String memberType;
    //会员类型描述
    private String memberTypeDescribe;
    //到期时间
    private Date expireTime;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
    //渠道名称（马甲包来源）
    private String appName;
}
