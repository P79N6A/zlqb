package com.nyd.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Dengw on 2017/11/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_account_password")
public class Password {
    @Id
    private Long id;
    //账号
    private String accountNumber;
    //密码类型
    private Integer passwordType;
    //密码
    private String password;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
}
