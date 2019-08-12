package com.nyd.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="t_user_source")
public class UserSource implements Serializable {

    @Id
    private Long id;

    /**
     * 手机号
     */
    private String accountNumber;

    /**
     * app名称
     */
    private String appName;

    /**
     * 来源
     */
    private String source;

    /**
     * 手机系统类型
     */
    private String os;

    /**
     * 是否已删除
     */
    private Integer deleteFlag;

    /**
     * 添加时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateBy;
}
