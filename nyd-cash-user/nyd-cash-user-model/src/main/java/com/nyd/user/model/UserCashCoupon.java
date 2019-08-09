package com.nyd.user.model;

import com.nyd.activity.model.vo.CashCouponInfoVo;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class UserCashCoupon implements Serializable {
    /**
     * 用户账户充值余额
     */
//    private BigDecimal balance;

    /**
     * 用户返回余额
     */
//    private BigDecimal returnBalance;

    /**
     * 账户总余额
     */
    private BigDecimal totalBalance;

    /**所有的现金券类型*/
    private List<CashCouponInfoVo> cashCouponInfoVos;
}
