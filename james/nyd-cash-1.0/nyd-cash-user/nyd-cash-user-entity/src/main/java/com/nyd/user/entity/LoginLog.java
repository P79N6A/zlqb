package com.nyd.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by hwei on 2018/5/8.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_login_log")
public class LoginLog implements Serializable {
    @Id
    private Long id;
    //账号号码
    private String accountNumber;
    //用户ID
    private String userId;
    //账号状态
    private String status;
    //ip
    private String ip;
    //用户来源
    private String source;
    //用户渠道
    private Integer channel;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
    //app名称
    private String appName;
}
