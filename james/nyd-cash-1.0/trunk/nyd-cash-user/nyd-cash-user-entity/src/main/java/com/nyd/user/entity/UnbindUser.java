package com.nyd.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by hwei
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_unbind_user")
public class UnbindUser {
    @Id
    private Long id;
    //原手机号
    private String originMobile;
    //原身份证号
    private String originIdNumber;
    //用户ID
    private String userId;
    //废弃手机号
    private String discardMobile;
    //废弃身份证号
    private String discardIdNumber;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;


}
