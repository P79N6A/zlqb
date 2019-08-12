package com.nyd.user.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "t_hit_rule_config")
public class HitRuleConfig {
    //
    @Id
    private Long id;

    //来源渠道
    private String source;

    //App名称
    private String appName;

    //规则编号
    private String ruleCode;

    //规则描述
    private String ruleDesc;

    //是否有效
    private Integer valid;

    //添加时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //修改人
    private String updateBy;
}
