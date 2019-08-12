package com.nyd.settlement.entity.refund;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by hwei on 2018/1/17.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_refund")
public class Refund {
    @Id
    private Long id;

    private String realName;//姓名

    private String mobile;//手机号

    private String orderNo;//订单号

    private String billNo;//账单号

    private BigDecimal offlineRepayAmount;//还款金额（线下）

    private String offlineRepayChannel;//还款渠道

    private Integer refundType;//退款类型（1 会员费 ，2 多还款，3 其他）

    private BigDecimal refundAmount;//退款金额

    private Date refundTime;//退款时间

    private String refundChannel;//退款方式

    private String refundFlow;//退款流水

    private String refundAccount;//退款账户

    private String refundBank;//退款银行

    private String refundReason;//退款原因

    private Integer refundStatus;//退款状态
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
}
