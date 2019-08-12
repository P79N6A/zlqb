package com.nyd.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Table;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_hit_log")
public class HitLog {

    //主键
    private Long id;

    //手机号
    private String mobile;

    //来源渠道
    private String source;

    //App名称
    private String appName;

    //规则编号
    private String ruleCode;

    //是否命中
    private Integer hitResult;

    //添加时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //修改人
    private String updateBy;
}
