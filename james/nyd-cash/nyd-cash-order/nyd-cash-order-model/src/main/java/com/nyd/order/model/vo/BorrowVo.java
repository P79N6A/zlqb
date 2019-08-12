package com.nyd.order.model.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by Dengw on 2017/11/20
 * 借款返回对象
 */
@Data
public class BorrowVo {
    //手机号
    private String mobile;
    //会员到期时间
    private String expireTime;
    //是否已评估
    private Integer ifAssess;
    //会员到期天数
    private Integer memberExpireDay;
    //借款金额list
    private List<ProductVo> productList;
    //银行卡号list
    private List<BankVo> bankList;
}
