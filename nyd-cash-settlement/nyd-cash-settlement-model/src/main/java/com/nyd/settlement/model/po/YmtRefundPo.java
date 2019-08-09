package com.nyd.settlement.model.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class YmtRefundPo {

    private String userId;//客户编号

    private Integer testStatus;//标签

    private Integer productType; //产品类型

    private String name;//姓名

    private String mobile;//手机号

    private String orderNo;//订单号

    private Integer refundType;//退款类型（1 评估费 ，2 推荐费，3 其他）

    private BigDecimal refundAmount;//退款金额

    private Date refundTime;//退款时间

    private String refundChannel;//退款方式

    private String refundFlowNo;//退款流水号

    private String refundAccount;//退款账户

    private String refundBank;//退款银行

    private String refundReason;//退款原因

    private Integer refundStatus;//退款状态

    private Integer deleteFlag; //是否已删除

    private Date createTime;//添加时间

    private Date updateTime;//更新时间

    private String updateBy; //修改人

    private BigDecimal refundFee;//退款手续费
}
