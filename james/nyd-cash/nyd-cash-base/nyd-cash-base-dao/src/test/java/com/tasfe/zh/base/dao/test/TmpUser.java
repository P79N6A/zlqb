package com.tasfe.zh.base.dao.test;

import com.tasfe.zh.base.dao.entity.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
public class TmpUser extends BaseEntity<Long> {
    private static final long serialVersionUID = 1L;

    /**
     * 用户名称
     */
    @Column(name = "username")
    private String username;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birthday")
    private Date birthday;

    /**
     * 手机号
     */
    @Column(name = "mobile")
    private String mobile;

    @Column(name = "avatarpath", length = 500)
    private String photo;

    /**
     * 当前用户的状态
     */
   /* @Enumerated(EnumType.ORDINAL)
    @Column(name="activestate")
    private UserStatus userStatus = UserStatus.VALID ;*/

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_login_time")
    private Date lastloginTime;

    /**
     * 登陆次数
     */
    @Column(name = "login_count")
    private Long loginCount;

    @Column(name = "password")
    private String password;

    /**
     * 所属部门
     */
    @Column
    private String department;

}
