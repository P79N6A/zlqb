package com.nyd.admin.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 18:21 2018/10/1
 */
@Data
@Table(name = "t_user_source")
public class UserSource {
    @Id
    private Long id;
    //账号号码
    private String accountNumber;
    //app名称
    private String appName;
    //用户来源
    private String source;
    /**
     * 手机系统类型
     */
    private String os;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
}
