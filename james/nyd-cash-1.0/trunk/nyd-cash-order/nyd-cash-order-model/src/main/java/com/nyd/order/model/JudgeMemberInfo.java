package com.nyd.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Dengw on 2018/1/26
 * 会员判断
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JudgeMemberInfo implements Serializable {
    //会员是否有效
    private boolean memberFlag;
    //会员id
    private String memberId;

    private String memberType;

    private BigDecimal memberFee;
}
