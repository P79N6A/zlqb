package com.nyd.settlement.model.dto.refund;

import com.nyd.settlement.model.annotation.RequireField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by hwei on 2018/1/17.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DoRefundDto {
    @RequireField
    private String realName;//姓名
    @RequireField
    private String mobile;//手机号
    @RequireField
    private String orderNo;//订单号

    private String billNo;//账单号

    @RequireField
    private Integer refundType;//退款类型（1 会员费 ，2 多还款，3 其他）
    @RequireField
    private BigDecimal refundAmount;//退款金额
    @RequireField
    private Date refundTime;//退款时间
    @RequireField
    private String refundChannel;//退款方式
    @RequireField
    private String refundFlow;//退款流水
    @RequireField
    private String refundAccount;//退款账户

    private String refundBank;//退款银行

    private Integer refundStatus;//退款状态

    //修改人
    private String updateBy;
}
