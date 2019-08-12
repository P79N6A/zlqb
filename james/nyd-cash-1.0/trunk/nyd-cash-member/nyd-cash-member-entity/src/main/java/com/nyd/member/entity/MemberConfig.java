package com.nyd.member.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Dengw on 2017/12/6
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_member_config")
public class MemberConfig {
    @Id
    private Long id;
    //会员类型
    private String type;
    //会员类型描述
    private String typeDescribe;
    //会员实际金额
    private BigDecimal realFee;
    //会员折扣价
    private BigDecimal discountFee;
    //会员有效周期
    private Integer effectTime;
    //会员使用业务范围
    private String business;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
}
